package unrn.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Agenda {
    static final String ERROR_CONTACTO_INVALIDO = "El contacto no puede ser nulo";
    static final String ERROR_CONTACTO_DUPLICADO = "No puede existir mas de un contacto con el mismo nombre";
    static final String ERROR_LISTA_CONTACTOS_INVALIDA = "La lista de contactos no puede ser nula";

    private final List<Contacto> contactos;

    public Agenda() {
        this(new ArrayList<>());
    }

    public Agenda(List<Contacto> contactos) {
        assertContactosValidos(contactos);
        this.contactos = new ArrayList<>();
        contactos.forEach(this::agregarContacto);
    }

    public void agregarContacto(Contacto contacto) {
        assertContactoValido(contacto);
        assertContactoNoDuplicado(contacto);
        contactos.add(contacto);
    }

    public List<Contacto> contactos() {
        return Collections.unmodifiableList(contactos);
    }

    private void assertContactosValidos(List<Contacto> contactos) {
        if (contactos == null) {
            throw new RuntimeException(ERROR_LISTA_CONTACTOS_INVALIDA);
        }
    }

    private void assertContactoValido(Contacto contacto) {
        if (contacto == null) {
            throw new RuntimeException(ERROR_CONTACTO_INVALIDO);
        }
    }

    private void assertContactoNoDuplicado(Contacto contacto) {
        if (contactos.stream().anyMatch(contactoExistente -> contactoExistente.esMismoNombre(contacto))) {
            throw new RuntimeException(ERROR_CONTACTO_DUPLICADO);
        }
    }
}
