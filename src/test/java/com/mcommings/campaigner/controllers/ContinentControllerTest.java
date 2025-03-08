package com.mcommings.campaigner.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mcommings.campaigner.controllers.locations.ContinentController;
import com.mcommings.campaigner.dtos.ContinentDTO;
import com.mcommings.campaigner.services.locations.ContinentService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@WebMvcTest(ContinentController.class)
class ContinentControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ContinentService continentService;

    @Test
    public void whenGetContinents_thenReturnListOfContinents() throws Exception {
        ContinentDTO continent1 = ContinentDTO.builder()
                .name("Continent 1")
                .description("Description 1")
                .fk_campaign_uuid(UUID.randomUUID())
                .build();
        ContinentDTO continent2 = ContinentDTO.builder()
                .name("Continent 2")
                .description("Description 2")
                .fk_campaign_uuid(UUID.randomUUID())
                .build();

        List<ContinentDTO> continents = Arrays.asList(continent1, continent2);
        when(continentService.getContinents()).thenReturn(continents);

        mockMvc.perform(get("/api/locations/continents"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("Continent 1"))
                .andExpect(jsonPath("$[1].name").value("Continent 2"));
    }

    @Test
    public void whenGetContinent_thenReturnContinent() throws Exception {
        int continentId = 1;
        ContinentDTO continent = ContinentDTO.builder()
                .name("Continent 1")
                .description("Description 1")
                .fk_campaign_uuid(UUID.randomUUID())
                .build();

        when(continentService.getContinent(continentId)).thenReturn(Optional.of(continent));

        mockMvc.perform(get("/api/locations/continents/{continentId}", continentId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Continent 1"));
    }

    @Test
    public void whenGetContinentNotFound_thenReturnNotFound() throws Exception {
        int continentId = 1;
        when(continentService.getContinent(continentId)).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/locations/continents/{continentId}", continentId))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Unable to update; This item was not found."));
        ;
    }

    @Test
    public void whenGetContinentsByCampaignUUID_thenReturnList() throws Exception {
        UUID campaignUuid = UUID.randomUUID();
        ContinentDTO continent = ContinentDTO.builder()
                .name("Continent 1")
                .description("Description 1")
                .fk_campaign_uuid(campaignUuid)
                .build();
        List<ContinentDTO> continents = Arrays.asList(continent);

        when(continentService.getContinentsByCampaignUUID(campaignUuid)).thenReturn(continents);

        mockMvc.perform(get("/api/locations/continents/campaign/{uuid}", campaignUuid))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("Continent 1"));
    }

    @Test
    public void whenSaveContinent_thenReturnNoContent() throws Exception {
        ContinentDTO continentDTO = ContinentDTO.builder()
                .name("Continent 1")
                .description("Description 1")
                .fk_campaign_uuid(UUID.randomUUID())
                .build();

        mockMvc.perform(post("/api/locations/continents")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(continentDTO)))
                .andExpect(status().isNoContent());

        verify(continentService, times(1)).saveContinent(continentDTO);
    }

    @Test
    public void whenContinentNameIsNull_saveContinent_ReturnsBadRequest() throws Exception {
        // Prepare a ContinentDTO with null name
        ContinentDTO continentDTO = ContinentDTO.builder()
                .name(null)  // Name is null
                .description("Test Description")
                .fk_campaign_uuid(UUID.randomUUID())
                .build();

        // Perform the POST request with invalid data
        mockMvc.perform(post("/api/locations/continents")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(continentDTO)))
                .andExpect(status().isBadRequest());  // Expect a Bad Request due to validation error
    }

    @Test
    public void whenContinentNameIsBlank_saveContinent_ReturnsBadRequest() throws Exception {
        // Prepare a ContinentDTO with blank name
        ContinentDTO continentDTO = ContinentDTO.builder()
                .name("")  // Name is blank
                .description("Test Description")
                .fk_campaign_uuid(UUID.randomUUID())
                .build();

        // Perform the POST request with invalid data
        mockMvc.perform(post("/api/locations/continents")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(continentDTO)))
                .andExpect(status().isBadRequest());  // Expect a Bad Request due to validation error
    }

    @Test
    public void whenDeleteContinent_thenReturnNoContent() throws Exception {
        int continentId = 1;
        when(continentService.deleteContinent(continentId)).thenReturn(true);

        mockMvc.perform(delete("/api/locations/continents/{continentId}", continentId))
                .andExpect(status().isNoContent());

        verify(continentService, times(1)).deleteContinent(continentId);
    }

    @Test
    public void whenDeleteContinentNotFound_thenReturnBadRequest() throws Exception {
        int continentId = 1;
        when(continentService.deleteContinent(continentId)).thenReturn(false);

        mockMvc.perform(delete("/api/locations/continents/{continentId}", continentId))
                .andExpect(status().isBadRequest())  // Return BadRequest for not found
                .andExpect(content().string("Unable to update; This item was not found."));
    }

    @Test
    public void whenUpdateContinent_thenReturnNoContent() throws Exception {
        int continentId = 1;
        ContinentDTO continentDTO = ContinentDTO.builder()
                .name("Updated Continent")
                .description("Updated Description")
                .fk_campaign_uuid(UUID.randomUUID())
                .build();

        when(continentService.updateContinent(continentId, continentDTO)).thenReturn(Optional.of(continentDTO));

        mockMvc.perform(put("/api/locations/continents/{continentId}", continentId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(continentDTO)))
                .andExpect(status().isNoContent());

        verify(continentService, times(1)).updateContinent(continentId, continentDTO);
    }

    @Test
    public void whenUpdateContinentNotFound_thenReturnBadRequest() throws Exception {
        int continentId = 1;
        ContinentDTO continentDTO = ContinentDTO.builder()
                .name("Updated Continent")
                .description("Updated Description")
                .fk_campaign_uuid(UUID.randomUUID())
                .build();

        when(continentService.updateContinent(continentId, continentDTO)).thenReturn(Optional.empty());

        mockMvc.perform(put("/api/locations/continents/{continentId}", continentId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(continentDTO)))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Unable to update; This item was not found."));  // Expect BadRequest due to not found
    }
}