package camp.CampManager.organisation.campaign.tables;

import camp.CampManager.organisation.campaign.CampaignRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

@Service
@Transactional
public class TableService {

    @Autowired
    private TableRepository tableRepository;
    @Autowired
    private CampaignRepository campaignRepository;

    public ResponseEntity<List<CampTable>> getAllTablesOfCampaign(Long orgId, Long campId) {
        var camp_o = campaignRepository.findByIdEqualsAndOrganisationIdEquals(campId, orgId);
        if (camp_o.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(tableRepository.findByCampaignIdEquals(campId));
    }

    public ResponseEntity<String> createNewTableInCampaign(Long orgId, Long campId, CampTable table) throws URISyntaxException {
        var camp_o = campaignRepository.findByIdEqualsAndOrganisationIdEquals(campId, orgId);
        if (camp_o.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        table.setCampaignId(campId);
        table.setDays(new LinkedList<>());
        table.setCounsellors(new LinkedList<>());
        table.setRestrictions(new LinkedList<>());
        table.setGrid(new HashMap<>());
        tableRepository.save(table);
        return ResponseEntity.created(new URI("/organisation/id/campaign/id/tables")).build();
    }
}
