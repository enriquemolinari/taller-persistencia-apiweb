package unrn.web;

public record NuevoContacto(
        Integer idUser, // inseguro, ELIMINAR
        String nombre,
        String codigoArea,
        String telefono) {
}
