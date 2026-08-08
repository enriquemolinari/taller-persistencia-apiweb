package unrn.model;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class NumeroTelefonoTest {

    @Test
    @DisplayName("Crear numero de telefono funciona con datos validos")
    void crearNumeroTelefono_funcionaConDatosValidos() {
        // Setup: Preparar el escenario
        var numero = new NumeroTelefono("0294", "4444444");

        // Ejercitacion: Ejecutar la accion a probar
        var mismoNumero = numero.esMismoNumero(new NumeroTelefono("0294", "4444444"));

        // Verificacion: Verificar el resultado esperado
        assertTrue(mismoNumero, "Dos numeros con mismos datos deben considerarse iguales");
    }

    @Test
    @DisplayName("Crear numero de telefono lanza excepcion cuando codigo de area es invalido")
    void crearNumeroTelefono_lanzaExcepcionCuandoCodigoAreaEsInvalido() {
        // Setup: Preparar el escenario
        var codigoAreaInvalido = "294";

        // Ejercitacion y Verificacion
        var ex = assertThrows(RuntimeException.class, () -> new NumeroTelefono(codigoAreaInvalido, "4444444"));
        assertEquals(NumeroTelefono.ERROR_CODIGO_AREA_INVALIDO, ex.getMessage(),
                "Debe lanzar el error correcto para codigo de area invalido");
    }

    @Test
    @DisplayName("Crear numero de telefono lanza excepcion cuando codigo de area contiene letras")
    void crearNumeroTelefono_lanzaExcepcionCuandoCodigoAreaContieneLetras() {
        // Setup: Preparar el escenario
        var codigoAreaInvalido = "02A4";

        // Ejercitacion y Verificacion
        var ex = assertThrows(RuntimeException.class, () -> new NumeroTelefono(codigoAreaInvalido, "4444444"));
        assertEquals(NumeroTelefono.ERROR_CODIGO_AREA_INVALIDO, ex.getMessage(),
                "Debe lanzar el error correcto para codigo de area no numerico");
    }

    @Test
    @DisplayName("Crear numero de telefono lanza excepcion cuando numero es menor a seis caracteres")
    void crearNumeroTelefono_lanzaExcepcionCuandoNumeroEsMenorASeisCaracteres() {
        // Setup: Preparar el escenario
        var numeroInvalido = "12345";

        // Ejercitacion y Verificacion
        var ex = assertThrows(RuntimeException.class, () -> new NumeroTelefono("0294", numeroInvalido));
        assertEquals(NumeroTelefono.ERROR_NUMERO_INVALIDO, ex.getMessage(),
                "Debe lanzar el error correcto para numero demasiado corto");
    }

    @Test
    @DisplayName("Crear numero de telefono lanza excepcion cuando numero es mayor a siete caracteres")
    void crearNumeroTelefono_lanzaExcepcionCuandoNumeroEsMayorASieteCaracteres() {
        // Setup: Preparar el escenario
        var numeroInvalido = "12345678";

        // Ejercitacion y Verificacion
        var ex = assertThrows(RuntimeException.class, () -> new NumeroTelefono("0294", numeroInvalido));
        assertEquals(NumeroTelefono.ERROR_NUMERO_INVALIDO, ex.getMessage(),
                "Debe lanzar el error correcto para numero demasiado largo");
    }
}
