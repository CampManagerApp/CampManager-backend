package camp.CampManager.organisation.campaign.tables;

import lombok.AllArgsConstructor;
import org.jobrunr.scheduling.JobScheduler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Date;

@CrossOrigin
@RestController
@RequestMapping(value = "/organisation/") // /organisation/{id}/campaign/{id}
public class TableEndpoint {

    @Autowired
    private JobScheduler scheduler;

    private String mockResult = "Nothing yet";
    private Date jobStartTime = null;
    private Date jobFinishTime = null;

    @GetMapping(path = "/{orgId}/campaign/{campId}/init-job")
    @ResponseBody
    public ResponseEntity<String> startJob(@PathVariable("orgId") Long orgId,
                                           @PathVariable("campId") Long campId) {
        mockResult = "Job started";
        jobStartTime = new Date();
        System.out.println("STARTING JOB");
        scheduler.enqueue(this::doJob);
        System.out.println("JOB ENQUEUED");
        return ResponseEntity.ok("Started");
    }

    public void doJob(){
        System.out.println("JOB BEGIN");
        try {
            Thread.sleep(8000);
        } catch (InterruptedException ignored) {
        }
        System.out.println("JOB FINISHED");
        mockResult = "Job finished";
        jobFinishTime = new Date();
    }

    @GetMapping(path = "/{orgId}/campaign/{campId}/job-status")
    @ResponseBody
    public ResponseEntity<JobInformation> checkJobStatus(@PathVariable("orgId") Long orgId,
                                                 @PathVariable("campId") Long campId) {
        return ResponseEntity.ok(new JobInformation(jobStartTime, jobFinishTime, mockResult));
    }

    @PostMapping(path = "/{orgId}/campaign/{campId}/reset")
    @ResponseBody
    public ResponseEntity<JobInformation> resetToStart(@PathVariable("orgId") Long orgId,
                                                       @PathVariable("campId") Long campId) {
        mockResult = "Reset";
        jobFinishTime = null;
        jobStartTime = null;
        return ResponseEntity.ok(new JobInformation(null, null, mockResult));
    }

    @AllArgsConstructor
    private static class JobInformation {
        public Date jobStartTime;
        public Date jobFinishTime;
        public String result;
    }
}
