<p align="center">
  <img src="https://github.com/user-attachments/assets/127d946f-4042-4ff5-b7ee-1ca181117e8d" alt="Logo" width="150"/>
</p>

# Backend - Generador de Informes

**Autor:** Guillermo David García  
**Licencia:** MIT License

---

## Descripción

Este proyecto es el **backend** de la [aplicación de escritorio JavaFX](https://github.com/Guillermo-David/generador_informes_frontend) **Generador de Informes**. Proporciona una **API REST** que procesa formularios y genera informes en distintos formatos.

### Flujo general:

1. El front envía los datos del formulario al backend vía REST.
2. El backend coloca la información en una **cola de RabbitMQ**.
3. Un **batch job** consume los mensajes de la cola.
4. Un servicio procesa los datos y genera el informe solicitado:
   - **PDF:** JasperReports o iText
   - **Excel:** Apache POI
5. Los informes se envían automáticamente al email indicado por el usuario.

---

## Tecnologías

- Java 21
- JavaFX 21
- Spring Boot 3
- Spring Batch
- JasperReports / iText / Apache POI
- Docker
- RabbitMQ y Postgres en contenedores Docker
- Spring Mail (JavaMailSender)
- Maven para gestión de dependencias
- H2 para pruebas unitarias

---

## Endpoints

| Método | URL   | Descripción |
|--------|-------|-------------|
| POST   | `/form` | Recibe los datos del formulario y genera el informe según el tipo seleccionado (Jasper, iText o Excel). Envía el informe por email. |

**Ejemplo de request:**
```json
{
  "nombre": "Juan Pérez",
  "email": "juan@example.com",
  "tipoInforme": "Jasper",
  "comentario": "Este es un comentario de prueba"
}
```

---

## Funcionalidades

- Recepción de datos vía REST.
- Validación de campos obligatorios.
- Generación de informes:
  - PDF con JasperReports
  - PDF con iText
  - Excel con Apache POI
- Envío automático de correos con los informes adjuntos.
- Configuración flexible de rutas de salida y parámetros de email.
- Manejo de errores con logs claros para debugging.

---

## Instalación y ejecución

1. Clona los repositorios:

```bash
git clone https://github.com/Guillermo-David/generador_informes_backend.git
git clone https://github.com/Guillermo-David/generador_informes_frontend.git
```
---
2. Construye los proyectos con Maven:
```bash
mvn clean install
```
---
3. Ejecuta el backend:
```bash
mvn spring-boot:run
```
---
4. Ejecuta el frontend:
```bash
mvn clean javafx:run
```
> **Nota:** Asegúrate de tener configurado tu servidor SMTP en el backend para el envío de correos.
---
### Opcional: levantar servicios con Docker
Antes de ejecutar el backend, puedes levantar Postgres y RabbitMQ usando Docker:

1. Levantar Postgres:
```bash
docker run --name generador-postgres -e POSTGRES_USER=postgres -e POSTGRES_PASSWORD=postgres -e POSTGRES_DB=postgres -p 5432:5432 -d postgres:15
```
---
2. Levantar RabbitMQ:
```bash
docker run --name generador-rabbit -p 5672:5672 -p 15672:15672 -d rabbitmq:3-management
```
---
3. Verificar que ambos contenedores están corriendo:
```bash
docker ps
```
> Con esto, el backend tendrá la base de datos y la cola d emensajería listas para funcionar.
---

## Licencia
Este proyecto está licenciado bajo MIT License. Puedes usar, copiar, modificar y distribuir este software, siempre conservando la atribución al autor.
