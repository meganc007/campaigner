package com.mcommings.campaigner.modules.overview.services;

import com.mcommings.campaigner.modules.RepositoryHelper;
import com.mcommings.campaigner.modules.overview.dtos.PeopleOverviewDTO;
import com.mcommings.campaigner.modules.overview.facades.mappers.PeopleMapperFacade;
import com.mcommings.campaigner.modules.overview.facades.repositories.PeopleRepositoryFacade;
import com.mcommings.campaigner.modules.overview.helpers.JobAssignmentOverview;
import com.mcommings.campaigner.modules.overview.services.interfaces.IPeopleOverview;
import com.mcommings.campaigner.modules.people.dtos.JobAssignmentDTO;
import com.mcommings.campaigner.modules.people.dtos.JobDTO;
import com.mcommings.campaigner.modules.people.dtos.PersonDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PeopleOverviewService implements IPeopleOverview {

    private final PeopleMapperFacade peopleMapperFacade;
    private final PeopleRepositoryFacade peopleRepositoryFacade;

    @Override
    public PeopleOverviewDTO getPeopleOverview(UUID uuid) {
        var abilityScores = peopleRepositoryFacade.findAbilityScores(uuid)
                .stream().map(peopleMapperFacade::toAbilityScoreDTO).toList();

        var eventPlacePersons = peopleRepositoryFacade.findEventPlacePersons(uuid)
                .stream().map(peopleMapperFacade::toEventPlacePersonDTO).toList();

        var genericMonsters = peopleRepositoryFacade.findGenericMonsters(uuid)
                .stream().map(peopleMapperFacade::toGenericMonsterDTO).toList();

        var jobAssignments = peopleRepositoryFacade.findJobAssignments(uuid)
                .stream().map(peopleMapperFacade::toJobAssignmentDTO).toList();

        var jobAssignmentOverviews = mapJobAssignmentDTOtoJobAssignmentOverview(jobAssignments);

        var jobs = peopleRepositoryFacade.findJobs()
                .stream().map(peopleMapperFacade::toJobDTO).toList();

        var namedMonsters = peopleRepositoryFacade.findNamedMonsters(uuid)
                .stream().map(peopleMapperFacade::toNamedMonsterDTO).toList();

        var persons = peopleRepositoryFacade.findPersons(uuid)
                .stream().map(peopleMapperFacade::toPersonDTO).toList();

        var races = peopleRepositoryFacade.findRaces()
                .stream().map(peopleMapperFacade::toRaceDTO).toList();

        return PeopleOverviewDTO.builder()
                .abilityScores(abilityScores)
                .eventPlacePersons(eventPlacePersons)
                .genericMonsters(genericMonsters)
                .jobAssignments(jobAssignmentOverviews)
                .jobs(jobs)
                .namedMonsters(namedMonsters)
                .persons(persons)
                .races(races)
                .build();
    }

    @Override
    public List<JobAssignmentOverview> getJobAssignmentOverview(UUID uuid) {
        var jobAssignments = peopleRepositoryFacade.findJobAssignments(uuid)
                .stream().map(peopleMapperFacade::toJobAssignmentDTO).toList();
        return mapJobAssignmentDTOtoJobAssignmentOverview(jobAssignments);
    }

    @Override
    public List<JobAssignmentOverview> mapJobAssignmentDTOtoJobAssignmentOverview(List<JobAssignmentDTO> jobAssignments) {
        List<JobAssignmentOverview> overviews = new ArrayList<>();
        for (JobAssignmentDTO jobAssignment : jobAssignments) {
            Optional<PersonDTO> person = getPerson(jobAssignment);
            Optional<JobDTO> job = getJob(jobAssignment);

            String personName = setPersonName(person);
            String jobName = setJobName(job);

            JobAssignmentOverview jao = JobAssignmentOverview.builder()
                    .id(jobAssignment.getId())
                    .fk_person(jobAssignment.getFk_person())
                    .fk_job(jobAssignment.getFk_job())
                    .fk_campaign_uuid(jobAssignment.getFk_campaign_uuid())
                    .personName(personName)
                    .jobName(jobName)
                    .build();
            overviews.add(jao);
        }
        return overviews;
    }

    private Optional<PersonDTO> getPerson(JobAssignmentDTO jobAssignment) {
        if (jobAssignment.getFk_person() != null) {
            return peopleRepositoryFacade.findPerson(jobAssignment.getFk_person()).map(peopleMapperFacade::toPersonDTO);
        }
        return Optional.empty();
    }

    private Optional<JobDTO> getJob(JobAssignmentDTO jobAssignment) {
        if (jobAssignment.getFk_job() != null) {
            return peopleRepositoryFacade.findJob(jobAssignment.getFk_job()).map(peopleMapperFacade::toJobDTO);
        }
        return Optional.empty();
    }

    private String setPersonName(Optional<PersonDTO> person) {
        if (!person.isEmpty()) {
            return RepositoryHelper.nameIsNullOrEmpty(person.get().getLastName()) ?
                    person.get().getFirstName()
                    : person.get().getFirstName() + " " + person.get().getLastName();
        }
        return null;
    }

    private String setJobName(Optional<JobDTO> job) {
        if (!job.isEmpty()) {
            return RepositoryHelper.nameIsNullOrEmpty(job.get().getName()) ? "" : job.get().getName();
        }
        return null;
    }
}
