package unrn.web;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import unrn.model.ContactoInfo;
import unrn.service.AgendaTelefonica;

import java.util.List;

@Controller
public class AgendaTelefonicaController {
    public static final String LISTA_CONTACTOS_VIEW = "lista-contactos";
    public static final String CONTACTOS_ATRIBUTE_NAME = "contactos";
    private final AgendaTelefonica agendaTelefonica;

    public AgendaTelefonicaController(AgendaTelefonica agendaTelefonica) {
        this.agendaTelefonica = agendaTelefonica;
    }

    // Handler para renderizar el formulario de alta de contacto
    @GetMapping("/contactos/nuevo")
    public String mostrarFormularioNuevoContacto(Model model) {
        model.addAttribute("nuevoContacto", NuevoContacto.empty());
        return "nuevo-contacto";
    }

    // Handler para guardar el contacto desde el formulario
    @PostMapping("/contactos")
    public String agregarContacto(@ModelAttribute NuevoContacto nuevoContacto) {
        this.agendaTelefonica.agregarContacto(nuevoContacto.nombre(), nuevoContacto.codigoArea(), nuevoContacto.telefono());
        return "redirect:/contactos?page=1";
    }

    @GetMapping("/contactos")
    public String contactos(@RequestParam int page, Model model) {
        List<ContactoInfo> contactos = this.agendaTelefonica.listarContactos(page);
        model.addAttribute(CONTACTOS_ATRIBUTE_NAME, contactos);
        return LISTA_CONTACTOS_VIEW;
    }
}
