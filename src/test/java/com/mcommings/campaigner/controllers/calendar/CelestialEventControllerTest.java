package com.mcommings.campaigner.controllers.calendar;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mcommings.campaigner.modules.calendar.controllers.CelestialEventController;
import com.mcommings.campaigner.modules.calendar.dtos.CelestialEventDTO;
import com.mcommings.campaigner.modules.calendar.entities.CelestialEvent;
import com.mcommings.campaigner.modules.calendar.services.CelestialEventService;
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

@WebMvcTest(CelestialEventController.class)
class CelestialEventControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    CelestialEventService celestialEventService;

    private static final int VALID_CELESTIALEVENT_ID = 1;
    private static final int INVALID_CELESTIALEVENT_ID = 999;
    private static final String URI = "/api/celestialevents";
    private CelestialEvent entity;
    private CelestialEventDTO dto;
    private Random random = new Random();

    @BeforeEach
    void setUp() {
        entity = new CelestialEvent();
        entity.setId(VALID_CELESTIALEVENT_ID);
        entity.setName("A name.");
        entity.setDescription("A description.");
        entity.setFk_campaign_uuid(UUID.randomUUID());
        entity.setFk_moon(random.nextInt(100) + 1);
        entity.setFk_sun(random.nextInt(100) + 1);
        entity.setFk_month(random.nextInt(100) + 1);
        entity.setFk_week(random.nextInt(100) + 1);
        entity.setFk_day(random.nextInt(100) + 1);
        entity.setEvent_year(random.nextInt(100) + 1);

        dto = new CelestialEventDTO();
        dto.setId(entity.getId());
        dto.setName(entity.getName());
        dto.setDescription(entity.getDescription());
        dto.setFk_campaign_uuid(entity.getFk_campaign_uuid());
        dto.setFk_moon(entity.getFk_moon());
        dto.setFk_sun(entity.getFk_sun());
        dto.setFk_month(entity.getFk_month());
        dto.setFk_week(entity.getFk_week());
        dto.setFk_day(entity.getFk_day());
        dto.setEvent_year(entity.getEvent_year());
    }

    @Test
    void whenThereAreCelestialEvents_getCelestialEvents_ReturnsCelestialEvents() throws Exception {
        when(celestialEventService.getCelestialEvents()).thenReturn(List.of(dto));

        String response = objectMapper.writeValueAsString(List.of(dto));

        mockMvc.perform(get(URI))
                .andExpect(status().isOk())
                .andExpect(content().json(response));
    }

    @Test
    void whenThereAreCelestialEvents_getCelestialEvents_ReturnsEmptyList() throws Exception {
        when(celestialEventService.getCelestialEvents()).thenReturn(List.of());

        mockMvc.perform(get(URI))
                .andExpect(status().isOk())
                .andExpect(content().json("[]"));
    }

    @Test
    void whenThereIsACelestialEvent_getCelestialEvent_ReturnsCelestialEvent() throws Exception {
        when(celestialEventService.getCelestialEvent(VALID_CELESTIALEVENT_ID)).thenReturn(Optional.of(dto));

        mockMvc.perform(get(URI + "/" + VALID_CELESTIALEVENT_ID))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(VALID_CELESTIALEVENT_ID))
                .andExpect(jsonPath("$.description").value(dto.getDescription()));
    }

    @Test
    void whenThereIsNotACelestialEvent_getCelestialEvent_ThrowsIllegalArgumentException() throws Exception {
        when(celestialEventService.getCelestialEvent(INVALID_CELESTIALEVENT_ID)).thenReturn(Optional.empty());

        mockMvc.perform(get(URI + "/" + INVALID_CELESTIALEVENT_ID))
                .andExpect(status().isBadRequest())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof IllegalArgumentException))
                .andExpect(result -> assertEquals(ID_NOT_FOUND.message, result.getResolvedException().getMessage()));
    }

    @Test
    void whenUserEntersWrongIdType_getCelestialEvent_ReturnsBadRequest() throws Exception {
        mockMvc.perform(get(URI + "/abc"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void whenServiceFails_getCelestialEvent_ReturnsInternalServerError() throws Exception {
        when(celestialEventService.getCelestialEvent(VALID_CELESTIALEVENT_ID)).thenThrow(new RuntimeException("Database failure"));

        mockMvc.perform(get(URI + "/" + VALID_CELESTIALEVENT_ID))
                .andExpect(status().isInternalServerError())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof RuntimeException))
                .andExpect(result -> assertEquals("Database failure", result.getResolvedException().getMessage()));
    }

    @Test
    void whenServiceReturnsNull_getCelestialEvent_ReturnsInternalServerError() throws Exception {
        when(celestialEventService.getCelestialEvent(VALID_CELESTIALEVENT_ID)).thenReturn(null);

        mockMvc.perform(get(URI + "/" + VALID_CELESTIALEVENT_ID))
                .andExpect(status().isInternalServerError());
    }

    @Test
    void whenCampaignUUIDIsValid_getCelestialEventsByCampaignUUID_ReturnsCelestialEvents() throws Exception {
        UUID uuid = UUID.randomUUID();
        when(celestialEventService.getCelestialEventsByCampaignUUID(uuid)).thenReturn(List.of(dto));

        String response = objectMapper.writeValueAsString(List.of(dto));

        mockMvc.perform(get(URI + "/campaign/" + uuid))
                .andExpect(status().isOk())
                .andExpect(content().json(response));
    }

    @Test
    void whenCampaignUUIDIsValid_getCelestialEventsByCampaignUUID_ReturnsEmptyList() throws Exception {
        UUID uuid = UUID.randomUUID();
        when(celestialEventService.getCelestialEventsByCampaignUUID(uuid)).thenReturn(List.of());

        mockMvc.perform(get(URI + "/campaign/" + uuid))
                .andExpect(status().isOk())
                .andExpect(content().json("[]"));
    }

    @Test
    void whenUserEntersWrongCampaignUUIDType_getCelestialEvent_ReturnsBadRequest() throws Exception {
        mockMvc.perform(get(URI + "/abc"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void whenMoonIDIsValid_getCelestialEventsByMoon_ReturnsMoons() throws Exception {
        int moonId = random.nextInt(100) + 1;
        when(celestialEventService.getCelestialEventsByMoon(moonId)).thenReturn(List.of(dto));

        String response = objectMapper.writeValueAsString(List.of(dto));

        mockMvc.perform(get(URI + "/moon/" + moonId))
                .andExpect(status().isOk())
                .andExpect(content().json(response));
    }

    @Test
    void whenMoonIDIsNotValid_getCelestialEventsByMoon_ReturnsEmptyList() throws Exception {
        int moonId = random.nextInt(100) + 1;
        when(celestialEventService.getCelestialEventsByMoon(moonId)).thenReturn(List.of());

        mockMvc.perform(get(URI + "/moon/" + moonId))
                .andExpect(status().isOk())
                .andExpect(content().json("[]"));
    }

    @Test
    void whenSunIDIsValid_getCelestialEventsBySun_ReturnsSuns() throws Exception {
        int sunId = random.nextInt(100) + 1;
        when(celestialEventService.getCelestialEventsBySun(sunId)).thenReturn(List.of(dto));

        String response = objectMapper.writeValueAsString(List.of(dto));

        mockMvc.perform(get(URI + "/sun/" + sunId))
                .andExpect(status().isOk())
                .andExpect(content().json(response));
    }

    @Test
    void whenSunIDIsNotValid_getCelestialEventsBySun_ReturnsEmptyList() throws Exception {
        int sunId = random.nextInt(100) + 1;
        when(celestialEventService.getCelestialEventsBySun(sunId)).thenReturn(List.of());

        mockMvc.perform(get(URI + "/sun/" + sunId))
                .andExpect(status().isOk())
                .andExpect(content().json("[]"));
    }

    @Test
    void whenMonthIDIsValid_getCelestialEventsByMonth_ReturnsMonths() throws Exception {
        int monthId = random.nextInt(100) + 1;
        when(celestialEventService.getCelestialEventsByMonth(monthId)).thenReturn(List.of(dto));

        String response = objectMapper.writeValueAsString(List.of(dto));

        mockMvc.perform(get(URI + "/month/" + monthId))
                .andExpect(status().isOk())
                .andExpect(content().json(response));
    }

    @Test
    void whenMonthIDIsNotValid_getCelestialEventsByMonth_ReturnsEmptyList() throws Exception {
        int monthId = random.nextInt(100) + 1;
        when(celestialEventService.getCelestialEventsByMonth(monthId)).thenReturn(List.of());

        mockMvc.perform(get(URI + "/month/" + monthId))
                .andExpect(status().isOk())
                .andExpect(content().json("[]"));
    }

    @Test
    void whenCelestialEventIsValid_saveCelestialEvent_RespondsOkRequest() throws Exception {
        UUID uuid = UUID.randomUUID();
        CelestialEventDTO requestDto = new CelestialEventDTO();
        requestDto.setId(2);
        requestDto.setName("This is a name");
        requestDto.setDescription("This is a description");
        requestDto.setFk_campaign_uuid(uuid);
        requestDto.setFk_week(4);

        String requestJson = objectMapper.writeValueAsString(requestDto);

        mockMvc.perform(post("/api/celestialevents")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andExpect(status().isOk());

        verify(celestialEventService, times(1)).saveCelestialEvent(any(CelestialEventDTO.class));
    }

    @Test
    void whenCelestialEventIsNotValid_saveCelestialEvent_RespondsBadRequest() throws Exception {
        CelestialEventDTO invalidCelestialEvent = new CelestialEventDTO();
        invalidCelestialEvent.setId(2);
        invalidCelestialEvent.setDescription("This is a description");
        invalidCelestialEvent.setFk_campaign_uuid(null); // Invalid UUID
        invalidCelestialEvent.setFk_week(4);

        String requestJson = objectMapper.writeValueAsString(invalidCelestialEvent);

        doThrow(new IllegalArgumentException("Campaign UUID cannot be null or empty."))
                .when(celestialEventService).saveCelestialEvent(any(CelestialEventDTO.class));

        MvcResult result = mockMvc.perform(post("/api/celestialevents")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andExpect(status().isBadRequest())
                .andReturn();

        String responseBody = result.getResponse().getContentAsString();
        ObjectMapper mapper = new ObjectMapper();
        List<String> errors = mapper.readValue(responseBody, new TypeReference<List<String>>() {
        });

        assertTrue(errors.contains("Celestial Event name cannot be empty"));
        assertTrue(errors.contains("Campaign UUID cannot be null or empty."));

        verify(celestialEventService, times(0)).saveCelestialEvent(any(CelestialEventDTO.class));
    }

    @Test
    void whenCelestialEventIdIsValid_deleteCelestialEvent_RespondsOkRequest() throws Exception {
        when(celestialEventService.getCelestialEvent(VALID_CELESTIALEVENT_ID)).thenReturn(Optional.of(dto));

        mockMvc.perform(delete(URI + "/" + VALID_CELESTIALEVENT_ID))
                .andExpect(status().isOk());

        verify(celestialEventService, times(1)).deleteCelestialEvent(VALID_CELESTIALEVENT_ID);
    }

    @Test
    void whenCelestialEventIdIsInvalid_deleteCelestialEvent_RespondsBadRequest() throws Exception {
        doThrow(new IllegalArgumentException("Unable to delete; This item was not found."))
                .when(celestialEventService).deleteCelestialEvent(INVALID_CELESTIALEVENT_ID);

        mockMvc.perform(delete(URI + "/" + INVALID_CELESTIALEVENT_ID))
                .andExpect(status().isBadRequest())  // Expecting 400 Bad Request
                .andExpect(content().string("Unable to delete; This item was not found."));

        verify(celestialEventService, times(1)).deleteCelestialEvent(INVALID_CELESTIALEVENT_ID);
    }

    @Test
    void whenCelestialEventIdIsValid_updateCelestialEvent_RespondsOkRequest() throws Exception {
        CelestialEventDTO updatedDto = new CelestialEventDTO();
        updatedDto.setId(VALID_CELESTIALEVENT_ID);
        updatedDto.setDescription("Updated description");
        updatedDto.setFk_campaign_uuid(UUID.randomUUID());
        updatedDto.setFk_week(2);

        String json = objectMapper.writeValueAsString(updatedDto);

        mockMvc.perform(put(URI + "/" + VALID_CELESTIALEVENT_ID)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk());

        verify(celestialEventService, times(1)).updateCelestialEvent(eq(VALID_CELESTIALEVENT_ID), any(CelestialEventDTO.class));
    }

    @Test
    void whenCelestialEventIdIsInvalid_updateCelestialEvent_RespondsBadRequest() throws Exception {
        CelestialEventDTO updatedDto = new CelestialEventDTO();
        updatedDto.setId(INVALID_CELESTIALEVENT_ID);
        updatedDto.setDescription("Some update");
        updatedDto.setFk_campaign_uuid(UUID.randomUUID());
        updatedDto.setFk_week(1);

        String json = objectMapper.writeValueAsString(updatedDto);

        doThrow(new IllegalArgumentException("Unable to update; This item was not found."))
                .when(celestialEventService).updateCelestialEvent(eq(INVALID_CELESTIALEVENT_ID), any(CelestialEventDTO.class));

        mockMvc.perform(put(URI + "/" + INVALID_CELESTIALEVENT_ID)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isBadRequest())
                .andExpect(content().string(containsString("Unable to update; This item was not found.")));
    }

    @Test
    void whenCelestialEventNameIsInvalid_updateCelestialEvent_RespondsBadRequest() throws Exception {
        CelestialEventDTO invalidDto = new CelestialEventDTO();
        invalidDto.setId(VALID_CELESTIALEVENT_ID);
        invalidDto.setDescription("");
        invalidDto.setFk_campaign_uuid(UUID.randomUUID());
        invalidDto.setFk_week(1);

        String json = objectMapper.writeValueAsString(invalidDto);

        when(celestialEventService.updateCelestialEvent(eq(VALID_CELESTIALEVENT_ID), any(CelestialEventDTO.class)))
                .thenThrow(new IllegalArgumentException("Item name cannot be null or empty."));

        mockMvc.perform(put(URI + "/" + VALID_CELESTIALEVENT_ID)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isBadRequest())
                .andExpect(content().string(containsString("Item name cannot be null or empty.")));
    }
}