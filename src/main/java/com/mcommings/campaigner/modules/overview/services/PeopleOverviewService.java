package com.mcommings.campaigner.modules.overview.services;

import com.mcommings.campaigner.modules.RepositoryHelper;
import com.mcommings.campaigner.modules.calendar.dtos.EventDTO;
import com.mcommings.campaigner.modules.locations.dtos.PlaceDTO;
import com.mcommings.campaigner.modules.overview.dtos.PeopleOverviewDTO;
import com.mcommings.campaigner.modules.overview.facades.mappers.PeopleMapperFacade;
import com.mcommings.campaigner.modules.overview.facades.repositories.PeopleRepositoryFacade;
import com.mcommings.campaigner.modules.overview.helpers.EventPlacePersonOverview;
import com.mcommings.campaigner.modules.overview.helpers.JobAssignmentOverview;
import com.mcommings.campaigner.modules.overview.services.interfaces.IPeopleOverview;
import com.mcommings.campaigner.modules.people.dtos.EventPlacePersonDTO;
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
    public List<EventPlacePersonOverview> getEventPlacePersonOverview(UUID uuid) {
        var epps = peopleRepositoryFacade.findEventPlacePersons(uuid)
                .stream().map(peopleMapperFacade::toEventPlacePersonDTO).toList();
        return mapEventPlacePersonDTOtoEventPlacePersonOverview(epps);
    }

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

    public List<EventPlacePersonOverview> mapEventPlacePersonDTOtoEventPlacePersonOverview(List<EventPlacePersonDTO> epps) {
        List<EventPlacePersonOverview> overviews = new ArrayList<>();
        for (EventPlacePersonDTO epp : epps) {
            Optional<EventDTO> event = getEvent(epp);
            Optional<PlaceDTO> place = getPlace(epp);
            Optional<PersonDTO> person = getPerson(epp);

            String eventName = setEventName(event);
            String placeName = setPlaceName(place);
            String personName = setPersonName(person);

            EventPlacePersonOverview eppo = EventPlacePersonOverview.builder()
                    .id(epp.getId())
                    .fk_event(epp.getFk_event())
                    .fk_place(epp.getFk_place())
                    .fk_person(epp.getFk_person())
                    .fk_campaign_uuid(epp.getFk_campaign_uuid())
                    .eventName(eventName)
                    .placeName(placeName)
                    .personName(personName)
                    .build();

            overviews.add(eppo);
        }
        return overviews;
    }

    private Optional<PersonDTO> getPerson(JobAssignmentDTO jobAssignment) {
        if (jobAssignment.getFk_person() != null) {
            return peopleRepositoryFacade.findPerson(jobAssignment.getFk_person()).map(peopleMapperFacade::toPersonDTO);
        }
        return Optional.empty();
    }

    private Optional<PersonDTO> getPerson(EventPlacePersonDTO eventPlacePerson) {
        if (eventPlacePerson.getFk_person() != null) {
            return peopleRepositoryFacade.findPerson(eventPlacePerson.getFk_person()).map(peopleMapperFacade::toPersonDTO);
        }
        return Optional.empty();
    }

    private Optional<JobDTO> getJob(JobAssignmentDTO jobAssignment) {
        if (jobAssignment.getFk_job() != null) {
            return peopleRepositoryFacade.findJob(jobAssignment.getFk_job()).map(peopleMapperFacade::toJobDTO);
        }
        return Optional.empty();
    }

    private Optional<EventDTO> getEvent(EventPlacePersonDTO eventPlacePerson) {
        if (eventPlacePerson.getFk_event() != null) {
            return peopleRepositoryFacade.findEvent(eventPlacePerson.getFk_event()).map(peopleMapperFacade::toEventDTO);
        }
        return Optional.empty();
    }

    private Optional<PlaceDTO> getPlace(EventPlacePersonDTO eventPlacePerson) {
        if (eventPlacePerson.getFk_place() != null) {
            return peopleRepositoryFacade.findPlace(eventPlacePerson.getFk_place()).map(peopleMapperFacade::toPlaceDTO);
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

    private String setEventName(Optional<EventDTO> event) {
        if (!event.isEmpty()) {
            return RepositoryHelper.nameIsNullOrEmpty(event.get().getName()) ? "" : event.get().getName();
        }
        return null;
    }

    private String setPlaceName(Optional<PlaceDTO> place) {
        if (!place.isEmpty()) {
            return RepositoryHelper.nameIsNullOrEmpty(place.get().getName()) ? "" : place.get().getName();
        }
        return null;
    }
}
