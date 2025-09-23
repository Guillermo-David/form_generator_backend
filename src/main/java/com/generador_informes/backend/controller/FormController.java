package com.generador_informes.backend.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.generador_informes.backend.Names;
import com.generador_informes.backend.dto.FormDataDto;
import com.generador_informes.backend.service.FormService;

@RestController
@RequestMapping("/form")
public class FormController {

    @Autowired
    private FormService formService;

    @PostMapping
    public String submitForm(@RequestBody FormDataDto formData) {
        formService.sendToQueue(formData);
        return Names.MENSAJE_FORMULARIO_RECIBIDO.get();
    }
}