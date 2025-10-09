package com.mcommings.campaigner.controllers.overview;

import com.mcommings.campaigner.modules.overview.controllers.PeopleOverviewController;
import com.mcommings.campaigner.modules.overview.dtos.PeopleOverviewDTO;
import com.mcommings.campaigner.modules.overview.services.PeopleOverviewService;
import com.mcommings.campaigner.modules.people.dtos.AbilityScoreDTO;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;
import java.util.List;
import java.util.UUID;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@WebMvcTest(PeopleOverviewController.class)
public class PeopleOverviewControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private PeopleOverviewService peopleOverviewService;

    private final String URI = "/api/people-overview/";

    @Test
    void whenCampaignUUIDIsValid_getPeopleOverview_returnsOverviewDataAsJson() throws Exception {
        UUID campaignId = UUID.randomUUID();

        PeopleOverviewDTO dto = PeopleOverviewDTO.builder()
                .abilityScores(List.of(new AbilityScoreDTO(1, campaignId, 10, 11, 12, 13, 14, 15)))
                .eventPlacePersons(Collections.emptyList())
                .genericMonsters(Collections.emptyList())
                .jobAssignments(Collections.emptyList())
                .jobs(Collections.emptyList())
                .namedMonsters(Collections.emptyList())
                .persons(Collections.emptyList())
                .races(Collections.emptyList())
                .build();

        when(peopleOverviewService.getPeopleOverview(campaignId)).thenReturn(dto);

        mockMvc.perform(get(URI + campaignId)
                        .param("campaignId", campaignId.toString()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.abilityScores[0].strength").value(10));
    }

    @Test
    void whenCampaignUUIDIsInvalid__thenReturnEmptyOverview() throws Exception {
        UUID invalidCampaignId = UUID.randomUUID();

        PeopleOverviewDTO emptyOverview = PeopleOverviewDTO.builder()
                .abilityScores(Collections.emptyList())
                .eventPlacePersons(Collections.emptyList())
                .genericMonsters(Collections.emptyList())
                .jobAssignments(Collections.emptyList())
                .jobs(Collections.emptyList())
                .namedMonsters(Collections.emptyList())
                .persons(Collections.emptyList())
                .races(Collections.emptyList())
                .build();

        when(peopleOverviewService.getPeopleOverview(invalidCampaignId)).thenReturn(emptyOverview);

        mockMvc.perform(get(URI + invalidCampaignId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.abilityScores").isEmpty())
                .andExpect(jsonPath("$.eventPlacePersons").isEmpty())
                .andExpect(jsonPath("$.genericMonsters").isEmpty())
                .andExpect(jsonPath("$.jobAssignments").isEmpty())
                .andExpect(jsonPath("$.jobs").isEmpty())
                .andExpect(jsonPath("$.namedMonsters").isEmpty())
                .andExpect(jsonPath("$.persons").isEmpty())
                .andExpect(jsonPath("$.races").isEmpty());

        verify(peopleOverviewService).getPeopleOverview(invalidCampaignId);
    }

}
