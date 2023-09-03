package com.mcommings.campaigner.services.people;

import com.mcommings.campaigner.interfaces.people.IJobAssignment;
import com.mcommings.campaigner.models.RepositoryHelper;
import com.mcommings.campaigner.models.people.JobAssignment;
import com.mcommings.campaigner.repositories.people.IJobAssignmentRepository;
import com.mcommings.campaigner.repositories.people.IJobRepository;
import com.mcommings.campaigner.repositories.people.IPersonRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.mcommings.campaigner.enums.ErrorMessage.*;

@Service
public class JobAssignmentService implements IJobAssignment {

    private final IJobAssignmentRepository jobAssignmentRepository;
    private final IPersonRepository personRepository;
    private final IJobRepository jobRepository;

    @Autowired
    public JobAssignmentService(IJobAssignmentRepository jobAssignmentRepository, IPersonRepository personRepository,
                                IJobRepository jobRepository) {
        this.jobAssignmentRepository = jobAssignmentRepository;
        this.personRepository = personRepository;
        this.jobRepository = jobRepository;
    }

    @Override
    public List<JobAssignment> getJobAssignments() {
        return jobAssignmentRepository.findAll();
    }

    @Override
    @Transactional
    public void saveJobAssignment(JobAssignment jobAssignment) throws IllegalArgumentException, DataIntegrityViolationException {
        if (jobAssignmentAlreadyExists(jobAssignment)) {
            throw new DataIntegrityViolationException(JOB_ASSIGNMENT_EXISTS.message);
        }
        if (hasForeignKeys(jobAssignment) &&
                RepositoryHelper.foreignKeyIsNotValid(jobAssignmentRepository, getListOfForeignKeyRepositories(), jobAssignment)) {
            throw new DataIntegrityViolationException(INSERT_FOREIGN_KEY.message);
        }

        jobAssignmentRepository.saveAndFlush(jobAssignment);
    }

    @Override
    @Transactional
    public void deleteJobAssignment(int jobAssignmentId) throws IllegalArgumentException, DataIntegrityViolationException {
        if (RepositoryHelper.cannotFindId(jobAssignmentRepository, jobAssignmentId)) {
            throw new IllegalArgumentException(DELETE_NOT_FOUND.message);
        }
        //TODO: fk check when JobAssignment is a fk

        jobAssignmentRepository.deleteById(jobAssignmentId);
    }

    @Override
    @Transactional
    public void updateJobAssignment(int jobAssignmentId, JobAssignment jobAssignment) throws IllegalArgumentException, DataIntegrityViolationException {
        if (RepositoryHelper.cannotFindId(jobAssignmentRepository, jobAssignmentId)) {
            throw new IllegalArgumentException(UPDATE_NOT_FOUND.message);
        }
        if (hasForeignKeys(jobAssignment) &&
                RepositoryHelper.foreignKeyIsNotValid(jobAssignmentRepository, getListOfForeignKeyRepositories(), jobAssignment)) {
            throw new DataIntegrityViolationException(UPDATE_FOREIGN_KEY.message);
        }
        JobAssignment jobAssignmentToUpdate = RepositoryHelper.getById(jobAssignmentRepository, jobAssignmentId);
        jobAssignmentToUpdate.setFk_person(jobAssignment.getFk_person());
        jobAssignmentToUpdate.setFk_job(jobAssignment.getFk_job());
    }

    private boolean jobAssignmentAlreadyExists(JobAssignment jobAssignment) {
        return jobAssignmentRepository.jobAssignmentExists(jobAssignment).isPresent();
    }

    private boolean hasForeignKeys(JobAssignment jobAssignment) {
        return jobAssignment.getFk_person() != null ||
                jobAssignment.getFk_job() != null;
    }

    private List<CrudRepository> getListOfForeignKeyRepositories() {
        return new ArrayList<>(Arrays.asList(personRepository, jobRepository));
    }
}
