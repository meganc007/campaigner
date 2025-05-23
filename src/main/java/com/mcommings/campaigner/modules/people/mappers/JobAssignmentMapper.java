package com.mcommings.campaigner.modules.people.mappers;

import com.mcommings.campaigner.modules.people.dtos.JobAssignmentDTO;
import com.mcommings.campaigner.modules.people.entities.JobAssignment;
import org.mapstruct.Mapper;

@Mapper
public interface JobAssignmentMapper {
    JobAssignment mapFromJobAssignmentDto(JobAssignmentDTO dto);

    JobAssignmentDTO mapToJobAssignmentDto(JobAssignment jobAssignment);
}
