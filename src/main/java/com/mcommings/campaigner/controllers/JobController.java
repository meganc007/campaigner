package com.mcommings.campaigner.controllers;

import com.mcommings.campaigner.models.Job;
import com.mcommings.campaigner.services.JobService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "api/people/jobs")
public class JobController {
    private final JobService jobService;

    @Autowired
    public JobController(JobService jobService) {this.jobService = jobService;}

    @GetMapping
    public List<Job> getJobs() {
        return jobService.getJobs();
    }

    @PostMapping
    public void saveJob(@RequestBody Job job) {
        jobService.saveJob(job);
    }

    @DeleteMapping(path = "{jobId}")
    public void deleteJob(@PathVariable("jobId") int jobId) {
        jobService.deleteJob(jobId);
    }

    @PutMapping(path = "{jobId}")
    public void updateJob(@PathVariable("jobId") int jobId, @RequestBody Job job) {
        jobService.updateJob(jobId, job);
    }
}
