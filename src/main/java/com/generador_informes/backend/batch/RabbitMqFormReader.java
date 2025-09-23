package com.generador_informes.backend.batch;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.batch.item.ItemReader;

import com.generador_informes.backend.Names;
import com.generador_informes.backend.dto.FormDataDto;

public class RabbitMqFormReader implements ItemReader<FormDataDto> {

    private final RabbitTemplate rabbitTemplate;
    private final String queueName;

    public RabbitMqFormReader(RabbitTemplate rabbitTemplate, String queueName) {
        this.rabbitTemplate = rabbitTemplate;
        this.queueName = queueName;
    }

    @Override
    public FormDataDto read() {
        FormDataDto item = (FormDataDto) rabbitTemplate.receiveAndConvert(queueName);
        if (item != null) {
            System.out.println(Names.MENSAJE_LEIDO_DE_LA_COLA.get() + item.getNombre());
        } else {
            System.out.println(Names.MENSAJE_NO_HAY_MENSAJES_COLA.get());
        }
        return item;
    }
}
