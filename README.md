# Escuela Colombiana de Ingeniería Julio Garavito
## Arquitectura de Software – ARSW
### Laboratorio – Parte 2: BluePrints API con Seguridad JWT (OAuth 2.0)

Este laboratorio extiende la **Parte 1** ([Lab_P1_BluePrints_Java21_API](https://github.com/DECSIS-ECI/Lab_P1_BluePrints_Java21_API)) agregando **seguridad a la API** usando **Spring Boot 3, Java 21 y JWT (OAuth 2.0)**.  
El API se convierte en un **Resource Server** protegido por tokens Bearer firmados con **RS256**.  
Incluye un endpoint didáctico `/auth/login` que emite el token para facilitar las pruebas.

---

## Objetivos
- Implementar seguridad en servicios REST usando **OAuth2 Resource Server**.
- Configurar emisión y validación de **JWT**.
- Proteger endpoints con **roles y scopes** (`blueprints.read`, `blueprints.write`).
- Integrar la documentación de seguridad en **Swagger/OpenAPI**.

---

## Requisitos
- JDK 21
- Maven 3.9+
- Git

---

## Ejecución del proyecto
1. Clonar o descomprimir el proyecto:
   ```bash
   git clone https://github.com/DECSIS-ECI/Lab_P2_BluePrints_Java21_API_Security_JWT.git
   cd Lab_P2_BluePrints_Java21_API_Security_JWT
   ```
   ó si el profesor entrega el `.zip`, descomprimirlo y entrar en la carpeta.

2. Ejecutar con Maven:
   ```bash
   mvn -q -DskipTests spring-boot:run
   ```

3. Verificar que la aplicación levante en `http://localhost:8080`.

---

## Endpoints principales

### 1. Login (emite token)
```
POST http://localhost:8080/auth/login
Content-Type: application/json

{
  "username": "student",
  "password": "student123"
}
```
Respuesta:
```json
{
  "access_token": "eyJhbGciOiJSUzI1NiIsInR5cCI6IkpXVCJ9...",
  "token_type": "Bearer",
  "expires_in": 3600
}
```

### 2. Consultar blueprints (requiere scope `blueprints.read`)
```
GET http://localhost:8080/api/blueprints
Authorization: Bearer <ACCESS_TOKEN>
```

### 3. Crear blueprint (requiere scope `blueprints.write`)
```
POST http://localhost:8080/api/blueprints
Authorization: Bearer <ACCESS_TOKEN>
Content-Type: application/json

{
  "name": "Nuevo Plano"
}
```

---

## Swagger UI
- URL: [http://localhost:8080/swagger-ui/index.html](http://localhost:8080/swagger-ui/index.html)
- Pulsa **Authorize**, ingresa el token en el formato:
  ```
  Bearer eyJhbGciOi...
  ```

---

## Estructura del proyecto
```
src/main/java/co/edu/eci/blueprints/
  ├── api/BlueprintController.java       # Endpoints protegidos
  ├── auth/AuthController.java           # Login didáctico para emitir tokens
  ├── config/OpenApiConfig.java          # Configuración Swagger + JWT
  └── security/
       ├── SecurityConfig.java
       ├── MethodSecurityConfig.java
       ├── JwtKeyProvider.java
       ├── InMemoryUserService.java
       └── RsaKeyProperties.java
src/main/resources/
  └── application.yml
```

---

## Actividades propuestas
1. Revisar el código de configuración de seguridad (`SecurityConfig`) e identificar cómo se definen los endpoints públicos y protegidos.
2. Explorar el flujo de login y analizar las claims del JWT emitido.
3. Extender los scopes (`blueprints.read`, `blueprints.write`) para controlar otros endpoints de la API, del laboratorio P1 trabajado.
4. Modificar el tiempo de expiración del token y observar el efecto.
5. Documentar en Swagger los endpoints de autenticación y de negocio.

---

## Lecturas recomendadas
- [Spring Security Reference – OAuth2 Resource Server](https://docs.spring.io/spring-security/reference/servlet/oauth2/resource-server/index.html)
- [Spring Boot – Securing Web Applications](https://spring.io/guides/gs/securing-web/)
- [JSON Web Tokens – jwt.io](https://jwt.io/introduction)

---

## Licencia
Proyecto educativo con fines académicos – Escuela Colombiana de Ingeniería Julio Garavito.

---

## Respuestas

### Migración del laboratorio P1
Se migró toda la funcionalidad del laboratorio anterior al paquete `co.edu.eci.blueprints`: el modelo (`Blueprint`, `Point`), los DTOs (`ApiResponse`, `NewBlueprintRequest`), los filtros (identity, redundancy, undersampling por perfiles de Spring), la capa de persistencia (in-memory por defecto y PostgreSQL con JDBC/JSONB bajo el perfil `postgres`, con `docker-compose.yml` y `schema.sql`), el servicio `BlueprintsServices`, el controlador REST `api/BlueprintsAPIController` (`/api/v1/blueprints`) con su manejo global de errores y la documentación Swagger/OpenAPI. El controlador de demostración `BlueprintController` se reemplazó por el controlador real del P1.

### Actividad 1 — Endpoints públicos y protegidos en `SecurityConfig`
En `SecurityConfig.filterChain` se definen las reglas con `authorizeHttpRequests`:
- **Públicos** (`permitAll`): `/auth/login`, `/actuator/health` y la documentación (`/v3/api-docs/**`, `/swagger-ui/**`, `/swagger-ui.html`).
- **Protegidos**: todo `/api/**` exige un JWT válido con alguno de los scopes `blueprints.read` o `blueprints.write` (`hasAnyAuthority("SCOPE_...")`), y cualquier otra ruta cae en `anyRequest().authenticated()`. La validación del token la hace el `oauth2ResourceServer` con el `JwtDecoder` basado en la llave pública RSA.

### Actividad 2 — Flujo de login y claims del JWT
`POST /auth/login` valida las credenciales contra `InMemoryUserService` (BCrypt). Si son válidas, `AuthController` construye un `JwtClaimsSet` y lo firma con RS256 usando la llave privada generada por `JwtKeyProvider`. Las claims del token emitido son:
- `iss`: `https://decsis-eci/blueprints` (configurado en `application.yml`).
- `sub`: el username autenticado (p. ej. `student`).
- `iat` / `exp`: emisión y expiración (TTL de 3600 s, `blueprints.security.token-ttl-seconds`).
- `scope`: `blueprints.read blueprints.write`, que Spring convierte en las authorities `SCOPE_blueprints.read` y `SCOPE_blueprints.write`.

### Actividad 3 — Scopes extendidos a los endpoints del P1
Los endpoints migrados del P1 quedaron protegidos con `@PreAuthorize` (habilitado por `MethodSecurityConfig`):
- Lecturas con `SCOPE_blueprints.read`: `GET /api/v1/blueprints`, `GET /api/v1/blueprints/{author}` y `GET /api/v1/blueprints/{author}/{bpname}`.
- Escrituras con `SCOPE_blueprints.write`: `POST /api/v1/blueprints` y `PUT /api/v1/blueprints/{author}/{bpname}/points`.

Con esto, un token que solo tenga el scope de lectura puede consultar planos pero recibe `403 Forbidden` al intentar crear o modificar.
