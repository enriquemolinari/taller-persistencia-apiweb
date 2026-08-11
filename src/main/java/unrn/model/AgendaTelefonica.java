package unrn.model;

import jakarta.persistence.EntityManagerFactory;

import java.util.List;
import java.util.Optional;

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
            var existe = em.createQuery("from Contacto c where c.nombre.nombre = :nombre", Contacto.class);
            existe.setParameter("nombre", nombreContacto.nombre());
            var contacto = Optional.ofNullable(existe.getSingleResultOrNull());
            contacto.ifPresentOrElse(
                    c -> c.nuevoNumero(numeroTelefono)
                    , () -> {
                        em.persist(Contacto.of(nombreContacto, numeroTelefono));
                    });
        });
    }

    List<Contacto> listarContactos(int pageNumber) {
        return emf.callInTransaction(em -> {
            var contactos = em.createQuery("from Contacto c join fetch c.telefonos", Contacto.class);
            contactos.setFirstResult((pageNumber - 1) * PAGE_SIZE);
            contactos.setMaxResults(PAGE_SIZE);
            return contactos.getResultList();
        });
    }
}