package com.mcommings.campaigner.controllers.calendar;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mcommings.campaigner.modules.calendar.controllers.WeekController;
import com.mcommings.campaigner.modules.calendar.dtos.WeekDTO;
import com.mcommings.campaigner.modules.calendar.entities.Week;
import com.mcommings.campaigner.modules.calendar.services.WeekService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.UUID;

import static com.mcommings.campaigner.enums.ErrorMessage.ID_NOT_FOUND;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(WeekController.class)
class WeekControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    WeekService weekService;

    private static final int VALID_WEEK_ID = 1;
    private static final int INVALID_WEEK_ID = 999;
    private static final String URI = "/api/weeks";
    private Week entity;
    private WeekDTO dto;
    private Random random = new Random();

    @BeforeEach
    void setUp() {
        entity = new Week();
        entity.setId(VALID_WEEK_ID);
        entity.setDescription("A week.");
        entity.setFk_campaign_uuid(UUID.randomUUID());
        entity.setWeek_number(random.nextInt(100) + 1);
        entity.setFk_month(random.nextInt(100) + 1);

        dto = new WeekDTO();
        dto.setId(entity.getId());
        dto.setDescription(entity.getDescription());
        dto.setFk_campaign_uuid(entity.getFk_campaign_uuid());
        dto.setWeek_number(entity.getWeek_number());
        dto.setFk_month(entity.getFk_month());
    }

    @Test
    void whenThereAreWeeks_getWeeks_ReturnsWeeks() throws Exception {
        when(weekService.getWeeks()).thenReturn(List.of(dto));

        String response = objectMapper.writeValueAsString(List.of(dto));

        mockMvc.perform(get(URI))
                .andExpect(status().isOk())
                .andExpect(content().json(response));
    }

    @Test
    void whenThereAreWeeks_getWeeks_ReturnsEmptyList() throws Exception {
        when(weekService.getWeeks()).thenReturn(List.of());

        mockMvc.perform(get(URI))
                .andExpect(status().isOk())
                .andExpect(content().json("[]"));
    }

    @Test
    void whenThereIsAWeek_getWeek_ReturnsWeek() throws Exception {
        when(weekService.getWeek(VALID_WEEK_ID)).thenReturn(Optional.of(dto));

        mockMvc.perform(get(URI + "/" + VALID_WEEK_ID))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(VALID_WEEK_ID))
                .andExpect(jsonPath("$.description").value(dto.getDescription()));
    }

    @Test
    void whenThereIsNotAWeek_getWeek_ThrowsIllegalArgumentException() throws Exception {
        when(weekService.getWeek(INVALID_WEEK_ID)).thenReturn(Optional.empty());

        mockMvc.perform(get(URI + "/" + INVALID_WEEK_ID))
                .andExpect(status().isBadRequest())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof IllegalArgumentException))
                .andExpect(result -> assertEquals(ID_NOT_FOUND.message, result.getResolvedException().getMessage()));
    }

    @Test
    void whenUserEntersWrongIdType_getWeek_ReturnsBadRequest() throws Exception {
        mockMvc.perform(get(URI + "/abc"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void whenServiceFails_getWeek_ReturnsInternalServerError() throws Exception {
        when(weekService.getWeek(VALID_WEEK_ID)).thenThrow(new RuntimeException("Database failure"));

        mockMvc.perform(get(URI + "/" + VALID_WEEK_ID))
                .andExpect(status().isInternalServerError())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof RuntimeException))
                .andExpect(result -> assertEquals("Database failure", result.getResolvedException().getMessage()));
    }

    @Test
    void whenServiceReturnsNull_getWeek_ReturnsInternalServerError() throws Exception {
        when(weekService.getWeek(VALID_WEEK_ID)).thenReturn(null);

        mockMvc.perform(get(URI + "/" + VALID_WEEK_ID))
                .andExpect(status().isInternalServerError());
    }

    @Test
    void whenCampaignUUIDIsValid_getWeeksByCampaignUUID_ReturnsWeeks() throws Exception {
        UUID uuid = UUID.randomUUID();
        when(weekService.getWeeksByCampaignUUID(uuid)).thenReturn(List.of(dto));

        String response = objectMapper.writeValueAsString(List.of(dto));

        mockMvc.perform(get(URI + "campaign/" + uuid))
                .andExpect(status().isOk())
                .andExpect(content().json(response));
    }

    @Test
    void whenCampaignUUIDIsValid_getWeeksByCampaignUUID_ReturnsEmptyList() throws Exception {
        UUID uuid = UUID.randomUUID();
        when(weekService.getWeeksByCampaignUUID(uuid)).thenReturn(List.of());

        mockMvc.perform(get(URI + "campaign/" + uuid))
                .andExpect(status().isOk())
                .andExpect(content().json("[]"));
    }

    @Test
    void whenUserEntersWrongCampaignUUIDType_getWeek_ReturnsBadRequest() throws Exception {
        mockMvc.perform(get(URI + "/abc"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void whenMonthIDIsValid_getWeeksByMonth_ReturnsWeeks() throws Exception {
        int monthId = random.nextInt(100) + 1;
        when(weekService.getWeeksByMonth(monthId)).thenReturn(List.of(dto));

        String response = objectMapper.writeValueAsString(List.of(dto));

        mockMvc.perform(get(URI + "/month/" + monthId))
                .andExpect(status().isOk())
                .andExpect(content().json(response));
    }

    @Test
    void whenMonthIDIsNotValid_getWeeksByMonth_ReturnsEmptyList() throws Exception {
        int monthId = random.nextInt(100) + 1;
        when(weekService.getWeeksByMonth(monthId)).thenReturn(List.of());

        mockMvc.perform(get(URI + "/month/" + monthId))
                .andExpect(status().isOk())
                .andExpect(content().json("[]"));
    }

    @Test
    void whenWeekIsValid_saveWeek_RespondsOkRequest() throws Exception {
        UUID uuid = UUID.randomUUID();
        WeekDTO requestDto = new WeekDTO();
        requestDto.setId(2);
        requestDto.setDescription("This is a description");
        requestDto.setFk_campaign_uuid(uuid);
        requestDto.setWeek_number(3);
        requestDto.setFk_month(4);

        String requestJson = objectMapper.writeValueAsString(requestDto);

        mockMvc.perform(post("/api/weeks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andExpect(status().isOk());

        verify(weekService, times(1)).saveWeek(any(WeekDTO.class));
    }

// TODO: modify once isCampaignUUIDNullOrEmpty() is created

//    @Test
//    void whenWeekIsNotValid_saveWeek_RespondsBadRequest() throws Exception {
//        Map<String, Object> invalidWeek = new HashMap<>();
//        invalidWeek.put("id", 1);
//        invalidWeek.put("name", "Week 1");
//
//        String requestJson = objectMapper.writeValueAsString(invalidWeek);
//
//        mockMvc.perform(post("/api/weeks")
//                        .contentType(MediaType.APPLICATION_JSON)
//                .content(requestJson))
//                .andExpect(status().isBadRequest());
//
//        verify(weekService, times(0)).saveWeek(any(WeekDTO.class));
//    }

    @Test
    void saveWeek() throws Exception {
    }

    @Test
    void deleteWeek() throws Exception {
    }

    @Test
    void updateWeek() throws Exception {
    }
}