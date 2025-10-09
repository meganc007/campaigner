package com.mcommings.campaigner.services.overview;

import com.mcommings.campaigner.modules.overview.dtos.PeopleOverviewDTO;
import com.mcommings.campaigner.modules.overview.facades.mappers.PeopleMapperFacade;
import com.mcommings.campaigner.modules.overview.facades.repositories.PeopleRepositoryFacade;
import com.mcommings.campaigner.modules.overview.services.PeopleOverviewService;
import com.mcommings.campaigner.modules.people.dtos.AbilityScoreDTO;
import com.mcommings.campaigner.modules.people.entities.AbilityScore;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Collections;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@SpringBootTest
public class PeopleOverviewTest {

    @Mock
    private PeopleRepositoryFacade peopleRepositoryFacade;

    @Mock
    private PeopleMapperFacade peopleMapperFacade;

    @InjectMocks
    private PeopleOverviewService peopleOverviewService;

    @Test
    void whenCampaignUUIDIsValid_getPeopleOverview_returnsAggregatedPeopleData() {
        UUID campaignId = UUID.randomUUID();

        AbilityScore abilityScore = new AbilityScore(1, campaignId, 10, 11, 12, 13, 14, 15);
        AbilityScoreDTO abilityScoreDTO = new AbilityScoreDTO(1, campaignId, 10, 11, 12, 13, 14, 15);

        when(peopleRepositoryFacade.findAbilityScores(campaignId)).thenReturn(List.of(abilityScore));
        when(peopleMapperFacade.toAbilityScoreDTO(abilityScore)).thenReturn(abilityScoreDTO);

        PeopleOverviewDTO result = peopleOverviewService.getPeopleOverview(campaignId);

        assertNotNull(result);
        assertEquals(1, result.getAbilityScores().size());
        assertEquals(10, result.getAbilityScores().get(0).getStrength());

        verify(peopleRepositoryFacade).findAbilityScores(campaignId);
        verify(peopleMapperFacade).toAbilityScoreDTO(abilityScore);
    }

    @Test
    void whenCampaignUUIDIsNotValid_getPeopleOverview_returnsEmptyList() {
        UUID invalidCampaignId = UUID.randomUUID();

        when(peopleRepositoryFacade.findAbilityScores(invalidCampaignId)).thenReturn(Collections.emptyList());
        when(peopleRepositoryFacade.findEventPlacePersons(invalidCampaignId)).thenReturn(Collections.emptyList());
        when(peopleRepositoryFacade.findGenericMonsters(invalidCampaignId)).thenReturn(Collections.emptyList());
        when(peopleRepositoryFacade.findJobAssignments(invalidCampaignId)).thenReturn(Collections.emptyList());
        when(peopleRepositoryFacade.findJobs()).thenReturn(Collections.emptyList());
        when(peopleRepositoryFacade.findNamedMonsters(invalidCampaignId)).thenReturn(Collections.emptyList());
        when(peopleRepositoryFacade.findPersons(invalidCampaignId)).thenReturn(Collections.emptyList());
        when(peopleRepositoryFacade.findRaces()).thenReturn(Collections.emptyList());

        PeopleOverviewDTO result = peopleOverviewService.getPeopleOverview(invalidCampaignId);

        assertNotNull(result);
        assertTrue(result.getAbilityScores().isEmpty());
        assertTrue(result.getEventPlacePersons().isEmpty());
        assertTrue(result.getGenericMonsters().isEmpty());
        assertTrue(result.getJobs().isEmpty());
        assertTrue(result.getJobAssignments().isEmpty());
        assertTrue(result.getNamedMonsters().isEmpty());
        assertTrue(result.getPersons().isEmpty());
        assertTrue(result.getRaces().isEmpty());

        verify(peopleRepositoryFacade).findAbilityScores(invalidCampaignId);
        verify(peopleRepositoryFacade).findEventPlacePersons(invalidCampaignId);
        verify(peopleRepositoryFacade).findGenericMonsters(invalidCampaignId);
        verify(peopleRepositoryFacade).findJobAssignments(invalidCampaignId);
        verify(peopleRepositoryFacade).findJobs();
        verify(peopleRepositoryFacade).findNamedMonsters(invalidCampaignId);
        verify(peopleRepositoryFacade).findPersons(invalidCampaignId);
        verify(peopleRepositoryFacade).findRaces();
    }
}
