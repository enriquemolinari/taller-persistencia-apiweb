package unrn.web;

import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
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
        this.agendaTelefonica.agregarContacto(nuevoContacto.idUser(), nuevoContacto.nombre(), nuevoContacto.codigoArea(), nuevoContacto.telefono());
    }

    @GetMapping("/contactos")
    public List<ContactoInfo> contactos(@RequestParam int page, @RequestParam int idUser) {
        //TODO: falta authenticacion
        return this.agendaTelefonica.listarContactos(idUser, page);
    }

    @PostMapping("/login")
    public ResponseEntity<Void> login(@RequestBody CredencialesUsuario credencialesUsuario) {
        var token = this.agendaTelefonica.login(credencialesUsuario.username(), credencialesUsuario.password());
        //agregar token a la response como cookie:
        var cookie = ResponseCookie.from("token", token)
                .httpOnly(true)
                .path("/")
                .secure(false) // must change for PROD
                .sameSite("Strict")
                .maxAge(180) // 1 hora alineado al jwt deberia estar
                .build();
        return ResponseEntity.ok().header(HttpHeaders.SET_COOKIE, cookie.toString()).build();
    }

    @PostMapping("/registrar")
    public void registrarUsuario(@RequestBody CredencialesUsuario credencialesUsuario) {
        this.agendaTelefonica.registrarUsuario(credencialesUsuario.username(), credencialesUsuario.password());
    }
}
