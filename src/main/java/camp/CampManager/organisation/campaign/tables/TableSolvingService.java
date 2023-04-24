package camp.CampManager.organisation.campaign.tables;

import camp.CampManager.organisation.Organisation;
import camp.CampManager.organisation.OrganisationRepository;
import camp.CampManager.organisation.campaign.Campaign;
import camp.CampManager.organisation.campaign.CampaignRepository;
import com.google.firebase.messaging.FirebaseMessagingException;
import org.jobrunr.jobs.annotations.Job;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TableSolvingService {

    @Autowired
    private TableService tableService;
    @Autowired
    private TableRepository tableRepository;

    @Autowired
    private CampaignRepository campaignRepository;

    @Autowired
    private OrganisationRepository organisationRepository;

   // @Job(name = "Solving table %1")
    public void solveTable(CampTable table, String table_name) {
        System.out.println("STARTING SOLVE TABLE " + table_name + " JOB");
        tableService.populateTable(table);
        table.setStatus("PROCESSING");
        tableRepository.save(table);
        table.solve();
        tableRepository.save(table);
        Campaign campaign = campaignRepository.findById(table.getCampaignId()).get();
        Organisation organisation = organisationRepository.findById(campaign.getOrganisationId()).get();
        System.out.println("FINISHED SOLVE TABLE JOB");
    }
}
