# Taller - Branch Capa Web: Servicios Web

## Requerimientos (Contactos Telefónicos)

- Un contacto conoce una lista de números de teléfono
- Un contacto posee un nombre que no debe tener más de 35 caracteres ni menos de 2.
- Un número de teléfono tiene un código de área y el número en sí.
- El código de áre tiene 4 dígitos el número un máximo de 7 caracteres y mínimo de 6.
- Una agenda conoce todos sus contactos y permite listarlos.
- La agenda permite agregar contactos.
- No deben existir contactos con el mismo nombre.

## Spring Framework (SpringBoot)

- Cuando nos acoplamos a un framework, tenemos muchas ventajas, pero tambien estamos atados a su schedule:
    - https://spring.io/projects/spring-boot#support
- **Spring** nació como un framework de *Inyección de Depedencias*.
    - Se encarga de instanciar e inyectar colaboradores en los objetos.
    - No cualquier objeto, básicamente los servicios, connection a la BD, etc.
    - Las entidades, value objects no lo puede hacer, son stateful.
- Módulos como SpringMVC, SpringData, SpringSecurity, etc. se apoyan en el core de Spring.
- **SpringBoot**: simplifica la configuración de todos los módulos para que funcione "out of the box".

### Model View Controller

- El patrón MVC viene de Smalltalk. El primer paper en describirlo es de
  1988: https://www.lri.fr/~mbl/ENS/FONDIHM/2013/papers/Krasner-JOOP88.pdf

### Main Class

- Anotada con `@SpringBootApplication` que es una combinación de:
    - @Configuration indica que la clase puede contener definiciones de beans.
    - @EnableAutoConfiguration habilita la configuración automática de Spring Boot basada en las dependencias
      presentes en el classpath.
- @ComponentScan es donde se definen sobre que paquetes se escanea encontrar clases anotadas para inyectar.

### Controllers

- Capa **muy fina** que expone las capa de servicios como servicios web/http.
- Anotamos la clase con `@Controller` para que Spring la detecte y sepa que es un controlador.
- Anotamos los métodos con `@GetMapping`, `@PostMapping`, segun el método HTTP.
- Los parámetros de los métodos pueden ser anotados con `@RequestParam`, `@PathVariable`, `@RequestBody`, etc.
- El retorno del método se convierte automáticamente a JSON (o XML si se configura) y se envía en la respuesta HTTP.

#### Returning Views

- SpringBoot se encarga de hacerlo por defecto usando Jackson.
- Pero, necesitamos que las clases tengan `getters` para que Jackson pueda serializar los atributos.
- Aca es donde hay que definir si quiero acoplar mi modelo de dominio a las clientes REST o no.

### Exception Handling Global

- Queremos manejar las excepciones de forma global y para ello el framework Web que usamos en general nos da una forma
  de hacerlo.
- Usar `@RestControllerAdvice` para anotar una clase que maneje excepciones globalmente.
- Dentro de esa clase, podemos definir métodos que manejen excepciones específicas usando
  `@ExceptionHandler(Exception.class)`.

### Testing Integracion Controllers Web

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
