package com.mcommings.campaigner.controllers.locations;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mcommings.campaigner.modules.locations.controllers.ContinentController;
import com.mcommings.campaigner.modules.locations.dtos.ContinentDTO;
import com.mcommings.campaigner.modules.locations.entities.Continent;
import com.mcommings.campaigner.modules.locations.services.ContinentService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.UUID;

import static com.mcommings.campaigner.enums.ErrorMessage.ID_NOT_FOUND;
import static org.hamcrest.Matchers.containsString;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ContinentController.class)
public class ContinentControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    ContinentService continentService;

    private static final int VALID_CONTINENT_ID = 1;
    private static final int INVALID_CONTINENT_ID = 999;
    private static final String URI = "/api/continents";
    private Continent entity;
    private ContinentDTO dto;
    private Random random = new Random();

    @BeforeEach
    void setUp() {
        entity = new Continent();
        entity.setId(VALID_CONTINENT_ID);
        entity.setName("A name.");
        entity.setDescription("A description.");
        entity.setFk_campaign_uuid(UUID.randomUUID());

        dto = new ContinentDTO();
        dto.setId(entity.getId());
        dto.setName(entity.getName());
        dto.setDescription(entity.getDescription());
        dto.setFk_campaign_uuid(entity.getFk_campaign_uuid());
    }

    @Test
    void whenThereAreContinents_getContinents_ReturnsContinents() throws Exception {
        when(continentService.getContinents()).thenReturn(List.of(dto));

        String response = objectMapper.writeValueAsString(List.of(dto));

        mockMvc.perform(get(URI))
                .andExpect(status().isOk())
                .andExpect(content().json(response));
    }

    @Test
    void whenThereAreContinents_getContinents_ReturnsEmptyList() throws Exception {
        when(continentService.getContinents()).thenReturn(List.of());

        mockMvc.perform(get(URI))
                .andExpect(status().isOk())
                .andExpect(content().json("[]"));
    }

    @Test
    void whenThereIsAContinent_getContinent_ReturnsContinent() throws Exception {
        when(continentService.getContinent(VALID_CONTINENT_ID)).thenReturn(Optional.of(dto));

        mockMvc.perform(get(URI + "/" + VALID_CONTINENT_ID))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(VALID_CONTINENT_ID))
                .andExpect(jsonPath("$.description").value(dto.getDescription()));
    }

    @Test
    void whenThereIsNotAContinent_getContinent_ThrowsIllegalArgumentException() throws Exception {
        when(continentService.getContinent(INVALID_CONTINENT_ID)).thenReturn(Optional.empty());

        mockMvc.perform(get(URI + "/" + INVALID_CONTINENT_ID))
                .andExpect(status().isBadRequest())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof IllegalArgumentException))
                .andExpect(result -> assertEquals(ID_NOT_FOUND.message, result.getResolvedException().getMessage()));
    }

    @Test
    void whenUserEntersWrongIdType_getContinent_ReturnsBadRequest() throws Exception {
        mockMvc.perform(get(URI + "/abc"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void whenServiceFails_getContinent_ReturnsInternalServerError() throws Exception {
        when(continentService.getContinent(VALID_CONTINENT_ID)).thenThrow(new RuntimeException("Database failure"));

        mockMvc.perform(get(URI + "/" + VALID_CONTINENT_ID))
                .andExpect(status().isInternalServerError())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof RuntimeException))
                .andExpect(result -> assertEquals("Database failure", result.getResolvedException().getMessage()));
    }

    @Test
    void whenServiceReturnsNull_getContinent_ReturnsInternalServerError() throws Exception {
        when(continentService.getContinent(VALID_CONTINENT_ID)).thenReturn(null);

        mockMvc.perform(get(URI + "/" + VALID_CONTINENT_ID))
                .andExpect(status().isInternalServerError());
    }

    @Test
    void whenCampaignUUIDIsValid_getContinentsByCampaignUUID_ReturnsContinents() throws Exception {
        UUID uuid = UUID.randomUUID();
        when(continentService.getContinentsByCampaignUUID(uuid)).thenReturn(List.of(dto));

        String response = objectMapper.writeValueAsString(List.of(dto));

        mockMvc.perform(get(URI + "/campaign/" + uuid))
                .andExpect(status().isOk())
                .andExpect(content().json(response));
    }

    @Test
    void whenCampaignUUIDIsValid_getContinentsByCampaignUUID_ReturnsEmptyList() throws Exception {
        UUID uuid = UUID.randomUUID();
        when(continentService.getContinentsByCampaignUUID(uuid)).thenReturn(List.of());

        mockMvc.perform(get(URI + "/campaign/" + uuid))
                .andExpect(status().isOk())
                .andExpect(content().json("[]"));
    }

    @Test
    void whenUserEntersWrongCampaignUUIDType_getContinent_ReturnsBadRequest() throws Exception {
        mockMvc.perform(get(URI + "/abc"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void whenContinentIsValid_saveContinent_RespondsOkRequest() throws Exception {
        UUID uuid = UUID.randomUUID();
        ContinentDTO requestDto = new ContinentDTO();
        requestDto.setId(2);
        requestDto.setName("This is a name");
        requestDto.setDescription("This is a description");
        requestDto.setFk_campaign_uuid(uuid);

        String requestJson = objectMapper.writeValueAsString(requestDto);

        mockMvc.perform(post("/api/continents")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andExpect(status().isOk());

        verify(continentService, times(1)).saveContinent(any(ContinentDTO.class));
    }

    @Test
    void whenContinentIsNotValid_saveContinent_RespondsBadRequest() throws Exception {
        ContinentDTO invalidContinent = new ContinentDTO();
        invalidContinent.setId(2);
        invalidContinent.setDescription("This is a description");
        invalidContinent.setFk_campaign_uuid(null); // Invalid UUID

        String requestJson = objectMapper.writeValueAsString(invalidContinent);

        doThrow(new IllegalArgumentException("Campaign UUID cannot be null or empty."))
                .when(continentService).saveContinent(any(ContinentDTO.class));

        MvcResult result = mockMvc.perform(post("/api/continents")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andExpect(status().isBadRequest())
                .andReturn();

        String responseBody = result.getResponse().getContentAsString();
        ObjectMapper mapper = new ObjectMapper();
        List<String> errors = mapper.readValue(responseBody, new TypeReference<List<String>>() {
        });

        assertTrue(errors.contains("Continent name cannot be empty"));
        assertTrue(errors.contains("Campaign UUID cannot be null or empty."));

        verify(continentService, times(0)).saveContinent(any(ContinentDTO.class));
    }

    @Test
    void whenContinentIdIsValid_deleteContinent_RespondsOkRequest() throws Exception {
        when(continentService.getContinent(VALID_CONTINENT_ID)).thenReturn(Optional.of(dto));

        mockMvc.perform(delete(URI + "/" + VALID_CONTINENT_ID))
                .andExpect(status().isOk());

        verify(continentService, times(1)).deleteContinent(VALID_CONTINENT_ID);
    }

    @Test
    void whenContinentIdIsInvalid_deleteContinent_RespondsBadRequest() throws Exception {
        doThrow(new IllegalArgumentException("Unable to delete; This item was not found."))
                .when(continentService).deleteContinent(INVALID_CONTINENT_ID);

        mockMvc.perform(delete(URI + "/" + INVALID_CONTINENT_ID))
                .andExpect(status().isBadRequest())  // Expecting 400 Bad Request
                .andExpect(content().string("Unable to delete; This item was not found."));

        verify(continentService, times(1)).deleteContinent(INVALID_CONTINENT_ID);
    }

    @Test
    void whenContinentIdIsValid_updateContinent_RespondsOkRequest() throws Exception {
        ContinentDTO updatedDto = new ContinentDTO();
        updatedDto.setId(VALID_CONTINENT_ID);
        updatedDto.setDescription("Updated description");
        updatedDto.setFk_campaign_uuid(UUID.randomUUID());

        String json = objectMapper.writeValueAsString(updatedDto);

        mockMvc.perform(put(URI + "/" + VALID_CONTINENT_ID)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk());

        verify(continentService, times(1)).updateContinent(eq(VALID_CONTINENT_ID), any(ContinentDTO.class));
    }

    @Test
    void whenContinentIdIsInvalid_updateContinent_RespondsBadRequest() throws Exception {
        ContinentDTO updatedDto = new ContinentDTO();
        updatedDto.setId(INVALID_CONTINENT_ID);
        updatedDto.setDescription("Some update");
        updatedDto.setFk_campaign_uuid(UUID.randomUUID());

        String json = objectMapper.writeValueAsString(updatedDto);

        doThrow(new IllegalArgumentException("Unable to update; This item was not found."))
                .when(continentService).updateContinent(eq(INVALID_CONTINENT_ID), any(ContinentDTO.class));

        mockMvc.perform(put(URI + "/" + INVALID_CONTINENT_ID)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isBadRequest())
                .andExpect(content().string(containsString("Unable to update; This item was not found.")));
    }

    @Test
    void whenContinentNameIsInvalid_updateContinent_RespondsBadRequest() throws Exception {
        ContinentDTO invalidDto = new ContinentDTO();
        invalidDto.setId(VALID_CONTINENT_ID);
        invalidDto.setDescription("");
        invalidDto.setFk_campaign_uuid(UUID.randomUUID());

        String json = objectMapper.writeValueAsString(invalidDto);

        when(continentService.updateContinent(eq(VALID_CONTINENT_ID), any(ContinentDTO.class)))
                .thenThrow(new IllegalArgumentException("Item name cannot be null or empty."));

        mockMvc.perform(put(URI + "/" + VALID_CONTINENT_ID)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isBadRequest())
                .andExpect(content().string(containsString("Item name cannot be null or empty.")));
    }
}
