package com.mcommings.campaigner.interfaces;

import com.mcommings.campaigner.models.Job;

import java.util.List;

public interface IJob {

    public List<Job> getJobs();

    public void saveJob(Job job);

    public void deleteJob(int jobId);

    public void updateJob(int jobId, Job job);
}
