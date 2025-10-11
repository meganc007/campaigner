package com.mcommings.campaigner.modules.overview.controllers;

import com.mcommings.campaigner.modules.overview.dtos.PeopleOverviewDTO;
import com.mcommings.campaigner.modules.overview.helpers.JobAssignmentOverview;
import com.mcommings.campaigner.modules.overview.services.interfaces.IPeopleOverview;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "api/people-overview")
public class PeopleOverviewController {

    private final IPeopleOverview peopleService;

    @GetMapping(path = "/{uuid}")
    public PeopleOverviewDTO getPeopleOverview(@PathVariable("uuid") UUID uuid) {
        return peopleService.getPeopleOverview(uuid);
    }

    @GetMapping(path = "/{uuid}/jobAssignments")
    public List<JobAssignmentOverview> getJobAssignmentOverview(@PathVariable("uuid") UUID uuid) {
        return peopleService.getJobAssignmentOverview(uuid);
    }
}
