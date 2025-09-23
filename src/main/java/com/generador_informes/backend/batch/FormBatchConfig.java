package com.generador_informes.backend.batch;

import java.util.Collections;

import org.apache.commons.lang3.StringUtils;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.support.ListItemReader;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

import com.generador_informes.backend.Names;
import com.generador_informes.backend.dto.FormDataDto;
import com.generador_informes.backend.service.EmailService;
import com.generador_informes.backend.service.ReportService;

@Configuration
@EnableBatchProcessing
public class FormBatchConfig {

	private static final String STEP_NAME = "generateAndSendReportStep";
	private static final String JOB_NAME = "generateAndSendReportsJob";
    
    @Bean
    @StepScope
    ListItemReader<FormDataDto> jobParamItemReader(
            @Value("#{jobParameters['nombre']}") String nombre,
            @Value("#{jobParameters['email']}") String email,
            @Value("#{jobParameters['tipoInforme']}") String tipoInforme,
            @Value("#{jobParameters['comentario']}") String comentario) {

        if (nombre == null) nombre = Names.SIN_NOMBRE.get();
        if (email == null) email = Names.SIN_EMAIL.get();
        if (tipoInforme == null) tipoInforme = Names.JASPER_LC.get();
        if (comentario == null) comentario = StringUtils.EMPTY;

        FormDataDto dto = new FormDataDto(nombre, email, tipoInforme, comentario);
        return new ListItemReader<>(Collections.singletonList(dto));
    }

    @Bean
    Step generateAndSendReportStep(JobRepository jobRepository,
                                   PlatformTransactionManager transactionManager,
                                   ReportService reportService,
                                   EmailService emailService,
                                   ListItemReader<FormDataDto> jobParamItemReader) {

        return new StepBuilder(STEP_NAME, jobRepository)
                .<FormDataDto, FormDataDto>chunk(1, transactionManager)
                .reader(jobParamItemReader)
                .processor(item -> {
                	String selectedType = item.getTipoInforme();
                	String generatedFile;
                	if (Names.JASPER.get().equals(selectedType)) {
                	    generatedFile = reportService.generateJasperReport(item);
                	} else if (Names.ITEXT.get().equals(selectedType)) {
                	    generatedFile = reportService.generateITextReport(item);
                	} else if (Names.EXCEL.get().equals(selectedType)) {
                	    generatedFile = reportService.generateExcelReport(item);
                	} else {
                	    throw new IllegalArgumentException(Names.MENSAJE_TIPO_INFORME_NO_SOPORTADO.get() + selectedType);
                	}

                	emailService.sendEmailWithAttachments(
                	        item.getEmail(),
                	        Names.MENSAJE_INFORME_GENERADO.get(),
                	        String.format(Names.MENSAJE_AQUI_TIENES_TU_INFORME.get(), item.getNombre()),
                	        generatedFile
                	);

                    return item;
                })
                .writer(items -> System.out.println(Names.MENSAJE_EMAIL_ENVIADO_A.get() + items.getItems().get(0).getEmail()))
                .build();
    }


    @Bean
    Job generateAndSendReportsJob(JobRepository jobRepository, Step generateAndSendReportStep) {
        return new JobBuilder(JOB_NAME, jobRepository)
                .incrementer(new RunIdIncrementer())
                .start(generateAndSendReportStep)
                .build();
    }
}