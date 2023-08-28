package com.mcommings.campaigner.services;

import com.mcommings.campaigner.interfaces.IJob;
import com.mcommings.campaigner.models.Job;
import com.mcommings.campaigner.models.RepositoryHelper;
import com.mcommings.campaigner.models.repositories.IJobRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.mcommings.campaigner.enums.ErrorMessage.*;

@Service
public class JobService implements IJob {

    private final IJobRepository jobRepository;

    @Autowired
    public JobService(IJobRepository jobRepository) {this.jobRepository = jobRepository;}

    @Override
    public List<Job> getJobs() {
        return jobRepository.findAll();
    }

    @Override
    @Transactional
    public void saveJob(Job job) throws IllegalArgumentException, DataIntegrityViolationException {
        if (RepositoryHelper.nameIsNullOrEmpty(job)) {
            throw new IllegalArgumentException(NULL_OR_EMPTY.message);
        }
        if (RepositoryHelper.nameAlreadyExists(jobRepository, job)) {
            throw new DataIntegrityViolationException(NAME_EXISTS.message);
        }
        jobRepository.saveAndFlush(job);
    }

    @Override
    @Transactional
    public void deleteJob(int jobId) throws IllegalArgumentException, DataIntegrityViolationException {
        if (RepositoryHelper.cannotFindId(jobRepository, jobId)) {
            throw new IllegalArgumentException(DELETE_NOT_FOUND.message);
        }
        //TODO: check if foreign key

        jobRepository.deleteById(jobId);
    }

    @Override
    @Transactional
    public void updateJob(int jobId, Job job) {
        if (RepositoryHelper.cannotFindId(jobRepository, jobId)) {
            throw new IllegalArgumentException(UPDATE_NOT_FOUND.message);
        }
        Job jobToUpdate = RepositoryHelper.getById(jobRepository, jobId);
        jobToUpdate.setName(job.getName());
        jobToUpdate.setDescription(job.getDescription());
    }
}
