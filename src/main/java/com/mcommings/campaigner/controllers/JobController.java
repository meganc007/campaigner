package com.mcommings.campaigner.controllers;

import com.mcommings.campaigner.models.Job;
import com.mcommings.campaigner.services.JobService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}
