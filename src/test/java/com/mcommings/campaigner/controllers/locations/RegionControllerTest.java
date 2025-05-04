package com.mcommings.campaigner.controllers.locations;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mcommings.campaigner.modules.locations.controllers.RegionController;
import com.mcommings.campaigner.modules.locations.dtos.RegionDTO;
import com.mcommings.campaigner.modules.locations.entities.Region;
import com.mcommings.campaigner.modules.locations.services.RegionService;
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

@WebMvcTest(RegionController.class)
public class RegionControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    RegionService regionService;

    private static final int VALID_REGION_ID = 1;
    private static final int INVALID_REGION_ID = 999;
    private static final String URI = "/api/regions";
    private Region entity;
    private RegionDTO dto;
    private Random random = new Random();

    @BeforeEach
    void setUp() {
        entity = new Region();
        entity.setId(VALID_REGION_ID);
        entity.setName("A name.");
        entity.setDescription("A description.");
        entity.setFk_campaign_uuid(UUID.randomUUID());
        entity.setFk_country(random.nextInt(100) + 1);
        entity.setFk_climate(random.nextInt(100) + 1);

        dto = new RegionDTO();
        dto.setId(entity.getId());
        dto.setName(entity.getName());
        dto.setDescription(entity.getDescription());
        dto.setFk_campaign_uuid(entity.getFk_campaign_uuid());
        dto.setFk_country(entity.getFk_country());
        dto.setFk_climate(entity.getFk_climate());
    }

    @Test
    void whenThereAreRegions_getRegions_ReturnsRegions() throws Exception {
        when(regionService.getRegions()).thenReturn(List.of(dto));

        String response = objectMapper.writeValueAsString(List.of(dto));

        mockMvc.perform(get(URI))
                .andExpect(status().isOk())
                .andExpect(content().json(response));
    }

    @Test
    void whenThereAreRegions_getRegions_ReturnsEmptyList() throws Exception {
        when(regionService.getRegions()).thenReturn(List.of());

        mockMvc.perform(get(URI))
                .andExpect(status().isOk())
                .andExpect(content().json("[]"));
    }

    @Test
    void whenThereIsARegion_getRegion_ReturnsRegion() throws Exception {
        when(regionService.getRegion(VALID_REGION_ID)).thenReturn(Optional.of(dto));

        mockMvc.perform(get(URI + "/" + VALID_REGION_ID))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(VALID_REGION_ID))
                .andExpect(jsonPath("$.description").value(dto.getDescription()));
    }

    @Test
    void whenThereIsNotARegion_getRegion_ThrowsIllegalArgumentException() throws Exception {
        when(regionService.getRegion(INVALID_REGION_ID)).thenReturn(Optional.empty());

        mockMvc.perform(get(URI + "/" + INVALID_REGION_ID))
                .andExpect(status().isBadRequest())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof IllegalArgumentException))
                .andExpect(result -> assertEquals(ID_NOT_FOUND.message, result.getResolvedException().getMessage()));
    }

    @Test
    void whenUserEntersWrongIdType_getRegion_ReturnsBadRequest() throws Exception {
        mockMvc.perform(get(URI + "/abc"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void whenServiceFails_getRegion_ReturnsInternalServerError() throws Exception {
        when(regionService.getRegion(VALID_REGION_ID)).thenThrow(new RuntimeException("Database failure"));

        mockMvc.perform(get(URI + "/" + VALID_REGION_ID))
                .andExpect(status().isInternalServerError())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof RuntimeException))
                .andExpect(result -> assertEquals("Database failure", result.getResolvedException().getMessage()));
    }

    @Test
    void whenServiceReturnsNull_getRegion_ReturnsInternalServerError() throws Exception {
        when(regionService.getRegion(VALID_REGION_ID)).thenReturn(null);

        mockMvc.perform(get(URI + "/" + VALID_REGION_ID))
                .andExpect(status().isInternalServerError());
    }

    @Test
    void whenCampaignUUIDIsValid_getRegionsByCampaignUUID_ReturnsRegions() throws Exception {
        UUID uuid = UUID.randomUUID();
        when(regionService.getRegionsByCampaignUUID(uuid)).thenReturn(List.of(dto));

        String response = objectMapper.writeValueAsString(List.of(dto));

        mockMvc.perform(get(URI + "/campaign/" + uuid))
                .andExpect(status().isOk())
                .andExpect(content().json(response));
    }

    @Test
    void whenCampaignUUIDIsValid_getRegionsByCampaignUUID_ReturnsEmptyList() throws Exception {
        UUID uuid = UUID.randomUUID();
        when(regionService.getRegionsByCampaignUUID(uuid)).thenReturn(List.of());

        mockMvc.perform(get(URI + "/campaign/" + uuid))
                .andExpect(status().isOk())
                .andExpect(content().json("[]"));
    }

    @Test
    void whenUserEntersWrongCampaignUUIDType_getRegion_ReturnsBadRequest() throws Exception {
        mockMvc.perform(get(URI + "/abc"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void whenCountryIDIsValid_getRegionsByCountry_ReturnsRegions() throws Exception {
        int countryId = random.nextInt(100) + 1;
        when(regionService.getRegionsByCountryId(countryId)).thenReturn(List.of(dto));

        String response = objectMapper.writeValueAsString(List.of(dto));

        mockMvc.perform(get(URI + "/country/" + countryId))
                .andExpect(status().isOk())
                .andExpect(content().json(response));
    }

    @Test
    void whenCountryIDIsNotValid_getRegionsByCountry_ReturnsEmptyList() throws Exception {
        int countryId = random.nextInt(100) + 1;
        when(regionService.getRegionsByCountryId(countryId)).thenReturn(List.of());

        mockMvc.perform(get(URI + "/country/" + countryId))
                .andExpect(status().isOk())
                .andExpect(content().json("[]"));
    }

    @Test
    void whenRegionIsValid_saveRegion_RespondsOkRequest() throws Exception {
        UUID uuid = UUID.randomUUID();
        RegionDTO requestDto = new RegionDTO();
        requestDto.setId(2);
        requestDto.setName("This is a name");
        requestDto.setDescription("This is a description");
        requestDto.setFk_campaign_uuid(uuid);
        requestDto.setFk_country(4);

        String requestJson = objectMapper.writeValueAsString(requestDto);

        mockMvc.perform(post("/api/regions")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andExpect(status().isOk());

        verify(regionService, times(1)).saveRegion(any(RegionDTO.class));
    }

    @Test
    void whenRegionIsNotValid_saveRegion_RespondsBadRequest() throws Exception {
        RegionDTO invalidRegion = new RegionDTO();
        invalidRegion.setId(2);
        invalidRegion.setDescription("This is a description");
        invalidRegion.setFk_campaign_uuid(null); // Invalid UUID
        invalidRegion.setFk_country(4);

        String requestJson = objectMapper.writeValueAsString(invalidRegion);

        doThrow(new IllegalArgumentException("Campaign UUID cannot be null or empty."))
                .when(regionService).saveRegion(any(RegionDTO.class));

        MvcResult result = mockMvc.perform(post("/api/regions")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andExpect(status().isBadRequest())
                .andReturn();

        String responseBody = result.getResponse().getContentAsString();
        ObjectMapper mapper = new ObjectMapper();
        List<String> errors = mapper.readValue(responseBody, new TypeReference<List<String>>() {
        });

        assertTrue(errors.contains("Region name cannot be empty"));
        assertTrue(errors.contains("Campaign UUID cannot be null or empty."));

        verify(regionService, times(0)).saveRegion(any(RegionDTO.class));
    }

    @Test
    void whenRegionIdIsValid_deleteRegion_RespondsOkRequest() throws Exception {
        when(regionService.getRegion(VALID_REGION_ID)).thenReturn(Optional.of(dto));

        mockMvc.perform(delete(URI + "/" + VALID_REGION_ID))
                .andExpect(status().isOk());

        verify(regionService, times(1)).deleteRegion(VALID_REGION_ID);
    }

    @Test
    void whenRegionIdIsInvalid_deleteRegion_RespondsBadRequest() throws Exception {
        doThrow(new IllegalArgumentException("Unable to delete; This item was not found."))
                .when(regionService).deleteRegion(INVALID_REGION_ID);

        mockMvc.perform(delete(URI + "/" + INVALID_REGION_ID))
                .andExpect(status().isBadRequest())  // Expecting 400 Bad Request
                .andExpect(content().string("Unable to delete; This item was not found."));

        verify(regionService, times(1)).deleteRegion(INVALID_REGION_ID);
    }

    @Test
    void whenRegionIdIsValid_updateRegion_RespondsOkRequest() throws Exception {
        RegionDTO updatedDto = new RegionDTO();
        updatedDto.setId(VALID_REGION_ID);
        updatedDto.setDescription("Updated description");
        updatedDto.setFk_campaign_uuid(UUID.randomUUID());
        updatedDto.setFk_country(2);

        String json = objectMapper.writeValueAsString(updatedDto);

        mockMvc.perform(put(URI + "/" + VALID_REGION_ID)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk());

        verify(regionService, times(1)).updateRegion(eq(VALID_REGION_ID), any(RegionDTO.class));
    }

    @Test
    void whenRegionIdIsInvalid_updateRegion_RespondsBadRequest() throws Exception {
        RegionDTO updatedDto = new RegionDTO();
        updatedDto.setId(INVALID_REGION_ID);
        updatedDto.setDescription("Some update");
        updatedDto.setFk_campaign_uuid(UUID.randomUUID());
        updatedDto.setFk_country(1);

        String json = objectMapper.writeValueAsString(updatedDto);

        doThrow(new IllegalArgumentException("Unable to update; This item was not found."))
                .when(regionService).updateRegion(eq(INVALID_REGION_ID), any(RegionDTO.class));

        mockMvc.perform(put(URI + "/" + INVALID_REGION_ID)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isBadRequest())
                .andExpect(content().string(containsString("Unable to update; This item was not found.")));
    }

    @Test
    void whenRegionNameIsInvalid_updateRegion_RespondsBadRequest() throws Exception {
        RegionDTO invalidDto = new RegionDTO();
        invalidDto.setId(VALID_REGION_ID);
        invalidDto.setDescription("");
        invalidDto.setFk_campaign_uuid(UUID.randomUUID());
        invalidDto.setFk_country(1);

        String json = objectMapper.writeValueAsString(invalidDto);

        when(regionService.updateRegion(eq(VALID_REGION_ID), any(RegionDTO.class)))
                .thenThrow(new IllegalArgumentException("Item name cannot be null or empty."));

        mockMvc.perform(put(URI + "/" + VALID_REGION_ID)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isBadRequest())
                .andExpect(content().string(containsString("Item name cannot be null or empty.")));
    }
}
