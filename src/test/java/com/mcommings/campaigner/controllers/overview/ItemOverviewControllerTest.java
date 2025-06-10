package com.mcommings.campaigner.controllers.overview;

import com.mcommings.campaigner.modules.items.dtos.DamageTypeDTO;
import com.mcommings.campaigner.modules.overview.controllers.ItemOverviewController;
import com.mcommings.campaigner.modules.overview.dtos.ItemOverviewDTO;
import com.mcommings.campaigner.modules.overview.services.ItemOverviewService;
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

@WebMvcTest(ItemOverviewController.class)
public class ItemOverviewControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private ItemOverviewService itemOverviewService;

    private final String URI = "/api/items-overview/";

    @Test
    void whenCampaignUUIDIsValid_getItemOverview_returnsOverviewDataAsJson() throws Exception {
        UUID campaignId = UUID.randomUUID();

        ItemOverviewDTO dto = ItemOverviewDTO.builder()
                .damageTypes(List.of(new DamageTypeDTO(1, "bludgeon", "bonk")))
                .diceTypes(Collections.emptyList())
                .inventories(Collections.emptyList())
                .items(Collections.emptyList())
                .itemTypes(Collections.emptyList())
                .weapons(Collections.emptyList())
                .weaponTypes(Collections.emptyList())
                .build();

        when(itemOverviewService.getItemOverview(campaignId)).thenReturn(dto);

        mockMvc.perform(get(URI + campaignId)
                        .param("campaignId", campaignId.toString()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.damageTypes[0].name").value("bludgeon"));
    }

    @Test
    void whenCampaignUUIDIsInvalid_thenReturnEmptyOverview() throws Exception {
        UUID invalidCampaignId = UUID.randomUUID();

        // Empty DTO to simulate no data found
        ItemOverviewDTO emptyOverview = ItemOverviewDTO.builder()
                .damageTypes(Collections.emptyList())
                .diceTypes(Collections.emptyList())
                .inventories(Collections.emptyList())
                .items(Collections.emptyList())
                .itemTypes(Collections.emptyList())
                .weapons(Collections.emptyList())
                .weaponTypes(Collections.emptyList())
                .build();

        when(itemOverviewService.getItemOverview(invalidCampaignId)).thenReturn(emptyOverview);

        mockMvc.perform(get(URI + invalidCampaignId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.damageTypes").isArray())
                .andExpect(jsonPath("$.damageTypes").isEmpty())
                .andExpect(jsonPath("$.diceTypes").isArray())
                .andExpect(jsonPath("$.diceTypes").isEmpty())
                .andExpect(jsonPath("$.inventories").isArray())
                .andExpect(jsonPath("$.inventories").isEmpty())
                .andExpect(jsonPath("$.items").isArray())
                .andExpect(jsonPath("$.items").isEmpty())
                .andExpect(jsonPath("$.itemTypes").isArray())
                .andExpect(jsonPath("$.itemTypes").isEmpty())
                .andExpect(jsonPath("$.weapons").isArray())
                .andExpect(jsonPath("$.weapons").isEmpty())
                .andExpect(jsonPath("$.weaponTypes").isArray())
                .andExpect(jsonPath("$.weaponTypes").isEmpty());

        verify(itemOverviewService).getItemOverview(invalidCampaignId);
    }
}
