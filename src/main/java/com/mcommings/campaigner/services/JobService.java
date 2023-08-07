package com.mcommings.campaigner.services;

import com.mcommings.campaigner.interfaces.IJob;
import com.mcommings.campaigner.models.repositories.IJobRepository;
import com.mcommings.campaigner.models.Job;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class JobService implements IJob {

    private final IJobRepository jobRepository;

    @Autowired
    public JobService(IJobRepository jobRepository) {this.jobRepository = jobRepository;}

    @Override
    public List<Job> getJobs() {
        return jobRepository.findAll();
    }
}
