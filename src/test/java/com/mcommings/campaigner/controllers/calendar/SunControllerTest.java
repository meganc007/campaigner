package com.mcommings.campaigner.controllers.calendar;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mcommings.campaigner.modules.calendar.controllers.SunController;
import com.mcommings.campaigner.modules.calendar.dtos.SunDTO;
import com.mcommings.campaigner.modules.calendar.entities.Sun;
import com.mcommings.campaigner.modules.calendar.services.SunService;
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

@WebMvcTest(SunController.class)
class SunControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    SunService sunService;

    private static final int VALID_SUN_ID = 1;
    private static final int INVALID_SUN_ID = 999;
    private static final String URI = "/api/suns";
    private Sun entity;
    private SunDTO dto;
    private Random random = new Random();

    @BeforeEach
    void setUp() {
        entity = new Sun();
        entity.setId(VALID_SUN_ID);
        entity.setName("A name.");
        entity.setDescription("A description.");
        entity.setFk_campaign_uuid(UUID.randomUUID());

        dto = new SunDTO();
        dto.setId(entity.getId());
        dto.setName(entity.getName());
        dto.setDescription(entity.getDescription());
        dto.setFk_campaign_uuid(entity.getFk_campaign_uuid());
    }

    @Test
    void whenThereAreSuns_getSuns_ReturnsSuns() throws Exception {
        when(sunService.getSuns()).thenReturn(List.of(dto));

        String response = objectMapper.writeValueAsString(List.of(dto));

        mockMvc.perform(get(URI))
                .andExpect(status().isOk())
                .andExpect(content().json(response));
    }

    @Test
    void whenThereAreSuns_getSuns_ReturnsEmptyList() throws Exception {
        when(sunService.getSuns()).thenReturn(List.of());

        mockMvc.perform(get(URI))
                .andExpect(status().isOk())
                .andExpect(content().json("[]"));
    }

    @Test
    void whenThereIsASun_getSun_ReturnsSun() throws Exception {
        when(sunService.getSun(VALID_SUN_ID)).thenReturn(Optional.of(dto));

        mockMvc.perform(get(URI + "/" + VALID_SUN_ID))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(VALID_SUN_ID))
                .andExpect(jsonPath("$.description").value(dto.getDescription()));
    }

    @Test
    void whenThereIsNotASun_getSun_ThrowsIllegalArgumentException() throws Exception {
        when(sunService.getSun(INVALID_SUN_ID)).thenReturn(Optional.empty());

        mockMvc.perform(get(URI + "/" + INVALID_SUN_ID))
                .andExpect(status().isBadRequest())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof IllegalArgumentException))
                .andExpect(result -> assertEquals(ID_NOT_FOUND.message, result.getResolvedException().getMessage()));
    }

    @Test
    void whenUserEntersWrongIdType_getSun_ReturnsBadRequest() throws Exception {
        mockMvc.perform(get(URI + "/abc"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void whenServiceFails_getSun_ReturnsInternalServerError() throws Exception {
        when(sunService.getSun(VALID_SUN_ID)).thenThrow(new RuntimeException("Database failure"));

        mockMvc.perform(get(URI + "/" + VALID_SUN_ID))
                .andExpect(status().isInternalServerError())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof RuntimeException))
                .andExpect(result -> assertEquals("Database failure", result.getResolvedException().getMessage()));
    }

    @Test
    void whenServiceReturnsNull_getSun_ReturnsInternalServerError() throws Exception {
        when(sunService.getSun(VALID_SUN_ID)).thenReturn(null);

        mockMvc.perform(get(URI + "/" + VALID_SUN_ID))
                .andExpect(status().isInternalServerError());
    }

    @Test
    void whenCampaignUUIDIsValid_getSunsByCampaignUUID_ReturnsSuns() throws Exception {
        UUID uuid = UUID.randomUUID();
        when(sunService.getSunsByCampaignUUID(uuid)).thenReturn(List.of(dto));

        String response = objectMapper.writeValueAsString(List.of(dto));

        mockMvc.perform(get(URI + "/campaign/" + uuid))
                .andExpect(status().isOk())
                .andExpect(content().json(response));
    }

    @Test
    void whenCampaignUUIDIsValid_getSunsByCampaignUUID_ReturnsEmptyList() throws Exception {
        UUID uuid = UUID.randomUUID();
        when(sunService.getSunsByCampaignUUID(uuid)).thenReturn(List.of());

        mockMvc.perform(get(URI + "/campaign/" + uuid))
                .andExpect(status().isOk())
                .andExpect(content().json("[]"));
    }

    @Test
    void whenUserEntersWrongCampaignUUIDType_getSun_ReturnsBadRequest() throws Exception {
        mockMvc.perform(get(URI + "/abc"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void whenSunIsValid_saveSun_RespondsOkRequest() throws Exception {
        UUID uuid = UUID.randomUUID();
        SunDTO requestDto = new SunDTO();
        requestDto.setId(2);
        requestDto.setName("This is a name");
        requestDto.setDescription("This is a description");
        requestDto.setFk_campaign_uuid(uuid);

        String requestJson = objectMapper.writeValueAsString(requestDto);

        mockMvc.perform(post("/api/suns")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andExpect(status().isOk());

        verify(sunService, times(1)).saveSun(any(SunDTO.class));
    }

    @Test
    void whenSunIsNotValid_saveSun_RespondsBadRequest() throws Exception {
        SunDTO invalidSun = new SunDTO();
        invalidSun.setId(2);
        invalidSun.setDescription("This is a description");
        invalidSun.setFk_campaign_uuid(null); // Invalid UUID

        String requestJson = objectMapper.writeValueAsString(invalidSun);

        doThrow(new IllegalArgumentException("Campaign UUID cannot be null or empty."))
                .when(sunService).saveSun(any(SunDTO.class));

        MvcResult result = mockMvc.perform(post("/api/suns")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andExpect(status().isBadRequest())
                .andReturn();

        String responseBody = result.getResponse().getContentAsString();
        ObjectMapper mapper = new ObjectMapper();
        List<String> errors = mapper.readValue(responseBody, new TypeReference<List<String>>() {
        });

        assertTrue(errors.contains("Sun name cannot be empty"));
        assertTrue(errors.contains("Campaign UUID cannot be null or empty."));

        verify(sunService, times(0)).saveSun(any(SunDTO.class));
    }

    @Test
    void whenSunIdIsValid_deleteSun_RespondsOkRequest() throws Exception {
        when(sunService.getSun(VALID_SUN_ID)).thenReturn(Optional.of(dto));

        mockMvc.perform(delete(URI + "/" + VALID_SUN_ID))
                .andExpect(status().isOk());

        verify(sunService, times(1)).deleteSun(VALID_SUN_ID);
    }

    @Test
    void whenSunIdIsInvalid_deleteSun_RespondsBadRequest() throws Exception {
        doThrow(new IllegalArgumentException("Unable to delete; This item was not found."))
                .when(sunService).deleteSun(INVALID_SUN_ID);

        mockMvc.perform(delete(URI + "/" + INVALID_SUN_ID))
                .andExpect(status().isBadRequest())  // Expecting 400 Bad Request
                .andExpect(content().string("Unable to delete; This item was not found."));

        verify(sunService, times(1)).deleteSun(INVALID_SUN_ID);
    }

    @Test
    void whenSunIdIsValid_updateSun_RespondsOkRequest() throws Exception {
        SunDTO updatedDto = new SunDTO();
        updatedDto.setId(VALID_SUN_ID);
        updatedDto.setDescription("Updated description");
        updatedDto.setFk_campaign_uuid(UUID.randomUUID());

        String json = objectMapper.writeValueAsString(updatedDto);

        mockMvc.perform(put(URI + "/" + VALID_SUN_ID)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk());

        verify(sunService, times(1)).updateSun(eq(VALID_SUN_ID), any(SunDTO.class));
    }

    @Test
    void whenSunIdIsInvalid_updateSun_RespondsBadRequest() throws Exception {
        SunDTO updatedDto = new SunDTO();
        updatedDto.setId(INVALID_SUN_ID);
        updatedDto.setDescription("Some update");
        updatedDto.setFk_campaign_uuid(UUID.randomUUID());

        String json = objectMapper.writeValueAsString(updatedDto);

        doThrow(new IllegalArgumentException("Unable to update; This item was not found."))
                .when(sunService).updateSun(eq(INVALID_SUN_ID), any(SunDTO.class));

        mockMvc.perform(put(URI + "/" + INVALID_SUN_ID)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isBadRequest())
                .andExpect(content().string(containsString("Unable to update; This item was not found.")));
    }

    @Test
    void whenSunNameIsInvalid_updateSun_RespondsBadRequest() throws Exception {
        SunDTO invalidDto = new SunDTO();
        invalidDto.setId(VALID_SUN_ID);
        invalidDto.setDescription("");
        invalidDto.setFk_campaign_uuid(UUID.randomUUID());

        String json = objectMapper.writeValueAsString(invalidDto);

        when(sunService.updateSun(eq(VALID_SUN_ID), any(SunDTO.class)))
                .thenThrow(new IllegalArgumentException("Item name cannot be null or empty."));

        mockMvc.perform(put(URI + "/" + VALID_SUN_ID)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isBadRequest())
                .andExpect(content().string(containsString("Item name cannot be null or empty.")));
    }
}