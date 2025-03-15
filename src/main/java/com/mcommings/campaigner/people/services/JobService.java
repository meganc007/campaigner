package com.mcommings.campaigner.people.services;

import com.mcommings.campaigner.common.entities.RepositoryHelper;
import com.mcommings.campaigner.people.entities.Job;
import com.mcommings.campaigner.people.repositories.IJobAssignmentRepository;
import com.mcommings.campaigner.people.repositories.IJobRepository;
import com.mcommings.campaigner.people.services.interfaces.IJob;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.mcommings.campaigner.enums.ErrorMessage.*;
import static com.mcommings.campaigner.enums.ForeignKey.FK_JOB;

@Service
public class JobService implements IJob {

    private final IJobRepository jobRepository;
    private final IJobAssignmentRepository jobAssignmentRepository;

    @Autowired
    public JobService(IJobRepository jobRepository, IJobAssignmentRepository jobAssignmentRepository) {
        this.jobRepository = jobRepository;
        this.jobAssignmentRepository = jobAssignmentRepository;
    }

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
        if (RepositoryHelper.isForeignKey(getReposWhereJobIsAForeignKey(), FK_JOB.columnName, jobId)) {
            throw new DataIntegrityViolationException(DELETE_FOREIGN_KEY.message);
        }

        jobRepository.deleteById(jobId);
    }

    @Override
    @Transactional
    public void updateJob(int jobId, Job job) {
        if (RepositoryHelper.cannotFindId(jobRepository, jobId)) {
            throw new IllegalArgumentException(UPDATE_NOT_FOUND.message);
        }
        Job jobToUpdate = RepositoryHelper.getById(jobRepository, jobId);
        if (job.getName() != null) jobToUpdate.setName(job.getName());
        if (job.getDescription() != null) jobToUpdate.setDescription(job.getDescription());
    }

    private List<CrudRepository> getReposWhereJobIsAForeignKey() {
        return new ArrayList<>(Arrays.asList(jobAssignmentRepository));
    }
}
