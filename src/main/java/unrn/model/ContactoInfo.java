package unrn.model;

import java.util.List;

public record ContactoInfo(int contactoId, String nombre,
                           List<String> telefonos) {
    public boolean esDe(String nombre) {
        return this.nombre().equals(nombre);
    }

    public boolean tieneElTelefono(String numero) {
        return this.telefonos().stream()
                .anyMatch(telefono -> telefono.equals(numero));
    }

    public int cantidadDeTelefonos() {
        return this.telefonos().size();
    }
}
