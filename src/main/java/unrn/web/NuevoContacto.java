package unrn.web;

public record NuevoContacto(
        Integer idUser, // inseguro, falta authenticacion
        String nombre,
        String codigoArea,
        String telefono) {
}
