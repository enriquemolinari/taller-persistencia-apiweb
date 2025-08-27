package unrn.console;

import unrn.service.AgendaTelefonica;

public class ContactosImporter {
    //si quiero implementar la importacion de contactos desde un archiv por ejemplo
    // desde aca consumo los servicios del paquete service

    //Sería mas complejo hacerlo contra la capa web
    //Además tengo un impacto de performance de serializar/deserializar JSON.W
    public void importar(String pathArchivo) {
        var emf = new unrn.util.EmfBuilder()
                .build();
        var agendaTelefonica = new AgendaTelefonica(emf);
        //for earch linea del archivo
        //agendaTelefonica.agregarContacto(...)
    }
}
