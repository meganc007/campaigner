package com.mcommings.campaigner.interfaces.people;

import com.mcommings.campaigner.models.people.JobAssignment;

import java.util.List;

public interface IJobAssignment {

    List<JobAssignment> getJobAssignments();

    void saveJobAssignment(JobAssignment jobAssignment);

    void deleteJobAssignment(int jobAssignmentId);

    void updateJobAssignment(int jobAssignmentId, JobAssignment jobAssignment);
}
