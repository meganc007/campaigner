package com.mcommings.campaigner.modules.overview.facades.mappers;

import com.mcommings.campaigner.modules.people.dtos.*;
import com.mcommings.campaigner.modules.people.entities.*;
import com.mcommings.campaigner.modules.people.mappers.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PeopleMapperFacade {

    private final AbilityScoreMapper abilityScoreMapper;
    private final EventPlacePersonMapper eventPlacePersonMapper;
    private final GenericMonsterMapper genericMonsterMapper;
    private final JobAssignmentMapper jobAssignmentMapper;
    private final JobMapper jobMapper;
    private final NamedMonsterMapper namedMonsterMapper;
    private final PersonMapper personMapper;
    private final RaceMapper raceMapper;

    public AbilityScoreDTO toAbilityScoreDTO(AbilityScore entity) {
        return abilityScoreMapper.mapToAbilityScoreDto(entity);
    }

    public EventPlacePersonDTO toEventPlacePersonDTO(EventPlacePerson entity) {
        return eventPlacePersonMapper.mapToEventPlacePersonDto(entity);
    }

    public GenericMonsterDTO toGenericMonsterDTO(GenericMonster entity) {
        return genericMonsterMapper.mapToGenericMonsterDto(entity);
    }

    public JobAssignmentDTO toJobAssignmentDTO(JobAssignment entity) {
        return jobAssignmentMapper.mapToJobAssignmentDto(entity);
    }

    public JobDTO toJobDTO(Job entity) {
        return jobMapper.mapToJobDto(entity);
    }

    public NamedMonsterDTO toNamedMonsterDTO(NamedMonster entity) {
        return namedMonsterMapper.mapToNamedMonsterDto(entity);
    }

    public PersonDTO toPersonDTO(Person entity) {
        return personMapper.mapToPersonDto(entity);
    }

    public RaceDTO toRaceDTO(Race entity) {
        return raceMapper.mapToRaceDto(entity);
    }

}
