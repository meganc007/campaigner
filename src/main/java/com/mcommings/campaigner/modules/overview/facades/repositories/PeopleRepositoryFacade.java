package com.mcommings.campaigner.modules.overview.facades.repositories;

import com.mcommings.campaigner.modules.people.entities.*;
import com.mcommings.campaigner.modules.people.repositories.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class PeopleRepositoryFacade {

    private final IAbilityScoreRepository abilityScoreRepository;
    private final IEventPlacePersonRepository eventPlacePersonRepository;
    private final IGenericMonsterRepository genericMonsterRepository;
    private final IJobAssignmentRepository jobAssignmentRepository;
    private final IJobRepository jobRepository;
    private final INamedMonsterRepository namedMonsterRepository;
    private final IPersonRepository personRepository;
    private final IRaceRepository raceRepository;

    public List<AbilityScore> findAbilityScores(UUID uuid) {
        return abilityScoreRepository.findByfk_campaign_uuid(uuid);
    }

    public List<EventPlacePerson> findEventPlacePersons(UUID uuid) {
        return eventPlacePersonRepository.findByfk_campaign_uuid(uuid);
    }

    public List<GenericMonster> findGenericMonsters(UUID uuid) {
        return genericMonsterRepository.findByfk_campaign_uuid(uuid);
    }

    public List<JobAssignment> findJobAssignments(UUID uuid) {
        return jobAssignmentRepository.findByfk_campaign_uuid(uuid);
    }

    public List<Job> findJobs() {
        return jobRepository.findAllByOrderByNameAsc();
    }

    public List<NamedMonster> findNamedMonsters(UUID uuid) {
        return namedMonsterRepository.findByfk_campaign_uuid(uuid);
    }

    public List<Person> findPersons(UUID uuid) {
        return personRepository.findByfk_campaign_uuid(uuid);
    }

    public List<Race> findRaces() {
        return raceRepository.findAllByOrderByNameAsc();
    }
}
