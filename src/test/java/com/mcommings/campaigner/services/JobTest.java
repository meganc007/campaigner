package com.mcommings.campaigner.services;

import com.mcommings.campaigner.models.Job;
import com.mcommings.campaigner.models.repositories.IJobRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.when;

@SpringBootTest
public class JobTest {

    @Mock
    private IJobRepository jobRepository;

    @InjectMocks
    private JobService jobService;

    @Test
    public void whenThereAreJobs_getJobs_ReturnsJobs() {
        List<Job> jobs = new ArrayList<>();
        jobs.add(new Job(1, "Job 1", "Description 1"));
        jobs.add(new Job(2, "Job 2", "Description 2"));
        when(jobRepository.findAll()).thenReturn(jobs);

        List<Job> result = jobService.getJobs();

        Assertions.assertEquals(2, result.size());
        Assertions.assertEquals(jobs, result);
    }

    @Test
    public void whenThereAreNoJobs_getJobs_ReturnsNothing() {
        List<Job> jobs = new ArrayList<>();
        when(jobRepository.findAll()).thenReturn(jobs);

        List<Job> result = jobService.getJobs();

        Assertions.assertEquals(0, result.size());
        Assertions.assertEquals(jobs, result);
    }
}
