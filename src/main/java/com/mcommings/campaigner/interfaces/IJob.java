package com.mcommings.campaigner.interfaces;

import com.mcommings.campaigner.models.Job;

import java.util.List;

public interface IJob {

    List<Job> getJobs();

    void saveJob(Job job);

    void deleteJob(int jobId);

    void updateJob(int jobId, Job job);
}
