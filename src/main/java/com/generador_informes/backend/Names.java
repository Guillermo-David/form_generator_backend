package com.generador_informes.backend;

public enum Names {

	QUEUE_NAME("formQueue"),
	
	TIME_LC("time"),
    
    NOMBRE("Nombre"),
    NOMBRE_LC("nombre"),
    NOMBRE_LABEL("Nombre:"),
    
    EMAIL("Email"),
    EMAIL_LC("email"),
    EMAIL_LABEL("Email:"),
    
    TIPO_INFORME("Tipo de Informe"),
    TIPO_INFORME_LC("tipoInforme"),
    TIPO_INFORME_LABEL("Tipo de Informe:"),
    
    COMENTARIO("Comentario"),
    COMENTARIO_LC("comentario"),
    COMENTARIO_LABEL("Comentario:"),
    
    FORMULARIO_TITULO("Formulario de Informe"),
    INFORME("Informe"),
    ENVIAR("Enviar"),
    ERROR("Error"),
    EXITO("Éxito"),
    JASPER("Jasper"),
    JASPER_LC("jasper"),
    ITEXT("iText"),
    EXCEL("Excel"),
    SIN_NOMBRE("sin-nombre"),
    SIN_EMAIL("sin-email"),
    
    ALERTA_CAMPOS_OBLIGATORIOS("Por favor rellena todos los campos obligatorios."),
    ALERTA_ENVIO_CORRECTO("Formulario enviado correctamente."),
    ALERTA_ENVIO_ERROR("No se pudo enviar el formulario: "), 
    
    INFORME_JASPER_GENERADO("Informe Jasper generado: "),
    INFORME_JASPER_SUFIJO("_jasper.pdf"),

    INFORME_ITEXT_GENERADO("Informe iText generado: "),
    INFORME_ITEXT_SUFIJO("_itext.pdf"),
	
    INFORME_EXCEL_GENERADO("Informe Excel generado: "), 
    INFORME_EXCEL_SUFIJO("_informe.xlsx"), 

    MENSAJE_AQUI_TIENES_TU_INFORME("Hola %s, adjunto tu informe."),
    MENSAJE_TIPO_INFORME_NO_SOPORTADO("Tipo de informe no soportado: "),
    MENSAJE_INFORME_GENERADO("Informe generado"),
    MENSAJE_EMAIL_ENVIADO_A("Correo enviado a "),
    MENSAJE_LEIDO_DE_LA_COLA("Leído de la cola: "),
    MENSAJE_NO_HAY_MENSAJES_COLA("No hay mensajes en la cola"),
    MENSAJE_FORMULARIO_RECIBIDO("Formulario recibido y enviado a la cola"),

	EMAIL_ENVIADO("Email enviado a: "), 
	
	MENSAJE_ENVIADO_COLA("Mensaje enviado a la cola: ");

    private final String texto;

    Names(String texto) {
        this.texto = texto;
    }

    public String get() {
        return texto;
    }
}