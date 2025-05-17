package com.mcommings.campaigner.modules.people.mappers;

import com.mcommings.campaigner.modules.people.dtos.JobDTO;
import com.mcommings.campaigner.modules.people.entities.Job;
import org.mapstruct.Mapper;

@Mapper
public interface JobMapper {
    Job mapFromJobDto(JobDTO dto);

    JobDTO mapToJobDto(Job job);
}
