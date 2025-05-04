package com.mcommings.campaigner.controllers.locations;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mcommings.campaigner.modules.locations.controllers.CountryController;
import com.mcommings.campaigner.modules.locations.dtos.CountryDTO;
import com.mcommings.campaigner.modules.locations.entities.Country;
import com.mcommings.campaigner.modules.locations.services.CountryService;
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

@WebMvcTest(CountryController.class)
public class CountryControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    CountryService countryService;

    private static final int VALID_COUNTRY_ID = 1;
    private static final int INVALID_COUNTRY_ID = 999;
    private static final String URI = "/api/countries";
    private Country entity;
    private CountryDTO dto;
    private Random random = new Random();

    @BeforeEach
    void setUp() {
        entity = new Country();
        entity.setId(VALID_COUNTRY_ID);
        entity.setName("A name.");
        entity.setDescription("A description.");
        entity.setFk_campaign_uuid(UUID.randomUUID());
        entity.setFk_continent(random.nextInt(100) + 1);
        entity.setFk_government(random.nextInt(100) + 1);

        dto = new CountryDTO();
        dto.setId(entity.getId());
        dto.setName(entity.getName());
        dto.setDescription(entity.getDescription());
        dto.setFk_campaign_uuid(entity.getFk_campaign_uuid());
        dto.setFk_continent(entity.getFk_continent());
        dto.setFk_government(entity.getFk_government());
    }

    @Test
    void whenThereAreCountries_getCountries_ReturnsCountries() throws Exception {
        when(countryService.getCountries()).thenReturn(List.of(dto));

        String response = objectMapper.writeValueAsString(List.of(dto));

        mockMvc.perform(get(URI))
                .andExpect(status().isOk())
                .andExpect(content().json(response));
    }

    @Test
    void whenThereAreCountries_getCountries_ReturnsEmptyList() throws Exception {
        when(countryService.getCountries()).thenReturn(List.of());

        mockMvc.perform(get(URI))
                .andExpect(status().isOk())
                .andExpect(content().json("[]"));
    }

    @Test
    void whenThereIsACountry_getCountry_ReturnsCountry() throws Exception {
        when(countryService.getCountry(VALID_COUNTRY_ID)).thenReturn(Optional.of(dto));

        mockMvc.perform(get(URI + "/" + VALID_COUNTRY_ID))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(VALID_COUNTRY_ID))
                .andExpect(jsonPath("$.description").value(dto.getDescription()));
    }

    @Test
    void whenThereIsNotACountry_getCountry_ThrowsIllegalArgumentException() throws Exception {
        when(countryService.getCountry(INVALID_COUNTRY_ID)).thenReturn(Optional.empty());

        mockMvc.perform(get(URI + "/" + INVALID_COUNTRY_ID))
                .andExpect(status().isBadRequest())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof IllegalArgumentException))
                .andExpect(result -> assertEquals(ID_NOT_FOUND.message, result.getResolvedException().getMessage()));
    }

    @Test
    void whenUserEntersWrongIdType_getCountry_ReturnsBadRequest() throws Exception {
        mockMvc.perform(get(URI + "/abc"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void whenServiceFails_getCountry_ReturnsInternalServerError() throws Exception {
        when(countryService.getCountry(VALID_COUNTRY_ID)).thenThrow(new RuntimeException("Database failure"));

        mockMvc.perform(get(URI + "/" + VALID_COUNTRY_ID))
                .andExpect(status().isInternalServerError())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof RuntimeException))
                .andExpect(result -> assertEquals("Database failure", result.getResolvedException().getMessage()));
    }

    @Test
    void whenServiceReturnsNull_getCountry_ReturnsInternalServerError() throws Exception {
        when(countryService.getCountry(VALID_COUNTRY_ID)).thenReturn(null);

        mockMvc.perform(get(URI + "/" + VALID_COUNTRY_ID))
                .andExpect(status().isInternalServerError());
    }

    @Test
    void whenCampaignUUIDIsValid_getCountriesByCampaignUUID_ReturnsCountries() throws Exception {
        UUID uuid = UUID.randomUUID();
        when(countryService.getCountriesByCampaignUUID(uuid)).thenReturn(List.of(dto));

        String response = objectMapper.writeValueAsString(List.of(dto));

        mockMvc.perform(get(URI + "/campaign/" + uuid))
                .andExpect(status().isOk())
                .andExpect(content().json(response));
    }

    @Test
    void whenCampaignUUIDIsValid_getCountriesByCampaignUUID_ReturnsEmptyList() throws Exception {
        UUID uuid = UUID.randomUUID();
        when(countryService.getCountriesByCampaignUUID(uuid)).thenReturn(List.of());

        mockMvc.perform(get(URI + "/campaign/" + uuid))
                .andExpect(status().isOk())
                .andExpect(content().json("[]"));
    }

    @Test
    void whenUserEntersWrongCampaignUUIDType_getCountry_ReturnsBadRequest() throws Exception {
        mockMvc.perform(get(URI + "/abc"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void whenContinentIDIsValid_getCountriesByContinent_ReturnsCountries() throws Exception {
        int continentId = random.nextInt(100) + 1;
        when(countryService.getCountriesByContinentId(continentId)).thenReturn(List.of(dto));

        String response = objectMapper.writeValueAsString(List.of(dto));

        mockMvc.perform(get(URI + "/continent/" + continentId))
                .andExpect(status().isOk())
                .andExpect(content().json(response));
    }

    @Test
    void whenContinentIDIsNotValid_getCountriesByContinent_ReturnsEmptyList() throws Exception {
        int continentId = random.nextInt(100) + 1;
        when(countryService.getCountriesByContinentId(continentId)).thenReturn(List.of());

        mockMvc.perform(get(URI + "/continent/" + continentId))
                .andExpect(status().isOk())
                .andExpect(content().json("[]"));
    }

    @Test
    void whenGovernmentIDIsValid_getCountriesByGovernment_ReturnsCountries() throws Exception {
        int governmentId = random.nextInt(100) + 1;
        when(countryService.getCountriesByGovernmentId(governmentId)).thenReturn(List.of(dto));

        String response = objectMapper.writeValueAsString(List.of(dto));

        mockMvc.perform(get(URI + "/government/" + governmentId))
                .andExpect(status().isOk())
                .andExpect(content().json(response));
    }

    @Test
    void whenGovernmentIDIsNotValid_getCountriesByGovernment_ReturnsEmptyList() throws Exception {
        int governmentId = random.nextInt(100) + 1;
        when(countryService.getCountriesByGovernmentId(governmentId)).thenReturn(List.of());

        mockMvc.perform(get(URI + "/government/" + governmentId))
                .andExpect(status().isOk())
                .andExpect(content().json("[]"));
    }

    @Test
    void whenCountryIsValid_saveCountry_RespondsOkRequest() throws Exception {
        UUID uuid = UUID.randomUUID();
        CountryDTO requestDto = new CountryDTO();
        requestDto.setId(2);
        requestDto.setName("This is a name");
        requestDto.setDescription("This is a description");
        requestDto.setFk_campaign_uuid(uuid);
        requestDto.setFk_continent(4);

        String requestJson = objectMapper.writeValueAsString(requestDto);

        mockMvc.perform(post("/api/countries")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andExpect(status().isOk());

        verify(countryService, times(1)).saveCountry(any(CountryDTO.class));
    }

    @Test
    void whenCountryIsNotValid_saveCountry_RespondsBadRequest() throws Exception {
        CountryDTO invalidCountry = new CountryDTO();
        invalidCountry.setId(2);
        invalidCountry.setDescription("This is a description");
        invalidCountry.setFk_campaign_uuid(null); // Invalid UUID
        invalidCountry.setFk_continent(4);

        String requestJson = objectMapper.writeValueAsString(invalidCountry);

        doThrow(new IllegalArgumentException("Campaign UUID cannot be null or empty."))
                .when(countryService).saveCountry(any(CountryDTO.class));

        MvcResult result = mockMvc.perform(post("/api/countries")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andExpect(status().isBadRequest())
                .andReturn();

        String responseBody = result.getResponse().getContentAsString();
        ObjectMapper mapper = new ObjectMapper();
        List<String> errors = mapper.readValue(responseBody, new TypeReference<List<String>>() {
        });

        assertTrue(errors.contains("Country name cannot be empty"));
        assertTrue(errors.contains("Campaign UUID cannot be null or empty."));

        verify(countryService, times(0)).saveCountry(any(CountryDTO.class));
    }

    @Test
    void whenCountryIdIsValid_deleteCountry_RespondsOkRequest() throws Exception {
        when(countryService.getCountry(VALID_COUNTRY_ID)).thenReturn(Optional.of(dto));

        mockMvc.perform(delete(URI + "/" + VALID_COUNTRY_ID))
                .andExpect(status().isOk());

        verify(countryService, times(1)).deleteCountry(VALID_COUNTRY_ID);
    }

    @Test
    void whenCountryIdIsInvalid_deleteCountry_RespondsBadRequest() throws Exception {
        doThrow(new IllegalArgumentException("Unable to delete; This item was not found."))
                .when(countryService).deleteCountry(INVALID_COUNTRY_ID);

        mockMvc.perform(delete(URI + "/" + INVALID_COUNTRY_ID))
                .andExpect(status().isBadRequest())  // Expecting 400 Bad Request
                .andExpect(content().string("Unable to delete; This item was not found."));

        verify(countryService, times(1)).deleteCountry(INVALID_COUNTRY_ID);
    }

    @Test
    void whenCountryIdIsValid_updateCountry_RespondsOkRequest() throws Exception {
        CountryDTO updatedDto = new CountryDTO();
        updatedDto.setId(VALID_COUNTRY_ID);
        updatedDto.setDescription("Updated description");
        updatedDto.setFk_campaign_uuid(UUID.randomUUID());
        updatedDto.setFk_continent(2);

        String json = objectMapper.writeValueAsString(updatedDto);

        mockMvc.perform(put(URI + "/" + VALID_COUNTRY_ID)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk());

        verify(countryService, times(1)).updateCountry(eq(VALID_COUNTRY_ID), any(CountryDTO.class));
    }

    @Test
    void whenCountryIdIsInvalid_updateCountry_RespondsBadRequest() throws Exception {
        CountryDTO updatedDto = new CountryDTO();
        updatedDto.setId(INVALID_COUNTRY_ID);
        updatedDto.setDescription("Some update");
        updatedDto.setFk_campaign_uuid(UUID.randomUUID());
        updatedDto.setFk_continent(1);

        String json = objectMapper.writeValueAsString(updatedDto);

        doThrow(new IllegalArgumentException("Unable to update; This item was not found."))
                .when(countryService).updateCountry(eq(INVALID_COUNTRY_ID), any(CountryDTO.class));

        mockMvc.perform(put(URI + "/" + INVALID_COUNTRY_ID)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isBadRequest())
                .andExpect(content().string(containsString("Unable to update; This item was not found.")));
    }

    @Test
    void whenCountryNameIsInvalid_updateCountry_RespondsBadRequest() throws Exception {
        CountryDTO invalidDto = new CountryDTO();
        invalidDto.setId(VALID_COUNTRY_ID);
        invalidDto.setDescription("");
        invalidDto.setFk_campaign_uuid(UUID.randomUUID());
        invalidDto.setFk_continent(1);

        String json = objectMapper.writeValueAsString(invalidDto);

        when(countryService.updateCountry(eq(VALID_COUNTRY_ID), any(CountryDTO.class)))
                .thenThrow(new IllegalArgumentException("Item name cannot be null or empty."));

        mockMvc.perform(put(URI + "/" + VALID_COUNTRY_ID)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isBadRequest())
                .andExpect(content().string(containsString("Item name cannot be null or empty.")));
    }
}
