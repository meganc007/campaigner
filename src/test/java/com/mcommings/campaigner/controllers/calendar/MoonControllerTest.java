package com.mcommings.campaigner.controllers.calendar;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mcommings.campaigner.modules.calendar.controllers.MoonController;
import com.mcommings.campaigner.modules.calendar.dtos.MoonDTO;
import com.mcommings.campaigner.modules.calendar.entities.Moon;
import com.mcommings.campaigner.modules.calendar.services.MoonService;
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

@WebMvcTest(MoonController.class)
class MoonControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    MoonService moonService;

    private static final int VALID_MOON_ID = 1;
    private static final int INVALID_MOON_ID = 999;
    private static final String URI = "/api/moons";
    private Moon entity;
    private MoonDTO dto;
    private Random random = new Random();

    @BeforeEach
    void setUp() {
        entity = new Moon();
        entity.setId(VALID_MOON_ID);
        entity.setName("A name.");
        entity.setDescription("A description.");
        entity.setFk_campaign_uuid(UUID.randomUUID());

        dto = new MoonDTO();
        dto.setId(entity.getId());
        dto.setName(entity.getName());
        dto.setDescription(entity.getDescription());
        dto.setFk_campaign_uuid(entity.getFk_campaign_uuid());
    }

    @Test
    void whenThereAreMoons_getMoons_ReturnsMoons() throws Exception {
        when(moonService.getMoons()).thenReturn(List.of(dto));

        String response = objectMapper.writeValueAsString(List.of(dto));

        mockMvc.perform(get(URI))
                .andExpect(status().isOk())
                .andExpect(content().json(response));
    }

    @Test
    void whenThereAreMoons_getMoons_ReturnsEmptyList() throws Exception {
        when(moonService.getMoons()).thenReturn(List.of());

        mockMvc.perform(get(URI))
                .andExpect(status().isOk())
                .andExpect(content().json("[]"));
    }

    @Test
    void whenThereIsAMoon_getMoon_ReturnsMoon() throws Exception {
        when(moonService.getMoon(VALID_MOON_ID)).thenReturn(Optional.of(dto));

        mockMvc.perform(get(URI + "/" + VALID_MOON_ID))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(VALID_MOON_ID))
                .andExpect(jsonPath("$.description").value(dto.getDescription()));
    }

    @Test
    void whenThereIsNotAMoon_getMoon_ThrowsIllegalArgumentException() throws Exception {
        when(moonService.getMoon(INVALID_MOON_ID)).thenReturn(Optional.empty());

        mockMvc.perform(get(URI + "/" + INVALID_MOON_ID))
                .andExpect(status().isBadRequest())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof IllegalArgumentException))
                .andExpect(result -> assertEquals(ID_NOT_FOUND.message, result.getResolvedException().getMessage()));
    }

    @Test
    void whenUserEntersWrongIdType_getMoon_ReturnsBadRequest() throws Exception {
        mockMvc.perform(get(URI + "/abc"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void whenServiceFails_getMoon_ReturnsInternalServerError() throws Exception {
        when(moonService.getMoon(VALID_MOON_ID)).thenThrow(new RuntimeException("Database failure"));

        mockMvc.perform(get(URI + "/" + VALID_MOON_ID))
                .andExpect(status().isInternalServerError())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof RuntimeException))
                .andExpect(result -> assertEquals("Database failure", result.getResolvedException().getMessage()));
    }

    @Test
    void whenServiceReturnsNull_getMoon_ReturnsInternalServerError() throws Exception {
        when(moonService.getMoon(VALID_MOON_ID)).thenReturn(null);

        mockMvc.perform(get(URI + "/" + VALID_MOON_ID))
                .andExpect(status().isInternalServerError());
    }

    @Test
    void whenCampaignUUIDIsValid_getMoonsByCampaignUUID_ReturnsMoons() throws Exception {
        UUID uuid = UUID.randomUUID();
        when(moonService.getMoonsByCampaignUUID(uuid)).thenReturn(List.of(dto));

        String response = objectMapper.writeValueAsString(List.of(dto));

        mockMvc.perform(get(URI + "/campaign/" + uuid))
                .andExpect(status().isOk())
                .andExpect(content().json(response));
    }

    @Test
    void whenCampaignUUIDIsValid_getMoonsByCampaignUUID_ReturnsEmptyList() throws Exception {
        UUID uuid = UUID.randomUUID();
        when(moonService.getMoonsByCampaignUUID(uuid)).thenReturn(List.of());

        mockMvc.perform(get(URI + "/campaign/" + uuid))
                .andExpect(status().isOk())
                .andExpect(content().json("[]"));
    }

    @Test
    void whenUserEntersWrongCampaignUUIDType_getMoon_ReturnsBadRequest() throws Exception {
        mockMvc.perform(get(URI + "/abc"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void whenMoonIsValid_saveMoon_RespondsOkRequest() throws Exception {
        UUID uuid = UUID.randomUUID();
        MoonDTO requestDto = new MoonDTO();
        requestDto.setId(2);
        requestDto.setName("This is a name");
        requestDto.setDescription("This is a description");
        requestDto.setFk_campaign_uuid(uuid);

        String requestJson = objectMapper.writeValueAsString(requestDto);

        mockMvc.perform(post("/api/moons")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andExpect(status().isOk());

        verify(moonService, times(1)).saveMoon(any(MoonDTO.class));
    }

    @Test
    void whenMoonIsNotValid_saveMoon_RespondsBadRequest() throws Exception {
        MoonDTO invalidMoon = new MoonDTO();
        invalidMoon.setId(2);
        invalidMoon.setDescription("This is a description");
        invalidMoon.setFk_campaign_uuid(null); // Invalid UUID

        String requestJson = objectMapper.writeValueAsString(invalidMoon);

        doThrow(new IllegalArgumentException("Campaign UUID cannot be null or empty."))
                .when(moonService).saveMoon(any(MoonDTO.class));

        MvcResult result = mockMvc.perform(post("/api/moons")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andExpect(status().isBadRequest())
                .andReturn();

        String responseBody = result.getResponse().getContentAsString();
        ObjectMapper mapper = new ObjectMapper();
        List<String> errors = mapper.readValue(responseBody, new TypeReference<List<String>>() {
        });

        assertTrue(errors.contains("Moon name cannot be empty"));
        assertTrue(errors.contains("Campaign UUID cannot be null or empty."));

        verify(moonService, times(0)).saveMoon(any(MoonDTO.class));
    }

    @Test
    void whenMoonIdIsValid_deleteMoon_RespondsOkRequest() throws Exception {
        when(moonService.getMoon(VALID_MOON_ID)).thenReturn(Optional.of(dto));

        mockMvc.perform(delete(URI + "/" + VALID_MOON_ID))
                .andExpect(status().isOk());

        verify(moonService, times(1)).deleteMoon(VALID_MOON_ID);
    }

    @Test
    void whenMoonIdIsInvalid_deleteMoon_RespondsBadRequest() throws Exception {
        doThrow(new IllegalArgumentException("Unable to delete; This item was not found."))
                .when(moonService).deleteMoon(INVALID_MOON_ID);

        mockMvc.perform(delete(URI + "/" + INVALID_MOON_ID))
                .andExpect(status().isBadRequest())  // Expecting 400 Bad Request
                .andExpect(content().string("Unable to delete; This item was not found."));

        verify(moonService, times(1)).deleteMoon(INVALID_MOON_ID);
    }

    @Test
    void whenMoonIdIsValid_updateMoon_RespondsOkRequest() throws Exception {
        MoonDTO updatedDto = new MoonDTO();
        updatedDto.setId(VALID_MOON_ID);
        updatedDto.setDescription("Updated description");
        updatedDto.setFk_campaign_uuid(UUID.randomUUID());

        String json = objectMapper.writeValueAsString(updatedDto);

        mockMvc.perform(put(URI + "/" + VALID_MOON_ID)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk());

        verify(moonService, times(1)).updateMoon(eq(VALID_MOON_ID), any(MoonDTO.class));
    }

    @Test
    void whenMoonIdIsInvalid_updateMoon_RespondsBadRequest() throws Exception {
        MoonDTO updatedDto = new MoonDTO();
        updatedDto.setId(INVALID_MOON_ID);
        updatedDto.setDescription("Some update");
        updatedDto.setFk_campaign_uuid(UUID.randomUUID());

        String json = objectMapper.writeValueAsString(updatedDto);

        doThrow(new IllegalArgumentException("Unable to update; This item was not found."))
                .when(moonService).updateMoon(eq(INVALID_MOON_ID), any(MoonDTO.class));

        mockMvc.perform(put(URI + "/" + INVALID_MOON_ID)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isBadRequest())
                .andExpect(content().string(containsString("Unable to update; This item was not found.")));
    }

    @Test
    void whenMoonNameIsInvalid_updateMoon_RespondsBadRequest() throws Exception {
        MoonDTO invalidDto = new MoonDTO();
        invalidDto.setId(VALID_MOON_ID);
        invalidDto.setDescription("");
        invalidDto.setFk_campaign_uuid(UUID.randomUUID());

        String json = objectMapper.writeValueAsString(invalidDto);

        when(moonService.updateMoon(eq(VALID_MOON_ID), any(MoonDTO.class)))
                .thenThrow(new IllegalArgumentException("Item name cannot be null or empty."));

        mockMvc.perform(put(URI + "/" + VALID_MOON_ID)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isBadRequest())
                .andExpect(content().string(containsString("Item name cannot be null or empty.")));
    }
}