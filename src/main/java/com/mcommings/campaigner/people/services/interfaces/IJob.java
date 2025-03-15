package com.mcommings.campaigner.people.services.interfaces;

import com.mcommings.campaigner.people.entities.Job;

import java.util.List;

public interface IJob {

    List<Job> getJobs();

    void saveJob(Job job);

    void deleteJob(int jobId);

    void updateJob(int jobId, Job job);
}
