package camp.CampManager.users;

import camp.CampManager.organisation.Organisation;
import camp.CampManager.organisation.OrganisationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class MembersConfiguration implements CommandLineRunner {

    @Autowired
    MembershipRepository membershipRepository;

    @Override
    public void run(String... args) throws Exception {
        membershipRepository.saveAll(
                List.of(
                        new Membership(1l, false, null, "Joel Aumedes", 2l, true, true)
                )
        );
    }
    /*
    @Bean
    CommandLineRunner commandLineRunner(MembershipRepository membershipRepository) {
        return args -> membershipRepository.saveAll(
                List.of(
                        new Membership(0l, false, null, "Joel Aumedes", 0l, true, true)
                )
        );
    }*/
}
