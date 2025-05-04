package com.mcommings.campaigner.controllers.common;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mcommings.campaigner.modules.common.controllers.CampaignController;
import com.mcommings.campaigner.modules.common.dtos.CampaignDTO;
import com.mcommings.campaigner.modules.common.entities.Campaign;
import com.mcommings.campaigner.modules.common.services.CampaignService;
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

@WebMvcTest(CampaignController.class)
public class CampaignControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    CampaignService campaignService;

    private static final UUID VALID_CAMPAIGN_ID = UUID.randomUUID();
    private static final UUID INVALID_CAMPAIGN_ID = UUID.randomUUID();
    private static final String URI = "/api/campaigns";
    private Campaign entity;
    private CampaignDTO dto;
    private Random random = new Random();

    @BeforeEach
    void setUp() {
        entity = new Campaign();
        entity.setUuid(VALID_CAMPAIGN_ID);
        entity.setName("A name.");
        entity.setDescription("A description.");

        dto = new CampaignDTO();
        dto.setUuid(entity.getUuid());
        dto.setName(entity.getName());
        dto.setDescription(entity.getDescription());
    }

    @Test
    void whenThereAreCampaigns_getCampaigns_ReturnsCampaigns() throws Exception {
        when(campaignService.getCampaigns()).thenReturn(List.of(dto));

        String response = objectMapper.writeValueAsString(List.of(dto));

        mockMvc.perform(get(URI))
                .andExpect(status().isOk())
                .andExpect(content().json(response));
    }

    @Test
    void whenThereAreCampaigns_getCampaigns_ReturnsEmptyList() throws Exception {
        when(campaignService.getCampaigns()).thenReturn(List.of());

        mockMvc.perform(get(URI))
                .andExpect(status().isOk())
                .andExpect(content().json("[]"));
    }

    @Test
    void whenThereIsACampaign_getCampaign_ReturnsCampaign() throws Exception {
        when(campaignService.getCampaign(VALID_CAMPAIGN_ID)).thenReturn(Optional.of(dto));

        mockMvc.perform(get(URI + "/" + VALID_CAMPAIGN_ID))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.uuid").value(VALID_CAMPAIGN_ID.toString()));
    }

    @Test
    void whenThereIsNotACampaign_getCampaign_ThrowsIllegalArgumentException() throws Exception {
        when(campaignService.getCampaign(INVALID_CAMPAIGN_ID)).thenReturn(Optional.empty());

        mockMvc.perform(get(URI + "/" + INVALID_CAMPAIGN_ID))
                .andExpect(status().isBadRequest())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof IllegalArgumentException))
                .andExpect(result -> assertEquals(ID_NOT_FOUND.message, result.getResolvedException().getMessage()));
    }

    @Test
    void whenUserEntersWrongIdType_getCampaign_ReturnsBadRequest() throws Exception {
        mockMvc.perform(get(URI + "/abc"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void whenServiceFails_getCampaign_ReturnsInternalServerError() throws Exception {
        when(campaignService.getCampaign(VALID_CAMPAIGN_ID)).thenThrow(new RuntimeException("Database failure"));

        mockMvc.perform(get(URI + "/" + VALID_CAMPAIGN_ID))
                .andExpect(status().isInternalServerError())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof RuntimeException))
                .andExpect(result -> assertEquals("Database failure", result.getResolvedException().getMessage()));
    }

    @Test
    void whenServiceReturnsNull_getCampaign_ReturnsInternalServerError() throws Exception {
        when(campaignService.getCampaign(VALID_CAMPAIGN_ID)).thenReturn(null);

        mockMvc.perform(get(URI + "/" + VALID_CAMPAIGN_ID))
                .andExpect(status().isInternalServerError());
    }

    @Test
    void whenUserEntersWrongCampaignUUIDType_getCampaign_ReturnsBadRequest() throws Exception {
        mockMvc.perform(get(URI + "/abc"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void whenCampaignIsValid_saveCampaign_RespondsOkRequest() throws Exception {
        UUID uuid = UUID.randomUUID();
        CampaignDTO requestDto = new CampaignDTO();
        requestDto.setUuid(uuid);
        requestDto.setName("This is a name");
        requestDto.setDescription("This is a description");

        String requestJson = objectMapper.writeValueAsString(requestDto);

        mockMvc.perform(post("/api/campaigns")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andExpect(status().isOk());

        verify(campaignService, times(1)).saveCampaign(any(CampaignDTO.class));
    }

    @Test
    void whenCampaignIsNotValid_saveCampaign_RespondsBadRequest() throws Exception {
        UUID uuid = UUID.randomUUID();
        CampaignDTO invalidCampaign = new CampaignDTO();
        invalidCampaign.setUuid(uuid);
        invalidCampaign.setDescription("This is a description");

        String requestJson = objectMapper.writeValueAsString(invalidCampaign);

        doThrow(new IllegalArgumentException("Campaign UUID cannot be null or empty."))
                .when(campaignService).saveCampaign(any(CampaignDTO.class));

        MvcResult result = mockMvc.perform(post("/api/campaigns")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andExpect(status().isBadRequest())
                .andReturn();

        String responseBody = result.getResponse().getContentAsString();
        ObjectMapper mapper = new ObjectMapper();
        List<String> errors = mapper.readValue(responseBody, new TypeReference<List<String>>() {
        });

        assertTrue(errors.contains("Campaign name cannot be empty"));
        verify(campaignService, times(0)).saveCampaign(any(CampaignDTO.class));
    }

    @Test
    void whenCampaignIdIsValid_deleteCampaign_RespondsOkRequest() throws Exception {
        when(campaignService.getCampaign(VALID_CAMPAIGN_ID)).thenReturn(Optional.of(dto));

        mockMvc.perform(delete(URI + "/" + VALID_CAMPAIGN_ID))
                .andExpect(status().isOk());

        verify(campaignService, times(1)).deleteCampaign(VALID_CAMPAIGN_ID);
    }

    @Test
    void whenCampaignIdIsInvalid_deleteCampaign_RespondsBadRequest() throws Exception {
        doThrow(new IllegalArgumentException("Unable to delete; This item was not found."))
                .when(campaignService).deleteCampaign(INVALID_CAMPAIGN_ID);

        mockMvc.perform(delete(URI + "/" + INVALID_CAMPAIGN_ID))
                .andExpect(status().isBadRequest())  // Expecting 400 Bad Request
                .andExpect(content().string("Unable to delete; This item was not found."));

        verify(campaignService, times(1)).deleteCampaign(INVALID_CAMPAIGN_ID);
    }

    @Test
    void whenCampaignIdIsValid_updateCampaign_RespondsOkRequest() throws Exception {
        CampaignDTO updatedDto = new CampaignDTO();
        updatedDto.setUuid(VALID_CAMPAIGN_ID);
        updatedDto.setDescription("Updated description");

        String json = objectMapper.writeValueAsString(updatedDto);

        mockMvc.perform(put(URI + "/" + VALID_CAMPAIGN_ID)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk());

        verify(campaignService, times(1)).updateCampaign(eq(VALID_CAMPAIGN_ID), any(CampaignDTO.class));
    }

    @Test
    void whenCampaignIdIsInvalid_updateCampaign_RespondsBadRequest() throws Exception {
        CampaignDTO updatedDto = new CampaignDTO();
        updatedDto.setUuid(INVALID_CAMPAIGN_ID);
        updatedDto.setDescription("Some update");

        String json = objectMapper.writeValueAsString(updatedDto);

        doThrow(new IllegalArgumentException("Unable to update; This item was not found."))
                .when(campaignService).updateCampaign(eq(INVALID_CAMPAIGN_ID), any(CampaignDTO.class));

        mockMvc.perform(put(URI + "/" + INVALID_CAMPAIGN_ID)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isBadRequest())
                .andExpect(content().string(containsString("Unable to update; This item was not found.")));
    }

    @Test
    void whenCampaignNameIsInvalid_updateCampaign_RespondsBadRequest() throws Exception {
        CampaignDTO invalidDto = new CampaignDTO();
        invalidDto.setUuid(VALID_CAMPAIGN_ID);
        invalidDto.setDescription("");

        String json = objectMapper.writeValueAsString(invalidDto);

        when(campaignService.updateCampaign(eq(VALID_CAMPAIGN_ID), any(CampaignDTO.class)))
                .thenThrow(new IllegalArgumentException("Item name cannot be null or empty."));

        mockMvc.perform(put(URI + "/" + VALID_CAMPAIGN_ID)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isBadRequest())
                .andExpect(content().string(containsString("Item name cannot be null or empty.")));
    }
}
