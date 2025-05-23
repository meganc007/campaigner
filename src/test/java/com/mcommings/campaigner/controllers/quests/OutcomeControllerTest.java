package com.mcommings.campaigner.controllers.quests;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mcommings.campaigner.modules.quests.controllers.OutcomeController;
import com.mcommings.campaigner.modules.quests.dtos.OutcomeDTO;
import com.mcommings.campaigner.modules.quests.entities.Outcome;
import com.mcommings.campaigner.modules.quests.services.OutcomeService;
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
import java.util.UUID;

import static com.mcommings.campaigner.enums.ErrorMessage.ID_NOT_FOUND;
import static org.hamcrest.Matchers.containsString;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(OutcomeController.class)
public class OutcomeControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    OutcomeService outcomeService;

    private static final int VALID_OUTCOME_ID = 1;
    private static final int INVALID_OUTCOME_ID = 999;
    private static final String URI = "/api/outcomes";
    private Outcome entity;
    private OutcomeDTO dto;

    @BeforeEach
    void setUp() {
        entity = new Outcome();
        entity.setId(1);
        entity.setDescription("This is a description.");
        entity.setNotes("This is a note.");
        entity.setFk_campaign_uuid(UUID.randomUUID());

        dto = new OutcomeDTO();
        dto.setId(entity.getId());
        dto.setDescription(entity.getDescription());
        dto.setNotes(entity.getNotes());
        dto.setFk_campaign_uuid(entity.getFk_campaign_uuid());
    }

    @Test
    void whenThereAreOutcomes_getOutcomes_ReturnsOutcomes() throws Exception {
        when(outcomeService.getOutcomes()).thenReturn(List.of(dto));

        String response = objectMapper.writeValueAsString(List.of(dto));

        mockMvc.perform(get(URI))
                .andExpect(status().isOk())
                .andExpect(content().json(response));
    }

    @Test
    void whenThereAreOutcomes_getOutcomes_ReturnsEmptyList() throws Exception {
        when(outcomeService.getOutcomes()).thenReturn(List.of());

        mockMvc.perform(get(URI))
                .andExpect(status().isOk())
                .andExpect(content().json("[]"));
    }

    @Test
    void whenThereIsAOutcome_getOutcome_ReturnsOutcome() throws Exception {
        when(outcomeService.getOutcome(VALID_OUTCOME_ID)).thenReturn(Optional.of(dto));

        mockMvc.perform(get(URI + "/" + VALID_OUTCOME_ID))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(VALID_OUTCOME_ID))
                .andExpect(jsonPath("$.description").value(dto.getDescription()));
    }

    @Test
    void whenThereIsNotAOutcome_getOutcome_ThrowsIllegalArgumentException() throws Exception {
        when(outcomeService.getOutcome(INVALID_OUTCOME_ID)).thenReturn(Optional.empty());

        mockMvc.perform(get(URI + "/" + INVALID_OUTCOME_ID))
                .andExpect(status().isBadRequest())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof IllegalArgumentException))
                .andExpect(result -> assertEquals(ID_NOT_FOUND.message, result.getResolvedException().getMessage()));
    }

    @Test
    void whenUserEntersWrongIdType_getOutcome_ReturnsBadRequest() throws Exception {
        mockMvc.perform(get(URI + "/abc"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void whenServiceFails_getOutcome_ReturnsInternalServerError() throws Exception {
        when(outcomeService.getOutcome(VALID_OUTCOME_ID)).thenThrow(new RuntimeException("Database failure"));

        mockMvc.perform(get(URI + "/" + VALID_OUTCOME_ID))
                .andExpect(status().isInternalServerError())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof RuntimeException))
                .andExpect(result -> assertEquals("Database failure", result.getResolvedException().getMessage()));
    }

    @Test
    void whenServiceReturnsNull_getOutcome_ReturnsInternalServerError() throws Exception {
        when(outcomeService.getOutcome(VALID_OUTCOME_ID)).thenReturn(null);

        mockMvc.perform(get(URI + "/" + VALID_OUTCOME_ID))
                .andExpect(status().isInternalServerError());
    }

    @Test
    void whenCampaignUUIDIsValid_getOutcomesByCampaignUUID_ReturnsOutcomes() throws Exception {
        UUID uuid = UUID.randomUUID();
        when(outcomeService.getOutcomesByCampaignUUID(uuid)).thenReturn(List.of(dto));

        String response = objectMapper.writeValueAsString(List.of(dto));

        mockMvc.perform(get(URI + "/campaign/" + uuid))
                .andExpect(status().isOk())
                .andExpect(content().json(response));
    }

    @Test
    void whenCampaignUUIDIsValid_getOutcomesByCampaignUUID_ReturnsEmptyList() throws Exception {
        UUID uuid = UUID.randomUUID();
        when(outcomeService.getOutcomesByCampaignUUID(uuid)).thenReturn(List.of());

        mockMvc.perform(get(URI + "/campaign/" + uuid))
                .andExpect(status().isOk())
                .andExpect(content().json("[]"));
    }

    @Test
    void whenUserEntersWrongCampaignUUIDType_getOutcome_ReturnsBadRequest() throws Exception {
        mockMvc.perform(get(URI + "/abc"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void whenKeywordIsValid_getOutcomesWhereDescriptionContainsKeyword_ReturnsOutcomes() throws Exception {
        UUID uuid = UUID.randomUUID();
        String keyword = "magic sword";

        OutcomeDTO outcome1 = new OutcomeDTO();
        outcome1.setId(1);
        outcome1.setDescription("A villager found a magic sword.");
        outcome1.setFk_campaign_uuid(uuid);

        OutcomeDTO outcome2 = new OutcomeDTO();
        outcome2.setId(2);
        outcome2.setDescription("A noble wants the party to retrieve their magic sword.");
        outcome2.setFk_campaign_uuid(uuid);

        OutcomeDTO outcome3 = new OutcomeDTO();
        outcome3.setId(2);
        outcome3.setDescription("You come across a winged cat hunting carrier pigeons.");
        outcome3.setFk_campaign_uuid(uuid);

        List<OutcomeDTO> outcomes = List.of(outcome1, outcome2);

        when(outcomeService.getOutcomesWhereDescriptionContainsKeyword(keyword)).thenReturn(outcomes);

        MvcResult result = mockMvc.perform(get(URI + "/description/" + keyword))
                .andExpect(status().isOk())
                .andReturn();

        String responseJson = result.getResponse().getContentAsString();

        ObjectMapper objectMapper = new ObjectMapper();
        List<OutcomeDTO> returnedOutcomes = objectMapper.readValue(responseJson,
                objectMapper.getTypeFactory().constructCollectionType(List.class, OutcomeDTO.class));

        assertTrue(returnedOutcomes.contains(outcome1));
        assertTrue(returnedOutcomes.contains(outcome2));
        assertFalse(returnedOutcomes.contains(outcome3));
    }

    @Test
    void whenKeywordIsValidButThereAreNoMatches_getOutcomesWhereDescriptionContainsKeyword_ReturnsEmptyList() throws Exception {
        UUID uuid = UUID.randomUUID();
        String keyword = "dog";

        OutcomeDTO outcome1 = new OutcomeDTO();
        outcome1.setId(1);
        outcome1.setDescription("A villager found a magic sword.");
        outcome1.setFk_campaign_uuid(uuid);

        OutcomeDTO outcome2 = new OutcomeDTO();
        outcome2.setId(2);
        outcome2.setDescription("A noble wants the party to retrieve their magic sword.");
        outcome2.setFk_campaign_uuid(uuid);

        OutcomeDTO outcome3 = new OutcomeDTO();
        outcome3.setId(2);
        outcome3.setDescription("You come across a winged cat hunting carrier pigeons.");
        outcome3.setFk_campaign_uuid(uuid);

        when(outcomeService.getOutcomesWhereDescriptionContainsKeyword(keyword)).thenReturn(List.of());

        MvcResult result = mockMvc.perform(get(URI + "/description/" + keyword))
                .andExpect(status().isOk())
                .andReturn();

        String responseJson = result.getResponse().getContentAsString();

        ObjectMapper objectMapper = new ObjectMapper();
        List<OutcomeDTO> returnedOutcomes = objectMapper.readValue(responseJson,
                objectMapper.getTypeFactory().constructCollectionType(List.class, OutcomeDTO.class));

        assertFalse(returnedOutcomes.contains(outcome1));
        assertFalse(returnedOutcomes.contains(outcome2));
        assertFalse(returnedOutcomes.contains(outcome3));
    }

    @Test
    void whenOutcomeIsValid_saveOutcome_RespondsOkRequest() throws Exception {
        UUID uuid = UUID.randomUUID();
        OutcomeDTO requestDto = new OutcomeDTO();
        requestDto.setId(2);
        requestDto.setDescription("This is a description");
        requestDto.setFk_campaign_uuid(uuid);

        String requestJson = objectMapper.writeValueAsString(requestDto);

        mockMvc.perform(post("/api/outcomes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andExpect(status().isOk());

        verify(outcomeService, times(1)).saveOutcome(any(OutcomeDTO.class));
    }

    @Test
    void whenOutcomeIsNotValid_saveOutcome_RespondsBadRequest() throws Exception {
        OutcomeDTO invalidOutcome = new OutcomeDTO();
        invalidOutcome.setId(2);
        invalidOutcome.setDescription("");
        invalidOutcome.setFk_campaign_uuid(null); // Invalid UUID

        String requestJson = objectMapper.writeValueAsString(invalidOutcome);

        doThrow(new IllegalArgumentException("Campaign UUID cannot be null or empty."))
                .when(outcomeService).saveOutcome(any(OutcomeDTO.class));

        MvcResult result = mockMvc.perform(post("/api/outcomes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andExpect(status().isBadRequest())
                .andReturn();

        String responseBody = result.getResponse().getContentAsString();
        ObjectMapper mapper = new ObjectMapper();
        List<String> errors = mapper.readValue(responseBody, new TypeReference<List<String>>() {
        });

        assertTrue(errors.contains("Outcome description cannot be empty"));
        assertTrue(errors.contains("Campaign UUID cannot be null or empty."));

        verify(outcomeService, times(0)).saveOutcome(any(OutcomeDTO.class));
    }

    @Test
    void whenOutcomeIdIsValid_deleteOutcome_RespondsOkRequest() throws Exception {
        when(outcomeService.getOutcome(VALID_OUTCOME_ID)).thenReturn(Optional.of(dto));

        mockMvc.perform(delete(URI + "/" + VALID_OUTCOME_ID))
                .andExpect(status().isOk());

        verify(outcomeService, times(1)).deleteOutcome(VALID_OUTCOME_ID);
    }

    @Test
    void whenOutcomeIdIsInvalid_deleteOutcome_RespondsBadRequest() throws Exception {
        doThrow(new IllegalArgumentException("Unable to delete; This item was not found."))
                .when(outcomeService).deleteOutcome(INVALID_OUTCOME_ID);

        mockMvc.perform(delete(URI + "/" + INVALID_OUTCOME_ID))
                .andExpect(status().isBadRequest())  // Expecting 400 Bad Request
                .andExpect(content().string("Unable to delete; This item was not found."));

        verify(outcomeService, times(1)).deleteOutcome(INVALID_OUTCOME_ID);
    }

    @Test
    void whenOutcomeIdIsValid_updateOutcome_RespondsOkRequest() throws Exception {
        OutcomeDTO updatedDto = new OutcomeDTO();
        updatedDto.setId(VALID_OUTCOME_ID);
        updatedDto.setDescription("Updated description");
        updatedDto.setFk_campaign_uuid(UUID.randomUUID());

        String json = objectMapper.writeValueAsString(updatedDto);

        mockMvc.perform(put(URI + "/" + VALID_OUTCOME_ID)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk());

        verify(outcomeService, times(1)).updateOutcome(eq(VALID_OUTCOME_ID), any(OutcomeDTO.class));
    }

    @Test
    void whenOutcomeIdIsInvalid_updateOutcome_RespondsBadRequest() throws Exception {
        OutcomeDTO updatedDto = new OutcomeDTO();
        updatedDto.setId(INVALID_OUTCOME_ID);
        updatedDto.setDescription("Some update");
        updatedDto.setFk_campaign_uuid(UUID.randomUUID());

        String json = objectMapper.writeValueAsString(updatedDto);

        doThrow(new IllegalArgumentException("Unable to update; This item was not found."))
                .when(outcomeService).updateOutcome(eq(INVALID_OUTCOME_ID), any(OutcomeDTO.class));

        mockMvc.perform(put(URI + "/" + INVALID_OUTCOME_ID)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isBadRequest())
                .andExpect(content().string(containsString("Unable to update; This item was not found.")));
    }

    @Test
    void whenOutcomeDescriptionIsInvalid_updateOutcome_RespondsBadRequest() throws Exception {
        OutcomeDTO invalidDto = new OutcomeDTO();
        invalidDto.setId(VALID_OUTCOME_ID);
        invalidDto.setDescription("");
        invalidDto.setFk_campaign_uuid(UUID.randomUUID());

        String json = objectMapper.writeValueAsString(invalidDto);

        when(outcomeService.updateOutcome(eq(VALID_OUTCOME_ID), any(OutcomeDTO.class)))
                .thenThrow(new IllegalArgumentException("Item name cannot be null or empty."));

        mockMvc.perform(put(URI + "/" + VALID_OUTCOME_ID)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isBadRequest())
                .andExpect(content().string(containsString("Item name cannot be null or empty.")));
    }
}
