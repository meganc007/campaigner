package com.mcommings.campaigner.modules.overview.facades.repositories;

import com.mcommings.campaigner.modules.calendar.entities.Event;
import com.mcommings.campaigner.modules.calendar.repositories.IEventRepository;
import com.mcommings.campaigner.modules.locations.entities.Place;
import com.mcommings.campaigner.modules.locations.repositories.IPlaceRepository;
import com.mcommings.campaigner.modules.people.entities.*;
import com.mcommings.campaigner.modules.people.repositories.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class PeopleRepositoryFacade {

    private final IAbilityScoreRepository abilityScoreRepository;
    private final IEventPlacePersonRepository eventPlacePersonRepository;
    private final IEventRepository eventRepository;
    private final IPlaceRepository placeRepository;
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

    public Optional<Event> findEvent(int id) {
        return eventRepository.findById(id);
    }

    public Optional<Place> findPlace(int id) {
        return placeRepository.findById(id);
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

    public Optional<Job> findJob(int id) {
        return jobRepository.findById(id);
    }

    public List<NamedMonster> findNamedMonsters(UUID uuid) {
        return namedMonsterRepository.findByfk_campaign_uuid(uuid);
    }

    public List<Person> findPersons(UUID uuid) {
        return personRepository.findByfk_campaign_uuid(uuid);
    }

    public Optional<Person> findPerson(int id) {
        return personRepository.findById(id);
    }

    public List<Race> findRaces() {
        return raceRepository.findAllByOrderByNameAsc();
    }
}
