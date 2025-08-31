# Taller - Branch Capa Web: Servicios Web

## Requerimientos (Contactos Telefónicos)

- Un contacto conoce una lista de números de teléfono
- Un contacto posee un nombre que no debe tener más de 35 caracteres ni menos de 2.
- Un número de teléfono tiene un código de área y el número en sí.
- El código de áre tiene 4 dígitos el número un máximo de 7 caracteres y mínimo de 6.
- Una agenda conoce todos sus contactos y permite listarlos.
- La agenda permite agregar contactos.
- No deben existir contactos con el mismo nombre.

## Servicios Web

En términos de arquitectura de software, un `servicio` es una aplicación o proceso que se encuentra *escuchando* en un
determinado host y puerto. Esperando recibir solicitudes de otros programas (clientes).
Un **servicio web** es un tipo especial de servicio que:

- Utiliza protocolos web como HTTP o HTTPS para comunicarse,
- Expone su funcionalidad a través de URLs,

Se llama *web* porque se construye sobre tecnologías propias de la web (como HTTP, URIs y formatos como JSON o XML).

Los servicios web permiten:

- Separar el frontend (cliente) del backend (servidor),
- Reutilizar lógica de negocio o datos en distintas interfaces (por ejemplo, web, móvil, otros sistemas),

### Reglas generales para nombres de URIs API REST

- ✅ Usar **nombres de recursos en plural**
- ✅ Usar **nombres sustantivos, no verbos**
- ✅ Evitar extensiones como `.json`, `.xml` en la URI
- ✅ El **verbo va en el método HTTP**, no en la URI

### 🔸 GET

| Acción                  | URI ejemplo                      | Descripción                     |
|-------------------------|----------------------------------|---------------------------------|
| Obtener todos           | `GET /users`                     | Lista de usuarios               |
| Obtener uno             | `GET /users/{id}`                | Usuario por ID                  |
| Sub-recursos            | `GET /users/{id}/posts`          | Posts del usuario               |
| Filtro con query params | `GET /products?category=zapatos` | Filtrar productos por categoría |

---

### 🔸 POST

| Acción            | URI ejemplo                  | Descripción                    |
|-------------------|------------------------------|--------------------------------|
| Crear recurso     | `POST /users`                | Crear un nuevo usuario         |
| Crear sub-recurso | `POST /users/{id}/telefonos` | Crear un post para ese usuario |

---

### 🔸 PUT

| Acción             | URI ejemplo       | Descripción                        |
|--------------------|-------------------|------------------------------------|
| Reemplazar recurso | `PUT /users/{id}` | Reemplaza completamente al usuario |

---

### 🔸 DELETE

| Acción           | URI ejemplo          | Descripción             |
|------------------|----------------------|-------------------------|
| Eliminar recurso | `DELETE /users/{id}` | Borra un usuario por ID |

## Otros Casos

| Caso           | URI ejemplo                | Descripción        |
|----------------|----------------------------|--------------------|
| Login          | `POST /auth/login`         | Autenticación      |
| Logout         | `POST /auth/logout`        | Cierre de sesión   |
| Acción puntual | `POST /orders/{id}/cancel` | Cancelar una orden |

## ✅ Códigos de respuesta recomendados

| Método | Código recomendado          | Cuándo usarlo                          |
|--------|-----------------------------|----------------------------------------|
| GET    | `200 OK`                    | Recurso(s) obtenido(s) correctamente   |
| POST   | `201 Created`               | Recurso creado exitosamente            |
| PUT    | `200 OK` / `204 No Content` | Actualización o creación de recurso    |
| DELETE | `200 OK` / `204 No Content` | Ok o Eliminación exitosa sin contenido |

## Spring Framework (SpringBoot)

- Cuando nos acoplamos a un framework, tenemos muchas ventajas, pero tambien estamos atados a su schedule:
    - https://spring.io/projects/spring-boot#support
- **Spring** nació como un framework de *Inyección de Depedencias*.
    - Se encarga de instanciar e inyectar colaboradores en los objetos.
    - No cualquier objeto, básicamente los servicios, connection a la BD, etc.
    - Las entidades, value objects no lo puede hacer, son stateful.
- Módulos como SpringMVC, SpringData, SpringSecurity, etc. se apoyan en el core de Spring.
- **SpringBoot**: simplifica la configuración de todos los módulos para que funcione "out of the box".

### Main Class

- Anotada con `@SpringBootApplication` que es una combinación de:
    - @Configuration indica que la clase puede contener definiciones de beans.
    - @EnableAutoConfiguration habilita la configuración automática de Spring Boot basada en las dependencias
      presentes en el classpath.
- @ComponentScan es donde se definen sobre que paquetes se escanea encontrar clases anotadas para inyectar.

### Rest Controllers

- Capa **muy fina** que expone las capa de servicios como servicios web/http.
- Anotamos la clase con `@RestController` para que Spring la detecte y sepa que es un controlador REST.
- Anotamos los métodos con `@GetMapping`, `@PostMapping`, segun el método HTTP.
- Los parámetros de los métodos pueden ser anotados con `@RequestParam`, `@PathVariable`, `@RequestBody`, etc.
- El retorno del método se convierte automáticamente a JSON (o XML si se configura) y se envía en la respuesta HTTP.

#### Returning JSON

- SpringBoot se encarga de hacerlo por defecto usando Jackson.
- Pero, necesitamos que las clases tengan `getters` para que Jackson pueda serializar los atributos.
- Aca es donde hay que definir si quiero acoplar mi modelo de dominio a las clientes REST o no.

### Exception Handling Global

- Queremos manejar las excepciones de forma global y para ello el framework Web que usamos en general nos da una forma
  de hacerlo.
- Usar `@RestControllerAdvice` para anotar una clase que maneje excepciones globalmente.
- Dentro de esa clase, podemos definir métodos que manejen excepciones específicas usando
  `@ExceptionHandler(Exception.class)`.

### Testing Integracion Servicios Web

- *Profiles*: Necesito crear instancias de objetos diferentes para tests de integración que para producción.
- MockMvc and WebTestClient: [Spring Docs](https://docs.spring.io/spring-framework/reference/testing.html).
- MockMvc ejecuta el controller y todo el stack en memoria, sin servidor, sin red. Perfecto para tests de integración
  rápidos y realistas a nivel de capa web.
- La otra es WebTestCliente como cliente y levantar un server real con @SpringBootTest(webEnvironment =
  WebEnvironment.DEFINED_PORT o RANDOM_PORT))
- Teniendo Tests escritos unitario y de integración a nivel servicio. ¿*Qué podemos testear de la capa web*?
    - Todo lo relacionado a las pocas líneas de código que debería haber en el controlador. Pero principalmente:
        - Que pasa si llega o no llegan los parámetros (query params, path variables, body).
        - Que retorne el json que esperamos en el formato que esperamos
        - Que retorne errores en el formato que esperamos (manejo de exceptions).

### Maven: compiler and builder

- Configuro el compilador con lombok y el flag -parameters.
- Agrego el plugin de spring-boot-maven-plugin que me permite entre otras cosas ejecutar la app con
  `mvn spring-boot:run`.
- `mvn clean compile`, lueg `mvn spring-boot:run`.
