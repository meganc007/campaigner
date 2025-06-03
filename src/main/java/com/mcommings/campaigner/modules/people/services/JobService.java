package com.mcommings.campaigner.modules.people.services;

import com.mcommings.campaigner.modules.RepositoryHelper;
import com.mcommings.campaigner.modules.people.dtos.JobDTO;
import com.mcommings.campaigner.modules.people.mappers.JobMapper;
import com.mcommings.campaigner.modules.people.repositories.IJobRepository;
import com.mcommings.campaigner.modules.people.services.interfaces.IJob;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.mcommings.campaigner.enums.ErrorMessage.*;

@Service
@RequiredArgsConstructor
public class JobService implements IJob {

    private final IJobRepository jobRepository;
    private final JobMapper jobMapper;

    @Override
    public List<JobDTO> getJobs() {
        return jobRepository.findAll()
                .stream()
                .map(jobMapper::mapToJobDto)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<JobDTO> getJob(int jobId) {
        return jobRepository.findById(jobId)
                .map(jobMapper::mapToJobDto);
    }

    @Override
    public void saveJob(JobDTO job) throws IllegalArgumentException, DataIntegrityViolationException {
        if (RepositoryHelper.nameIsNullOrEmpty(job)) {
            throw new IllegalArgumentException(NULL_OR_EMPTY.message);
        }
        if (RepositoryHelper.nameAlreadyExists(jobRepository, job.getName())) {
            throw new DataIntegrityViolationException(NAME_EXISTS.message);
        }
        jobMapper.mapToJobDto(jobRepository
                .save(jobMapper.mapFromJobDto(job))
        );
    }

    @Override
    public void deleteJob(int jobId) throws IllegalArgumentException, DataIntegrityViolationException {
        if (RepositoryHelper.cannotFindId(jobRepository, jobId)) {
            throw new IllegalArgumentException(DELETE_NOT_FOUND.message);
        }

        jobRepository.deleteById(jobId);
    }

    @Override
    public Optional<JobDTO> updateJob(int jobId, JobDTO job) {
        if (RepositoryHelper.cannotFindId(jobRepository, jobId)) {
            throw new IllegalArgumentException(UPDATE_NOT_FOUND.message);
        }
        if (RepositoryHelper.nameIsNullOrEmpty(job)) {
            throw new IllegalArgumentException(NULL_OR_EMPTY.message);
        }
        if (RepositoryHelper.nameAlreadyExistsInAnotherRecord(jobRepository, job.getName(), jobId)) {
            throw new DataIntegrityViolationException(NAME_EXISTS.message);
        }

        return jobRepository.findById(jobId).map(foundJob -> {
            if (job.getName() != null) foundJob.setName(job.getName());
            if (job.getDescription() != null) foundJob.setDescription(job.getDescription());

            return jobMapper.mapToJobDto(jobRepository.save(foundJob));
        });
    }

}
