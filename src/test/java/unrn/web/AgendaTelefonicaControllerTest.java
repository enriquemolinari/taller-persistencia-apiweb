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
import unrn.service.AgendaTelefonica;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

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
        mockMvc.perform(get("/contactos?page=1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(3)))
                .andExpect(jsonPath("$[0].nombre", is("Ana Torres")))
                .andExpect(jsonPath("$[0].telefonos", containsInAnyOrder("0299 7654321", "0299 1234567")))
                .andExpect(jsonPath("$[1].nombre", is("Luis Pérez")))
                .andExpect(jsonPath("$[1].telefonos", contains("0114 654321")))
                .andExpect(jsonPath("$[2].nombre", is("Mia Solis")))
                .andExpect(jsonPath("$[2].telefonos", contains("0114 234567")));
    }

    @Test
    @DisplayName("GET /contactos sin parámetro page retorna error 400 y JSON de error")
    void getContactos_sinParametroPage_error400() throws Exception {
        mockMvc.perform(get("/contactos"))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.message", is("Parámetros inválidos")));
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

    @Test
    @DisplayName("POST /contactos con campos vacíos retorna error 400 y JSON de error")
    void postContactos_camposVacios_error400() throws Exception {
        String json = "{" +
                "\"nombre\":\"\"," +
                "\"codigoArea\":\"\"," +
                "\"telefono\":\"\"}";
        // Ejercitación y Verificación
        mockMvc.perform(post("/contactos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.message", not(emptyString())));
    }

    @Test
    @DisplayName("POST /contactos con JSON inválido retorna error 400 y JSON de error")
    void postContactos_jsonInvalido_error400() throws Exception {
        String json = "{" +
                "\"nombre\":\"Juan\"," +
                "\"codigoArea\":\"0114\"}"; // Falta teléfono
        // Ejercitación y Verificación
        mockMvc.perform(post("/contactos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.message", is("El número no puede ser null")));
    }
}
