package com.mcommings.campaigner.modules.overview.dtos;

import com.mcommings.campaigner.modules.overview.helpers.JobAssignmentOverview;
import com.mcommings.campaigner.modules.people.dtos.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PeopleOverviewDTO {

    private List<AbilityScoreDTO> abilityScores;
    private List<EventPlacePersonDTO> eventPlacePersons;
    private List<GenericMonsterDTO> genericMonsters;
    private List<JobAssignmentOverview> jobAssignments;
    private List<JobDTO> jobs;
    private List<NamedMonsterDTO> namedMonsters;
    private List<PersonDTO> persons;
    private List<RaceDTO> races;
}
