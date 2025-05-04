package com.mcommings.campaigner.controllers.locations;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mcommings.campaigner.modules.locations.controllers.TerrainController;
import com.mcommings.campaigner.modules.locations.dtos.TerrainDTO;
import com.mcommings.campaigner.modules.locations.entities.Terrain;
import com.mcommings.campaigner.modules.locations.services.TerrainService;
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

@WebMvcTest(TerrainController.class)
public class TerrainControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    TerrainService terrainService;

    private static final int VALID_TERRAIN_ID = 1;
    private static final int INVALID_TERRAIN_ID = 999;
    private static final String URI = "/api/terrains";
    private Terrain entity;
    private TerrainDTO dto;
    private Random random = new Random();

    @BeforeEach
    void setUp() {
        entity = new Terrain();
        entity.setId(VALID_TERRAIN_ID);
        entity.setName("A name.");
        entity.setDescription("A description.");

        dto = new TerrainDTO();
        dto.setId(entity.getId());
        dto.setName(entity.getName());
        dto.setDescription(entity.getDescription());
    }

    @Test
    void whenThereAreTerrains_getTerrains_ReturnsTerrains() throws Exception {
        when(terrainService.getTerrains()).thenReturn(List.of(dto));

        String response = objectMapper.writeValueAsString(List.of(dto));

        mockMvc.perform(get(URI))
                .andExpect(status().isOk())
                .andExpect(content().json(response));
    }

    @Test
    void whenThereAreTerrains_getTerrains_ReturnsEmptyList() throws Exception {
        when(terrainService.getTerrains()).thenReturn(List.of());

        mockMvc.perform(get(URI))
                .andExpect(status().isOk())
                .andExpect(content().json("[]"));
    }

    @Test
    void whenThereIsATerrain_getTerrain_ReturnsTerrain() throws Exception {
        when(terrainService.getTerrain(VALID_TERRAIN_ID)).thenReturn(Optional.of(dto));

        mockMvc.perform(get(URI + "/" + VALID_TERRAIN_ID))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(VALID_TERRAIN_ID))
                .andExpect(jsonPath("$.description").value(dto.getDescription()));
    }

    @Test
    void whenThereIsNotATerrain_getTerrain_ThrowsIllegalArgumentException() throws Exception {
        when(terrainService.getTerrain(INVALID_TERRAIN_ID)).thenReturn(Optional.empty());

        mockMvc.perform(get(URI + "/" + INVALID_TERRAIN_ID))
                .andExpect(status().isBadRequest())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof IllegalArgumentException))
                .andExpect(result -> assertEquals(ID_NOT_FOUND.message, result.getResolvedException().getMessage()));
    }

    @Test
    void whenUserEntersWrongIdType_getTerrain_ReturnsBadRequest() throws Exception {
        mockMvc.perform(get(URI + "/abc"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void whenServiceFails_getTerrain_ReturnsInternalServerError() throws Exception {
        when(terrainService.getTerrain(VALID_TERRAIN_ID)).thenThrow(new RuntimeException("Database failure"));

        mockMvc.perform(get(URI + "/" + VALID_TERRAIN_ID))
                .andExpect(status().isInternalServerError())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof RuntimeException))
                .andExpect(result -> assertEquals("Database failure", result.getResolvedException().getMessage()));
    }

    @Test
    void whenServiceReturnsNull_getTerrain_ReturnsInternalServerError() throws Exception {
        when(terrainService.getTerrain(VALID_TERRAIN_ID)).thenReturn(null);

        mockMvc.perform(get(URI + "/" + VALID_TERRAIN_ID))
                .andExpect(status().isInternalServerError());
    }


    @Test
    void whenTerrainIsValid_saveTerrain_RespondsOkRequest() throws Exception {
        UUID uuid = UUID.randomUUID();
        TerrainDTO requestDto = new TerrainDTO();
        requestDto.setId(2);
        requestDto.setName("This is a name");
        requestDto.setDescription("This is a description");

        String requestJson = objectMapper.writeValueAsString(requestDto);

        mockMvc.perform(post("/api/terrains")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andExpect(status().isOk());

        verify(terrainService, times(1)).saveTerrain(any(TerrainDTO.class));
    }

    @Test
    void whenTerrainIsNotValid_saveTerrain_RespondsBadRequest() throws Exception {
        TerrainDTO invalidTerrain = new TerrainDTO();
        invalidTerrain.setId(2);
        invalidTerrain.setDescription("This is a description");

        String requestJson = objectMapper.writeValueAsString(invalidTerrain);

        doThrow(new IllegalArgumentException("Campaign UUID cannot be null or empty."))
                .when(terrainService).saveTerrain(any(TerrainDTO.class));

        MvcResult result = mockMvc.perform(post("/api/terrains")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andExpect(status().isBadRequest())
                .andReturn();

        String responseBody = result.getResponse().getContentAsString();
        ObjectMapper mapper = new ObjectMapper();
        List<String> errors = mapper.readValue(responseBody, new TypeReference<List<String>>() {
        });

        assertTrue(errors.contains("Terrain name cannot be empty"));

        verify(terrainService, times(0)).saveTerrain(any(TerrainDTO.class));
    }

    @Test
    void whenTerrainIdIsValid_deleteTerrain_RespondsOkRequest() throws Exception {
        when(terrainService.getTerrain(VALID_TERRAIN_ID)).thenReturn(Optional.of(dto));

        mockMvc.perform(delete(URI + "/" + VALID_TERRAIN_ID))
                .andExpect(status().isOk());

        verify(terrainService, times(1)).deleteTerrain(VALID_TERRAIN_ID);
    }

    @Test
    void whenTerrainIdIsInvalid_deleteTerrain_RespondsBadRequest() throws Exception {
        doThrow(new IllegalArgumentException("Unable to delete; This item was not found."))
                .when(terrainService).deleteTerrain(INVALID_TERRAIN_ID);

        mockMvc.perform(delete(URI + "/" + INVALID_TERRAIN_ID))
                .andExpect(status().isBadRequest())  // Expecting 400 Bad Request
                .andExpect(content().string("Unable to delete; This item was not found."));

        verify(terrainService, times(1)).deleteTerrain(INVALID_TERRAIN_ID);
    }

    @Test
    void whenTerrainIdIsValid_updateTerrain_RespondsOkRequest() throws Exception {
        TerrainDTO updatedDto = new TerrainDTO();
        updatedDto.setId(VALID_TERRAIN_ID);
        updatedDto.setDescription("Updated description");

        String json = objectMapper.writeValueAsString(updatedDto);

        mockMvc.perform(put(URI + "/" + VALID_TERRAIN_ID)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk());

        verify(terrainService, times(1)).updateTerrain(eq(VALID_TERRAIN_ID), any(TerrainDTO.class));
    }

    @Test
    void whenTerrainIdIsInvalid_updateTerrain_RespondsBadRequest() throws Exception {
        TerrainDTO updatedDto = new TerrainDTO();
        updatedDto.setId(INVALID_TERRAIN_ID);
        updatedDto.setDescription("Some update");

        String json = objectMapper.writeValueAsString(updatedDto);

        doThrow(new IllegalArgumentException("Unable to update; This item was not found."))
                .when(terrainService).updateTerrain(eq(INVALID_TERRAIN_ID), any(TerrainDTO.class));

        mockMvc.perform(put(URI + "/" + INVALID_TERRAIN_ID)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isBadRequest())
                .andExpect(content().string(containsString("Unable to update; This item was not found.")));
    }

    @Test
    void whenTerrainNameIsInvalid_updateTerrain_RespondsBadRequest() throws Exception {
        TerrainDTO invalidDto = new TerrainDTO();
        invalidDto.setId(VALID_TERRAIN_ID);
        invalidDto.setDescription("");

        String json = objectMapper.writeValueAsString(invalidDto);

        when(terrainService.updateTerrain(eq(VALID_TERRAIN_ID), any(TerrainDTO.class)))
                .thenThrow(new IllegalArgumentException("Item name cannot be null or empty."));

        mockMvc.perform(put(URI + "/" + VALID_TERRAIN_ID)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isBadRequest())
                .andExpect(content().string(containsString("Item name cannot be null or empty.")));
    }
}
