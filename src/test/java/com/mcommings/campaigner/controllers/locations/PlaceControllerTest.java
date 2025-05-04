package com.mcommings.campaigner.controllers.locations;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mcommings.campaigner.modules.locations.controllers.PlaceController;
import com.mcommings.campaigner.modules.locations.dtos.PlaceDTO;
import com.mcommings.campaigner.modules.locations.entities.Place;
import com.mcommings.campaigner.modules.locations.services.PlaceService;
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

@WebMvcTest(PlaceController.class)
public class PlaceControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    PlaceService placeService;

    private static final int VALID_PLACE_ID = 1;
    private static final int INVALID_PLACE_ID = 999;
    private static final String URI = "/api/places";
    private Place entity;
    private PlaceDTO dto;
    private Random random = new Random();

    @BeforeEach
    void setUp() {
        entity = new Place();
        entity.setId(VALID_PLACE_ID);
        entity.setName("A name.");
        entity.setDescription("A description.");
        entity.setFk_campaign_uuid(UUID.randomUUID());
        entity.setFk_place_type(random.nextInt(100) + 1);
        entity.setFk_terrain(random.nextInt(100) + 1);
        entity.setFk_country(random.nextInt(100) + 1);
        entity.setFk_city(random.nextInt(100) + 1);
        entity.setFk_region(random.nextInt(100) + 1);

        dto = new PlaceDTO();
        dto.setId(entity.getId());
        dto.setName(entity.getName());
        dto.setDescription(entity.getDescription());
        dto.setFk_campaign_uuid(entity.getFk_campaign_uuid());
        dto.setFk_place_type(entity.getFk_place_type());
        dto.setFk_terrain(entity.getFk_terrain());
        dto.setFk_country(entity.getFk_country());
        dto.setFk_city(entity.getFk_city());
        dto.setFk_region(entity.getFk_region());
    }

    @Test
    void whenThereArePlaces_getPlaces_ReturnsPlaces() throws Exception {
        when(placeService.getPlaces()).thenReturn(List.of(dto));

        String response = objectMapper.writeValueAsString(List.of(dto));

        mockMvc.perform(get(URI))
                .andExpect(status().isOk())
                .andExpect(content().json(response));
    }

    @Test
    void whenThereArePlaces_getPlaces_ReturnsEmptyList() throws Exception {
        when(placeService.getPlaces()).thenReturn(List.of());

        mockMvc.perform(get(URI))
                .andExpect(status().isOk())
                .andExpect(content().json("[]"));
    }

    @Test
    void whenThereIsAPlace_getPlace_ReturnsPlace() throws Exception {
        when(placeService.getPlace(VALID_PLACE_ID)).thenReturn(Optional.of(dto));

        mockMvc.perform(get(URI + "/" + VALID_PLACE_ID))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(VALID_PLACE_ID))
                .andExpect(jsonPath("$.description").value(dto.getDescription()));
    }

    @Test
    void whenThereIsNotAPlace_getPlace_ThrowsIllegalArgumentException() throws Exception {
        when(placeService.getPlace(INVALID_PLACE_ID)).thenReturn(Optional.empty());

        mockMvc.perform(get(URI + "/" + INVALID_PLACE_ID))
                .andExpect(status().isBadRequest())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof IllegalArgumentException))
                .andExpect(result -> assertEquals(ID_NOT_FOUND.message, result.getResolvedException().getMessage()));
    }

    @Test
    void whenUserEntersWrongIdType_getPlace_ReturnsBadRequest() throws Exception {
        mockMvc.perform(get(URI + "/abc"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void whenServiceFails_getPlace_ReturnsInternalServerError() throws Exception {
        when(placeService.getPlace(VALID_PLACE_ID)).thenThrow(new RuntimeException("Database failure"));

        mockMvc.perform(get(URI + "/" + VALID_PLACE_ID))
                .andExpect(status().isInternalServerError())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof RuntimeException))
                .andExpect(result -> assertEquals("Database failure", result.getResolvedException().getMessage()));
    }

    @Test
    void whenServiceReturnsNull_getPlace_ReturnsInternalServerError() throws Exception {
        when(placeService.getPlace(VALID_PLACE_ID)).thenReturn(null);

        mockMvc.perform(get(URI + "/" + VALID_PLACE_ID))
                .andExpect(status().isInternalServerError());
    }

    @Test
    void whenCampaignUUIDIsValid_getPlacesByCampaignUUID_ReturnsPlaces() throws Exception {
        UUID uuid = UUID.randomUUID();
        when(placeService.getPlacesByCampaignUUID(uuid)).thenReturn(List.of(dto));

        String response = objectMapper.writeValueAsString(List.of(dto));

        mockMvc.perform(get(URI + "/campaign/" + uuid))
                .andExpect(status().isOk())
                .andExpect(content().json(response));
    }

    @Test
    void whenCampaignUUIDIsValid_getPlacesByCampaignUUID_ReturnsEmptyList() throws Exception {
        UUID uuid = UUID.randomUUID();
        when(placeService.getPlacesByCampaignUUID(uuid)).thenReturn(List.of());

        mockMvc.perform(get(URI + "/campaign/" + uuid))
                .andExpect(status().isOk())
                .andExpect(content().json("[]"));
    }

    @Test
    void whenUserEntersWrongCampaignUUIDType_getPlace_ReturnsBadRequest() throws Exception {
        mockMvc.perform(get(URI + "/abc"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void whenCountryIDIsValid_getPlacesByCountry_ReturnsPlaces() throws Exception {
        int countryId = random.nextInt(100) + 1;
        when(placeService.getPlacesByCountryId(countryId)).thenReturn(List.of(dto));

        String response = objectMapper.writeValueAsString(List.of(dto));

        mockMvc.perform(get(URI + "/country/" + countryId))
                .andExpect(status().isOk())
                .andExpect(content().json(response));
    }

    @Test
    void whenCountryIDIsNotValid_getPlacesByCountry_ReturnsEmptyList() throws Exception {
        int countryId = random.nextInt(100) + 1;
        when(placeService.getPlacesByCountryId(countryId)).thenReturn(List.of());

        mockMvc.perform(get(URI + "/country/" + countryId))
                .andExpect(status().isOk())
                .andExpect(content().json("[]"));
    }

    @Test
    void whenCityIDIsValid_getPlacesByCity_ReturnsPlaces() throws Exception {
        int cityId = random.nextInt(100) + 1;
        when(placeService.getPlacesByCityId(cityId)).thenReturn(List.of(dto));

        String response = objectMapper.writeValueAsString(List.of(dto));

        mockMvc.perform(get(URI + "/city/" + cityId))
                .andExpect(status().isOk())
                .andExpect(content().json(response));
    }

    @Test
    void whenCityIDIsNotValid_getPlacesByCity_ReturnsEmptyList() throws Exception {
        int cityId = random.nextInt(100) + 1;
        when(placeService.getPlacesByCityId(cityId)).thenReturn(List.of());

        mockMvc.perform(get(URI + "/city/" + cityId))
                .andExpect(status().isOk())
                .andExpect(content().json("[]"));
    }

    @Test
    void whenRegionIDIsValid_getPlacesByRegion_ReturnsPlaces() throws Exception {
        int regionId = random.nextInt(100) + 1;
        when(placeService.getPlacesByRegionId(regionId)).thenReturn(List.of(dto));

        String response = objectMapper.writeValueAsString(List.of(dto));

        mockMvc.perform(get(URI + "/region/" + regionId))
                .andExpect(status().isOk())
                .andExpect(content().json(response));
    }

    @Test
    void whenRegionIDIsNotValid_getPlacesByRegion_ReturnsEmptyList() throws Exception {
        int regionId = random.nextInt(100) + 1;
        when(placeService.getPlacesByRegionId(regionId)).thenReturn(List.of());

        mockMvc.perform(get(URI + "/region/" + regionId))
                .andExpect(status().isOk())
                .andExpect(content().json("[]"));
    }

    @Test
    void whenPlaceIsValid_savePlace_RespondsOkRequest() throws Exception {
        UUID uuid = UUID.randomUUID();
        PlaceDTO requestDto = new PlaceDTO();
        requestDto.setId(2);
        requestDto.setName("This is a name");
        requestDto.setDescription("This is a description");
        requestDto.setFk_campaign_uuid(uuid);
        requestDto.setFk_country(4);

        String requestJson = objectMapper.writeValueAsString(requestDto);

        mockMvc.perform(post("/api/places")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andExpect(status().isOk());

        verify(placeService, times(1)).savePlace(any(PlaceDTO.class));
    }

    @Test
    void whenPlaceIsNotValid_savePlace_RespondsBadRequest() throws Exception {
        PlaceDTO invalidPlace = new PlaceDTO();
        invalidPlace.setId(2);
        invalidPlace.setDescription("This is a description");
        invalidPlace.setFk_campaign_uuid(null); // Invalid UUID
        invalidPlace.setFk_country(4);

        String requestJson = objectMapper.writeValueAsString(invalidPlace);

        doThrow(new IllegalArgumentException("Campaign UUID cannot be null or empty."))
                .when(placeService).savePlace(any(PlaceDTO.class));

        MvcResult result = mockMvc.perform(post("/api/places")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andExpect(status().isBadRequest())
                .andReturn();

        String responseBody = result.getResponse().getContentAsString();
        ObjectMapper mapper = new ObjectMapper();
        List<String> errors = mapper.readValue(responseBody, new TypeReference<List<String>>() {
        });

        assertTrue(errors.contains("Place name cannot be empty"));
        assertTrue(errors.contains("Campaign UUID cannot be null or empty."));

        verify(placeService, times(0)).savePlace(any(PlaceDTO.class));
    }

    @Test
    void whenPlaceIdIsValid_deletePlace_RespondsOkRequest() throws Exception {
        when(placeService.getPlace(VALID_PLACE_ID)).thenReturn(Optional.of(dto));

        mockMvc.perform(delete(URI + "/" + VALID_PLACE_ID))
                .andExpect(status().isOk());

        verify(placeService, times(1)).deletePlace(VALID_PLACE_ID);
    }

    @Test
    void whenPlaceIdIsInvalid_deletePlace_RespondsBadRequest() throws Exception {
        doThrow(new IllegalArgumentException("Unable to delete; This item was not found."))
                .when(placeService).deletePlace(INVALID_PLACE_ID);

        mockMvc.perform(delete(URI + "/" + INVALID_PLACE_ID))
                .andExpect(status().isBadRequest())  // Expecting 400 Bad Request
                .andExpect(content().string("Unable to delete; This item was not found."));

        verify(placeService, times(1)).deletePlace(INVALID_PLACE_ID);
    }

    @Test
    void whenPlaceIdIsValid_updatePlace_RespondsOkRequest() throws Exception {
        PlaceDTO updatedDto = new PlaceDTO();
        updatedDto.setId(VALID_PLACE_ID);
        updatedDto.setDescription("Updated description");
        updatedDto.setFk_campaign_uuid(UUID.randomUUID());
        updatedDto.setFk_country(2);

        String json = objectMapper.writeValueAsString(updatedDto);

        mockMvc.perform(put(URI + "/" + VALID_PLACE_ID)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk());

        verify(placeService, times(1)).updatePlace(eq(VALID_PLACE_ID), any(PlaceDTO.class));
    }

    @Test
    void whenPlaceIdIsInvalid_updatePlace_RespondsBadRequest() throws Exception {
        PlaceDTO updatedDto = new PlaceDTO();
        updatedDto.setId(INVALID_PLACE_ID);
        updatedDto.setDescription("Some update");
        updatedDto.setFk_campaign_uuid(UUID.randomUUID());
        updatedDto.setFk_country(1);

        String json = objectMapper.writeValueAsString(updatedDto);

        doThrow(new IllegalArgumentException("Unable to update; This item was not found."))
                .when(placeService).updatePlace(eq(INVALID_PLACE_ID), any(PlaceDTO.class));

        mockMvc.perform(put(URI + "/" + INVALID_PLACE_ID)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isBadRequest())
                .andExpect(content().string(containsString("Unable to update; This item was not found.")));
    }

    @Test
    void whenPlaceNameIsInvalid_updatePlace_RespondsBadRequest() throws Exception {
        PlaceDTO invalidDto = new PlaceDTO();
        invalidDto.setId(VALID_PLACE_ID);
        invalidDto.setDescription("");
        invalidDto.setFk_campaign_uuid(UUID.randomUUID());
        invalidDto.setFk_country(1);

        String json = objectMapper.writeValueAsString(invalidDto);

        when(placeService.updatePlace(eq(VALID_PLACE_ID), any(PlaceDTO.class)))
                .thenThrow(new IllegalArgumentException("Item name cannot be null or empty."));

        mockMvc.perform(put(URI + "/" + VALID_PLACE_ID)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isBadRequest())
                .andExpect(content().string(containsString("Item name cannot be null or empty.")));
    }
}
