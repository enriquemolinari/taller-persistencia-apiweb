# Taller - Persistencia, Servicios Web y Testing Automatizado

Este repositorio contiene diferentes ramas donde vamos iterando el desarrollo de un pequeño sistemas sobre una agenda de
teléfonos.

- Branches
    - [main](https://github.com/enriquemolinari/taller-persistencia-apiweb): Modelo de dominio y
      tests unitarios.
    - [persistencia](https://github.com/enriquemolinari/taller-persistencia-apiweb/tree/persistencia): Le agregamos
      JPA/Hibernate, más tests de integración.
    - [persistencia-con-repositorio](https://github.com/enriquemolinari/taller-persistencia-apiweb/tree/persistencia-con-repositorio):
      Refactorizamos un poco AgendaTelefonica para usar repositorios.
    - [capa-web-service-repositorios](https://github.com/enriquemolinari/taller-persistencia-apiweb/tree/capa-web-service-repositorios):
      Agregamos una capa web con SpringBoot y exponemos la funcionalidad mediante un servicio REST.

## Diseño Bottom-up vs Top-Down

El diseño Top-Down consiste en comenzar con una visión general del sistema y descomponerla en partes más pequeñas y
específicas. Primero se define la arquitectura, luego se diseñan los componentes individuales.

El diseño Bottom-up consiste en construir primero componentes individuales o de bajo nivel (en Objetos es el Modelo de
Dominio), y luego integrarlos con compontes de más alto nivel formando subsistemas y finalmente el sistema completo.

## TDD Inside-out vs Outside-in

El enfoque **inside-out** comienza escribiendo tests unitarios de bajo nivel sobre las clases del dominio, y
gradualmente se construye hacia fuera, integrando otros componentes del sistema.

- Se empieza por el **núcleo de la lógica de negocio**. En objetos, el modelo de dominio.
- Los tests unitarios guían la implementación de clases individuales.
- Luego se agregan capas externas como servicios, controladores, APIs.
- Conocido como: método clásico, Chicago school.
- **Menos riesgo** de escribir lógica fuera del modelo de dominio.
- Poca necesidad de usar fakes/mocks.

El enfoque **outside-in** comienza escribiendo tests de aceptación o de alto nivel desde el punto de vista del usuario o
cliente. Luego se escriben los tests de colaboración entre objetos (con mocks) para guiar el diseño interno.

- Se parte del **comportamiento visible del sistema**.
- Primero se escriben tests de aceptación o de capa externa (como APIs o UI).
- Se crean objetos simulados (*fakes*) para las dependencias internas aún no implementadas.
- El diseño interno emerge a medida que se satisfacen esos contratos.
- Conocido como: London school, Mockist style
- Aparece en el libro *Growing Object-Oriented Software, Guided by Tests*, de Steve Freeman y Nat Pryce.

## Requerimientos (Contactos Telefónicos)

- Un contacto conoce una lista de números de teléfono
- Un contacto posee un nombre que no debe tener más de 35 caracteres ni menos de 2.
- Un número de teléfono tiene un código de área y el número en sí.
- El código de áre tiene 4 dígitos el número un máximo de 7 caracteres y mínimo de 6.
- Una agenda conoce todos sus contactos y permite listarlos.
- La agenda permite agregar contactos.
- No deben existir contactos con el mismo nombre.

## Testing Unitario

- Usamos JUnit
- Nombre claro y descriptivo de la forma: *cuestionATestear_resultadoEsperado*
- Estructura del test:
    - Setup: Preparar el escenario
    - Ejercitación: Ejecutar la acción a probar
    - Verificación: Comprobar el resultado esperado
- Utiliza mensajes que expliquen el propósito de la verificación.
    - Por ejemplo:
      ```java
      assertEquals(expectedValue, actualValue, "El valor esperado no coincide con el valor actual");
      ```
- Verifica excepciones correctamente
    - Utiliza `assertThrows` para verificar que se lanza la excepción esperada.
    - Ejemplo:
      ```java
        var ex = assertThrows(RuntimeException.class, () -> {
                // Código que debería lanzar la excepción 
            });
            assertEquals("Mensaje de error esperado", ex.getMessage());
        ```

## Troubleshooting

- Si tenes este error:
    - jakarta.servlet.ServletException: Request processing failed: java.lang.IllegalArgumentException: Name for argument
      of type [int] not specified, and parameter name information not available via reflection. Ensure that the compiler
      uses the '-parameters' flag.
- En Settings > Build, Execution, Deployment > Compiler > Java Compiler, en Javac Options, agregar:
    - -parameters
- Luego ReBuild Project