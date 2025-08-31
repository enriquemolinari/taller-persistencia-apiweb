# Taller - Branch Persistencia

## Requerimientos (Contactos Telefónicos)

- Un contacto conoce una lista de números de teléfono
- Un contacto posee un nombre que no debe tener más de 35 caracteres ni menos de 2.
- Un número de teléfono tiene un código de área y el número en sí.
- El código de áre tiene 4 dígitos el número un máximo de 7 caracteres y mínimo de 6.
- Una agenda conoce todos sus contactos y permite listarlos.
- La agenda permite agregar contactos.
- No deben existir contactos con el mismo nombre.

## Persistencia

- Para persistencia usaremos JPA 3.2
  y [Hibernate 7](https://docs.jboss.org/hibernate/orm/7.0/introduction/html_single/Hibernate_Introduction.html)

- Agregamos dependencias en pom.xml
- Definimos cuál clase representa el sistema.
    - Será `AgendaTelefonica`
    - No la vamos a mapear como entidad porque manejar los contactos como collecion mapeada uno a muchos, dado que la
      cantidad de contactos puede ser grande sabemos que no performa bien.
    - Esta clase representa la entrada a la lógica de negocios del sistema.
    - Responsabilidades:
        - Gestiona las Transacciones
        - Sus servicios reciben tipos primitivos, o estructuras de datos simples.
        - Crea instancias del modelo, invoca sus servicios y los coordina.
        - Persiste o remueve si es necesario.
- Agrego Mapeos
    - Entidades con Id`@Entity`, `@Id`
    - Lombok: `@NoArgsConstructor(access = AccessLevel.PROTECTED)`, `@Getter(AccessLevel.PRIVATE)`, `@Setter(
      AccessLevel.PRIVATE)`
    - Y relaciones.
- Al implementar `AgendaTelefonica.agregarContacto(...)`
    - Se vuelve necesario validar el nombre de contacto cuya validación se encuentra en `Contacto`.
    - ¿Cómo reuso esa validación? Con un value object:`NombreDeContacto`.
- Al implementar `AgendaTelefonica.listarContactos()`
    - No puedo devolver grafos de objetos proxieados.
    - Además tengo que paginar si devuelvo colecciones.

## Testing Integracion

- [Hibernate 7 Docs](https://docs.jboss.org/hibernate/orm/7.0/introduction/html_single/Hibernate_Introduction.html#testing).
- ¿Qué testeamos aca?
    - End to end desde la clase de entrada a mi modelo, hasta la BD
    - Cambiamos la BD real para usar una en memoria (para que sea más rapido).
    - Cada test debe iniciar con la BD en el mismo estado (no hay dependencia entre los tests idealmente).
    - No re-testear lógica cubierta por tests del modelo.
    - Verifica persistencia real y recuperación de objetos de la BD
