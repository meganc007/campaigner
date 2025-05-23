package com.mcommings.campaigner.controllers.quests;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mcommings.campaigner.modules.quests.controllers.ObjectiveController;
import com.mcommings.campaigner.modules.quests.dtos.ObjectiveDTO;
import com.mcommings.campaigner.modules.quests.entities.Objective;
import com.mcommings.campaigner.modules.quests.services.ObjectiveService;
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

@WebMvcTest(ObjectiveController.class)
public class ObjectiveControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    ObjectiveService objectiveService;

    private static final int VALID_OBJECTIVE_ID = 1;
    private static final int INVALID_OBJECTIVE_ID = 999;
    private static final String URI = "/api/objectives";
    private Objective entity;
    private ObjectiveDTO dto;

    @BeforeEach
    void setUp() {
        entity = new Objective();
        entity.setId(1);
        entity.setDescription("This is a description.");
        entity.setNotes("This is a note.");
        entity.setFk_campaign_uuid(UUID.randomUUID());

        dto = new ObjectiveDTO();
        dto.setId(entity.getId());
        dto.setDescription(entity.getDescription());
        dto.setNotes(entity.getNotes());
        dto.setFk_campaign_uuid(entity.getFk_campaign_uuid());
    }

    @Test
    void whenThereAreObjectives_getObjectives_ReturnsObjectives() throws Exception {
        when(objectiveService.getObjectives()).thenReturn(List.of(dto));

        String response = objectMapper.writeValueAsString(List.of(dto));

        mockMvc.perform(get(URI))
                .andExpect(status().isOk())
                .andExpect(content().json(response));
    }

    @Test
    void whenThereAreObjectives_getObjectives_ReturnsEmptyList() throws Exception {
        when(objectiveService.getObjectives()).thenReturn(List.of());

        mockMvc.perform(get(URI))
                .andExpect(status().isOk())
                .andExpect(content().json("[]"));
    }

    @Test
    void whenThereIsAObjective_getObjective_ReturnsObjective() throws Exception {
        when(objectiveService.getObjective(VALID_OBJECTIVE_ID)).thenReturn(Optional.of(dto));

        mockMvc.perform(get(URI + "/" + VALID_OBJECTIVE_ID))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(VALID_OBJECTIVE_ID))
                .andExpect(jsonPath("$.description").value(dto.getDescription()));
    }

    @Test
    void whenThereIsNotAObjective_getObjective_ThrowsIllegalArgumentException() throws Exception {
        when(objectiveService.getObjective(INVALID_OBJECTIVE_ID)).thenReturn(Optional.empty());

        mockMvc.perform(get(URI + "/" + INVALID_OBJECTIVE_ID))
                .andExpect(status().isBadRequest())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof IllegalArgumentException))
                .andExpect(result -> assertEquals(ID_NOT_FOUND.message, result.getResolvedException().getMessage()));
    }

    @Test
    void whenUserEntersWrongIdType_getObjective_ReturnsBadRequest() throws Exception {
        mockMvc.perform(get(URI + "/abc"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void whenServiceFails_getObjective_ReturnsInternalServerError() throws Exception {
        when(objectiveService.getObjective(VALID_OBJECTIVE_ID)).thenThrow(new RuntimeException("Database failure"));

        mockMvc.perform(get(URI + "/" + VALID_OBJECTIVE_ID))
                .andExpect(status().isInternalServerError())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof RuntimeException))
                .andExpect(result -> assertEquals("Database failure", result.getResolvedException().getMessage()));
    }

    @Test
    void whenServiceReturnsNull_getObjective_ReturnsInternalServerError() throws Exception {
        when(objectiveService.getObjective(VALID_OBJECTIVE_ID)).thenReturn(null);

        mockMvc.perform(get(URI + "/" + VALID_OBJECTIVE_ID))
                .andExpect(status().isInternalServerError());
    }

    @Test
    void whenCampaignUUIDIsValid_getObjectivesByCampaignUUID_ReturnsObjectives() throws Exception {
        UUID uuid = UUID.randomUUID();
        when(objectiveService.getObjectivesByCampaignUUID(uuid)).thenReturn(List.of(dto));

        String response = objectMapper.writeValueAsString(List.of(dto));

        mockMvc.perform(get(URI + "/campaign/" + uuid))
                .andExpect(status().isOk())
                .andExpect(content().json(response));
    }

    @Test
    void whenCampaignUUIDIsValid_getObjectivesByCampaignUUID_ReturnsEmptyList() throws Exception {
        UUID uuid = UUID.randomUUID();
        when(objectiveService.getObjectivesByCampaignUUID(uuid)).thenReturn(List.of());

        mockMvc.perform(get(URI + "/campaign/" + uuid))
                .andExpect(status().isOk())
                .andExpect(content().json("[]"));
    }

    @Test
    void whenUserEntersWrongCampaignUUIDType_getObjective_ReturnsBadRequest() throws Exception {
        mockMvc.perform(get(URI + "/abc"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void whenKeywordIsValid_getObjectivesWhereDescriptionContainsKeyword_ReturnsObjectives() throws Exception {
        UUID uuid = UUID.randomUUID();
        String keyword = "magic sword";

        ObjectiveDTO objective1 = new ObjectiveDTO();
        objective1.setId(1);
        objective1.setDescription("A villager found a magic sword.");
        objective1.setFk_campaign_uuid(uuid);

        ObjectiveDTO objective2 = new ObjectiveDTO();
        objective2.setId(2);
        objective2.setDescription("A noble wants the party to retrieve their magic sword.");
        objective2.setFk_campaign_uuid(uuid);

        ObjectiveDTO objective3 = new ObjectiveDTO();
        objective3.setId(2);
        objective3.setDescription("You come across a winged cat hunting carrier pigeons.");
        objective3.setFk_campaign_uuid(uuid);

        List<ObjectiveDTO> objectives = List.of(objective1, objective2);

        when(objectiveService.getObjectivesWhereDescriptionContainsKeyword(keyword)).thenReturn(objectives);

        MvcResult result = mockMvc.perform(get(URI + "/description/" + keyword))
                .andExpect(status().isOk())
                .andReturn();

        String responseJson = result.getResponse().getContentAsString();

        ObjectMapper objectMapper = new ObjectMapper();
        List<ObjectiveDTO> returnedObjectives = objectMapper.readValue(responseJson,
                objectMapper.getTypeFactory().constructCollectionType(List.class, ObjectiveDTO.class));

        assertTrue(returnedObjectives.contains(objective1));
        assertTrue(returnedObjectives.contains(objective2));
        assertFalse(returnedObjectives.contains(objective3));
    }

    @Test
    void whenKeywordIsValidButThereAreNoMatches_getObjectivesWhereDescriptionContainsKeyword_ReturnsEmptyList() throws Exception {
        UUID uuid = UUID.randomUUID();
        String keyword = "dog";

        ObjectiveDTO objective1 = new ObjectiveDTO();
        objective1.setId(1);
        objective1.setDescription("A villager found a magic sword.");
        objective1.setFk_campaign_uuid(uuid);

        ObjectiveDTO objective2 = new ObjectiveDTO();
        objective2.setId(2);
        objective2.setDescription("A noble wants the party to retrieve their magic sword.");
        objective2.setFk_campaign_uuid(uuid);

        ObjectiveDTO objective3 = new ObjectiveDTO();
        objective3.setId(2);
        objective3.setDescription("You come across a winged cat hunting carrier pigeons.");
        objective3.setFk_campaign_uuid(uuid);

        when(objectiveService.getObjectivesWhereDescriptionContainsKeyword(keyword)).thenReturn(List.of());

        MvcResult result = mockMvc.perform(get(URI + "/description/" + keyword))
                .andExpect(status().isOk())
                .andReturn();

        String responseJson = result.getResponse().getContentAsString();

        ObjectMapper objectMapper = new ObjectMapper();
        List<ObjectiveDTO> returnedObjectives = objectMapper.readValue(responseJson,
                objectMapper.getTypeFactory().constructCollectionType(List.class, ObjectiveDTO.class));

        assertFalse(returnedObjectives.contains(objective1));
        assertFalse(returnedObjectives.contains(objective2));
        assertFalse(returnedObjectives.contains(objective3));
    }

    @Test
    void whenObjectiveIsValid_saveObjective_RespondsOkRequest() throws Exception {
        UUID uuid = UUID.randomUUID();
        ObjectiveDTO requestDto = new ObjectiveDTO();
        requestDto.setId(2);
        requestDto.setDescription("This is a description");
        requestDto.setFk_campaign_uuid(uuid);

        String requestJson = objectMapper.writeValueAsString(requestDto);

        mockMvc.perform(post("/api/objectives")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andExpect(status().isOk());

        verify(objectiveService, times(1)).saveObjective(any(ObjectiveDTO.class));
    }

    @Test
    void whenObjectiveIsNotValid_saveObjective_RespondsBadRequest() throws Exception {
        ObjectiveDTO invalidObjective = new ObjectiveDTO();
        invalidObjective.setId(2);
        invalidObjective.setDescription("");
        invalidObjective.setFk_campaign_uuid(null); // Invalid UUID

        String requestJson = objectMapper.writeValueAsString(invalidObjective);

        doThrow(new IllegalArgumentException("Campaign UUID cannot be null or empty."))
                .when(objectiveService).saveObjective(any(ObjectiveDTO.class));

        MvcResult result = mockMvc.perform(post("/api/objectives")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andExpect(status().isBadRequest())
                .andReturn();

        String responseBody = result.getResponse().getContentAsString();
        ObjectMapper mapper = new ObjectMapper();
        List<String> errors = mapper.readValue(responseBody, new TypeReference<List<String>>() {
        });

        assertTrue(errors.contains("Objective description cannot be empty"));
        assertTrue(errors.contains("Campaign UUID cannot be null or empty."));

        verify(objectiveService, times(0)).saveObjective(any(ObjectiveDTO.class));
    }

    @Test
    void whenObjectiveIdIsValid_deleteObjective_RespondsOkRequest() throws Exception {
        when(objectiveService.getObjective(VALID_OBJECTIVE_ID)).thenReturn(Optional.of(dto));

        mockMvc.perform(delete(URI + "/" + VALID_OBJECTIVE_ID))
                .andExpect(status().isOk());

        verify(objectiveService, times(1)).deleteObjective(VALID_OBJECTIVE_ID);
    }

    @Test
    void whenObjectiveIdIsInvalid_deleteObjective_RespondsBadRequest() throws Exception {
        doThrow(new IllegalArgumentException("Unable to delete; This item was not found."))
                .when(objectiveService).deleteObjective(INVALID_OBJECTIVE_ID);

        mockMvc.perform(delete(URI + "/" + INVALID_OBJECTIVE_ID))
                .andExpect(status().isBadRequest())  // Expecting 400 Bad Request
                .andExpect(content().string("Unable to delete; This item was not found."));

        verify(objectiveService, times(1)).deleteObjective(INVALID_OBJECTIVE_ID);
    }

    @Test
    void whenObjectiveIdIsValid_updateObjective_RespondsOkRequest() throws Exception {
        ObjectiveDTO updatedDto = new ObjectiveDTO();
        updatedDto.setId(VALID_OBJECTIVE_ID);
        updatedDto.setDescription("Updated description");
        updatedDto.setFk_campaign_uuid(UUID.randomUUID());

        String json = objectMapper.writeValueAsString(updatedDto);

        mockMvc.perform(put(URI + "/" + VALID_OBJECTIVE_ID)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk());

        verify(objectiveService, times(1)).updateObjective(eq(VALID_OBJECTIVE_ID), any(ObjectiveDTO.class));
    }

    @Test
    void whenObjectiveIdIsInvalid_updateObjective_RespondsBadRequest() throws Exception {
        ObjectiveDTO updatedDto = new ObjectiveDTO();
        updatedDto.setId(INVALID_OBJECTIVE_ID);
        updatedDto.setDescription("Some update");
        updatedDto.setFk_campaign_uuid(UUID.randomUUID());

        String json = objectMapper.writeValueAsString(updatedDto);

        doThrow(new IllegalArgumentException("Unable to update; This item was not found."))
                .when(objectiveService).updateObjective(eq(INVALID_OBJECTIVE_ID), any(ObjectiveDTO.class));

        mockMvc.perform(put(URI + "/" + INVALID_OBJECTIVE_ID)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isBadRequest())
                .andExpect(content().string(containsString("Unable to update; This item was not found.")));
    }

    @Test
    void whenObjectiveDescriptionIsInvalid_updateObjective_RespondsBadRequest() throws Exception {
        ObjectiveDTO invalidDto = new ObjectiveDTO();
        invalidDto.setId(VALID_OBJECTIVE_ID);
        invalidDto.setDescription("");
        invalidDto.setFk_campaign_uuid(UUID.randomUUID());

        String json = objectMapper.writeValueAsString(invalidDto);

        when(objectiveService.updateObjective(eq(VALID_OBJECTIVE_ID), any(ObjectiveDTO.class)))
                .thenThrow(new IllegalArgumentException("Item name cannot be null or empty."));

        mockMvc.perform(put(URI + "/" + VALID_OBJECTIVE_ID)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isBadRequest())
                .andExpect(content().string(containsString("Item name cannot be null or empty.")));
    }
}
