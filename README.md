# LogHub

Sistema centralizado de monitoreo y registro de eventos para aplicaciones Spring Boot.

LogHub permite que aplicaciones externas envíen eventos y logs mediante una API REST segura utilizando API Keys.

---

## Características

- Registro centralizado de logs
- API REST con Spring Boot
- Autenticación mediante API Key
- Persistencia con PostgreSQL
- Swagger/OpenAPI
- Integración entre aplicaciones
- Filtrado de logs por aplicación y rango de fechas

---

## Tecnologías utilizadas

- Java 21
- Spring Boot 3
- Spring Security
- Spring Data JPA
- PostgreSQL
- Swagger/OpenAPI
- Lombok

---

## Configuración

### 1. Clonar el repositorio

```bash
git clone https://github.com/TU-USUARIO/loghub.git
cd loghub
```

### 2. Crear archivo `.env`

```env
DB_URL=jdbc:postgresql://HOST:PORT/DATABASE
DB_USER=TU_USUARIO
DB_PASS=TU_PASSWORD
```

### 3. Ejecutar la aplicación

```bash
./mvnw spring-boot:run
```

La aplicación se ejecutará en:

```text
http://localhost:8080
```

---

## Swagger UI

Documentación interactiva disponible en:

```text
http://localhost:8080/swagger-ui/index.html
```

---

## Endpoints principales

### Registrar un log

```http
POST /logs
```

#### Headers

```http
X-API-KEY: tu-api-key
```

#### Body

```json
{
  "message": "GET /tasks -> 200",
  "logLevel": "INFO",
  "appId": 1
}
```

---

### Obtener logs de una aplicación

```http
GET /logs/application/{appId}
```

---

### Filtrar logs por fecha

```http
GET /logs/application/{appId}/dates
```

#### Parámetros

- from
- to

#### Ejemplo

```text
/logs/application/1/dates?from=2026-05-01T00:00:00&to=2026-05-30T23:59:59
```

---

# Integración de ejemplo con TaskAPI

Este proyecto fue probado utilizando una aplicación externa llamada **TaskAPI**.
[TaskAPI](https://github.com/nazareno11/TaskApi.git)

TaskAPI intercepta todas las requests HTTP mediante un filtro (`OncePerRequestFilter`) y envía automáticamente logs hacia LogHub usando `RestTemplate`.

## Arquitectura

```text
TaskAPI → LogHub → PostgreSQL
```

## Ejemplos reales de logs

- GET /tasks -> 200
- POST /tasks -> 201
- DELETE /tasks/1 -> 204

---

## ¿Cómo funciona la integración?

### TaskAPI

1. Intercepta requests HTTP
2. Obtiene:
   - método HTTP
   - path
   - código de estado
3. Envía el evento a LogHub mediante una petición REST autenticada con API Key

---

## Autor

Nazareno Luna