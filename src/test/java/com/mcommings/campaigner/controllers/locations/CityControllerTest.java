package com.mcommings.campaigner.controllers.locations;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mcommings.campaigner.modules.locations.controllers.CityController;
import com.mcommings.campaigner.modules.locations.dtos.CityDTO;
import com.mcommings.campaigner.modules.locations.entities.City;
import com.mcommings.campaigner.modules.locations.services.CityService;
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

@WebMvcTest(CityController.class)
public class CityControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    CityService cityService;

    private static final int VALID_CITY_ID = 1;
    private static final int INVALID_CITY_ID = 999;
    private static final String URI = "/api/cities";
    private City entity;
    private CityDTO dto;
    private Random random = new Random();

    @BeforeEach
    void setUp() {
        entity = new City();
        entity.setId(VALID_CITY_ID);
        entity.setName("A name.");
        entity.setDescription("A description.");
        entity.setFk_campaign_uuid(UUID.randomUUID());
        entity.setFk_wealth(random.nextInt(100) + 1);
        entity.setFk_country(random.nextInt(100) + 1);
        entity.setFk_settlement(random.nextInt(100) + 1);
        entity.setFk_government(random.nextInt(100) + 1);

        dto = new CityDTO();
        dto.setId(entity.getId());
        dto.setName(entity.getName());
        dto.setDescription(entity.getDescription());
        dto.setFk_campaign_uuid(entity.getFk_campaign_uuid());
        dto.setFk_wealth(entity.getFk_wealth());
        dto.setFk_country(entity.getFk_country());
        dto.setFk_settlement(entity.getFk_settlement());
        dto.setFk_government(entity.getFk_government());
    }

    @Test
    void whenThereAreCities_getCities_ReturnsCities() throws Exception {
        when(cityService.getCities()).thenReturn(List.of(dto));

        String response = objectMapper.writeValueAsString(List.of(dto));

        mockMvc.perform(get(URI))
                .andExpect(status().isOk())
                .andExpect(content().json(response));
    }

    @Test
    void whenThereAreCities_getCities_ReturnsEmptyList() throws Exception {
        when(cityService.getCities()).thenReturn(List.of());

        mockMvc.perform(get(URI))
                .andExpect(status().isOk())
                .andExpect(content().json("[]"));
    }

    @Test
    void whenThereIsACity_getCity_ReturnsCity() throws Exception {
        when(cityService.getCity(VALID_CITY_ID)).thenReturn(Optional.of(dto));

        mockMvc.perform(get(URI + "/" + VALID_CITY_ID))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(VALID_CITY_ID))
                .andExpect(jsonPath("$.description").value(dto.getDescription()));
    }

    @Test
    void whenThereIsNotACity_getCity_ThrowsIllegalArgumentException() throws Exception {
        when(cityService.getCity(INVALID_CITY_ID)).thenReturn(Optional.empty());

        mockMvc.perform(get(URI + "/" + INVALID_CITY_ID))
                .andExpect(status().isBadRequest())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof IllegalArgumentException))
                .andExpect(result -> assertEquals(ID_NOT_FOUND.message, result.getResolvedException().getMessage()));
    }

    @Test
    void whenUserEntersWrongIdType_getCity_ReturnsBadRequest() throws Exception {
        mockMvc.perform(get(URI + "/abc"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void whenServiceFails_getCity_ReturnsInternalServerError() throws Exception {
        when(cityService.getCity(VALID_CITY_ID)).thenThrow(new RuntimeException("Database failure"));

        mockMvc.perform(get(URI + "/" + VALID_CITY_ID))
                .andExpect(status().isInternalServerError())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof RuntimeException))
                .andExpect(result -> assertEquals("Database failure", result.getResolvedException().getMessage()));
    }

    @Test
    void whenServiceReturnsNull_getCity_ReturnsInternalServerError() throws Exception {
        when(cityService.getCity(VALID_CITY_ID)).thenReturn(null);

        mockMvc.perform(get(URI + "/" + VALID_CITY_ID))
                .andExpect(status().isInternalServerError());
    }

    @Test
    void whenCampaignUUIDIsValid_getCitiesByCampaignUUID_ReturnsCities() throws Exception {
        UUID uuid = UUID.randomUUID();
        when(cityService.getCitiesByCampaignUUID(uuid)).thenReturn(List.of(dto));

        String response = objectMapper.writeValueAsString(List.of(dto));

        mockMvc.perform(get(URI + "/campaign/" + uuid))
                .andExpect(status().isOk())
                .andExpect(content().json(response));
    }

    @Test
    void whenCampaignUUIDIsValid_getCitiesByCampaignUUID_ReturnsEmptyList() throws Exception {
        UUID uuid = UUID.randomUUID();
        when(cityService.getCitiesByCampaignUUID(uuid)).thenReturn(List.of());

        mockMvc.perform(get(URI + "/campaign/" + uuid))
                .andExpect(status().isOk())
                .andExpect(content().json("[]"));
    }

    @Test
    void whenUserEntersWrongCampaignUUIDType_getCity_ReturnsBadRequest() throws Exception {
        mockMvc.perform(get(URI + "/abc"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void whenCountryIDIsValid_getCitiesByCountry_ReturnsCities() throws Exception {
        int countryId = random.nextInt(100) + 1;
        when(cityService.getCitiesByCountryId(countryId)).thenReturn(List.of(dto));

        String response = objectMapper.writeValueAsString(List.of(dto));

        mockMvc.perform(get(URI + "/country/" + countryId))
                .andExpect(status().isOk())
                .andExpect(content().json(response));
    }

    @Test
    void whenCountryIDIsNotValid_getCitiesByCountry_ReturnsEmptyList() throws Exception {
        int countryId = random.nextInt(100) + 1;
        when(cityService.getCitiesByCountryId(countryId)).thenReturn(List.of());

        mockMvc.perform(get(URI + "/country/" + countryId))
                .andExpect(status().isOk())
                .andExpect(content().json("[]"));
    }

    @Test
    void whenRegionIDIsValid_getCitiesByRegion_ReturnsCities() throws Exception {
        int regionId = random.nextInt(100) + 1;
        when(cityService.getCitiesByRegionId(regionId)).thenReturn(List.of(dto));

        String response = objectMapper.writeValueAsString(List.of(dto));

        mockMvc.perform(get(URI + "/region/" + regionId))
                .andExpect(status().isOk())
                .andExpect(content().json(response));
    }

    @Test
    void whenRegionIDIsNotValid_getCitiesByRegion_ReturnsEmptyList() throws Exception {
        int regionId = random.nextInt(100) + 1;
        when(cityService.getCitiesByRegionId(regionId)).thenReturn(List.of());

        mockMvc.perform(get(URI + "/region/" + regionId))
                .andExpect(status().isOk())
                .andExpect(content().json("[]"));
    }

    @Test
    void whenCityIsValid_saveCity_RespondsOkRequest() throws Exception {
        UUID uuid = UUID.randomUUID();
        CityDTO requestDto = new CityDTO();
        requestDto.setId(2);
        requestDto.setName("This is a name");
        requestDto.setDescription("This is a description");
        requestDto.setFk_campaign_uuid(uuid);

        String requestJson = objectMapper.writeValueAsString(requestDto);

        mockMvc.perform(post("/api/cities")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andExpect(status().isOk());

        verify(cityService, times(1)).saveCity(any(CityDTO.class));
    }

    @Test
    void whenCityIsNotValid_saveCity_RespondsBadRequest() throws Exception {
        CityDTO invalidCity = new CityDTO();
        invalidCity.setId(2);
        invalidCity.setDescription("This is a description");
        invalidCity.setFk_campaign_uuid(null); // Invalid UUID

        String requestJson = objectMapper.writeValueAsString(invalidCity);

        doThrow(new IllegalArgumentException("Campaign UUID cannot be null or empty."))
                .when(cityService).saveCity(any(CityDTO.class));

        MvcResult result = mockMvc.perform(post("/api/cities")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andExpect(status().isBadRequest())
                .andReturn();

        String responseBody = result.getResponse().getContentAsString();
        ObjectMapper mapper = new ObjectMapper();
        List<String> errors = mapper.readValue(responseBody, new TypeReference<List<String>>() {
        });

        assertTrue(errors.contains("City name cannot be empty"));
        assertTrue(errors.contains("Campaign UUID cannot be null or empty."));

        verify(cityService, times(0)).saveCity(any(CityDTO.class));
    }

    @Test
    void whenCityIdIsValid_deleteCity_RespondsOkRequest() throws Exception {
        when(cityService.getCity(VALID_CITY_ID)).thenReturn(Optional.of(dto));

        mockMvc.perform(delete(URI + "/" + VALID_CITY_ID))
                .andExpect(status().isOk());

        verify(cityService, times(1)).deleteCity(VALID_CITY_ID);
    }

    @Test
    void whenCityIdIsInvalid_deleteCity_RespondsBadRequest() throws Exception {
        doThrow(new IllegalArgumentException("Unable to delete; This city was not found."))
                .when(cityService).deleteCity(INVALID_CITY_ID);

        mockMvc.perform(delete(URI + "/" + INVALID_CITY_ID))
                .andExpect(status().isBadRequest())  // Expecting 400 Bad Request
                .andExpect(content().string("Unable to delete; This city was not found."));

        verify(cityService, times(1)).deleteCity(INVALID_CITY_ID);
    }

    @Test
    void whenCityIdIsValid_updateCity_RespondsOkRequest() throws Exception {
        CityDTO updatedDto = new CityDTO();
        updatedDto.setId(VALID_CITY_ID);
        updatedDto.setDescription("Updated description");
        updatedDto.setFk_campaign_uuid(UUID.randomUUID());

        String json = objectMapper.writeValueAsString(updatedDto);

        mockMvc.perform(put(URI + "/" + VALID_CITY_ID)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk());

        verify(cityService, times(1)).updateCity(eq(VALID_CITY_ID), any(CityDTO.class));
    }

    @Test
    void whenCityIdIsInvalid_updateCity_RespondsBadRequest() throws Exception {
        CityDTO updatedDto = new CityDTO();
        updatedDto.setId(INVALID_CITY_ID);
        updatedDto.setDescription("Some update");
        updatedDto.setFk_campaign_uuid(UUID.randomUUID());

        String json = objectMapper.writeValueAsString(updatedDto);

        doThrow(new IllegalArgumentException("Unable to update; This city was not found."))
                .when(cityService).updateCity(eq(INVALID_CITY_ID), any(CityDTO.class));

        mockMvc.perform(put(URI + "/" + INVALID_CITY_ID)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isBadRequest())
                .andExpect(content().string(containsString("Unable to update; This city was not found.")));
    }

    @Test
    void whenCityNameIsInvalid_updateCity_RespondsBadRequest() throws Exception {
        CityDTO invalidDto = new CityDTO();
        invalidDto.setId(VALID_CITY_ID);
        invalidDto.setDescription("");
        invalidDto.setFk_campaign_uuid(UUID.randomUUID());

        String json = objectMapper.writeValueAsString(invalidDto);

        when(cityService.updateCity(eq(VALID_CITY_ID), any(CityDTO.class)))
                .thenThrow(new IllegalArgumentException("City name cannot be null or empty."));

        mockMvc.perform(put(URI + "/" + VALID_CITY_ID)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isBadRequest())
                .andExpect(content().string(containsString("City name cannot be null or empty.")));
    }
}
