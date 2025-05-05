package com.mcommings.campaigner.controllers.common;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mcommings.campaigner.modules.common.controllers.ClimateController;
import com.mcommings.campaigner.modules.common.dtos.ClimateDTO;
import com.mcommings.campaigner.modules.common.entities.Climate;
import com.mcommings.campaigner.modules.common.services.ClimateService;
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

@WebMvcTest(ClimateController.class)
public class ClimateControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    ClimateService climateService;

    private static final int VALID_CLIMATE_ID = 1;
    private static final int INVALID_CLIMATE_ID = 999;
    private static final String URI = "/api/climates";
    private Climate entity;
    private ClimateDTO dto;
    private Random random = new Random();

    @BeforeEach
    void setUp() {
        entity = new Climate();
        entity.setId(VALID_CLIMATE_ID);
        entity.setName("A name.");
        entity.setDescription("A description.");

        dto = new ClimateDTO();
        dto.setId(entity.getId());
        dto.setName(entity.getName());
        dto.setDescription(entity.getDescription());
    }

    @Test
    void whenThereAreClimates_getClimates_ReturnsClimates() throws Exception {
        when(climateService.getClimates()).thenReturn(List.of(dto));

        String response = objectMapper.writeValueAsString(List.of(dto));

        mockMvc.perform(get(URI))
                .andExpect(status().isOk())
                .andExpect(content().json(response));
    }

    @Test
    void whenThereAreClimates_getClimates_ReturnsEmptyList() throws Exception {
        when(climateService.getClimates()).thenReturn(List.of());

        mockMvc.perform(get(URI))
                .andExpect(status().isOk())
                .andExpect(content().json("[]"));
    }

    @Test
    void whenThereIsAClimate_getClimate_ReturnsClimate() throws Exception {
        when(climateService.getClimate(VALID_CLIMATE_ID)).thenReturn(Optional.of(dto));

        mockMvc.perform(get(URI + "/" + VALID_CLIMATE_ID))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(VALID_CLIMATE_ID))
                .andExpect(jsonPath("$.description").value(dto.getDescription()));
    }

    @Test
    void whenThereIsNotAClimate_getClimate_ThrowsIllegalArgumentException() throws Exception {
        when(climateService.getClimate(INVALID_CLIMATE_ID)).thenReturn(Optional.empty());

        mockMvc.perform(get(URI + "/" + INVALID_CLIMATE_ID))
                .andExpect(status().isBadRequest())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof IllegalArgumentException))
                .andExpect(result -> assertEquals(ID_NOT_FOUND.message, result.getResolvedException().getMessage()));
    }

    @Test
    void whenUserEntersWrongIdType_getClimate_ReturnsBadRequest() throws Exception {
        mockMvc.perform(get(URI + "/abc"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void whenServiceFails_getClimate_ReturnsInternalServerError() throws Exception {
        when(climateService.getClimate(VALID_CLIMATE_ID)).thenThrow(new RuntimeException("Database failure"));

        mockMvc.perform(get(URI + "/" + VALID_CLIMATE_ID))
                .andExpect(status().isInternalServerError())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof RuntimeException))
                .andExpect(result -> assertEquals("Database failure", result.getResolvedException().getMessage()));
    }

    @Test
    void whenServiceReturnsNull_getClimate_ReturnsInternalServerError() throws Exception {
        when(climateService.getClimate(VALID_CLIMATE_ID)).thenReturn(null);

        mockMvc.perform(get(URI + "/" + VALID_CLIMATE_ID))
                .andExpect(status().isInternalServerError());
    }

    @Test
    void whenUserEntersWrongCampaignUUIDType_getClimate_ReturnsBadRequest() throws Exception {
        mockMvc.perform(get(URI + "/abc"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void whenClimateIsValid_saveClimate_RespondsOkRequest() throws Exception {
        UUID uuid = UUID.randomUUID();
        ClimateDTO requestDto = new ClimateDTO();
        requestDto.setId(2);
        requestDto.setName("This is a name");
        requestDto.setDescription("This is a description");

        String requestJson = objectMapper.writeValueAsString(requestDto);

        mockMvc.perform(post("/api/climates")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andExpect(status().isOk());

        verify(climateService, times(1)).saveClimate(any(ClimateDTO.class));
    }

    @Test
    void whenClimateIsNotValid_saveClimate_RespondsBadRequest() throws Exception {
        ClimateDTO invalidClimate = new ClimateDTO();
        invalidClimate.setId(2);
        invalidClimate.setDescription("This is a description");

        String requestJson = objectMapper.writeValueAsString(invalidClimate);

        doThrow(new IllegalArgumentException("Campaign UUID cannot be null or empty."))
                .when(climateService).saveClimate(any(ClimateDTO.class));

        MvcResult result = mockMvc.perform(post("/api/climates")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andExpect(status().isBadRequest())
                .andReturn();

        String responseBody = result.getResponse().getContentAsString();
        ObjectMapper mapper = new ObjectMapper();
        List<String> errors = mapper.readValue(responseBody, new TypeReference<List<String>>() {
        });

        assertTrue(errors.contains("Climate name cannot be empty"));

        verify(climateService, times(0)).saveClimate(any(ClimateDTO.class));
    }

    @Test
    void whenClimateIdIsValid_deleteClimate_RespondsOkRequest() throws Exception {
        when(climateService.getClimate(VALID_CLIMATE_ID)).thenReturn(Optional.of(dto));

        mockMvc.perform(delete(URI + "/" + VALID_CLIMATE_ID))
                .andExpect(status().isOk());

        verify(climateService, times(1)).deleteClimate(VALID_CLIMATE_ID);
    }

    @Test
    void whenClimateIdIsInvalid_deleteClimate_RespondsBadRequest() throws Exception {
        doThrow(new IllegalArgumentException("Unable to delete; This item was not found."))
                .when(climateService).deleteClimate(INVALID_CLIMATE_ID);

        mockMvc.perform(delete(URI + "/" + INVALID_CLIMATE_ID))
                .andExpect(status().isBadRequest())  // Expecting 400 Bad Request
                .andExpect(content().string("Unable to delete; This item was not found."));

        verify(climateService, times(1)).deleteClimate(INVALID_CLIMATE_ID);
    }

    @Test
    void whenClimateIdIsValid_updateClimate_RespondsOkRequest() throws Exception {
        ClimateDTO updatedDto = new ClimateDTO();
        updatedDto.setId(VALID_CLIMATE_ID);
        updatedDto.setDescription("Updated description");

        String json = objectMapper.writeValueAsString(updatedDto);

        mockMvc.perform(put(URI + "/" + VALID_CLIMATE_ID)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk());

        verify(climateService, times(1)).updateClimate(eq(VALID_CLIMATE_ID), any(ClimateDTO.class));
    }

    @Test
    void whenClimateIdIsInvalid_updateClimate_RespondsBadRequest() throws Exception {
        ClimateDTO updatedDto = new ClimateDTO();
        updatedDto.setId(INVALID_CLIMATE_ID);
        updatedDto.setDescription("Some update");

        String json = objectMapper.writeValueAsString(updatedDto);

        doThrow(new IllegalArgumentException("Unable to update; This item was not found."))
                .when(climateService).updateClimate(eq(INVALID_CLIMATE_ID), any(ClimateDTO.class));

        mockMvc.perform(put(URI + "/" + INVALID_CLIMATE_ID)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isBadRequest())
                .andExpect(content().string(containsString("Unable to update; This item was not found.")));
    }

    @Test
    void whenClimateNameIsInvalid_updateClimate_RespondsBadRequest() throws Exception {
        ClimateDTO invalidDto = new ClimateDTO();
        invalidDto.setId(VALID_CLIMATE_ID);
        invalidDto.setDescription("");

        String json = objectMapper.writeValueAsString(invalidDto);

        when(climateService.updateClimate(eq(VALID_CLIMATE_ID), any(ClimateDTO.class)))
                .thenThrow(new IllegalArgumentException("Item name cannot be null or empty."));

        mockMvc.perform(put(URI + "/" + VALID_CLIMATE_ID)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isBadRequest())
                .andExpect(content().string(containsString("Item name cannot be null or empty.")));
    }
}
