package com.mcommings.campaigner.controllers.calendar;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mcommings.campaigner.modules.calendar.controllers.MonthController;
import com.mcommings.campaigner.modules.calendar.dtos.MonthDTO;
import com.mcommings.campaigner.modules.calendar.entities.Month;
import com.mcommings.campaigner.modules.calendar.services.MonthService;
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
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(MonthController.class)
class MonthControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    MonthService monthService;

    private static final int VALID_MONTH_ID = 1;
    private static final int INVALID_MONTH_ID = 999;
    private static final String URI = "/api/months";
    private Month entity;
    private MonthDTO dto;
    private Random random = new Random();

    @BeforeEach
    void setUp() {
        entity = new Month();
        entity.setId(VALID_MONTH_ID);
        entity.setName("A name.");
        entity.setDescription("A description.");
        entity.setFk_campaign_uuid(UUID.randomUUID());
        entity.setSeason("A season.");

        dto = new MonthDTO();
        dto.setId(entity.getId());
        dto.setName(entity.getName());
        dto.setDescription(entity.getDescription());
        dto.setFk_campaign_uuid(entity.getFk_campaign_uuid());
        dto.setSeason(entity.getSeason());
    }

    @Test
    void whenThereAreMonths_getMonths_ReturnsMonths() throws Exception {
        when(monthService.getMonths()).thenReturn(List.of(dto));

        String response = objectMapper.writeValueAsString(List.of(dto));

        mockMvc.perform(get(URI))
                .andExpect(status().isOk())
                .andExpect(content().json(response));
    }

    @Test
    void whenThereAreMonths_getMonths_ReturnsEmptyList() throws Exception {
        when(monthService.getMonths()).thenReturn(List.of());

        mockMvc.perform(get(URI))
                .andExpect(status().isOk())
                .andExpect(content().json("[]"));
    }

    @Test
    void whenThereIsAMonth_getMonth_ReturnsMonth() throws Exception {
        when(monthService.getMonth(VALID_MONTH_ID)).thenReturn(Optional.of(dto));

        mockMvc.perform(get(URI + "/" + VALID_MONTH_ID))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(VALID_MONTH_ID))
                .andExpect(jsonPath("$.description").value(dto.getDescription()));
    }

    @Test
    void whenThereIsNotAMonth_getMonth_ThrowsIllegalArgumentException() throws Exception {
        when(monthService.getMonth(INVALID_MONTH_ID)).thenReturn(Optional.empty());

        mockMvc.perform(get(URI + "/" + INVALID_MONTH_ID))
                .andExpect(status().isBadRequest())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof IllegalArgumentException))
                .andExpect(result -> assertEquals(ID_NOT_FOUND.message, result.getResolvedException().getMessage()));
    }

    @Test
    void whenUserEntersWrongIdType_getMonth_ReturnsBadRequest() throws Exception {
        mockMvc.perform(get(URI + "/abc"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void whenServiceFails_getMonth_ReturnsInternalServerError() throws Exception {
        when(monthService.getMonth(VALID_MONTH_ID)).thenThrow(new RuntimeException("Database failure"));

        mockMvc.perform(get(URI + "/" + VALID_MONTH_ID))
                .andExpect(status().isInternalServerError())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof RuntimeException))
                .andExpect(result -> assertEquals("Database failure", result.getResolvedException().getMessage()));
    }

    @Test
    void whenServiceReturnsNull_getMonth_ReturnsInternalServerError() throws Exception {
        when(monthService.getMonth(VALID_MONTH_ID)).thenReturn(null);

        mockMvc.perform(get(URI + "/" + VALID_MONTH_ID))
                .andExpect(status().isInternalServerError());
    }

    @Test
    void whenCampaignUUIDIsValid_getMonthsByCampaignUUID_ReturnsMonths() throws Exception {
        UUID uuid = UUID.randomUUID();
        when(monthService.getMonthsByCampaignUUID(uuid)).thenReturn(List.of(dto));

        String response = objectMapper.writeValueAsString(List.of(dto));

        mockMvc.perform(get(URI + "/campaign/" + uuid))
                .andExpect(status().isOk())
                .andExpect(content().json(response));
    }

    @Test
    void whenCampaignUUIDIsValid_getMonthsByCampaignUUID_ReturnsEmptyList() throws Exception {
        UUID uuid = UUID.randomUUID();
        when(monthService.getMonthsByCampaignUUID(uuid)).thenReturn(List.of());

        mockMvc.perform(get(URI + "/campaign/" + uuid))
                .andExpect(status().isOk())
                .andExpect(content().json("[]"));
    }

    @Test
    void whenUserEntersWrongCampaignUUIDType_getMonth_ReturnsBadRequest() throws Exception {
        mockMvc.perform(get(URI + "/abc"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void whenMonthIsValid_saveMonth_RespondsOkRequest() throws Exception {
        UUID uuid = UUID.randomUUID();
        MonthDTO requestDto = new MonthDTO();
        requestDto.setId(2);
        requestDto.setName("This is a name");
        requestDto.setDescription("This is a description");
        requestDto.setFk_campaign_uuid(uuid);

        String requestJson = objectMapper.writeValueAsString(requestDto);

        mockMvc.perform(post("/api/months")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andExpect(status().isOk());

        verify(monthService, times(1)).saveMonth(any(MonthDTO.class));
    }

    @Test
    void whenMonthIsNotValid_saveMonth_RespondsBadRequest() throws Exception {
        MonthDTO invalidMonth = new MonthDTO();
        invalidMonth.setId(2);
        invalidMonth.setDescription("This is a description");
        invalidMonth.setFk_campaign_uuid(null); // Invalid UUID

        String requestJson = objectMapper.writeValueAsString(invalidMonth);

        doThrow(new IllegalArgumentException("Campaign UUID cannot be null or empty."))
                .when(monthService).saveMonth(any(MonthDTO.class));

        MvcResult result = mockMvc.perform(post("/api/months")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andExpect(status().isBadRequest())
                .andReturn();

        String responseBody = result.getResponse().getContentAsString();
        ObjectMapper mapper = new ObjectMapper();
        List<String> errors = mapper.readValue(responseBody, new TypeReference<List<String>>() {
        });

        assertTrue(errors.contains("Month name cannot be empty"));
        assertTrue(errors.contains("Campaign UUID cannot be null or empty."));

        verify(monthService, times(0)).saveMonth(any(MonthDTO.class));
    }

    @Test
    void whenMonthIdIsValid_deleteMonth_RespondsOkRequest() throws Exception {
        when(monthService.getMonth(VALID_MONTH_ID)).thenReturn(Optional.of(dto));

        mockMvc.perform(delete(URI + "/" + VALID_MONTH_ID))
                .andExpect(status().isOk());

        verify(monthService, times(1)).deleteMonth(VALID_MONTH_ID);
    }

    @Test
    void whenMonthIdIsInvalid_deleteMonth_RespondsBadRequest() throws Exception {
        doThrow(new IllegalArgumentException("Unable to delete; This item was not found."))
                .when(monthService).deleteMonth(INVALID_MONTH_ID);

        mockMvc.perform(delete(URI + "/" + INVALID_MONTH_ID))
                .andExpect(status().isBadRequest())  // Expecting 400 Bad Request
                .andExpect(content().string("Unable to delete; This item was not found."));

        verify(monthService, times(1)).deleteMonth(INVALID_MONTH_ID);
    }

    @Test
    void whenMonthIdIsValid_updateMonth_RespondsOkRequest() throws Exception {
        MonthDTO updatedDto = new MonthDTO();
        updatedDto.setId(VALID_MONTH_ID);
        updatedDto.setDescription("Updated description");
        updatedDto.setFk_campaign_uuid(UUID.randomUUID());

        String json = objectMapper.writeValueAsString(updatedDto);

        mockMvc.perform(put(URI + "/" + VALID_MONTH_ID)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk());

        verify(monthService, times(1)).updateMonth(eq(VALID_MONTH_ID), any(MonthDTO.class));
    }

    @Test
    void whenMonthIdIsInvalid_updateMonth_RespondsBadRequest() throws Exception {
        MonthDTO updatedDto = new MonthDTO();
        updatedDto.setId(INVALID_MONTH_ID);
        updatedDto.setDescription("Some update");
        updatedDto.setFk_campaign_uuid(UUID.randomUUID());

        String json = objectMapper.writeValueAsString(updatedDto);

        doThrow(new IllegalArgumentException("Unable to update; This item was not found."))
                .when(monthService).updateMonth(eq(INVALID_MONTH_ID), any(MonthDTO.class));

        mockMvc.perform(put(URI + "/" + INVALID_MONTH_ID)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isBadRequest())
                .andExpect(content().string(containsString("Unable to update; This item was not found.")));
    }

    @Test
    void whenMonthNameIsInvalid_updateMonth_RespondsBadRequest() throws Exception {
        MonthDTO invalidDto = new MonthDTO();
        invalidDto.setId(VALID_MONTH_ID);
        invalidDto.setDescription("");
        invalidDto.setFk_campaign_uuid(UUID.randomUUID());

        String json = objectMapper.writeValueAsString(invalidDto);

        when(monthService.updateMonth(eq(VALID_MONTH_ID), any(MonthDTO.class)))
                .thenThrow(new IllegalArgumentException("Item name cannot be null or empty."));

        mockMvc.perform(put(URI + "/" + VALID_MONTH_ID)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isBadRequest())
                .andExpect(content().string(containsString("Item name cannot be null or empty.")));
    }
}