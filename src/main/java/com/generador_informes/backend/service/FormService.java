package com.generador_informes.backend.service;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.generador_informes.backend.Names;
import com.generador_informes.backend.config.RabbitMQConfig;
import com.generador_informes.backend.dto.FormDataDto;

@Service
public class FormService {

    @Autowired
    private final RabbitTemplate rabbitTemplate;

    public FormService(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
        this.rabbitTemplate.setMessageConverter(new Jackson2JsonMessageConverter());
    }

    public void sendToQueue(FormDataDto formData) {
        rabbitTemplate.convertAndSend(RabbitMQConfig.QUEUE_NAME, formData);
        System.out.println(Names.MENSAJE_ENVIADO_COLA.get() + formData.getNombre());
    }
}