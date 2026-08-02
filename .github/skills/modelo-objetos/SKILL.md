---
name: modelo-objetos
description: Guía para diseñar clases del modelo de dominio en Java siguiendo principios de OOP robustos. Usá este skill cuando vayas a crear o modificar clases del modelo en src/main/java/unrn/model/.
---

## Contexto del Proyecto

- Proyecto Java 23 con paradigma orientado a objetos
- Modelo de dominio implementa todas las reglas de negocio
- Ubicación: `src/main/java/unrn/model/`

## Principios Fundamentales

### 1. No Objetos Anémicos

**NUNCA generes getters ni setters**. Los objetos deben tener comportamiento, no solo datos.

❌ **Incorrecto:**
```java
public class Cliente {
    private String nombre;
    
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
}
```

✅ **Correcto:**
```java
public class Cliente {
    private final String nombre;
    
    public Cliente(String nombre) {
        assertNombreValido(nombre);
        this.nombre = nombre;
    }
    
    public void cambiarNombre(String nuevoNombre) {
        assertNombreValido(nuevoNombre);
        // lógica de cambio de nombre
    }
}
```

### 2. Encapsulación de Colecciones

Si necesitas devolver una lista, **devuélvela solo lectura**:

```java
public List<Contacto> contactos() {
    return Collections.unmodifiableList(contactos);
}
```

### 3. Objetos Completos desde el Constructor

Los objetos se inicializan **siempre por constructor**, generando objetos completos y listos para usar:

```java
public Cliente(String nombre, String email, List<Contacto> contactos) {
    assertNombreValido(nombre);
    assertEmailValido(email);
    assertContactosNoVacio(contactos);
    
    this.nombre = nombre;
    this.email = email;
    this.contactos = new ArrayList<>(contactos);
}
```

### 4. Validaciones en el Constructor

Pon **todas las validaciones en el constructor** siempre que sea posible, para instanciar objetos válidos.

### 5. Manejo de Excepciones

- **Usa siempre RuntimeException** para errores de validación
- **El mensaje de error va en una constante estática** con visibilidad de paquete para usarla en tests:

```java
public class Cliente {
    static final String ERROR_NOMBRE_INVALIDO = "El nombre no puede ser nulo o vacío";
    static final String ERROR_EMAIL_INVALIDO = "El email debe tener formato válido";
    
    public Cliente(String nombre, String email) {
        assertNombreValido(nombre);
        assertEmailValido(email);
        this.nombre = nombre;
        this.email = email;
    }
    
    private void assertNombreValido(String nombre) {
        if (nombre == null || nombre.isBlank()) {
            throw new RuntimeException(ERROR_NOMBRE_INVALIDO);
        }
    }
    
    private void assertEmailValido(String email) {
        if (email == null || !email.contains("@")) {
            throw new RuntimeException(ERROR_EMAIL_INVALIDO);
        }
    }
}
```

### 6. Patrón de Validación

Cada validación del constructor **hazla en un método privado** llamado `assert{LO_QUE_ESTAS_VALIDANDO}`:

```java
private void assertNombreValido(String nombre) { ... }
private void assertEdadPositiva(int edad) { ... }
private void assertContactoUnico(Contacto contacto) { ... }
```

### 7. Tell, Don't Ask

**Usa el principio Tell Don't Ask siempre que sea posible**. Los objetos deben hacer cosas, no solo responder preguntas.

❌ **Incorrecto (Ask):**
```java
private void assertContactoUnico(Contacto contacto) {
    for (Contacto c : contactos) {
        if (c.nombre().equals(contacto.nombre())) {
            throw new RuntimeException(ERROR_CONTACTO_DUPLICADO);
        }
    }
}
```

✅ **Correcto (Tell):**
```java
private void assertContactoUnico(Contacto contacto) {
    if (contactos.stream().anyMatch(c -> c.esMismoNombre(contacto))) {
        throw new RuntimeException(ERROR_CONTACTO_DUPLICADO);
    }
}

// En la clase Contacto:
boolean esMismoNombre(Contacto otro) {
    return this.nombre.equals(otro.nombre);
}
```

## Checklist de Implementación

Cuando crees una clase del modelo de dominio:

- [ ] Constructor con todos los parámetros necesarios
- [ ] Validaciones en métodos `assertX()` privados
- [ ] Constantes estáticas (package-private) para mensajes de error
- [ ] RuntimeException para errores de validación
- [ ] Sin getters/setters (solo métodos de comportamiento)
- [ ] Listas encapsuladas devueltas como `unmodifiableList`
- [ ] Aplicar Tell Don't Ask en la lógica de negocio
