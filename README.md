# LogHub

LogHub es una API centralizada para registrar y consultar logs de aplicaciones externas.

Permite que distintas APIs envíen información sobre sus peticiones HTTP y almacenarla en un único lugar.

## Tecnologías

- Java 21
- Spring Boot
- Spring Security
- Spring Data JPA
- PostgreSQL
- Maven
- Swagger / OpenAPI

---

# Instalación

## 1. Clonar el repositorio

```bash
git clone [https://github.com/nazareno11/LogHub.git](https://github.com/nazareno11/LogHub.git)
cd LogHub
```

## 2. Configurar las variables de entorno

Crear un archivo `.env` en la raíz del proyecto:

```env
DB_URL=jdbc:postgresql://HOST:PORT/DATABASE
DB_USERNAME=TU_USUARIO
DB_PASSWORD=TU_PASSWORD

LOGHUB_ADMIN_API_KEY=TU_ADMIN_API_KEY
```

El archivo `.env` no debe subirse al repositorio.

## 3. Ejecutar LogHub

```bash
./mvnw spring-boot:run
```

Por defecto, LogHub se ejecuta en:

```text
http://localhost:8081
```

---

# Health Check

LogHub cuenta con un endpoint para verificar que el servicio se encuentra funcionando:

```http
GET /health
```

Ejemplo:

```bash
curl http://localhost:8081/health
```

Respuesta:

```json
{
  "status": "UP",
  "service": "LogHub",
  "timestamp": "2026-09-08T19:00:00"
}
```

---

# Swagger

La documentación de la API está disponible en:

```text
http://localhost:8081/swagger-ui/index.html
```

Desde Swagger se pueden consultar y probar los endpoints disponibles.

---

# Registrar una aplicación

Antes de enviar logs, la aplicación que utilizará LogHub debe registrarse.

### Crear aplicación

```http
POST /applications
```

Requiere la API Key administrativa:

```http
X-ADMIN-API-KEY: TU_ADMIN_API_KEY
```

Body:

```json
{
  "name": "Mi API",
  "description": "API de ejemplo",
  "email": "ejemplo@email.com"
}
```

Ejemplo de respuesta:

```json
{
  "id": 1,
  "name": "Mi API",
  "description": "API de ejemplo",
  "email": "ejemplo@email.com",
  "apiKey": "xxxxxxxx-xxxx-xxxx-xxxx-xxxxxxxxxxxx"
}
```

La API Key generada pertenece exclusivamente a esa aplicación y debe guardarse de forma segura.

### Consultar aplicaciones

```http
GET /applications
```

Requiere:

```http
X-ADMIN-API-KEY: TU_ADMIN_API_KEY
```

Este endpoint devuelve las aplicaciones registradas, pero no expone sus API Keys.

---

# Enviar logs

Una aplicación registrada puede enviar logs utilizando su API Key.

```http
POST /logs
```

Header:

```http
X-API-KEY: API_KEY_DE_LA_APLICACION
Content-Type: application/json
```

Body:

```json
{
  "message": "GET /api/productos",
  "logLevel": "INFO",
  "appId": 1,
  "statusCode": 200,
  "durationMs": 45
}
```

### Niveles disponibles

- `INFO`
- `WARNING`
- `ERROR`
- `CRITICAL`

### Clasificación recomendada

| Código HTTP | Nivel |
| --- | --- |
| 2xx | `INFO` |
| 3xx | `INFO` |
| 4xx | `WARNING` |
| 5xx | `ERROR` |
| Situaciones realmente críticas | `CRITICAL` |

---

# Consultar logs

Para consultar los logs de una aplicación:

```http
GET /logs/application/{appId}
```

Header:

```http
X-API-KEY: API_KEY_DE_LA_APLICACION
```

Ejemplo:

```bash
curl \
  -H "X-API-KEY: API_KEY_DE_LA_APLICACION" \
  http://localhost:8081/logs/application/1
```

### Consultar logs por fecha

También se pueden filtrar los logs por un rango de fechas:

```http
GET /logs/application/{appId}/dates?from=&to=
```

Ejemplo:

```http
GET /logs/application/1/dates?from=2026-09-08T00:00:00&to=2026-09-08T23:59:59
```

Header:

```http
X-API-KEY: API_KEY_DE_LA_APLICACION
```

---

# Integrar otra API con LogHub

Cualquier API puede utilizar LogHub siempre que pueda realizar peticiones HTTP.
La integración requiere tres datos:

```env
LOGHUB_URL=http://localhost:8081
LOGHUB_API_KEY=API_KEY_DE_LA_APLICACION
LOGHUB_APP_ID=ID_DE_LA_APLICACION
```

La aplicación debe enviar una petición `POST /logs` después de procesar cada request.
Por ejemplo:

```json
{
  "message": "GET /api/clientes",
  "logLevel": "INFO",
  "appId": 1,
  "statusCode": 200,
  "durationMs": 38
}
```

La API Key debe enviarse mediante el header:

```http
X-API-KEY: API_KEY_DE_LA_APLICACION
```

---

## Integración automática

En aplicaciones Spring Boot se puede implementar un filtro HTTP que:

1. Registre el momento en que comienza la petición.
2. Permita que la aplicación procese normalmente el request.
3. Obtenga el código de estado de la respuesta.
4. Calcule la duración.
5. Determine el nivel del log.
6. Envíe la información a LogHub.

De esta forma no es necesario agregar código de logging manualmente en cada controlador.

### Ejemplo del flujo:

```text
Cliente
   │
   ▼
API externa
   │
   ├── Procesa request
   │
   └── Envía información a LogHub
              │
              ▼
           LogHub
              │
              ▼
          PostgreSQL
```

---

# Seguridad

LogHub utiliza dos tipos de API Keys:

- **API Key administrativa (`X-ADMIN-API-KEY`)**: Se utiliza para administrar las aplicaciones registradas.
- **API Key de aplicación (`X-API-KEY`)**: Cada aplicación registrada tiene su propia API Key.

Las API Keys deben almacenarse mediante variables de entorno y nunca deben incluirse directamente en el código fuente ni subirse al repositorio.

---

# Dashboard

El proyecto incluye un dashboard web para realizar pruebas sobre LogHub.
Desde el dashboard se puede:

- Registrar aplicaciones.
- Consultar aplicaciones.
- Enviar logs.
- Consultar logs.
- Filtrar logs por fechas.
- Visualizar método HTTP, endpoint, código de estado, duración e IP.

Para utilizarlo, configurar como URL del backend:

```text
http://localhost:8081
```

y utilizar las API Keys correspondientes.

---

# Ejemplo de integración

Una aplicación externa puede registrar una petición como:

```http
GET /api/clientes
```

Si la respuesta es:

```text
HTTP 200
Duración: 52 ms
```

puede enviar a LogHub:

```json
{
  "message": "GET /api/clientes",
  "logLevel": "INFO",
  "appId": 1,
  "statusCode": 200,
  "durationMs": 52
}
```

LogHub almacena el evento junto con información adicional de la petición, como:

- Aplicación.
- IP del cliente.
- Método HTTP.
- Endpoint.
- Fecha y hora.
- Código de estado.
- Duración.
- Nivel del log.

<img width="1400" height="1050" alt="image" src="https://github.com/user-attachments/assets/94567f45-084b-4fef-9d90-802e6a67561b" />

