package unrn.model;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ContactoTest {

    @Test
    @DisplayName("Crear contacto funciona con nombre y telefonos validos")
    void crearContacto_funcionaConNombreYTelefonosValidos() {
        // Setup: Preparar el escenario
        var telefono = new NumeroTelefono("0294", "4444444");

        // Ejercitacion: Ejecutar la accion a probar
        var contacto = new Contacto("Juan", List.of(telefono));

        // Verificacion: Verificar el resultado esperado
        assertEquals(1, contacto.telefonos().size(), "El contacto debe conservar sus telefonos");
    }

    @Test
    @DisplayName("Crear contacto lanza excepcion cuando nombre es nulo")
    void crearContacto_lanzaExcepcionCuandoNombreEsNulo() {
        // Setup: Preparar el escenario
        var telefono = new NumeroTelefono("0294", "4444444");

        // Ejercitacion y Verificacion
        var ex = assertThrows(RuntimeException.class, () -> new Contacto(null, List.of(telefono)));
        assertEquals(Contacto.ERROR_NOMBRE_INVALIDO, ex.getMessage(),
                "Debe lanzar el error correcto para nombre nulo");
    }

    @Test
    @DisplayName("Crear contacto lanza excepcion cuando nombre es menor a dos caracteres")
    void crearContacto_lanzaExcepcionCuandoNombreEsMenorADosCaracteres() {
        // Setup: Preparar el escenario
        var telefono = new NumeroTelefono("0294", "4444444");

        // Ejercitacion y Verificacion
        var ex = assertThrows(RuntimeException.class, () -> new Contacto("J", List.of(telefono)));
        assertEquals(Contacto.ERROR_NOMBRE_INVALIDO, ex.getMessage(),
                "Debe lanzar el error correcto para nombre demasiado corto");
    }

    @Test
    @DisplayName("Crear contacto lanza excepcion cuando nombre supera treinta y cinco caracteres")
    void crearContacto_lanzaExcepcionCuandoNombreSuperaTreintaYCincoCaracteres() {
        // Setup: Preparar el escenario
        var telefono = new NumeroTelefono("0294", "4444444");
        var nombreLargo = "123456789012345678901234567890123456";

        // Ejercitacion y Verificacion
        var ex = assertThrows(RuntimeException.class, () -> new Contacto(nombreLargo, List.of(telefono)));
        assertEquals(Contacto.ERROR_NOMBRE_INVALIDO, ex.getMessage(),
                "Debe lanzar el error correcto para nombre demasiado largo");
    }

    @Test
    @DisplayName("Crear contacto lanza excepcion cuando lista de telefonos es nula")
    void crearContacto_lanzaExcepcionCuandoListaDeTelefonosEsNula() {
        // Setup: Preparar el escenario
        List<NumeroTelefono> telefonosNulos = null;

        // Ejercitacion y Verificacion
        var ex = assertThrows(RuntimeException.class, () -> new Contacto("Juan", telefonosNulos));
        assertEquals(Contacto.ERROR_TELEFONOS_INVALIDOS, ex.getMessage(),
                "Debe lanzar el error correcto para lista de telefonos nula");
    }

    @Test
    @DisplayName("Crear contacto lanza excepcion cuando lista de telefonos contiene nulos")
    void crearContacto_lanzaExcepcionCuandoListaDeTelefonosContieneNulos() {
        // Setup: Preparar el escenario
        var telefonos = new ArrayList<NumeroTelefono>();
        telefonos.add(new NumeroTelefono("0294", "4444444"));
        telefonos.add(null);

        // Ejercitacion y Verificacion
        var ex = assertThrows(RuntimeException.class, () -> new Contacto("Juan", telefonos));
        assertEquals(Contacto.ERROR_TELEFONOS_INVALIDOS, ex.getMessage(),
                "Debe lanzar el error correcto cuando hay telefonos nulos");
    }

    @Test
    @DisplayName("Telefonos devuelve lista de solo lectura")
    void telefonos_devuelveListaDeSoloLectura() {
        // Setup: Preparar el escenario
        var contacto = new Contacto("Juan", List.of(new NumeroTelefono("0294", "4444444")));

        // Ejercitacion y Verificacion
        assertThrows(UnsupportedOperationException.class, () -> contacto.telefonos().add(new NumeroTelefono("0294", "5555555")),
                "La lista de telefonos expuesta debe ser de solo lectura");
    }

    @Test
    @DisplayName("Es mismo nombre devuelve true cuando nombres solo difieren en mayusculas")
    void esMismoNombre_devuelveTrueCuandoNombresSoloDifierenEnMayusculas() {
        // Setup: Preparar el escenario
        var contacto1 = new Contacto("Juan", List.of(new NumeroTelefono("0294", "4444444")));
        var contacto2 = new Contacto("juan", List.of(new NumeroTelefono("0294", "5555555")));

        // Ejercitacion: Ejecutar la accion a probar
        var mismoNombre = contacto1.esMismoNombre(contacto2);

        // Verificacion: Verificar el resultado esperado
        assertTrue(mismoNombre, "Dos contactos con mismo nombre sin distinguir mayusculas deben coincidir");
    }
}
