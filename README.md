## API REST para un Foro usando Spring Boot | Challenge Foro Hub

### Colección de Postman

Para facilitar la prueba de los endpoints de esta API, puedes importar la colección de Postman que contiene todas las solicitudes configuradas:

[Descargar Colección de Postman](https://github.com/axel-23/challenge-one-forohub/blob/main/ForoHub.postman_collection.json)

#### Instrucciones de Uso:

1. **Descarga** la colección de Postman desde el enlace proporcionado.
2. **Importa** la colección en tu cliente de Postman:
   - Abre Postman.
   - En la barra lateral izquierda, haz clic en el botón "Importar" para abrir el menú de importación.
   - Selecciona el archivo JSON que acabas de descargar.
3. Una vez importada, verás la colección "Foro Hub API" en tu barra lateral de Postman.
4. **Configura las variables** necesarias como la URL base (`{{baseurl}}`) para apuntar a tu servidor local (http://localhost:8080) o a la URL de producción de la API.
5. **Realiza las solicitudes** a los diferentes endpoints (GET, POST, DELETE) para interactuar con la API y probar su funcionamiento.

---

### Configuración y Desarrollo

1. **Clonar el Repositorio**: Abrir una terminal (o CMD) y clona el repositorio Challenge ONE Literalura desde GitHub:

   ```bash
   git clone https://github.com/axel-23/challenge-one-literalura.git
   ```

2. **Configuración de la Base de Datos**: Para la persistencia de datos, configuré una base de datos PostgreSQL. Utilicé variables de entorno en el archivo `application.properties` para mantener la configuración de conexión segura y flexible. Las variables de entorno necesarias son:

    - `DB_USER`: Nombre de usuario de la base de datos.
    - `DB_PASSWORD`: Contraseña del usuario de la base de datos.
    - `DB_HOST`: Dirección del host donde se encuentra la base de datos.
    - `DB_NAME`: Nombre de la base de datos que se utilizará para el proyecto.
    
    Ejemplo de `application.properties`:
    
    ```properties
    spring.datasource.url=jdbc:mysql://${DB_HOST}/${DB_NAME}
    spring.datasource.username=${DB_USER}
    spring.datasource.password=${DB_PASSWORD}
    ```
    
### Descripción de los Endpoints

- **GET /topics**
  - Método: `GET`
  - Descripción: Obtiene la lista de todos los tópicos disponibles en el foro.
  - Códigos HTTP:
    ```http
    200 OK: Se devuelve la lista de tópicos correctamente.
    ```

- **POST /topics**
  - Método: `POST`
  - Descripción: Crea un nuevo tópico en el foro.
  - Códigos HTTP:
    ```http
    201 Created: Tópico creado exitosamente.
    ```

- **GET /topics/{id}**
  - Método: `GET`
  - Descripción: Obtiene información detallada de un tópico específico por su ID.
  - Códigos HTTP:
    ```http
    200 OK: Se devuelve la información del tópico correctamente.
    404 Not Found: No se encontró el tópico con el ID especificado.
    ```

- **DELETE /topics/{id}**
  - Método: `DELETE`
  - Descripción: Elimina un tópico específico según su ID.
  - Códigos HTTP:
    ```http
    204 No Content: Tópico eliminado correctamente.
    403 Forbidden: Intentar eliminar un topico que no fue creado por usuario.
    404 Not Found: No se encontró el tópico con el ID especificado.
    ```

- **POST /login**
  - Método: `POST`
  - Descripción: Autentica a los usuarios y genera un token JWT para acceder a recursos protegidos.
  - Códigos HTTP:
    ```http
    200 OK: Autenticación exitosa y se genera un token JWT.
    401 Unauthorized: Credenciales inválidas.
    ```

- **POST /register**
  - Método: `POST`
  - Descripción: Registra un nuevo usuario en el sistema del foro.
  - Códigos HTTP:
    ```http
    201 Created: Usuario registrado exitosamente.
    400 Bad Request: Error en los datos proporcionados para el registro.
    ```

### Autenticación con JSON Web Tokens (JWT)

- **POST /login**
  - Método: `POST`
  - Descripción: Autentica al usuario y genera un token JWT válido.
  - Uso: Después de la autenticación exitosa, se genera un token JWT que debe incluirse en las cabeceras de las solicitudes subsiguientes para acceder a los recursos protegidos `/topics`.
