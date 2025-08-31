package unrn.web;

import jakarta.persistence.EntityManagerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import unrn.service.AgendaTelefonica;
import unrn.util.EmfBuilder;

@Configuration
@Profile("test-integracion")
public class AppTestConfiguration {
    @Bean
    public EntityManagerFactory entityManagerFactory() {
        return new EmfBuilder()
                .memory()
                .withDropAndCreateDDL()
                .build();
    }

    @Bean
    public AgendaTelefonica agenda(EntityManagerFactory emf) {
        return new AgendaTelefonica(emf);
    }
}
