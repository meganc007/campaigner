package com.mcommings.campaigner.modules.overview.services;

import com.mcommings.campaigner.modules.overview.dtos.PeopleOverviewDTO;
import com.mcommings.campaigner.modules.overview.facades.mappers.PeopleMapperFacade;
import com.mcommings.campaigner.modules.overview.facades.repositories.PeopleRepositoryFacade;
import com.mcommings.campaigner.modules.overview.services.interfaces.IPeopleOverview;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

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
                .jobAssignments(jobAssignments)
                .jobs(jobs)
                .namedMonsters(namedMonsters)
                .persons(persons)
                .races(races)
                .build();
    }
}
