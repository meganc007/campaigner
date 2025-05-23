package com.mcommings.campaigner.controllers.people;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mcommings.campaigner.modules.people.controllers.EventPlacePersonController;
import com.mcommings.campaigner.modules.people.dtos.EventPlacePersonDTO;
import com.mcommings.campaigner.modules.people.entities.EventPlacePerson;
import com.mcommings.campaigner.modules.people.services.EventPlacePersonService;
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

@WebMvcTest(EventPlacePersonController.class)
public class EventPlacePersonControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    EventPlacePersonService eventPlacePersonService;

    private static final int VALID_EPP_ID = 1;
    private static final int INVALID_EPP_ID = 999;
    private static final String URI = "/api/eventsPlacesPeople";
    private EventPlacePerson entity;
    private EventPlacePersonDTO dto;
    private Random random = new Random();

    @BeforeEach
    void setUp() {
        entity = new EventPlacePerson();
        entity.setId(1);
        entity.setFk_campaign_uuid(UUID.randomUUID());
        entity.setFk_event(random.nextInt(100) + 1);
        entity.setFk_person(random.nextInt(100) + 1);
        entity.setFk_place(random.nextInt(100) + 1);

        dto = new EventPlacePersonDTO();
        dto.setId(entity.getId());
        dto.setFk_campaign_uuid(entity.getFk_campaign_uuid());
        dto.setFk_event(entity.getFk_event());
        dto.setFk_person(entity.getFk_person());
        dto.setFk_place(entity.getFk_place());
    }

    @Test
    void whenThereAreEventsPlacesPeople_getEventsPlacesPeople_ReturnsEventsPlacesPeople() throws Exception {
        when(eventPlacePersonService.getEventsPlacesPeople()).thenReturn(List.of(dto));

        String response = objectMapper.writeValueAsString(List.of(dto));

        mockMvc.perform(get(URI))
                .andExpect(status().isOk())
                .andExpect(content().json(response));
    }

    @Test
    void whenThereAreEventsPlacesPeople_getEventsPlacesPeople_ReturnsEmptyList() throws Exception {
        when(eventPlacePersonService.getEventsPlacesPeople()).thenReturn(List.of());

        mockMvc.perform(get(URI))
                .andExpect(status().isOk())
                .andExpect(content().json("[]"));
    }

    @Test
    void whenThereIsAEventPlacePerson_getEventPlacePerson_ReturnsEventPlacePerson() throws Exception {
        when(eventPlacePersonService.getEventPlacePerson(VALID_EPP_ID)).thenReturn(Optional.of(dto));

        mockMvc.perform(get(URI + "/" + VALID_EPP_ID))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(VALID_EPP_ID))
                .andExpect(jsonPath("$.fk_event").value(dto.getFk_event()));
    }

    @Test
    void whenThereIsNotAEventPlacePerson_getEventPlacePerson_ThrowsIllegalArgumentException() throws Exception {
        when(eventPlacePersonService.getEventPlacePerson(INVALID_EPP_ID)).thenReturn(Optional.empty());

        mockMvc.perform(get(URI + "/" + INVALID_EPP_ID))
                .andExpect(status().isBadRequest())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof IllegalArgumentException))
                .andExpect(result -> assertEquals(ID_NOT_FOUND.message, result.getResolvedException().getMessage()));
    }

    @Test
    void whenUserEntersWrongIdType_getEventPlacePerson_ReturnsBadRequest() throws Exception {
        mockMvc.perform(get(URI + "/abc"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void whenServiceFails_getEventPlacePerson_ReturnsInternalServerError() throws Exception {
        when(eventPlacePersonService.getEventPlacePerson(VALID_EPP_ID)).thenThrow(new RuntimeException("Database failure"));

        mockMvc.perform(get(URI + "/" + VALID_EPP_ID))
                .andExpect(status().isInternalServerError())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof RuntimeException))
                .andExpect(result -> assertEquals("Database failure", result.getResolvedException().getMessage()));
    }

    @Test
    void whenServiceReturnsNull_getEventPlacePerson_ReturnsInternalServerError() throws Exception {
        when(eventPlacePersonService.getEventPlacePerson(VALID_EPP_ID)).thenReturn(null);

        mockMvc.perform(get(URI + "/" + VALID_EPP_ID))
                .andExpect(status().isInternalServerError());
    }

    @Test
    void whenCampaignUUIDIsValid_getEventsPlacesPeopleByCampaignUUID_ReturnsEventsPlacesPeople() throws Exception {
        UUID uuid = UUID.randomUUID();
        when(eventPlacePersonService.getEventsPlacesPeopleByCampaignUUID(uuid)).thenReturn(List.of(dto));

        String response = objectMapper.writeValueAsString(List.of(dto));

        mockMvc.perform(get(URI + "/campaign/" + uuid))
                .andExpect(status().isOk())
                .andExpect(content().json(response));
    }

    @Test
    void whenCampaignUUIDIsValid_getEventsPlacesPeopleByCampaignUUID_ReturnsEmptyList() throws Exception {
        UUID uuid = UUID.randomUUID();
        when(eventPlacePersonService.getEventsPlacesPeopleByCampaignUUID(uuid)).thenReturn(List.of());

        mockMvc.perform(get(URI + "/campaign/" + uuid))
                .andExpect(status().isOk())
                .andExpect(content().json("[]"));
    }

    @Test
    void whenUserEntersWrongCampaignUUIDType_getEventPlacePerson_ReturnsBadRequest() throws Exception {
        mockMvc.perform(get(URI + "/abc"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void whenEventIDIsValid_getEventsPlacesPeopleByEvent_ReturnsEventsPlacesPeople() throws Exception {
        int eventId = random.nextInt(100) + 1;
        when(eventPlacePersonService.getEventsPlacesPeopleByEvent(eventId)).thenReturn(List.of(dto));

        String response = objectMapper.writeValueAsString(List.of(dto));

        mockMvc.perform(get(URI + "/event/" + eventId))
                .andExpect(status().isOk())
                .andExpect(content().json(response));
    }

    @Test
    void whenEventIDIsNotValid_getEventsPlacesPeopleByEvent_ReturnsEmptyList() throws Exception {
        int eventId = random.nextInt(100) + 1;
        when(eventPlacePersonService.getEventsPlacesPeopleByEvent(eventId)).thenReturn(List.of());

        mockMvc.perform(get(URI + "/event/" + eventId))
                .andExpect(status().isOk())
                .andExpect(content().json("[]"));
    }

    @Test
    void whenPlaceIDIsValid_getEventsPlacesPeopleByPlace_ReturnsEventsPlacesPeople() throws Exception {
        int placeId = random.nextInt(100) + 1;
        when(eventPlacePersonService.getEventsPlacesPeopleByPlace(placeId)).thenReturn(List.of(dto));

        String response = objectMapper.writeValueAsString(List.of(dto));

        mockMvc.perform(get(URI + "/place/" + placeId))
                .andExpect(status().isOk())
                .andExpect(content().json(response));
    }

    @Test
    void whenPlaceIDIsNotValid_getEventsPlacesPeopleByPlace_ReturnsEmptyList() throws Exception {
        int placeId = random.nextInt(100) + 1;
        when(eventPlacePersonService.getEventsPlacesPeopleByPlace(placeId)).thenReturn(List.of());

        mockMvc.perform(get(URI + "/place/" + placeId))
                .andExpect(status().isOk())
                .andExpect(content().json("[]"));
    }

    @Test
    void whenPersonIDIsValid_getEventsPlacesPeopleByPerson_ReturnsEventsPlacesPeople() throws Exception {
        int personId = random.nextInt(100) + 1;
        when(eventPlacePersonService.getEventsPlacesPeopleByPerson(personId)).thenReturn(List.of(dto));

        String response = objectMapper.writeValueAsString(List.of(dto));

        mockMvc.perform(get(URI + "/person/" + personId))
                .andExpect(status().isOk())
                .andExpect(content().json(response));
    }

    @Test
    void whenPersonIDIsNotValid_getEventsPlacesPeopleByPerson_ReturnsEmptyList() throws Exception {
        int personId = random.nextInt(100) + 1;
        when(eventPlacePersonService.getEventsPlacesPeopleByPerson(personId)).thenReturn(List.of());

        mockMvc.perform(get(URI + "/person/" + personId))
                .andExpect(status().isOk())
                .andExpect(content().json("[]"));
    }

    @Test
    void whenEventPlacePersonIsValid_saveEventPlacePerson_RespondsOkRequest() throws Exception {
        UUID uuid = UUID.randomUUID();
        EventPlacePersonDTO requestDto = new EventPlacePersonDTO();
        requestDto.setId(2);
        requestDto.setFk_campaign_uuid(uuid);
        requestDto.setFk_event(4);
        requestDto.setFk_person(6);
        requestDto.setFk_place(8);

        String requestJson = objectMapper.writeValueAsString(requestDto);

        mockMvc.perform(post("/api/eventsPlacesPeople")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andExpect(status().isOk());

        verify(eventPlacePersonService, times(1)).saveEventPlacePerson(any(EventPlacePersonDTO.class));
    }

    @Test
    void whenEventPlacePersonIsNotValid_saveEventPlacePerson_RespondsBadRequest() throws Exception {
        EventPlacePersonDTO invalidEventPlacePerson = new EventPlacePersonDTO();
        invalidEventPlacePerson.setId(2);
        invalidEventPlacePerson.setFk_campaign_uuid(null); // Invalid UUID

        String requestJson = objectMapper.writeValueAsString(invalidEventPlacePerson);

        doThrow(new IllegalArgumentException("Campaign UUID cannot be null or empty."))
                .when(eventPlacePersonService).saveEventPlacePerson(any(EventPlacePersonDTO.class));

        MvcResult result = mockMvc.perform(post("/api/eventsPlacesPeople")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andExpect(status().isBadRequest())
                .andReturn();

        String responseBody = result.getResponse().getContentAsString();
        ObjectMapper mapper = new ObjectMapper();
        List<String> errors = mapper.readValue(responseBody, new TypeReference<List<String>>() {
        });

        assertTrue(errors.contains("Campaign UUID cannot be null or empty."));

        verify(eventPlacePersonService, times(0)).saveEventPlacePerson(any(EventPlacePersonDTO.class));
    }

    @Test
    void whenEventPlacePersonIdIsValid_deleteEventPlacePerson_RespondsOkRequest() throws Exception {
        when(eventPlacePersonService.getEventPlacePerson(VALID_EPP_ID)).thenReturn(Optional.of(dto));

        mockMvc.perform(delete(URI + "/" + VALID_EPP_ID))
                .andExpect(status().isOk());

        verify(eventPlacePersonService, times(1)).deleteEventPlacePerson(VALID_EPP_ID);
    }

    @Test
    void whenEventPlacePersonIdIsInvalid_deleteEventPlacePerson_RespondsBadRequest() throws Exception {
        doThrow(new IllegalArgumentException("Unable to delete; This item was not found."))
                .when(eventPlacePersonService).deleteEventPlacePerson(INVALID_EPP_ID);

        mockMvc.perform(delete(URI + "/" + INVALID_EPP_ID))
                .andExpect(status().isBadRequest())  // Expecting 400 Bad Request
                .andExpect(content().string("Unable to delete; This item was not found."));

        verify(eventPlacePersonService, times(1)).deleteEventPlacePerson(INVALID_EPP_ID);
    }

    @Test
    void whenEventPlacePersonIdIsValid_updateEventPlacePerson_RespondsOkRequest() throws Exception {
        EventPlacePersonDTO updatedDto = new EventPlacePersonDTO();
        updatedDto.setId(VALID_EPP_ID);
        updatedDto.setFk_campaign_uuid(UUID.randomUUID());
        updatedDto.setFk_place(2);

        String json = objectMapper.writeValueAsString(updatedDto);

        mockMvc.perform(put(URI + "/" + VALID_EPP_ID)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk());

        verify(eventPlacePersonService, times(1)).updateEventPlacePerson(eq(VALID_EPP_ID), any(EventPlacePersonDTO.class));
    }

    @Test
    void whenEventPlacePersonIdIsInvalid_updateEventPlacePerson_RespondsBadRequest() throws Exception {
        EventPlacePersonDTO updatedDto = new EventPlacePersonDTO();
        updatedDto.setId(INVALID_EPP_ID);
        updatedDto.setFk_campaign_uuid(UUID.randomUUID());
        updatedDto.setFk_event(1);

        String json = objectMapper.writeValueAsString(updatedDto);

        doThrow(new IllegalArgumentException("Unable to update; This item was not found."))
                .when(eventPlacePersonService).updateEventPlacePerson(eq(INVALID_EPP_ID), any(EventPlacePersonDTO.class));

        mockMvc.perform(put(URI + "/" + INVALID_EPP_ID)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isBadRequest())
                .andExpect(content().string(containsString("Unable to update; This item was not found.")));
    }
}
