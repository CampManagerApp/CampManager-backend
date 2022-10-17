package camp.CampManager.organisation;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.lang.reflect.Array;
import java.util.List;

@Configuration
public class OrganizationConfiguration {

    @Bean
    CommandLineRunner commandLineRunner(OrganisationRepository organisationRepository) {
        return  args -> {
            organisationRepository.saveAll(
                    List.of(
                            new Organisation("Org1", "Admin"),
                            new Organisation("Org2", "Admin"),
                            new Organisation("Org3", "Admin"),
                            new Organisation("Org4", "Admin"),
                            new Organisation("Org5", "Admin"),
                            new Organisation("Org6", "Admin"),
                            new Organisation("Org7", "Admin"),
                            new Organisation("Org8", "Admin")
                            )
            );
        };
    }
}
