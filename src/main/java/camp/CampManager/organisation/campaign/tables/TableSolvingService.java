package camp.CampManager.organisation.campaign.tables;

import org.jobrunr.jobs.annotations.Job;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TableSolvingService {

    @Autowired
    private TableService tableService;
    @Autowired
    private TableRepository tableRepository;

    @Job(name = "Solving table %1")
    public void solveTable(CampTable table, String table_name) {
        System.out.println("STARTING SOLVE TABLE " + table_name + " JOB");
        tableService.populateTable(table);
        table.setStatus("PROCESSING");
        tableRepository.save(table);
        table.solve();
        tableRepository.save(table);
        System.out.println("FINISHED SOLVE TABLE JOB");
    }
}
