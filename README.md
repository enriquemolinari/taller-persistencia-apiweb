# Taller - Branch Persistencia Usando Repositorios

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

### Repositorios

> For each type of object that needs global access, create an object that can provide the illusion of an in-memory
> collection of all objects of that type. Set up access through a well-known global interface. Provide methods to *add*
> and *remove* objects, which will encapsulate the actual insertion or removal of data in the data store. Provide
> methods that *select objects based on some criteria* and return *fully instantiated objects* or collections of objects
> whose attribute values meet the criteria, thereby encapsulating the actual storage and query technology. Provide
> REPOSITORIES only for AGGREGATE roots that actually need direct access. Keep the client focused on the model,
> delegating all object storage and access to the REPOSITORIES. **Eric Evans DDD Book**.

- Collection-like interface. Con semántica de un Set (sin repetidos).
    - add(Contacto contacto)
    - remove(Contacto contacto)
    - Optional<Contacto> findByName(String name)
    - Optional<Contacto> findById(Long id)
    - List<Contacto> findXXX(...)
- Un Repository por agregate root.
- Se instancian recibiendo la transacción iniciada por quien lo inova.
- Otra idea clave es que no necesitas "volver a guardar" los objetos modificados que ya están en el Repositorio.
  Piensa nuevamente en cómo modificarías un objeto que forma parte de una colección. En realidad, es muy simple: solo
  recuperarías de la colección la referencia al objeto que deseas modificar y luego le pedirías al objeto que ejecute
  algún comportamiento de transición de estado invocando un método de comando. Implementing Domain Driven Design Vaughn
  Vernon.
    - Esto es posible por persistencia por alcance. No tiene que ver con el patron repositorio, sino que tiene que ver
      con el ORM utilizado para implementar el repositorio.
