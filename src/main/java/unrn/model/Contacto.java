package unrn.model;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter(AccessLevel.PRIVATE)
@Setter(AccessLevel.PRIVATE)
public class Contacto {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;
    @Embedded
    private NombreDeContacto nombre;

    @OneToMany(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "contacto_id")
    private List<NumeroTelefono> telefonos;

    public Contacto(NombreDeContacto nombre) {
        this.nombre = nombre;
        this.telefonos = new ArrayList<>();
    }

    public static Contacto of(NombreDeContacto nombre, NumeroTelefono numeroTelefono) {
        var contacto = new Contacto(nombre);
        contacto.nuevoNumero(numeroTelefono);
        return contacto;
    }

    public void nuevoNumero(NumeroTelefono numeroTelefono) {
        this.telefonos.add(numeroTelefono);
    }

    public boolean esDe(String nombre) {
        return this.nombre.equals(new NombreDeContacto(nombre));
    }

    public int cantidadDeTelefonos() {
        return this.telefonos.size();
    }

    public boolean tieneElTelefono(String telefono) {
        return this.telefonos.stream()
                .anyMatch(numero -> numero.numero().equals(telefono));
    }

    public ContactoInfo toInfo() {
        var telefonosInfo = this.telefonos.stream()
                .map(NumeroTelefono::numero)
                .toList();
        return new ContactoInfo(this.id, this.nombre.nombre(), telefonosInfo);
    }
}

