package com.mcommings.campaigner.controllers.people;

import com.mcommings.campaigner.entities.people.JobAssignment;
import com.mcommings.campaigner.services.people.JobAssignmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping(path = "api/people/job-assignments")
public class JobAssignmentController {

    private final JobAssignmentService jobAssignmentService;

    @Autowired
    public JobAssignmentController(JobAssignmentService jobAssignmentService) {
        this.jobAssignmentService = jobAssignmentService;
    }

    @GetMapping
    public List<JobAssignment> getJobAssignments() {
        return jobAssignmentService.getJobAssignments();
    }

    @GetMapping(path = "/campaign/{uuid}")
    public List<JobAssignment> getJobAssignmentsByCampaignUUID(@PathVariable("uuid") UUID uuid) {
        return jobAssignmentService.getJobAssignmentsByCampaignUUID(uuid);
    }

    @GetMapping(path = "/person/{personId}")
    public List<JobAssignment> getJobAssignmentsByPersonId(@PathVariable("personId") int personId) {
        return jobAssignmentService.getJobAssignmentsByPersonId(personId);
    }

    @PostMapping
    public void saveJobAssignment(@RequestBody JobAssignment jobAssignment) {
        jobAssignmentService.saveJobAssignment(jobAssignment);
    }

    @DeleteMapping(path = "{id}")
    public void JobAssignment(@PathVariable("id") int id) {
        jobAssignmentService.deleteJobAssignment(id);
    }

    @PutMapping(path = "{id}")
    public void JobAssignment(@PathVariable("id") int id, @RequestBody JobAssignment jobAssignment) {
        jobAssignmentService.updateJobAssignment(id, jobAssignment);
    }
}
