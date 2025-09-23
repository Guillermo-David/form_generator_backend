package com.generador_informes.backend.listener;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.generador_informes.backend.Names;
import com.generador_informes.backend.config.RabbitMQConfig;
import com.generador_informes.backend.dto.FormDataDto;

@Component
public class FormQueueListener {

    @Autowired
    private JobLauncher jobLauncher;

    @Autowired
    private Job generateAndSendReportsJob;

    @RabbitListener(queues = RabbitMQConfig.QUEUE_NAME)
    public void onMessage(FormDataDto formData) {
        try {
            jobLauncher.run(generateAndSendReportsJob,
                    new JobParametersBuilder()
                        .addLong(Names.TIME_LC.get(), System.currentTimeMillis())
                        .addString(Names.NOMBRE_LC.get(), formData.getNombre())
                        .addString(Names.EMAIL_LC.get(), formData.getEmail())
                        .addString(Names.TIPO_INFORME_LC.get(), formData.getTipoInforme())
                        .addString(Names.COMENTARIO_LC.get(), formData.getComentario())
                        .toJobParameters());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}