package com.mcommings.campaigner.modules.people.controllers;

import com.mcommings.campaigner.modules.people.dtos.JobAssignmentDTO;
import com.mcommings.campaigner.modules.people.services.interfaces.IJobAssignment;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

import static com.mcommings.campaigner.enums.ErrorMessage.ID_NOT_FOUND;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "api/jobAssignments")
public class JobAssignmentController {

    private final IJobAssignment jobAssignmentService;

    @GetMapping
    public List<JobAssignmentDTO> getJobAssignments() {
        return jobAssignmentService.getJobAssignments();
    }

    @GetMapping(path = "/{jobAssignmentId}")
    public JobAssignmentDTO getJobAssignment(@PathVariable("jobAssignmentId") int jobAssignmentId) {
        return jobAssignmentService.getJobAssignment(jobAssignmentId).orElseThrow(() -> new IllegalArgumentException(ID_NOT_FOUND.message));
    }

    @GetMapping(path = "/campaign/{uuid}")
    public List<JobAssignmentDTO> getJobAssignmentsByCampaignUUID(@PathVariable("uuid") UUID uuid) {
        return jobAssignmentService.getJobAssignmentsByCampaignUUID(uuid);
    }

    @GetMapping(path = "/person/{personId}")
    public List<JobAssignmentDTO> getJobAssignmentsByPersonId(@PathVariable("personId") int personId) {
        return jobAssignmentService.getJobAssignmentsByPersonId(personId);
    }

    @GetMapping(path = "/job/{jobId}")
    public List<JobAssignmentDTO> getJobAssignmentsByJobId(@PathVariable("jobId") int jobId) {
        return jobAssignmentService.getJobAssignmentsByJobId(jobId);
    }

    @PostMapping
    public void saveJobAssignment(@Valid @RequestBody JobAssignmentDTO jobAssignment) {
        jobAssignmentService.saveJobAssignment(jobAssignment);
    }

    @DeleteMapping(path = "{id}")
    public void JobAssignment(@PathVariable("id") int id) {
        jobAssignmentService.deleteJobAssignment(id);
    }

    @PutMapping(path = "{id}")
    public void JobAssignment(@PathVariable("id") int id, @RequestBody JobAssignmentDTO jobAssignment) {
        jobAssignmentService.updateJobAssignment(id, jobAssignment);
    }
}
