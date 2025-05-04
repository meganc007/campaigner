package com.mcommings.campaigner.controllers.locations;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mcommings.campaigner.modules.locations.controllers.LandmarkController;
import com.mcommings.campaigner.modules.locations.dtos.LandmarkDTO;
import com.mcommings.campaigner.modules.locations.entities.Landmark;
import com.mcommings.campaigner.modules.locations.services.LandmarkService;
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

@WebMvcTest(LandmarkController.class)
public class LandmarkControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    LandmarkService landmarkService;

    private static final int VALID_LANDMARK_ID = 1;
    private static final int INVALID_LANDMARK_ID = 999;
    private static final String URI = "/api/landmarks";
    private Landmark entity;
    private LandmarkDTO dto;
    private Random random = new Random();

    @BeforeEach
    void setUp() {
        entity = new Landmark();
        entity.setId(VALID_LANDMARK_ID);
        entity.setName("A name.");
        entity.setDescription("A description.");
        entity.setFk_campaign_uuid(UUID.randomUUID());
        entity.setFk_region(random.nextInt(100) + 1);

        dto = new LandmarkDTO();
        dto.setId(entity.getId());
        dto.setName(entity.getName());
        dto.setDescription(entity.getDescription());
        dto.setFk_campaign_uuid(entity.getFk_campaign_uuid());
        dto.setFk_region(entity.getFk_region());
    }

    @Test
    void whenThereAreLandmarks_getLandmarks_ReturnsLandmarks() throws Exception {
        when(landmarkService.getLandmarks()).thenReturn(List.of(dto));

        String response = objectMapper.writeValueAsString(List.of(dto));

        mockMvc.perform(get(URI))
                .andExpect(status().isOk())
                .andExpect(content().json(response));
    }

    @Test
    void whenThereAreLandmarks_getLandmarks_ReturnsEmptyList() throws Exception {
        when(landmarkService.getLandmarks()).thenReturn(List.of());

        mockMvc.perform(get(URI))
                .andExpect(status().isOk())
                .andExpect(content().json("[]"));
    }

    @Test
    void whenThereIsALandmark_getLandmark_ReturnsLandmark() throws Exception {
        when(landmarkService.getLandmark(VALID_LANDMARK_ID)).thenReturn(Optional.of(dto));

        mockMvc.perform(get(URI + "/" + VALID_LANDMARK_ID))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(VALID_LANDMARK_ID))
                .andExpect(jsonPath("$.description").value(dto.getDescription()));
    }

    @Test
    void whenThereIsNotALandmark_getLandmark_ThrowsIllegalArgumentException() throws Exception {
        when(landmarkService.getLandmark(INVALID_LANDMARK_ID)).thenReturn(Optional.empty());

        mockMvc.perform(get(URI + "/" + INVALID_LANDMARK_ID))
                .andExpect(status().isBadRequest())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof IllegalArgumentException))
                .andExpect(result -> assertEquals(ID_NOT_FOUND.message, result.getResolvedException().getMessage()));
    }

    @Test
    void whenUserEntersWrongIdType_getLandmark_ReturnsBadRequest() throws Exception {
        mockMvc.perform(get(URI + "/abc"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void whenServiceFails_getLandmark_ReturnsInternalServerError() throws Exception {
        when(landmarkService.getLandmark(VALID_LANDMARK_ID)).thenThrow(new RuntimeException("Database failure"));

        mockMvc.perform(get(URI + "/" + VALID_LANDMARK_ID))
                .andExpect(status().isInternalServerError())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof RuntimeException))
                .andExpect(result -> assertEquals("Database failure", result.getResolvedException().getMessage()));
    }

    @Test
    void whenServiceReturnsNull_getLandmark_ReturnsInternalServerError() throws Exception {
        when(landmarkService.getLandmark(VALID_LANDMARK_ID)).thenReturn(null);

        mockMvc.perform(get(URI + "/" + VALID_LANDMARK_ID))
                .andExpect(status().isInternalServerError());
    }

    @Test
    void whenCampaignUUIDIsValid_getLandmarksByCampaignUUID_ReturnsLandmarks() throws Exception {
        UUID uuid = UUID.randomUUID();
        when(landmarkService.getLandmarksByCampaignUUID(uuid)).thenReturn(List.of(dto));

        String response = objectMapper.writeValueAsString(List.of(dto));

        mockMvc.perform(get(URI + "/campaign/" + uuid))
                .andExpect(status().isOk())
                .andExpect(content().json(response));
    }

    @Test
    void whenCampaignUUIDIsValid_getLandmarksByCampaignUUID_ReturnsEmptyList() throws Exception {
        UUID uuid = UUID.randomUUID();
        when(landmarkService.getLandmarksByCampaignUUID(uuid)).thenReturn(List.of());

        mockMvc.perform(get(URI + "/campaign/" + uuid))
                .andExpect(status().isOk())
                .andExpect(content().json("[]"));
    }

    @Test
    void whenUserEntersWrongCampaignUUIDType_getLandmark_ReturnsBadRequest() throws Exception {
        mockMvc.perform(get(URI + "/abc"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void whenRegionIDIsValid_getLandmarksByRegion_ReturnsLandmarks() throws Exception {
        int regionId = random.nextInt(100) + 1;
        when(landmarkService.getLandmarksByRegionId(regionId)).thenReturn(List.of(dto));

        String response = objectMapper.writeValueAsString(List.of(dto));

        mockMvc.perform(get(URI + "/region/" + regionId))
                .andExpect(status().isOk())
                .andExpect(content().json(response));
    }

    @Test
    void whenRegionIDIsNotValid_getLandmarksByRegion_ReturnsEmptyList() throws Exception {
        int regionId = random.nextInt(100) + 1;
        when(landmarkService.getLandmarksByRegionId(regionId)).thenReturn(List.of());

        mockMvc.perform(get(URI + "/region/" + regionId))
                .andExpect(status().isOk())
                .andExpect(content().json("[]"));
    }

    @Test
    void whenLandmarkIsValid_saveLandmark_RespondsOkRequest() throws Exception {
        UUID uuid = UUID.randomUUID();
        LandmarkDTO requestDto = new LandmarkDTO();
        requestDto.setId(2);
        requestDto.setName("This is a name");
        requestDto.setDescription("This is a description");
        requestDto.setFk_campaign_uuid(uuid);
        requestDto.setFk_region(4);

        String requestJson = objectMapper.writeValueAsString(requestDto);

        mockMvc.perform(post("/api/landmarks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andExpect(status().isOk());

        verify(landmarkService, times(1)).saveLandmark(any(LandmarkDTO.class));
    }

    @Test
    void whenLandmarkIsNotValid_saveLandmark_RespondsBadRequest() throws Exception {
        LandmarkDTO invalidLandmark = new LandmarkDTO();
        invalidLandmark.setId(2);
        invalidLandmark.setDescription("This is a description");
        invalidLandmark.setFk_campaign_uuid(null); // Invalid UUID
        invalidLandmark.setFk_region(4);

        String requestJson = objectMapper.writeValueAsString(invalidLandmark);

        doThrow(new IllegalArgumentException("Campaign UUID cannot be null or empty."))
                .when(landmarkService).saveLandmark(any(LandmarkDTO.class));

        MvcResult result = mockMvc.perform(post("/api/landmarks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andExpect(status().isBadRequest())
                .andReturn();

        String responseBody = result.getResponse().getContentAsString();
        ObjectMapper mapper = new ObjectMapper();
        List<String> errors = mapper.readValue(responseBody, new TypeReference<List<String>>() {
        });

        assertTrue(errors.contains("Landmark name cannot be empty"));
        assertTrue(errors.contains("Campaign UUID cannot be null or empty."));

        verify(landmarkService, times(0)).saveLandmark(any(LandmarkDTO.class));
    }

    @Test
    void whenLandmarkIdIsValid_deleteLandmark_RespondsOkRequest() throws Exception {
        when(landmarkService.getLandmark(VALID_LANDMARK_ID)).thenReturn(Optional.of(dto));

        mockMvc.perform(delete(URI + "/" + VALID_LANDMARK_ID))
                .andExpect(status().isOk());

        verify(landmarkService, times(1)).deleteLandmark(VALID_LANDMARK_ID);
    }

    @Test
    void whenLandmarkIdIsInvalid_deleteLandmark_RespondsBadRequest() throws Exception {
        doThrow(new IllegalArgumentException("Unable to delete; This item was not found."))
                .when(landmarkService).deleteLandmark(INVALID_LANDMARK_ID);

        mockMvc.perform(delete(URI + "/" + INVALID_LANDMARK_ID))
                .andExpect(status().isBadRequest())  // Expecting 400 Bad Request
                .andExpect(content().string("Unable to delete; This item was not found."));

        verify(landmarkService, times(1)).deleteLandmark(INVALID_LANDMARK_ID);
    }

    @Test
    void whenLandmarkIdIsValid_updateLandmark_RespondsOkRequest() throws Exception {
        LandmarkDTO updatedDto = new LandmarkDTO();
        updatedDto.setId(VALID_LANDMARK_ID);
        updatedDto.setDescription("Updated description");
        updatedDto.setFk_campaign_uuid(UUID.randomUUID());
        updatedDto.setFk_region(2);

        String json = objectMapper.writeValueAsString(updatedDto);

        mockMvc.perform(put(URI + "/" + VALID_LANDMARK_ID)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk());

        verify(landmarkService, times(1)).updateLandmark(eq(VALID_LANDMARK_ID), any(LandmarkDTO.class));
    }

    @Test
    void whenLandmarkIdIsInvalid_updateLandmark_RespondsBadRequest() throws Exception {
        LandmarkDTO updatedDto = new LandmarkDTO();
        updatedDto.setId(INVALID_LANDMARK_ID);
        updatedDto.setDescription("Some update");
        updatedDto.setFk_campaign_uuid(UUID.randomUUID());
        updatedDto.setFk_region(1);

        String json = objectMapper.writeValueAsString(updatedDto);

        doThrow(new IllegalArgumentException("Unable to update; This item was not found."))
                .when(landmarkService).updateLandmark(eq(INVALID_LANDMARK_ID), any(LandmarkDTO.class));

        mockMvc.perform(put(URI + "/" + INVALID_LANDMARK_ID)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isBadRequest())
                .andExpect(content().string(containsString("Unable to update; This item was not found.")));
    }

    @Test
    void whenLandmarkNameIsInvalid_updateLandmark_RespondsBadRequest() throws Exception {
        LandmarkDTO invalidDto = new LandmarkDTO();
        invalidDto.setId(VALID_LANDMARK_ID);
        invalidDto.setDescription("");
        invalidDto.setFk_campaign_uuid(UUID.randomUUID());
        invalidDto.setFk_region(1);

        String json = objectMapper.writeValueAsString(invalidDto);

        when(landmarkService.updateLandmark(eq(VALID_LANDMARK_ID), any(LandmarkDTO.class)))
                .thenThrow(new IllegalArgumentException("Item name cannot be null or empty."));

        mockMvc.perform(put(URI + "/" + VALID_LANDMARK_ID)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isBadRequest())
                .andExpect(content().string(containsString("Item name cannot be null or empty.")));
    }
}
