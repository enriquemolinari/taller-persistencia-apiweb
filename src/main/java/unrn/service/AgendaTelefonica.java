package unrn.service;

import jakarta.persistence.EntityManagerFactory;
import unrn.model.Contacto;
import unrn.model.ContactoInfo;
import unrn.model.NombreDeContacto;
import unrn.model.NumeroTelefono;

import java.util.List;

import static unrn.repositorios.ContactoRepository.repositoryOf;

public class AgendaTelefonica {
    public static final int PAGE_SIZE = 10;
    private final EntityManagerFactory emf;

    public AgendaTelefonica(EntityManagerFactory emf) {
        this.emf = emf;
    }

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

    public List<ContactoInfo> listarContactos(int pageNumber) {
        return emf.callInTransaction(em ->
                repositoryOf(em, PAGE_SIZE).listar(pageNumber)
        );
    }
}