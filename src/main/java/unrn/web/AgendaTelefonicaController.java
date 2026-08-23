package unrn.web;

import org.springframework.web.bind.annotation.*;
import unrn.model.ContactoInfo;
import unrn.service.AgendaTelefonica;

import java.util.List;

@RestController
@CrossOrigin(origins = "http://localhost:5173")
public class AgendaTelefonicaController {
    //¿Cómo se construye AgendaTelefonica para inyectarla acá?
    private final AgendaTelefonica agendaTelefonica;

    public AgendaTelefonicaController(AgendaTelefonica agendaTelefonica) {
        this.agendaTelefonica = agendaTelefonica;
    }

    @PostMapping("/contactos")
    public void agregarContacto(@RequestBody NuevoContacto nuevoContacto) {
        //TODO: falta authenticacion
        this.agendaTelefonica.agregarContacto(1, nuevoContacto.nombre(), nuevoContacto.codigoArea(), nuevoContacto.telefono());
    }

    @GetMapping("/contactos")
    public List<ContactoInfo> contactos(@RequestParam int page) {
        //TODO: para que usuario
        return null;
        //return this.agendaTelefonica.listarContactos(page);
    }
}
