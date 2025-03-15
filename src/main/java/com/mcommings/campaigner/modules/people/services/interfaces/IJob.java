package com.mcommings.campaigner.modules.people.services.interfaces;

import com.mcommings.campaigner.modules.people.entities.Job;

import java.util.List;

public interface IJob {

    List<Job> getJobs();

    void saveJob(Job job);

    void deleteJob(int jobId);

    void updateJob(int jobId, Job job);
}
