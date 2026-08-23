package unrn.model;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter(AccessLevel.PRIVATE)
@Setter(AccessLevel.PRIVATE)
public class Usuario {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String username;
    //ojo, nunca debe escaparse con un getter...
    private String password;

    @OneToMany
    @JoinColumn(name = "id_usuario")
    private List<Contacto> contactos;

    public Usuario(String username, String password) {
        this.username = username;
        //encriptar antes de guardar
        this.password = password;
    }
    
    public int identificador() {
        return id;
    }
}
