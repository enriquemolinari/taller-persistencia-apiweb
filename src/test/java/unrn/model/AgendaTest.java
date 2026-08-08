package unrn.model;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class AgendaTest {

    @Test
    @DisplayName("Agregar contacto incorpora contacto en agenda")
    void agregarContacto_incorporaContactoEnAgenda() {
        // Setup: Preparar el escenario
        var agenda = new Agenda();
        var contacto = new Contacto("Juan", List.of(new NumeroTelefono("0294", "4444444")));

        // Ejercitacion: Ejecutar la accion a probar
        agenda.agregarContacto(contacto);

        // Verificacion: Verificar el resultado esperado
        assertEquals(1, agenda.contactos().size(), "La agenda debe contener el contacto agregado");
    }

    @Test
    @DisplayName("Agregar contacto lanza excepcion cuando contacto es nulo")
    void agregarContacto_lanzaExcepcionCuandoContactoEsNulo() {
        // Setup: Preparar el escenario
        var agenda = new Agenda();

        // Ejercitacion y Verificacion
        var ex = assertThrows(RuntimeException.class, () -> agenda.agregarContacto(null));
        assertEquals(Agenda.ERROR_CONTACTO_INVALIDO, ex.getMessage(),
                "Debe lanzar el error correcto para contacto nulo");
    }

    @Test
    @DisplayName("Agregar contacto lanza excepcion cuando nombre ya existe ignorando mayusculas")
    void agregarContacto_lanzaExcepcionCuandoNombreYaExisteIgnorandoMayusculas() {
        // Setup: Preparar el escenario
        var agenda = new Agenda();
        agenda.agregarContacto(new Contacto("Juan", List.of(new NumeroTelefono("0294", "4444444"))));
        var duplicado = new Contacto("juan", List.of(new NumeroTelefono("0294", "5555555")));

        // Ejercitacion y Verificacion
        var ex = assertThrows(RuntimeException.class, () -> agenda.agregarContacto(duplicado));
        assertEquals(Agenda.ERROR_CONTACTO_DUPLICADO, ex.getMessage(),
                "Debe lanzar el error correcto para contacto duplicado por nombre");
    }

    @Test
    @DisplayName("Crear agenda lanza excepcion cuando lista de contactos es nula")
    void crearAgenda_lanzaExcepcionCuandoListaDeContactosEsNula() {
        // Setup: Preparar el escenario
        List<Contacto> contactosNulos = null;

        // Ejercitacion y Verificacion
        var ex = assertThrows(RuntimeException.class, () -> new Agenda(contactosNulos));
        assertEquals(Agenda.ERROR_LISTA_CONTACTOS_INVALIDA, ex.getMessage(),
                "Debe lanzar el error correcto para lista de contactos nula");
    }

    @Test
    @DisplayName("Crear agenda lanza excepcion cuando lista inicial tiene duplicados por nombre")
    void crearAgenda_lanzaExcepcionCuandoListaInicialTieneDuplicadosPorNombre() {
        // Setup: Preparar el escenario
        var contacto1 = new Contacto("Juan", List.of(new NumeroTelefono("0294", "4444444")));
        var contacto2 = new Contacto("juan", List.of(new NumeroTelefono("0294", "5555555")));

        // Ejercitacion y Verificacion
        var ex = assertThrows(RuntimeException.class, () -> new Agenda(List.of(contacto1, contacto2)));
        assertEquals(Agenda.ERROR_CONTACTO_DUPLICADO, ex.getMessage(),
                "Debe lanzar el error correcto para duplicados en lista inicial");
    }

    @Test
    @DisplayName("Contactos devuelve lista de solo lectura")
    void contactos_devuelveListaDeSoloLectura() {
        // Setup: Preparar el escenario
        var agenda = new Agenda(List.of(new Contacto("Juan", List.of(new NumeroTelefono("0294", "4444444")))));

        // Ejercitacion y Verificacion
        assertThrows(UnsupportedOperationException.class, () -> agenda.contactos().add(
                        new Contacto("Ana", List.of(new NumeroTelefono("0294", "5555555")))),
                "La lista de contactos expuesta debe ser de solo lectura");
    }
}
