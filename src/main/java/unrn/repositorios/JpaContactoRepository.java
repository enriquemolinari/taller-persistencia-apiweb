package unrn.repositorios;

import jakarta.persistence.EntityManager;
import unrn.model.Contacto;
import unrn.model.ContactoInfo;

import java.util.List;
import java.util.Optional;

class JpaContactoRepository implements ContactoRepository {
    private final EntityManager em;
    private final int pageSize;

    public JpaContactoRepository(EntityManager em, int pageSize) {
        this.em = em;
        this.pageSize = pageSize;
    }

    public Optional<Contacto> buscarPorNombre(String nombre) {
        var existe = em.createQuery("from Contacto c where c.nombre.nombre = :nombre", Contacto.class);
        existe.setParameter("nombre", nombre);
        return Optional.ofNullable(existe.getSingleResultOrNull());
    }

    @Override
    public void agregar(Contacto contacto) {
        em.persist(contacto);
    }

    @Override
    public List<ContactoInfo> listar(int pageNumber) {
        var contactos = em.createQuery("from Contacto c join fetch c.telefonos order by c.nombre", Contacto.class);
        contactos.setFirstResult((pageNumber - 1) * pageSize);
        contactos.setMaxResults(pageSize);
        var resultList = contactos.getResultList();
        return resultList.stream().map(c -> c.toInfo()).toList();
    }
}
