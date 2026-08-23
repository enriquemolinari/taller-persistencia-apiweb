package unrn.service;

import jakarta.persistence.EntityManagerFactory;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import unrn.util.EmfBuilder;

import static org.junit.jupiter.api.Assertions.*;

class AgendaTelefonicaTest {

    private static EntityManagerFactory emf;
    private AgendaTelefonica agenda;

    @BeforeAll
    static void beforeEverything() {
//        emf = new EmfBuilder().memory().withTestData().withDropAndCreateDDL().build();
        emf = new EmfBuilder().memory().withDropAndCreateDDL().build();
    }

    @BeforeEach
    void beforeEach() {
        // Truncate data before each test to ensure isolation
        emf.getSchemaManager().truncate();

        agenda = new AgendaTelefonica(emf);
        agenda.agregarContacto("Ana Torres", "0299", "1234567");
        agenda.agregarContacto("Ana Torres", "0299", "7654321");
        agenda.agregarContacto("Luis Pérez", "0114", "654321");
        agenda.agregarContacto("Mia Solis", "0114", "234567");


    }

    @Test
    @DisplayName("Agregar un contacto nuevo lo almacena en la agenda")
    void agregarContactoNuevo_contactoSeAgrega() {
        String nombre = "Juan Perez";
        String codigoArea = "0299";
        String telefono = "1234567";
        // setup
        agenda.agregarContacto(nombre, codigoArea, telefono);

        // Assert
        var contactos = agenda.listarContactos(1);
        assertEquals(4, contactos.size(), "Debería haber exactamente un contacto");

        var juanPerezOptional = contactos.stream().filter(c -> c.esDe(nombre)).findFirst();
        var juanPerez = juanPerezOptional.get();
        assertNotNull(juanPerez, "Juan Perez debería existir como contacto");
        assertTrue(juanPerez.tieneElTelefono(codigoArea + " " + telefono), "Juan Perez debería tener un número de teléfono");
        assertEquals(1, juanPerez.cantidadDeTelefonos(), "Juan Perez debería tener un número de teléfono");
    }

    @Test
    @DisplayName("Agregar un número a un contacto existente lo agrega correctamente")
    void agregarNumeroAContactoExistente_numeroSeAgrega() {
        String nombre = "Mia Solis";
        String codigoArea = "7898";
        String telefono = "6589547";
        // setup
        agenda.agregarContacto(nombre, codigoArea, telefono);
        //verificar
        var contactos = agenda.listarContactos(1);
        assertEquals(3, contactos.size(), "Debería haber exactamente 3 contactos");

        var miaOptional = contactos.stream().filter(c -> c.esDe(nombre)).findFirst();
        var mia = miaOptional.get();
        assertNotNull(mia, "Mia debería existir como contacto");
        assertTrue(mia.tieneElTelefono(codigoArea + " " + telefono), "Mia debería tener este número de teléfono");
        assertTrue(mia.tieneElTelefono("0114 234567"), "Mia debería tener este número de teléfono");
        assertEquals(2, mia.cantidadDeTelefonos(), "Juan Perez debería tener un número de teléfono");
    }

    @Test
    @DisplayName("Listar todos los contactos")
    void listarContactos_listaSoloLectura() {
        var contactos = agenda.listarContactos(1);
        assertEquals(3, contactos.size(), "Debería haber exactamente 3 contactos");

        // Falta verificar que los contactos tienen los nombres los números correctos
    }
}

