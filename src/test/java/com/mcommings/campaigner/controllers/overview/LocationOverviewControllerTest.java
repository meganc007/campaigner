package com.mcommings.campaigner.controllers.overview;

import com.mcommings.campaigner.modules.locations.dtos.ContinentDTO;
import com.mcommings.campaigner.modules.overview.controllers.LocationOverviewController;
import com.mcommings.campaigner.modules.overview.dtos.LocationOverviewDTO;
import com.mcommings.campaigner.modules.overview.services.LocationOverviewService;
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

@WebMvcTest(LocationOverviewController.class)
public class LocationOverviewControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private LocationOverviewService locationOverviewService;

    private final String URI = "/api/locations/";

    @Test
    void whenCampaignUUIDIsValid_getLocationOverview_returnsOverviewDataAsJson() throws Exception {
        UUID campaignId = UUID.randomUUID();

        LocationOverviewDTO dto = LocationOverviewDTO.builder()
                .continents(List.of(new ContinentDTO(1, "Continentia", "desc", campaignId)))
                .countries(Collections.emptyList())
                .cities(Collections.emptyList())
                .regions(Collections.emptyList())
                .landmarks(Collections.emptyList())
                .places(Collections.emptyList())
                .placeTypes(Collections.emptyList())
                .terrains(Collections.emptyList())
                .climates(Collections.emptyList())
                .build();

        when(locationOverviewService.getLocationOverview(campaignId)).thenReturn(dto);

        mockMvc.perform(get(URI + campaignId)
                        .param("campaignId", campaignId.toString()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.continents[0].name").value("Continentia"));
    }

    @Test
    void whenCampaignUUIDIsInvalid_thenReturnEmptyOverview() throws Exception {
        UUID invalidCampaignId = UUID.randomUUID();

        // Empty DTO to simulate no data found
        LocationOverviewDTO emptyOverview = LocationOverviewDTO.builder()
                .continents(Collections.emptyList())
                .countries(Collections.emptyList())
                .cities(Collections.emptyList())
                .regions(Collections.emptyList())
                .landmarks(Collections.emptyList())
                .places(Collections.emptyList())
                .placeTypes(Collections.emptyList())
                .terrains(Collections.emptyList())
                .climates(Collections.emptyList())
                .build();

        when(locationOverviewService.getLocationOverview(invalidCampaignId)).thenReturn(emptyOverview);

        mockMvc.perform(get(URI + invalidCampaignId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.continents").isArray())
                .andExpect(jsonPath("$.continents").isEmpty())
                .andExpect(jsonPath("$.countries").isArray())
                .andExpect(jsonPath("$.countries").isEmpty())
                .andExpect(jsonPath("$.cities").isArray())
                .andExpect(jsonPath("$.cities").isEmpty())
                .andExpect(jsonPath("$.regions").isArray())
                .andExpect(jsonPath("$.regions").isEmpty())
                .andExpect(jsonPath("$.landmarks").isArray())
                .andExpect(jsonPath("$.landmarks").isEmpty())
                .andExpect(jsonPath("$.places").isArray())
                .andExpect(jsonPath("$.places").isEmpty())
                .andExpect(jsonPath("$.placeTypes").isArray())
                .andExpect(jsonPath("$.placeTypes").isEmpty())
                .andExpect(jsonPath("$.terrains").isArray())
                .andExpect(jsonPath("$.terrains").isEmpty())
                .andExpect(jsonPath("$.climates").isArray())
                .andExpect(jsonPath("$.climates").isEmpty());

        verify(locationOverviewService).getLocationOverview(invalidCampaignId);
    }
}
