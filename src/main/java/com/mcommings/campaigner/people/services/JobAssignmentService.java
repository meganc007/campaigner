package com.mcommings.campaigner.people.services;

import com.mcommings.campaigner.common.entities.RepositoryHelper;
import com.mcommings.campaigner.people.entities.JobAssignment;
import com.mcommings.campaigner.people.repositories.IJobAssignmentRepository;
import com.mcommings.campaigner.people.repositories.IJobRepository;
import com.mcommings.campaigner.people.repositories.IPersonRepository;
import com.mcommings.campaigner.people.services.interfaces.IJobAssignment;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Service;

import java.util.*;

import static com.mcommings.campaigner.enums.ErrorMessage.*;
import static com.mcommings.campaigner.enums.ForeignKey.FK_JOB;
import static com.mcommings.campaigner.enums.ForeignKey.FK_PERSON;

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
    public List<JobAssignment> getJobAssignmentsByCampaignUUID(UUID uuid) {
        return jobAssignmentRepository.findByfk_campaign_uuid(uuid);
    }

    @Override
    public List<JobAssignment> getJobAssignmentsByPersonId(int personId) {
        return jobAssignmentRepository.findByfk_person(personId);
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
                RepositoryHelper.foreignKeyIsNotValid(buildReposAndColumnsHashMap(jobAssignment), jobAssignment)) {
            throw new DataIntegrityViolationException(INSERT_FOREIGN_KEY.message);
        }
        JobAssignment jobAssignmentToUpdate = RepositoryHelper.getById(jobAssignmentRepository, jobAssignmentId);
        if (jobAssignment.getFk_person() != null) jobAssignmentToUpdate.setFk_person(jobAssignment.getFk_person());
        if (jobAssignment.getFk_job() != null) jobAssignmentToUpdate.setFk_job(jobAssignment.getFk_job());
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

    private HashMap<CrudRepository, String> buildReposAndColumnsHashMap(JobAssignment jobAssignment) {
        HashMap<CrudRepository, String> reposAndColumns = new HashMap<>();

        if (jobAssignment.getFk_person() != null) {
            reposAndColumns.put(personRepository, FK_PERSON.columnName);
        }
        if (jobAssignment.getFk_job() != null) {
            reposAndColumns.put(jobRepository, FK_JOB.columnName);
        }
        return reposAndColumns;
    }
}
