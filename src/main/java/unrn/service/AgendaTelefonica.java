package unrn.service;

import jakarta.persistence.EntityManagerFactory;
import unrn.model.*;
import unrn.repositorios.UsuarioRepository;

import java.util.List;

import static unrn.model.auth.Escribano.escribano;
import static unrn.repositorios.ContactoRepository.repositoryOf;

public class AgendaTelefonica {
    public static final int PAGE_SIZE = 10;
    private final EntityManagerFactory emf;

    public AgendaTelefonica(EntityManagerFactory emf) {
        this.emf = emf;
    }

    public void agregarContacto(Integer paraUserId, String nombre, String codigoArea, String telefono) {
        var numeroTelefono = new NumeroTelefono(codigoArea, telefono);
        emf.runInTransaction(em -> {
            var nombreContacto = new NombreDeContacto(nombre);
            var repository = repositoryOf(em, PAGE_SIZE);
            var contacto = repository.buscarPorNombre(nombreContacto.nombre());
            contacto.ifPresentOrElse(
                    c -> c.nuevoNumero(numeroTelefono)
                    , () -> {
                        var usuario = UsuarioRepository.repositoryOf(em).buscarPorId(paraUserId)
                                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
                        usuario.agregarContacto(Contacto.of(nombreContacto, numeroTelefono));
                        //repository.agregar(Contacto.of(nombreContacto, numeroTelefono))
                    });
        });
    }

    public List<ContactoInfo> listarContactos(Integer userId, int pageNumber) {
        return emf.callInTransaction(em ->
                repositoryOf(em, PAGE_SIZE).listar(userId, pageNumber)
        );
    }

    public String login(String username, String password) {
        return emf.callInTransaction(em -> {
            var usuarioOptional = UsuarioRepository.repositoryOf(em).buscarPorUsernameAndPassword(username, password);
            var usuario = usuarioOptional.orElseThrow(() -> new RuntimeException("Usuario o contraseña incorrectos"));
            return escribano().generarTokenPara(usuario.identificador());
        });
    }

    public Integer registrarUsuario(String username, String password) {
        return emf.callInTransaction(em -> {
            var usuarioOptional = UsuarioRepository.repositoryOf(em).buscarPorUsername(username);
            usuarioOptional.ifPresent(u -> {
                throw new RuntimeException("El nombre de usuario ya existe");
            });
            var usuario = new Usuario(username, password);
            em.persist(usuario);
            return usuario.identificador();
        });
    }
}