package com.mcommings.campaigner.controllers.calendar;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mcommings.campaigner.modules.calendar.controllers.DayController;
import com.mcommings.campaigner.modules.calendar.dtos.DayDTO;
import com.mcommings.campaigner.modules.calendar.entities.Day;
import com.mcommings.campaigner.modules.calendar.services.DayService;
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
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(DayController.class)
class DayControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    DayService dayService;

    private static final int VALID_DAY_ID = 1;
    private static final int INVALID_DAY_ID = 999;
    private static final String URI = "/api/days";
    private Day entity;
    private DayDTO dto;
    private Random random = new Random();

    @BeforeEach
    void setUp() {
        entity = new Day();
        entity.setId(VALID_DAY_ID);
        entity.setName("A name.");
        entity.setDescription("A description.");
        entity.setFk_campaign_uuid(UUID.randomUUID());
        entity.setFk_week(random.nextInt(100) + 1);

        dto = new DayDTO();
        dto.setId(entity.getId());
        dto.setName(entity.getName());
        dto.setDescription(entity.getDescription());
        dto.setFk_campaign_uuid(entity.getFk_campaign_uuid());
        dto.setFk_week(entity.getFk_week());
    }

    @Test
    void whenThereAreDays_getDays_ReturnsDays() throws Exception {
        when(dayService.getDays()).thenReturn(List.of(dto));

        String response = objectMapper.writeValueAsString(List.of(dto));

        mockMvc.perform(get(URI))
                .andExpect(status().isOk())
                .andExpect(content().json(response));
    }

    @Test
    void whenThereAreDays_getDays_ReturnsEmptyList() throws Exception {
        when(dayService.getDays()).thenReturn(List.of());

        mockMvc.perform(get(URI))
                .andExpect(status().isOk())
                .andExpect(content().json("[]"));
    }

    @Test
    void whenThereIsADay_getDay_ReturnsDay() throws Exception {
        when(dayService.getDay(VALID_DAY_ID)).thenReturn(Optional.of(dto));

        mockMvc.perform(get(URI + "/" + VALID_DAY_ID))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(VALID_DAY_ID))
                .andExpect(jsonPath("$.description").value(dto.getDescription()));
    }

    @Test
    void whenThereIsNotADay_getDay_ThrowsIllegalArgumentException() throws Exception {
        when(dayService.getDay(INVALID_DAY_ID)).thenReturn(Optional.empty());

        mockMvc.perform(get(URI + "/" + INVALID_DAY_ID))
                .andExpect(status().isBadRequest())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof IllegalArgumentException))
                .andExpect(result -> assertEquals(ID_NOT_FOUND.message, result.getResolvedException().getMessage()));
    }

    @Test
    void whenUserEntersWrongIdType_getDay_ReturnsBadRequest() throws Exception {
        mockMvc.perform(get(URI + "/abc"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void whenServiceFails_getDay_ReturnsInternalServerError() throws Exception {
        when(dayService.getDay(VALID_DAY_ID)).thenThrow(new RuntimeException("Database failure"));

        mockMvc.perform(get(URI + "/" + VALID_DAY_ID))
                .andExpect(status().isInternalServerError())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof RuntimeException))
                .andExpect(result -> assertEquals("Database failure", result.getResolvedException().getMessage()));
    }

    @Test
    void whenServiceReturnsNull_getDay_ReturnsInternalServerError() throws Exception {
        when(dayService.getDay(VALID_DAY_ID)).thenReturn(null);

        mockMvc.perform(get(URI + "/" + VALID_DAY_ID))
                .andExpect(status().isInternalServerError());
    }

    @Test
    void whenCampaignUUIDIsValid_getDaysByCampaignUUID_ReturnsDays() throws Exception {
        UUID uuid = UUID.randomUUID();
        when(dayService.getDaysByCampaignUUID(uuid)).thenReturn(List.of(dto));

        String response = objectMapper.writeValueAsString(List.of(dto));

        mockMvc.perform(get(URI + "/campaign/" + uuid))
                .andExpect(status().isOk())
                .andExpect(content().json(response));
    }

    @Test
    void whenCampaignUUIDIsValid_getDaysByCampaignUUID_ReturnsEmptyList() throws Exception {
        UUID uuid = UUID.randomUUID();
        when(dayService.getDaysByCampaignUUID(uuid)).thenReturn(List.of());

        mockMvc.perform(get(URI + "/campaign/" + uuid))
                .andExpect(status().isOk())
                .andExpect(content().json("[]"));
    }

    @Test
    void whenUserEntersWrongCampaignUUIDType_getDay_ReturnsBadRequest() throws Exception {
        mockMvc.perform(get(URI + "/abc"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void whenWeekIDIsValid_getDaysByWeek_ReturnsDays() throws Exception {
        int weekId = random.nextInt(100) + 1;
        when(dayService.getDaysByWeek(weekId)).thenReturn(List.of(dto));

        String response = objectMapper.writeValueAsString(List.of(dto));

        mockMvc.perform(get(URI + "/week/" + weekId))
                .andExpect(status().isOk())
                .andExpect(content().json(response));
    }

    @Test
    void whenWeekIDIsNotValid_getDaysByWeek_ReturnsEmptyList() throws Exception {
        int weekId = random.nextInt(100) + 1;
        when(dayService.getDaysByWeek(weekId)).thenReturn(List.of());

        mockMvc.perform(get(URI + "/week/" + weekId))
                .andExpect(status().isOk())
                .andExpect(content().json("[]"));
    }

    @Test
    void whenDayIsValid_saveDay_RespondsOkRequest() throws Exception {
        UUID uuid = UUID.randomUUID();
        DayDTO requestDto = new DayDTO();
        requestDto.setId(2);
        requestDto.setName("This is a name");
        requestDto.setDescription("This is a description");
        requestDto.setFk_campaign_uuid(uuid);
        requestDto.setFk_week(4);

        String requestJson = objectMapper.writeValueAsString(requestDto);

        mockMvc.perform(post("/api/days")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andExpect(status().isOk());

        verify(dayService, times(1)).saveDay(any(DayDTO.class));
    }

    @Test
    void whenDayIsNotValid_saveDay_RespondsBadRequest() throws Exception {
        DayDTO invalidDay = new DayDTO();
        invalidDay.setId(2);
        invalidDay.setDescription("This is a description");
        invalidDay.setFk_campaign_uuid(null); // Invalid UUID
        invalidDay.setFk_week(4);

        String requestJson = objectMapper.writeValueAsString(invalidDay);

        doThrow(new IllegalArgumentException("Campaign UUID cannot be null or empty."))
                .when(dayService).saveDay(any(DayDTO.class));

        MvcResult result = mockMvc.perform(post("/api/days")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andExpect(status().isBadRequest())
                .andReturn();

        String responseBody = result.getResponse().getContentAsString();
        ObjectMapper mapper = new ObjectMapper();
        List<String> errors = mapper.readValue(responseBody, new TypeReference<List<String>>() {
        });

        assertTrue(errors.contains("Day name cannot be empty"));
        assertTrue(errors.contains("Campaign UUID cannot be null or empty."));

        verify(dayService, times(0)).saveDay(any(DayDTO.class));
    }

    @Test
    void whenDayIdIsValid_deleteDay_RespondsOkRequest() throws Exception {
        when(dayService.getDay(VALID_DAY_ID)).thenReturn(Optional.of(dto));

        mockMvc.perform(delete(URI + "/" + VALID_DAY_ID))
                .andExpect(status().isOk());

        verify(dayService, times(1)).deleteDay(VALID_DAY_ID);
    }

    @Test
    void whenDayIdIsInvalid_deleteDay_RespondsBadRequest() throws Exception {
        doThrow(new IllegalArgumentException("Unable to delete; This item was not found."))
                .when(dayService).deleteDay(INVALID_DAY_ID);

        mockMvc.perform(delete(URI + "/" + INVALID_DAY_ID))
                .andExpect(status().isBadRequest())  // Expecting 400 Bad Request
                .andExpect(content().string("Unable to delete; This item was not found."));

        verify(dayService, times(1)).deleteDay(INVALID_DAY_ID);
    }

    @Test
    void whenDayIdIsValid_updateDay_RespondsOkRequest() throws Exception {
        DayDTO updatedDto = new DayDTO();
        updatedDto.setId(VALID_DAY_ID);
        updatedDto.setDescription("Updated description");
        updatedDto.setFk_campaign_uuid(UUID.randomUUID());
        updatedDto.setFk_week(2);

        String json = objectMapper.writeValueAsString(updatedDto);

        mockMvc.perform(put(URI + "/" + VALID_DAY_ID)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk());

        verify(dayService, times(1)).updateDay(eq(VALID_DAY_ID), any(DayDTO.class));
    }

    @Test
    void whenDayIdIsInvalid_updateDay_RespondsBadRequest() throws Exception {
        DayDTO updatedDto = new DayDTO();
        updatedDto.setId(INVALID_DAY_ID);
        updatedDto.setDescription("Some update");
        updatedDto.setFk_campaign_uuid(UUID.randomUUID());
        updatedDto.setFk_week(1);

        String json = objectMapper.writeValueAsString(updatedDto);

        doThrow(new IllegalArgumentException("Unable to update; This item was not found."))
                .when(dayService).updateDay(eq(INVALID_DAY_ID), any(DayDTO.class));

        mockMvc.perform(put(URI + "/" + INVALID_DAY_ID)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isBadRequest())
                .andExpect(content().string(containsString("Unable to update; This item was not found.")));
    }

    @Test
    void whenDayNameIsInvalid_updateDay_RespondsBadRequest() throws Exception {
        DayDTO invalidDto = new DayDTO();
        invalidDto.setId(VALID_DAY_ID);
        invalidDto.setDescription("");
        invalidDto.setFk_campaign_uuid(UUID.randomUUID());
        invalidDto.setFk_week(1);

        String json = objectMapper.writeValueAsString(invalidDto);

        when(dayService.updateDay(eq(VALID_DAY_ID), any(DayDTO.class)))
                .thenThrow(new IllegalArgumentException("Item name cannot be null or empty."));

        mockMvc.perform(put(URI + "/" + VALID_DAY_ID)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isBadRequest())
                .andExpect(content().string(containsString("Item name cannot be null or empty.")));
    }
}