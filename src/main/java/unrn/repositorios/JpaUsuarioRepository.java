package unrn.repositorios;

import jakarta.persistence.EntityManager;
import unrn.model.Usuario;

import java.util.Optional;

public class JpaUsuarioRepository implements UsuarioRepository {

    private final EntityManager em;

    public JpaUsuarioRepository(EntityManager em) {
        this.em = em;
    }

    @Override
    public Optional<Usuario> buscarPorUsernameAndPassword(String username, String password) {
        var existe = em.createQuery("from Usuario u where u.username = :username and u.password = :password", Usuario.class);
        existe.setParameter("username", username);
        existe.setParameter("password", password);
        return Optional.ofNullable(existe.getSingleResultOrNull());
    }

    @Override
    public Optional<Usuario> buscarPorUsername(String username) {
        var existe = em.createQuery("from Usuario u where u.username = :username", Usuario.class);
        existe.setParameter("username", username);
        return Optional.ofNullable(existe.getSingleResultOrNull());
    }

    @Override
    public Optional<Usuario> buscarPorId(Integer id) {
        return Optional.ofNullable(em.find(Usuario.class, id));
    }
}
