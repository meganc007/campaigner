package com.mcommings.campaigner.controllers.locations;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mcommings.campaigner.modules.locations.controllers.PlaceTypeController;
import com.mcommings.campaigner.modules.locations.dtos.PlaceTypeDTO;
import com.mcommings.campaigner.modules.locations.entities.PlaceType;
import com.mcommings.campaigner.modules.locations.services.PlaceTypeService;
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

@WebMvcTest(PlaceTypeController.class)
public class PlaceTypeControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    PlaceTypeService placeTypeService;

    private static final int VALID_PLACETYPE_ID = 1;
    private static final int INVALID_PLACETYPE_ID = 999;
    private static final String URI = "/api/placetypes";
    private PlaceType entity;
    private PlaceTypeDTO dto;
    private Random random = new Random();

    @BeforeEach
    void setUp() {
        entity = new PlaceType();
        entity.setId(VALID_PLACETYPE_ID);
        entity.setName("A name.");
        entity.setDescription("A description.");

        dto = new PlaceTypeDTO();
        dto.setId(entity.getId());
        dto.setName(entity.getName());
        dto.setDescription(entity.getDescription());
    }

    @Test
    void whenThereArePlaceTypes_getPlaceTypes_ReturnsPlaceTypes() throws Exception {
        when(placeTypeService.getPlaceTypes()).thenReturn(List.of(dto));

        String response = objectMapper.writeValueAsString(List.of(dto));

        mockMvc.perform(get(URI))
                .andExpect(status().isOk())
                .andExpect(content().json(response));
    }

    @Test
    void whenThereArePlaceTypes_getPlaceTypes_ReturnsEmptyList() throws Exception {
        when(placeTypeService.getPlaceTypes()).thenReturn(List.of());

        mockMvc.perform(get(URI))
                .andExpect(status().isOk())
                .andExpect(content().json("[]"));
    }

    @Test
    void whenThereIsAPlaceType_getPlaceType_ReturnsPlaceType() throws Exception {
        when(placeTypeService.getPlaceType(VALID_PLACETYPE_ID)).thenReturn(Optional.of(dto));

        mockMvc.perform(get(URI + "/" + VALID_PLACETYPE_ID))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(VALID_PLACETYPE_ID))
                .andExpect(jsonPath("$.description").value(dto.getDescription()));
    }

    @Test
    void whenThereIsNotAPlaceType_getPlaceType_ThrowsIllegalArgumentException() throws Exception {
        when(placeTypeService.getPlaceType(INVALID_PLACETYPE_ID)).thenReturn(Optional.empty());

        mockMvc.perform(get(URI + "/" + INVALID_PLACETYPE_ID))
                .andExpect(status().isBadRequest())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof IllegalArgumentException))
                .andExpect(result -> assertEquals(ID_NOT_FOUND.message, result.getResolvedException().getMessage()));
    }

    @Test
    void whenUserEntersWrongIdType_getPlaceType_ReturnsBadRequest() throws Exception {
        mockMvc.perform(get(URI + "/abc"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void whenServiceFails_getPlaceType_ReturnsInternalServerError() throws Exception {
        when(placeTypeService.getPlaceType(VALID_PLACETYPE_ID)).thenThrow(new RuntimeException("Database failure"));

        mockMvc.perform(get(URI + "/" + VALID_PLACETYPE_ID))
                .andExpect(status().isInternalServerError())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof RuntimeException))
                .andExpect(result -> assertEquals("Database failure", result.getResolvedException().getMessage()));
    }

    @Test
    void whenServiceReturnsNull_getPlaceType_ReturnsInternalServerError() throws Exception {
        when(placeTypeService.getPlaceType(VALID_PLACETYPE_ID)).thenReturn(null);

        mockMvc.perform(get(URI + "/" + VALID_PLACETYPE_ID))
                .andExpect(status().isInternalServerError());
    }

    @Test
    void whenUserEntersWrongCampaignUUIDType_getPlaceType_ReturnsBadRequest() throws Exception {
        mockMvc.perform(get(URI + "/abc"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void whenPlaceTypeIsValid_savePlaceType_RespondsOkRequest() throws Exception {
        UUID uuid = UUID.randomUUID();
        PlaceTypeDTO requestDto = new PlaceTypeDTO();
        requestDto.setId(2);
        requestDto.setName("This is a name");
        requestDto.setDescription("This is a description");

        String requestJson = objectMapper.writeValueAsString(requestDto);

        mockMvc.perform(post("/api/placetypes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andExpect(status().isOk());

        verify(placeTypeService, times(1)).savePlaceType(any(PlaceTypeDTO.class));
    }

    @Test
    void whenPlaceTypeIsNotValid_savePlaceType_RespondsBadRequest() throws Exception {
        PlaceTypeDTO invalidPlaceType = new PlaceTypeDTO();
        invalidPlaceType.setId(2);
        invalidPlaceType.setDescription("This is a description");

        String requestJson = objectMapper.writeValueAsString(invalidPlaceType);

        doThrow(new IllegalArgumentException("Campaign UUID cannot be null or empty."))
                .when(placeTypeService).savePlaceType(any(PlaceTypeDTO.class));

        MvcResult result = mockMvc.perform(post("/api/placetypes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andExpect(status().isBadRequest())
                .andReturn();

        String responseBody = result.getResponse().getContentAsString();
        ObjectMapper mapper = new ObjectMapper();
        List<String> errors = mapper.readValue(responseBody, new TypeReference<List<String>>() {
        });

        assertTrue(errors.contains("PlaceType name cannot be empty"));

        verify(placeTypeService, times(0)).savePlaceType(any(PlaceTypeDTO.class));
    }

    @Test
    void whenPlaceTypeIdIsValid_deletePlaceType_RespondsOkRequest() throws Exception {
        when(placeTypeService.getPlaceType(VALID_PLACETYPE_ID)).thenReturn(Optional.of(dto));

        mockMvc.perform(delete(URI + "/" + VALID_PLACETYPE_ID))
                .andExpect(status().isOk());

        verify(placeTypeService, times(1)).deletePlaceType(VALID_PLACETYPE_ID);
    }

    @Test
    void whenPlaceTypeIdIsInvalid_deletePlaceType_RespondsBadRequest() throws Exception {
        doThrow(new IllegalArgumentException("Unable to delete; This item was not found."))
                .when(placeTypeService).deletePlaceType(INVALID_PLACETYPE_ID);

        mockMvc.perform(delete(URI + "/" + INVALID_PLACETYPE_ID))
                .andExpect(status().isBadRequest())  // Expecting 400 Bad Request
                .andExpect(content().string("Unable to delete; This item was not found."));

        verify(placeTypeService, times(1)).deletePlaceType(INVALID_PLACETYPE_ID);
    }

    @Test
    void whenPlaceTypeIdIsValid_updatePlaceType_RespondsOkRequest() throws Exception {
        PlaceTypeDTO updatedDto = new PlaceTypeDTO();
        updatedDto.setId(VALID_PLACETYPE_ID);
        updatedDto.setDescription("Updated description");

        String json = objectMapper.writeValueAsString(updatedDto);

        mockMvc.perform(put(URI + "/" + VALID_PLACETYPE_ID)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk());

        verify(placeTypeService, times(1)).updatePlaceType(eq(VALID_PLACETYPE_ID), any(PlaceTypeDTO.class));
    }

    @Test
    void whenPlaceTypeIdIsInvalid_updatePlaceType_RespondsBadRequest() throws Exception {
        PlaceTypeDTO updatedDto = new PlaceTypeDTO();
        updatedDto.setId(INVALID_PLACETYPE_ID);
        updatedDto.setDescription("Some update");

        String json = objectMapper.writeValueAsString(updatedDto);

        doThrow(new IllegalArgumentException("Unable to update; This item was not found."))
                .when(placeTypeService).updatePlaceType(eq(INVALID_PLACETYPE_ID), any(PlaceTypeDTO.class));

        mockMvc.perform(put(URI + "/" + INVALID_PLACETYPE_ID)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isBadRequest())
                .andExpect(content().string(containsString("Unable to update; This item was not found.")));
    }

    @Test
    void whenPlaceTypeNameIsInvalid_updatePlaceType_RespondsBadRequest() throws Exception {
        PlaceTypeDTO invalidDto = new PlaceTypeDTO();
        invalidDto.setId(VALID_PLACETYPE_ID);
        invalidDto.setDescription("");

        String json = objectMapper.writeValueAsString(invalidDto);

        when(placeTypeService.updatePlaceType(eq(VALID_PLACETYPE_ID), any(PlaceTypeDTO.class)))
                .thenThrow(new IllegalArgumentException("Item name cannot be null or empty."));

        mockMvc.perform(put(URI + "/" + VALID_PLACETYPE_ID)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isBadRequest())
                .andExpect(content().string(containsString("Item name cannot be null or empty.")));
    }
}
