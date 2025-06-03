package com.mcommings.campaigner.modules.people.services;

import com.mcommings.campaigner.modules.RepositoryHelper;
import com.mcommings.campaigner.modules.people.dtos.JobAssignmentDTO;
import com.mcommings.campaigner.modules.people.entities.JobAssignment;
import com.mcommings.campaigner.modules.people.mappers.JobAssignmentMapper;
import com.mcommings.campaigner.modules.people.repositories.IJobAssignmentRepository;
import com.mcommings.campaigner.modules.people.services.interfaces.IJobAssignment;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import static com.mcommings.campaigner.enums.ErrorMessage.*;

@Service
@RequiredArgsConstructor
public class JobAssignmentService implements IJobAssignment {

    private final IJobAssignmentRepository jobAssignmentRepository;
    private final JobAssignmentMapper jobAssignmentMapper;

    @Override
    public List<JobAssignmentDTO> getJobAssignments() {

        return jobAssignmentRepository.findAll()
                .stream()
                .map(jobAssignmentMapper::mapToJobAssignmentDto)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<JobAssignmentDTO> getJobAssignment(int jobAssignmentId) {
        return jobAssignmentRepository.findById(jobAssignmentId)
                .map(jobAssignmentMapper::mapToJobAssignmentDto);
    }

    @Override
    public List<JobAssignmentDTO> getJobAssignmentsByCampaignUUID(UUID uuid) {
        return jobAssignmentRepository.findByfk_campaign_uuid(uuid)
                .stream()
                .map(jobAssignmentMapper::mapToJobAssignmentDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<JobAssignmentDTO> getJobAssignmentsByPersonId(int personId) {
        return jobAssignmentRepository.findByfk_person(personId)
                .stream()
                .map(jobAssignmentMapper::mapToJobAssignmentDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<JobAssignmentDTO> getJobAssignmentsByJobId(int jobId) {
        return jobAssignmentRepository.findByfk_job(jobId)
                .stream()
                .map(jobAssignmentMapper::mapToJobAssignmentDto)
                .collect(Collectors.toList());
    }

    @Override
    public void saveJobAssignment(JobAssignmentDTO jobAssignment) throws IllegalArgumentException, DataIntegrityViolationException {
        if (jobAssignmentAlreadyExists(jobAssignmentMapper.mapFromJobAssignmentDto(jobAssignment))) {
            throw new DataIntegrityViolationException(JOB_ASSIGNMENT_EXISTS.message);
        }

        jobAssignmentMapper.mapToJobAssignmentDto(
                jobAssignmentRepository.save(jobAssignmentMapper.mapFromJobAssignmentDto(jobAssignment))
        );
    }

    @Override
    public void deleteJobAssignment(int jobAssignmentId) throws IllegalArgumentException, DataIntegrityViolationException {
        if (RepositoryHelper.cannotFindId(jobAssignmentRepository, jobAssignmentId)) {
            throw new IllegalArgumentException(DELETE_NOT_FOUND.message);
        }

        jobAssignmentRepository.deleteById(jobAssignmentId);
    }

    @Override
    public Optional<JobAssignmentDTO> updateJobAssignment(int jobAssignmentId, JobAssignmentDTO jobAssignment) throws IllegalArgumentException, DataIntegrityViolationException {
        if (RepositoryHelper.cannotFindId(jobAssignmentRepository, jobAssignmentId)) {
            throw new IllegalArgumentException(UPDATE_NOT_FOUND.message);
        }
        if (jobAssignmentAlreadyExists(jobAssignmentMapper.mapFromJobAssignmentDto(jobAssignment))) {
            throw new DataIntegrityViolationException(JOB_ASSIGNMENT_EXISTS.message);
        }

        return jobAssignmentRepository.findById(jobAssignmentId).map(foundJobAssignment -> {
            if (jobAssignment.getFk_person() != null) foundJobAssignment.setFk_person(jobAssignment.getFk_person());
            if (jobAssignment.getFk_job() != null) foundJobAssignment.setFk_job(jobAssignment.getFk_job());

            return jobAssignmentMapper.mapToJobAssignmentDto(jobAssignmentRepository.save(foundJobAssignment));
        });

    }

    private boolean jobAssignmentAlreadyExists(JobAssignment jobAssignment) {
        return jobAssignmentRepository
                .jobAssignmentExists(jobAssignment)
                .map(existing -> existing.getId() != jobAssignment.getId())
                .orElse(false);
    }

}
