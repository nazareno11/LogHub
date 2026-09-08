# LogHub

Sistema centralizado de monitoreo y registro de eventos para aplicaciones Spring Boot.

LogHub permite registrar y consultar eventos HTTP provenientes de aplicaciones externas mediante una API REST protegida con API Keys.

Actualmente se encuentra integrado con **HENNOVO**, cuya API envía automáticamente información de sus peticiones HTTP a LogHub.

---

## Características

* Registro centralizado de logs.
* Autenticación mediante API Keys.
* Registro de aplicaciones cliente.
* Identificación de la aplicación que genera cada log.
* Registro automático de:
  * Método HTTP.
  * Endpoint.
  * Código de estado HTTP.
  * Duración de la petición.
  * Dirección IP del cliente.
  * Fecha y hora.
  * Nivel del log.
* Clasificación automática de eventos según el código de estado.
* Consulta de logs por aplicación.
* Filtrado de logs por rango de fechas.
* Persistencia con PostgreSQL.
* Documentación mediante Swagger/OpenAPI.
* Integración automática con aplicaciones Spring Boot mediante filtros HTTP.
* Dashboard web para realizar pruebas y consultar los logs.

---

## Arquitectura

```text
┌──────────────┐
│   HENNOVO    │
│   :8080      │
└──────┬───────┘
       │
       │ HTTP + API Key
       ▼
┌──────────────┐
│    LogHub    │
│   :8081      │
└──────┬───────┘
       │
       ▼
┌──────────────┐
│  PostgreSQL  │
└──────────────┘
```

HENNOVO intercepta automáticamente sus peticiones HTTP y envía a LogHub información sobre cada request.

LogHub almacena los eventos y permite posteriormente consultarlos por aplicación y rango de fechas.

---

## Tecnologías utilizadas

* Java 21
* Spring Boot
* Spring Security
* Spring Data JPA
* PostgreSQL
* Maven
* Lombok
* Swagger / OpenAPI
* Java Dotenv

---

# Configuración

## 1. Clonar el repositorio

```bash
git clone [https://github.com/nazareno11/LogHub.git](https://github.com/nazareno11/LogHub.git)
cd LogHub
```

---

## 2. Configurar variables de entorno

Crear un archivo `.env` en la raíz del proyecto.

```env
DB_URL=jdbc:postgresql://HOST:PORT/DATABASE
DB_USERNAME=TU_USUARIO
DB_PASSWORD=TU_PASSWORD

LOGHUB_ADMIN_API_KEY=TU_ADMIN_API_KEY
```

La API Key administrativa se utiliza para administrar las aplicaciones registradas en LogHub.

El archivo `.env` no debe subirse al repositorio.

---

## 3. Ejecutar LogHub

```bash
./mvnw spring-boot:run
```

LogHub se ejecuta en:

```text
http://localhost:8081
```

El puerto `8081` se utiliza para evitar conflictos con HENNOVO, que utiliza el puerto `8080`.

---

# Health Check

LogHub dispone de un endpoint para verificar que el servicio se encuentra funcionando:

```http
GET /health
```

Ejemplo de respuesta:

```json
{
  "status": "UP",
  "service": "LogHub",
  "timestamp": "2026-09-08T19:00:00"
}
```

---

# Swagger

La documentación interactiva de la API está disponible en:

```text
http://localhost:8081/swagger-ui/index.html
```

---

# API

## Aplicaciones

### Registrar una aplicación

```http
POST /applications
```

Requiere:

```http
X-ADMIN-API-KEY: tu-admin-api-key
```

Body:

```json
{
  "name": "HENNOVO",
  "description": "API de gestión de HENNOVO",
  "email": "ejemplo@email.com"
}
```

Al registrarse, la aplicación recibe una API Key propia.

**La API Key debe guardarse de forma segura, ya que es la credencial utilizada por la aplicación para comunicarse con LogHub.**

---

### Listar aplicaciones

```http
GET /applications
```

Requiere:

```http
X-ADMIN-API-KEY: tu-admin-api-key
```

Las API Keys de las aplicaciones no se exponen en este endpoint.

---

# Logs

## Registrar un log

```http
POST /logs
```

Requiere:

```http
X-API-KEY: api-key-de-la-aplicacion
```

Body:

```json
{
  "message": "GET /api/productos/todos",
  "logLevel": "INFO",
  "appId": 1,
  "statusCode": 200,
  "durationMs": 42
}
```

### Niveles disponibles

```text
INFO
WARNING
ERROR
CRITICAL
```

### Clasificación utilizada

