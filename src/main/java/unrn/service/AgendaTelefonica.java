package unrn.service;

import jakarta.persistence.EntityManagerFactory;
import unrn.model.*;

import java.util.List;
import java.util.Optional;

import static unrn.model.auth.Escribano.escribano;
import static unrn.repositorios.ContactoRepository.repositoryOf;

public class AgendaTelefonica {
    public static final int PAGE_SIZE = 10;
    private final EntityManagerFactory emf;

    public AgendaTelefonica(EntityManagerFactory emf) {
        this.emf = emf;
    }

    //Falta segurizar ! a que usuario le pertenece?
    public void agregarContacto(String nombre, String codigoArea, String telefono) {
        var numeroTelefono = new NumeroTelefono(codigoArea, telefono);
        emf.runInTransaction(em -> {
            var nombreContacto = new NombreDeContacto(nombre);
            var repository = repositoryOf(em, PAGE_SIZE);
            var contacto = repository.buscarPorNombre(nombreContacto.nombre());
            contacto.ifPresentOrElse(
                    c -> c.nuevoNumero(numeroTelefono)
                    , () ->
                            repository.agregar(Contacto.of(nombreContacto, numeroTelefono))
            );
        });
    }

    // de que usuario ?
    public List<ContactoInfo> listarContactos(int pageNumber) {
        return emf.callInTransaction(em ->
                repositoryOf(em, PAGE_SIZE).listar(pageNumber)
        );
    }

    public String login(String username, String password) {
        return emf.callInTransaction(em -> {
            var existe = em.createQuery("from Usuario u where u.username = :username and u.password = :password", Usuario.class);
            existe.setParameter("username", username);
            existe.setParameter("password", password);
            var usuarioOptional = Optional.ofNullable(existe.getSingleResultOrNull());
            var usuario = usuarioOptional.orElseThrow(() -> new RuntimeException("Usuario o contraseña incorrectos"));
            return escribano().generarTokenPara(usuario.identificador());
        });
    }

    public void registrarUsuario(String username, String password) {
        emf.runInTransaction(em -> {
            var existe = em.createQuery("from Usuario u where u.username = :username", Usuario.class);
            existe.setParameter("username", username);
            var usuarioOptional = Optional.ofNullable(existe.getSingleResultOrNull());
            usuarioOptional.ifPresent(u -> {
                throw new RuntimeException("El nombre de usuario ya existe");
            });
            var usuario = new Usuario(username, password);
            em.persist(usuario);
        });
    }
}