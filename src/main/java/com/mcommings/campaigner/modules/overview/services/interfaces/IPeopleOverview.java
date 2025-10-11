package com.mcommings.campaigner.modules.overview.services.interfaces;

import com.mcommings.campaigner.modules.overview.dtos.PeopleOverviewDTO;
import com.mcommings.campaigner.modules.overview.helpers.JobAssignmentOverview;
import com.mcommings.campaigner.modules.people.dtos.JobAssignmentDTO;

import java.util.List;
import java.util.UUID;

public interface IPeopleOverview {

    PeopleOverviewDTO getPeopleOverview(UUID campaignId);

    List<JobAssignmentOverview> getJobAssignmentOverview(UUID uuid);

    List<JobAssignmentOverview> mapJobAssignmentDTOtoJobAssignmentOverview(List<JobAssignmentDTO> jobAssignmentDTOS);
}
