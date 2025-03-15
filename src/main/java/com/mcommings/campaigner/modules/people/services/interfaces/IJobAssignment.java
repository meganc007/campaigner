package com.mcommings.campaigner.modules.people.services.interfaces;

import com.mcommings.campaigner.modules.people.entities.JobAssignment;

import java.util.List;
import java.util.UUID;

public interface IJobAssignment {

    List<JobAssignment> getJobAssignments();

    List<JobAssignment> getJobAssignmentsByCampaignUUID(UUID uuid);

    List<JobAssignment> getJobAssignmentsByPersonId(int personId);

    void saveJobAssignment(JobAssignment jobAssignment);

    void deleteJobAssignment(int jobAssignmentId);

    void updateJobAssignment(int jobAssignmentId, JobAssignment jobAssignment);
}
