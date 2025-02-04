package com.mcommings.campaigner.interfaces.people;

import com.mcommings.campaigner.entities.people.Job;

import java.util.List;

public interface IJob {

    List<Job> getJobs();

    void saveJob(Job job);

    void deleteJob(int jobId);

    void updateJob(int jobId, Job job);
}
