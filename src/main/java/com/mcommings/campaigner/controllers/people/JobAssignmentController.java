package com.mcommings.campaigner.controllers.people;

import com.mcommings.campaigner.models.people.JobAssignment;
import com.mcommings.campaigner.services.people.JobAssignmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
