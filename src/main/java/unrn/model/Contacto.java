package unrn.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Contacto {
    static final String ERROR_NOMBRE_INVALIDO = "El nombre debe tener entre 2 y 35 caracteres";
    static final String ERROR_TELEFONOS_INVALIDOS = "La lista de telefonos no puede ser nula ni contener valores nulos";

    private final String nombre;
    private final List<NumeroTelefono> telefonos;

    public Contacto(String nombre, List<NumeroTelefono> telefonos) {
        assertNombreValido(nombre);
        assertTelefonosValidos(telefonos);
        this.nombre = nombre;
        this.telefonos = new ArrayList<>(telefonos);
    }

    public List<NumeroTelefono> telefonos() {
        return Collections.unmodifiableList(telefonos);
    }

    boolean esMismoNombre(Contacto otroContacto) {
        return otroContacto != null && nombre.equalsIgnoreCase(otroContacto.nombre);
    }

    private void assertNombreValido(String nombre) {
        if (nombre == null || nombre.length() < 2 || nombre.length() > 35) {
            throw new RuntimeException(ERROR_NOMBRE_INVALIDO);
        }
    }

    private void assertTelefonosValidos(List<NumeroTelefono> telefonos) {
        if (telefonos == null || telefonos.stream().anyMatch(telefono -> telefono == null)) {
            throw new RuntimeException(ERROR_TELEFONOS_INVALIDOS);
        }
    }
}
