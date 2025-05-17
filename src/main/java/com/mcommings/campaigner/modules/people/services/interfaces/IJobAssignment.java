package com.mcommings.campaigner.modules.people.services.interfaces;

import com.mcommings.campaigner.modules.people.dtos.JobAssignmentDTO;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface IJobAssignment {

    List<JobAssignmentDTO> getJobAssignments();

    Optional<JobAssignmentDTO> getJobAssignment(int jobAssignmentId);

    List<JobAssignmentDTO> getJobAssignmentsByCampaignUUID(UUID uuid);

    List<JobAssignmentDTO> getJobAssignmentsByPersonId(int personId);

    List<JobAssignmentDTO> getJobAssignmentsByJobId(int jobId);

    void saveJobAssignment(JobAssignmentDTO jobAssignment);

    void deleteJobAssignment(int jobAssignmentId);

    Optional<JobAssignmentDTO> updateJobAssignment(int jobAssignmentId, JobAssignmentDTO jobAssignment);
}
