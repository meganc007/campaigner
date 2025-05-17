package com.mcommings.campaigner.modules.people.controllers;

import com.mcommings.campaigner.modules.people.dtos.JobDTO;
import com.mcommings.campaigner.modules.people.services.interfaces.IJob;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.mcommings.campaigner.enums.ErrorMessage.ID_NOT_FOUND;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "api/jobs")
public class JobController {
    private final IJob jobService;

    @GetMapping
    public List<JobDTO> getJobs() {
        return jobService.getJobs();
    }

    @GetMapping(path = "/{jobId}")
    public JobDTO getJob(@PathVariable("jobId") int jobId) {
        return jobService.getJob(jobId).orElseThrow(() -> new IllegalArgumentException(ID_NOT_FOUND.message));
    }

    @PostMapping
    public void saveJob(@Valid @RequestBody JobDTO job) {
        jobService.saveJob(job);
    }

    @DeleteMapping(path = "{jobId}")
    public void deleteJob(@PathVariable("jobId") int jobId) {
        jobService.deleteJob(jobId);
    }

    @PutMapping(path = "{jobId}")
    public void updateJob(@PathVariable("jobId") int jobId, @RequestBody JobDTO job) {
        jobService.updateJob(jobId, job);
    }
}
