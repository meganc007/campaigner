package com.mcommings.campaigner.modules.people.services.interfaces;

import com.mcommings.campaigner.modules.people.dtos.JobDTO;

import java.util.List;
import java.util.Optional;

public interface IJob {

    List<JobDTO> getJobs();

    Optional<JobDTO> getJob(int jobId);

    void saveJob(JobDTO job);

    void deleteJob(int jobId);

    Optional<JobDTO> updateJob(int jobId, JobDTO job);
}
