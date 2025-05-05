package com.mcommings.campaigner.controllers.common;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mcommings.campaigner.modules.common.controllers.EventController;
import com.mcommings.campaigner.modules.common.dtos.EventDTO;
import com.mcommings.campaigner.modules.common.entities.Event;
import com.mcommings.campaigner.modules.common.services.EventService;
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

@WebMvcTest(EventController.class)
public class EventControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    EventService eventService;

    private static final int VALID_EVENT_ID = 1;
    private static final int INVALID_EVENT_ID = 999;
    private static final String URI = "/api/events";
    private Event entity;
    private EventDTO dto;
    private Random random = new Random();

    @BeforeEach
    void setUp() {
        entity = new Event();
        entity.setId(VALID_EVENT_ID);
        entity.setName("A name.");
        entity.setDescription("A description.");
        entity.setFk_campaign_uuid(UUID.randomUUID());
        entity.setEventYear(random.nextInt(100) + 1);
        entity.setFk_month(random.nextInt(100) + 1);
        entity.setFk_week(random.nextInt(100) + 1);
        entity.setFk_day(random.nextInt(100) + 1);
        entity.setFk_city(random.nextInt(100) + 1);
        entity.setFk_continent(random.nextInt(100) + 1);

        dto = new EventDTO();
        dto.setId(entity.getId());
        dto.setName(entity.getName());
        dto.setDescription(entity.getDescription());
        dto.setFk_campaign_uuid(entity.getFk_campaign_uuid());
        dto.setEventYear(entity.getEventYear());
        dto.setFk_month(entity.getFk_month());
        dto.setFk_week(entity.getFk_week());
        dto.setFk_day(entity.getFk_day());
        dto.setFk_city(entity.getFk_city());
        dto.setFk_continent(entity.getFk_continent());
    }

    @Test
    void whenThereAreEvents_getEvents_ReturnsEvents() throws Exception {
        when(eventService.getEvents()).thenReturn(List.of(dto));

        String response = objectMapper.writeValueAsString(List.of(dto));

        mockMvc.perform(get(URI))
                .andExpect(status().isOk())
                .andExpect(content().json(response));
    }

    @Test
    void whenThereAreEvents_getEvents_ReturnsEmptyList() throws Exception {
        when(eventService.getEvents()).thenReturn(List.of());

        mockMvc.perform(get(URI))
                .andExpect(status().isOk())
                .andExpect(content().json("[]"));
    }

    @Test
    void whenThereIsAEvent_getEvent_ReturnsEvent() throws Exception {
        when(eventService.getEvent(VALID_EVENT_ID)).thenReturn(Optional.of(dto));

        mockMvc.perform(get(URI + "/" + VALID_EVENT_ID))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(VALID_EVENT_ID))
                .andExpect(jsonPath("$.description").value(dto.getDescription()));
    }

    @Test
    void whenThereIsNotAEvent_getEvent_ThrowsIllegalArgumentException() throws Exception {
        when(eventService.getEvent(INVALID_EVENT_ID)).thenReturn(Optional.empty());

        mockMvc.perform(get(URI + "/" + INVALID_EVENT_ID))
                .andExpect(status().isBadRequest())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof IllegalArgumentException))
                .andExpect(result -> assertEquals(ID_NOT_FOUND.message, result.getResolvedException().getMessage()));
    }

    @Test
    void whenUserEntersWrongIdType_getEvent_ReturnsBadRequest() throws Exception {
        mockMvc.perform(get(URI + "/abc"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void whenServiceFails_getEvent_ReturnsInternalServerError() throws Exception {
        when(eventService.getEvent(VALID_EVENT_ID)).thenThrow(new RuntimeException("Database failure"));

        mockMvc.perform(get(URI + "/" + VALID_EVENT_ID))
                .andExpect(status().isInternalServerError())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof RuntimeException))
                .andExpect(result -> assertEquals("Database failure", result.getResolvedException().getMessage()));
    }

    @Test
    void whenServiceReturnsNull_getEvent_ReturnsInternalServerError() throws Exception {
        when(eventService.getEvent(VALID_EVENT_ID)).thenReturn(null);

        mockMvc.perform(get(URI + "/" + VALID_EVENT_ID))
                .andExpect(status().isInternalServerError());
    }

    @Test
    void whenCampaignUUIDIsValid_getEventsByCampaignUUID_ReturnsEvents() throws Exception {
        UUID uuid = UUID.randomUUID();
        when(eventService.getEventsByCampaignUUID(uuid)).thenReturn(List.of(dto));

        String response = objectMapper.writeValueAsString(List.of(dto));

        mockMvc.perform(get(URI + "/campaign/" + uuid))
                .andExpect(status().isOk())
                .andExpect(content().json(response));
    }

    @Test
    void whenCampaignUUIDIsValid_getEventsByCampaignUUID_ReturnsEmptyList() throws Exception {
        UUID uuid = UUID.randomUUID();
        when(eventService.getEventsByCampaignUUID(uuid)).thenReturn(List.of());

        mockMvc.perform(get(URI + "/campaign/" + uuid))
                .andExpect(status().isOk())
                .andExpect(content().json("[]"));
    }

    @Test
    void whenUserEntersWrongCampaignUUIDType_getEvent_ReturnsBadRequest() throws Exception {
        mockMvc.perform(get(URI + "/abc"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void whenYearIsValid_getEventsByYear_ReturnsEvents() throws Exception {
        int year = random.nextInt(100) + 1;
        when(eventService.getEventsByYear(year)).thenReturn(List.of(dto));

        String response = objectMapper.writeValueAsString(List.of(dto));

        mockMvc.perform(get(URI + "/year/" + year))
                .andExpect(status().isOk())
                .andExpect(content().json(response));
    }

    @Test
    void whenYearIsNotValid_getEventsByYear_ReturnsEmptyList() throws Exception {
        int year = random.nextInt(100) + 1;
        when(eventService.getEventsByYear(year)).thenReturn(List.of());

        mockMvc.perform(get(URI + "/year/" + year))
                .andExpect(status().isOk())
                .andExpect(content().json("[]"));
    }

    @Test
    void whenMonthIsValid_getEventsByMonth_ReturnsEvents() throws Exception {
        int month = random.nextInt(100) + 1;
        when(eventService.getEventsByMonth(month)).thenReturn(List.of(dto));

        String response = objectMapper.writeValueAsString(List.of(dto));

        mockMvc.perform(get(URI + "/month/" + month))
                .andExpect(status().isOk())
                .andExpect(content().json(response));
    }

    @Test
    void whenMonthIsNotValid_getEventsByMonth_ReturnsEmptyList() throws Exception {
        int month = random.nextInt(100) + 1;
        when(eventService.getEventsByMonth(month)).thenReturn(List.of());

        mockMvc.perform(get(URI + "/month/" + month))
                .andExpect(status().isOk())
                .andExpect(content().json("[]"));
    }

    @Test
    void whenWeekIsValid_getEventsByWeek_ReturnsEvents() throws Exception {
        int week = random.nextInt(100) + 1;
        when(eventService.getEventsByWeek(week)).thenReturn(List.of(dto));

        String response = objectMapper.writeValueAsString(List.of(dto));

        mockMvc.perform(get(URI + "/week/" + week))
                .andExpect(status().isOk())
                .andExpect(content().json(response));
    }

    @Test
    void whenWeekIsNotValid_getEventsByWeek_ReturnsEmptyList() throws Exception {
        int week = random.nextInt(100) + 1;
        when(eventService.getEventsByWeek(week)).thenReturn(List.of());

        mockMvc.perform(get(URI + "/week/" + week))
                .andExpect(status().isOk())
                .andExpect(content().json("[]"));
    }

    @Test
    void whenDayIsValid_getEventsByDay_ReturnsEvents() throws Exception {
        int day = random.nextInt(100) + 1;
        when(eventService.getEventsByDay(day)).thenReturn(List.of(dto));

        String response = objectMapper.writeValueAsString(List.of(dto));

        mockMvc.perform(get(URI + "/day/" + day))
                .andExpect(status().isOk())
                .andExpect(content().json(response));
    }

    @Test
    void whenDayIsNotValid_getEventsByDay_ReturnsEmptyList() throws Exception {
        int day = random.nextInt(100) + 1;
        when(eventService.getEventsByDay(day)).thenReturn(List.of());

        mockMvc.perform(get(URI + "/day/" + day))
                .andExpect(status().isOk())
                .andExpect(content().json("[]"));
    }

    @Test
    void whenCityIsValid_getEventsByCity_ReturnsEvents() throws Exception {
        int city = random.nextInt(100) + 1;
        when(eventService.getEventsByCity(city)).thenReturn(List.of(dto));

        String response = objectMapper.writeValueAsString(List.of(dto));

        mockMvc.perform(get(URI + "/city/" + city))
                .andExpect(status().isOk())
                .andExpect(content().json(response));
    }

    @Test
    void whenCityIsNotValid_getEventsByCity_ReturnsEmptyList() throws Exception {
        int city = random.nextInt(100) + 1;
        when(eventService.getEventsByCity(city)).thenReturn(List.of());

        mockMvc.perform(get(URI + "/city/" + city))
                .andExpect(status().isOk())
                .andExpect(content().json("[]"));
    }

    @Test
    void whenContinentIsValid_getEventsByContinent_ReturnsEvents() throws Exception {
        int continent = random.nextInt(100) + 1;
        when(eventService.getEventsByContinent(continent)).thenReturn(List.of(dto));

        String response = objectMapper.writeValueAsString(List.of(dto));

        mockMvc.perform(get(URI + "/continent/" + continent))
                .andExpect(status().isOk())
                .andExpect(content().json(response));
    }

    @Test
    void whenContinentIsNotValid_getEventsByContinent_ReturnsEmptyList() throws Exception {
        int continent = random.nextInt(100) + 1;
        when(eventService.getEventsByContinent(continent)).thenReturn(List.of());

        mockMvc.perform(get(URI + "/continent/" + continent))
                .andExpect(status().isOk())
                .andExpect(content().json("[]"));
    }

    @Test
    void whenCountryIsValid_getEventsByCountry_ReturnsEvents() throws Exception {
        int country = random.nextInt(100) + 1;
        when(eventService.getEventsByCountry(country)).thenReturn(List.of(dto));

        String response = objectMapper.writeValueAsString(List.of(dto));

        mockMvc.perform(get(URI + "/country/" + country))
                .andExpect(status().isOk())
                .andExpect(content().json(response));
    }

    @Test
    void whenCountryIsNotValid_getEventsByCountry_ReturnsEmptyList() throws Exception {
        int country = random.nextInt(100) + 1;
        when(eventService.getEventsByCountry(country)).thenReturn(List.of());

        mockMvc.perform(get(URI + "/country/" + country))
                .andExpect(status().isOk())
                .andExpect(content().json("[]"));
    }

    @Test
    void whenEventIsValid_saveEvent_RespondsOkRequest() throws Exception {
        UUID uuid = UUID.randomUUID();
        EventDTO requestDto = new EventDTO();
        requestDto.setId(2);
        requestDto.setName("This is a name");
        requestDto.setDescription("This is a description");
        requestDto.setFk_campaign_uuid(uuid);
        requestDto.setFk_continent(4);

        String requestJson = objectMapper.writeValueAsString(requestDto);

        mockMvc.perform(post("/api/events")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andExpect(status().isOk());

        verify(eventService, times(1)).saveEvent(any(EventDTO.class));
    }

    @Test
    void whenEventIsNotValid_saveEvent_RespondsBadRequest() throws Exception {
        EventDTO invalidEvent = new EventDTO();
        invalidEvent.setId(2);
        invalidEvent.setDescription("This is a description");
        invalidEvent.setFk_campaign_uuid(null); // Invalid UUID
        invalidEvent.setFk_continent(4);

        String requestJson = objectMapper.writeValueAsString(invalidEvent);

        doThrow(new IllegalArgumentException("Campaign UUID cannot be null or empty."))
                .when(eventService).saveEvent(any(EventDTO.class));

        MvcResult result = mockMvc.perform(post("/api/events")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andExpect(status().isBadRequest())
                .andReturn();

        String responseBody = result.getResponse().getContentAsString();
        ObjectMapper mapper = new ObjectMapper();
        List<String> errors = mapper.readValue(responseBody, new TypeReference<List<String>>() {
        });

        assertTrue(errors.contains("Event name cannot be empty"));
        assertTrue(errors.contains("Campaign UUID cannot be null or empty."));

        verify(eventService, times(0)).saveEvent(any(EventDTO.class));
    }

    @Test
    void whenEventIdIsValid_deleteEvent_RespondsOkRequest() throws Exception {
        when(eventService.getEvent(VALID_EVENT_ID)).thenReturn(Optional.of(dto));

        mockMvc.perform(delete(URI + "/" + VALID_EVENT_ID))
                .andExpect(status().isOk());

        verify(eventService, times(1)).deleteEvent(VALID_EVENT_ID);
    }

    @Test
    void whenEventIdIsInvalid_deleteEvent_RespondsBadRequest() throws Exception {
        doThrow(new IllegalArgumentException("Unable to delete; This item was not found."))
                .when(eventService).deleteEvent(INVALID_EVENT_ID);

        mockMvc.perform(delete(URI + "/" + INVALID_EVENT_ID))
                .andExpect(status().isBadRequest())  // Expecting 400 Bad Request
                .andExpect(content().string("Unable to delete; This item was not found."));

        verify(eventService, times(1)).deleteEvent(INVALID_EVENT_ID);
    }

    @Test
    void whenEventIdIsValid_updateEvent_RespondsOkRequest() throws Exception {
        EventDTO updatedDto = new EventDTO();
        updatedDto.setId(VALID_EVENT_ID);
        updatedDto.setDescription("Updated description");
        updatedDto.setFk_campaign_uuid(UUID.randomUUID());
        updatedDto.setFk_continent(2);

        String json = objectMapper.writeValueAsString(updatedDto);

        mockMvc.perform(put(URI + "/" + VALID_EVENT_ID)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk());

        verify(eventService, times(1)).updateEvent(eq(VALID_EVENT_ID), any(EventDTO.class));
    }

    @Test
    void whenEventIdIsInvalid_updateEvent_RespondsBadRequest() throws Exception {
        EventDTO updatedDto = new EventDTO();
        updatedDto.setId(INVALID_EVENT_ID);
        updatedDto.setDescription("Some update");
        updatedDto.setFk_campaign_uuid(UUID.randomUUID());
        updatedDto.setFk_continent(1);

        String json = objectMapper.writeValueAsString(updatedDto);

        doThrow(new IllegalArgumentException("Unable to update; This item was not found."))
                .when(eventService).updateEvent(eq(INVALID_EVENT_ID), any(EventDTO.class));

        mockMvc.perform(put(URI + "/" + INVALID_EVENT_ID)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isBadRequest())
                .andExpect(content().string(containsString("Unable to update; This item was not found.")));
    }

    @Test
    void whenEventNameIsInvalid_updateEvent_RespondsBadRequest() throws Exception {
        EventDTO invalidDto = new EventDTO();
        invalidDto.setId(VALID_EVENT_ID);
        invalidDto.setDescription("");
        invalidDto.setFk_campaign_uuid(UUID.randomUUID());
        invalidDto.setFk_continent(1);

        String json = objectMapper.writeValueAsString(invalidDto);

        when(eventService.updateEvent(eq(VALID_EVENT_ID), any(EventDTO.class)))
                .thenThrow(new IllegalArgumentException("Item name cannot be null or empty."));

        mockMvc.perform(put(URI + "/" + VALID_EVENT_ID)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isBadRequest())
                .andExpect(content().string(containsString("Item name cannot be null or empty.")));
    }

}