| Código HTTP | Nivel |
| --- | --- |
| 2xx | INFO |
| 3xx | INFO |
| 4xx | WARNING |
| 5xx | ERROR |
| Casos críticos específicos | CRITICAL |

---

## Consultar logs de una aplicación

```http
GET /logs/application/{appId}
```

Requiere:

```http
X-API-KEY: api-key-de-la-aplicacion
```

Ejemplo:

```text
GET /logs/application/1
```

---

## Filtrar logs por fecha

```http
GET /logs/application/{appId}/dates
```

Parámetros:

* `from`
* `to`

Ejemplo:

```text
GET /logs/application/1/dates?from=2026-09-08T00:00:00&to=2026-09-08T23:59:59
```

---

# Integración con HENNOVO

LogHub se encuentra integrado con HENNOVO mediante un filtro HTTP.

HENNOVO intercepta automáticamente las peticiones realizadas contra su API y, una vez obtenida la respuesta, envía a LogHub los datos principales de la operación.

No es necesario agregar código de logging individual en cada controlador o servicio de HENNOVO.

### Flujo

```text
Cliente
   │
   ▼
HENNOVO
   │
   │ Request
   ▼
LogHubLoggingFilter
   │
   ▼
Controlador HENNOVO
   │
   ▼
Response
   │
   ▼
LogHubClient
   │
   │ POST /logs
   ▼
LogHub
   │
   ▼
PostgreSQL
```

---

## Configuración de HENNOVO

En el `.env` de HENNOVO se deben configurar:

```env
LOGHUB_URL=http://localhost:8081
LOGHUB_API_KEY=API_KEY_DE_HENNOVO
LOGHUB_APP_ID=ID_DE_HENNOVO
```

Donde:

* `LOGHUB_URL` indica la dirección donde se encuentra ejecutándose LogHub.
* `LOGHUB_API_KEY` es la API Key asignada a HENNOVO.
* `LOGHUB_APP_ID` es el identificador de HENNOVO registrado en LogHub.

---

## Información registrada automáticamente

Cada petición realizada contra HENNOVO puede generar un evento en LogHub con información como:

```text
Método HTTP
Endpoint
Código de estado
Duración
IP del cliente
Fecha y hora
Nivel del log
Aplicación
```

Ejemplo:

```text
INFO
GET /api/productos/todos
Status: 200
Duration: 42 ms
```

Un error de recurso inexistente:

```text
WARNING
GET /api/productos/999999
Status: 404
Duration: 35 ms
```

Un error interno:

```text
ERROR
POST /api/pagos
Status: 500
Duration: 350 ms
```

---

# Seguridad

LogHub utiliza dos tipos de API Keys.

### API Key administrativa

```text
X-ADMIN-API-KEY
```

Se utiliza para operaciones administrativas, como registrar y consultar aplicaciones.

### API Key de aplicación

```text
X-API-KEY
```

Cada aplicación registrada posee su propia API Key.

Esta clave se utiliza para enviar y consultar los logs correspondientes a esa aplicación.

Las API Keys no deben almacenarse en el código fuente ni subirse al repositorio.

---

# Tolerancia a fallos

La comunicación entre HENNOVO y LogHub está diseñada para que una falla de LogHub no impida el funcionamiento de HENNOVO.

Si LogHub no se encuentra disponible, HENNOVO continúa procesando normalmente las peticiones y el error de comunicación se maneja dentro del cliente de logging.

Esto evita que el sistema de monitoreo se convierta en una dependencia crítica de la aplicación principal.

---

# Dashboard

El proyecto cuenta con un dashboard web para realizar pruebas y visualizar los logs registrados.

Desde el dashboard es posible:

* Configurar la URL de LogHub.
* Utilizar la API Key administrativa.
* Registrar aplicaciones.
* Consultar aplicaciones registradas.
* Utilizar la API Key de una aplicación.
* Registrar logs manualmente.
* Consultar logs.
* Filtrar logs por fechas.
* Visualizar método HTTP, endpoint, status, duración e IP.

---

# Pruebas realizadas

La integración fue probada verificando:

* Registro de aplicaciones.
* Generación de API Keys.
* Validación de API Keys.
* Rechazo de API Keys inválidas.
* Validación de `appId`.
* Registro de logs.
* Logs `INFO`.
* Logs `WARNING`.
* Logs `ERROR`.
* Logs `CRITICAL`.
* Códigos HTTP `2xx`, `4xx` y `5xx`.
* Registro de duración de las peticiones.
* Consulta de logs.
* Filtrado por rango de fechas.
* Integración automática con HENNOVO.
* Continuidad de funcionamiento de HENNOVO ante una caída de LogHub.
