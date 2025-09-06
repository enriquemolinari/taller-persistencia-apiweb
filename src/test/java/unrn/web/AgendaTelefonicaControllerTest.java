package unrn.web;

import jakarta.persistence.EntityManagerFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import unrn.main.Main;
import unrn.model.ContactoInfo;
import unrn.service.AgendaTelefonica;

import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static unrn.web.AgendaTelefonicaController.CONTACTOS_ATRIBUTE_NAME;
import static unrn.web.AgendaTelefonicaGlobalExceptionHandler.ERROR_MESSAGE_KEY;
import static unrn.web.AgendaTelefonicaGlobalExceptionHandler.ERROR_VIEW_NAME;

@SpringBootTest(classes = Main.class)
@AutoConfigureMockMvc
@ActiveProfiles(value = "test-integracion")
public class AgendaTelefonicaControllerTest {

    @Autowired
    private EntityManagerFactory emf;
    private AgendaTelefonica agenda;

    @Autowired
    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        emf.getSchemaManager().truncate();

        agenda = new AgendaTelefonica(emf);
        agenda.agregarContacto("Ana Torres", "0299", "1234567");
        agenda.agregarContacto("Ana Torres", "0299", "7654321");
        agenda.agregarContacto("Luis Pérez", "0114", "654321");
        agenda.agregarContacto("Mia Solis", "0114", "234567");
    }

    @Test
    @DisplayName("GET /contactos devuelve la lista de contactos correctamente")
    void getContactos_listaContactosOk() throws Exception {
        var mvcResult = mockMvc.perform(get("/contactos?page=1"))
                .andExpect(status().isOk()).andReturn();
        var contactos = (List<ContactoInfo>) mvcResult.getModelAndView().getModel().get(CONTACTOS_ATRIBUTE_NAME);
        assertEquals(3, contactos.size());
        assertEquals("Ana Torres", contactos.get(0).nombre());
        assertThat(contactos.get(0).telefonos(), containsInAnyOrder("0299 1234567", "0299 7654321"));
        assertEquals("Luis Pérez", contactos.get(1).nombre());
        assertThat(contactos.get(1).telefonos(), contains("0114 654321"));
        assertEquals("Mia Solis", contactos.get(2).nombre());
        assertThat(contactos.get(2).telefonos(), contains("0114 234567"));
    }

    @Test
    @DisplayName("GET /contactos sin parámetro page retorna error 400 y JSON de error")
    void getContactos_sinParametroPage_renderizo_error_view() throws Exception {
        mockMvc.perform(get("/contactos"))
                .andExpect(model().attributeExists(ERROR_MESSAGE_KEY))
                .andExpect(view().name(ERROR_VIEW_NAME))
                .andExpect(model().attribute(ERROR_MESSAGE_KEY, is("Parámetros inválidos")));
    }

    @Test
    @DisplayName("POST /contactos agrega un contacto válido correctamente")
    void postContactos_contactoValido_ok() throws Exception {
        String json = "{" +
                "\"nombre\":\"Juan Perez\"," +
                "\"codigoArea\":\"0114\"," +
                "\"telefono\":\"999999\"}";
        // Ejercitación y Verificación
        mockMvc.perform(post("/contactos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk());
    }

    //TODO: agregar casos de error...
}
