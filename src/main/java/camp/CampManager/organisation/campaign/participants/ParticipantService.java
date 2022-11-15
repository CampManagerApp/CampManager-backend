package camp.CampManager.organisation.campaign.participants;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class ParticipantService {
    @Autowired
    private ParticipantRepository participantRepository;
}
