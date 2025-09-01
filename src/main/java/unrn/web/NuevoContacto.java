package unrn.web;

public record NuevoContacto(
        String nombre,
        String codigoArea,
        String telefono) {
    static NuevoContacto empty() {
        return new NuevoContacto("", "", "");
    }
}
