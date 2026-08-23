package unrn.repositorios;

import jakarta.persistence.EntityManager;
import unrn.model.Usuario;

import java.util.Optional;

public interface UsuarioRepository {
    static UsuarioRepository repositoryOf(EntityManager em) {
        return new JpaUsuarioRepository(em);
    }

    Optional<Usuario> buscarPorUsernameAndPassword(String username, String password);

    Optional<Usuario> buscarPorUsername(String username);

    Optional<Usuario> buscarPorId(Integer id);

}
