package com.mcommings.campaigner.controllers.people;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mcommings.campaigner.modules.people.controllers.AbilityScoreController;
import com.mcommings.campaigner.modules.people.dtos.AbilityScoreDTO;
import com.mcommings.campaigner.modules.people.entities.AbilityScore;
import com.mcommings.campaigner.modules.people.services.AbilityScoreService;
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

@WebMvcTest(AbilityScoreController.class)
public class AbilityScoreControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    AbilityScoreService abilityScoreService;

    private static final int VALID_ABILITY_SCORE_ID = 1;
    private static final int INVALID_ABILITY_SCORE_ID = 999;
    private static final String URI = "/api/abilityScores";
    private AbilityScore entity;
    private AbilityScoreDTO dto;
    private Random random = new Random();

    @BeforeEach
    void setUp() {
        entity = new AbilityScore();
        entity.setId(VALID_ABILITY_SCORE_ID);
        entity.setFk_campaign_uuid(UUID.randomUUID());
        entity.setStrength(random.nextInt(100) + 1);
        entity.setDexterity(random.nextInt(100) + 1);
        entity.setConstitution(random.nextInt(100) + 1);
        entity.setIntelligence(random.nextInt(100) + 1);
        entity.setWisdom(random.nextInt(100) + 1);
        entity.setCharisma(random.nextInt(100) + 1);

        dto = new AbilityScoreDTO();
        dto.setId(entity.getId());
        dto.setFk_campaign_uuid(entity.getFk_campaign_uuid());
        dto.setStrength(entity.getStrength());
        dto.setDexterity(entity.getDexterity());
        dto.setConstitution(entity.getConstitution());
        dto.setIntelligence(entity.getIntelligence());
        dto.setWisdom(entity.getWisdom());
        dto.setCharisma(entity.getCharisma());
    }

    @Test
    void whenThereAreAbilityScores_getAbilityScores_ReturnsAbilityScores() throws Exception {
        when(abilityScoreService.getAbilityScores()).thenReturn(List.of(dto));

        String response = objectMapper.writeValueAsString(List.of(dto));

        mockMvc.perform(get(URI))
                .andExpect(status().isOk())
                .andExpect(content().json(response));
    }

    @Test
    void whenThereAreAbilityScores_getAbilityScores_ReturnsEmptyList() throws Exception {
        when(abilityScoreService.getAbilityScores()).thenReturn(List.of());

        mockMvc.perform(get(URI))
                .andExpect(status().isOk())
                .andExpect(content().json("[]"));
    }

    @Test
    void whenThereIsAAbilityScore_getAbilityScore_ReturnsAbilityScore() throws Exception {
        when(abilityScoreService.getAbilityScore(VALID_ABILITY_SCORE_ID)).thenReturn(Optional.of(dto));

        mockMvc.perform(get(URI + "/" + VALID_ABILITY_SCORE_ID))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(VALID_ABILITY_SCORE_ID));
    }

    @Test
    void whenThereIsNotAAbilityScore_getAbilityScore_ThrowsIllegalArgumentException() throws Exception {
        when(abilityScoreService.getAbilityScore(INVALID_ABILITY_SCORE_ID)).thenReturn(Optional.empty());

        mockMvc.perform(get(URI + "/" + INVALID_ABILITY_SCORE_ID))
                .andExpect(status().isBadRequest())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof IllegalArgumentException))
                .andExpect(result -> assertEquals(ID_NOT_FOUND.message, result.getResolvedException().getMessage()));
    }

    @Test
    void whenUserEntersWrongIdType_getAbilityScore_ReturnsBadRequest() throws Exception {
        mockMvc.perform(get(URI + "/abc"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void whenServiceFails_getAbilityScore_ReturnsInternalServerError() throws Exception {
        when(abilityScoreService.getAbilityScore(VALID_ABILITY_SCORE_ID)).thenThrow(new RuntimeException("Database failure"));

        mockMvc.perform(get(URI + "/" + VALID_ABILITY_SCORE_ID))
                .andExpect(status().isInternalServerError())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof RuntimeException))
                .andExpect(result -> assertEquals("Database failure", result.getResolvedException().getMessage()));
    }

    @Test
    void whenServiceReturnsNull_getAbilityScore_ReturnsInternalServerError() throws Exception {
        when(abilityScoreService.getAbilityScore(VALID_ABILITY_SCORE_ID)).thenReturn(null);

        mockMvc.perform(get(URI + "/" + VALID_ABILITY_SCORE_ID))
                .andExpect(status().isInternalServerError());
    }

    @Test
    void whenCampaignUUIDIsValid_getAbilityScoresByCampaignUUID_ReturnsAbilityScores() throws Exception {
        UUID uuid = UUID.randomUUID();
        when(abilityScoreService.getAbilityScoresByCampaignUUID(uuid)).thenReturn(List.of(dto));

        String response = objectMapper.writeValueAsString(List.of(dto));

        mockMvc.perform(get(URI + "/campaign/" + uuid))
                .andExpect(status().isOk())
                .andExpect(content().json(response));
    }

    @Test
    void whenCampaignUUIDIsValid_getAbilityScoresByCampaignUUID_ReturnsEmptyList() throws Exception {
        UUID uuid = UUID.randomUUID();
        when(abilityScoreService.getAbilityScoresByCampaignUUID(uuid)).thenReturn(List.of());

        mockMvc.perform(get(URI + "/campaign/" + uuid))
                .andExpect(status().isOk())
                .andExpect(content().json("[]"));
    }

    @Test
    void whenUserEntersWrongCampaignUUIDType_getAbilityScore_ReturnsBadRequest() throws Exception {
        mockMvc.perform(get(URI + "/abc"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void whenAbilityScoreIsValid_saveAbilityScore_RespondsOkRequest() throws Exception {
        UUID uuid = UUID.randomUUID();
        AbilityScoreDTO requestDto = new AbilityScoreDTO();
        requestDto.setId(2);
        requestDto.setFk_campaign_uuid(uuid);
        requestDto.setStrength(5);
        requestDto.setDexterity(6);
        requestDto.setConstitution(7);
        requestDto.setIntelligence(8);
        requestDto.setWisdom(9);
        requestDto.setCharisma(4);

        String requestJson = objectMapper.writeValueAsString(requestDto);

        mockMvc.perform(post("/api/abilityScores")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andExpect(status().isOk());

        verify(abilityScoreService, times(1)).saveAbilityScore(any(AbilityScoreDTO.class));
    }

    @Test
    void whenAbilityScoreIsNotValid_saveAbilityScore_RespondsBadRequest() throws Exception {
        AbilityScoreDTO invalidAbilityScore = new AbilityScoreDTO();
        invalidAbilityScore.setId(2);
        invalidAbilityScore.setFk_campaign_uuid(null); // Invalid UUID
        invalidAbilityScore.setStrength(0);

        String requestJson = objectMapper.writeValueAsString(invalidAbilityScore);

        doThrow(new IllegalArgumentException("Campaign UUID cannot be null or empty."))
                .when(abilityScoreService).saveAbilityScore(any(AbilityScoreDTO.class));

        MvcResult result = mockMvc.perform(post("/api/abilityScores")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andExpect(status().isBadRequest())
                .andReturn();

        String responseBody = result.getResponse().getContentAsString();
        ObjectMapper mapper = new ObjectMapper();
        List<String> errors = mapper.readValue(responseBody, new TypeReference<List<String>>() {
        });

        assertTrue(errors.contains("Strength must be greater than zero."));
        assertTrue(errors.contains("Campaign UUID cannot be null or empty."));

        verify(abilityScoreService, times(0)).saveAbilityScore(any(AbilityScoreDTO.class));
    }

    @Test
    void whenAbilityScoreIdIsValid_deleteAbilityScore_RespondsOkRequest() throws Exception {
        when(abilityScoreService.getAbilityScore(VALID_ABILITY_SCORE_ID)).thenReturn(Optional.of(dto));

        mockMvc.perform(delete(URI + "/" + VALID_ABILITY_SCORE_ID))
                .andExpect(status().isOk());

        verify(abilityScoreService, times(1)).deleteAbilityScore(VALID_ABILITY_SCORE_ID);
    }

    @Test
    void whenAbilityScoreIdIsInvalid_deleteAbilityScore_RespondsBadRequest() throws Exception {
        doThrow(new IllegalArgumentException("Unable to delete; This item was not found."))
                .when(abilityScoreService).deleteAbilityScore(INVALID_ABILITY_SCORE_ID);

        mockMvc.perform(delete(URI + "/" + INVALID_ABILITY_SCORE_ID))
                .andExpect(status().isBadRequest())  // Expecting 400 Bad Request
                .andExpect(content().string("Unable to delete; This item was not found."));

        verify(abilityScoreService, times(1)).deleteAbilityScore(INVALID_ABILITY_SCORE_ID);
    }

    @Test
    void whenAbilityScoreIdIsValid_updateAbilityScore_RespondsOkRequest() throws Exception {
        AbilityScoreDTO updatedDto = new AbilityScoreDTO();
        updatedDto.setId(VALID_ABILITY_SCORE_ID);
        updatedDto.setFk_campaign_uuid(UUID.randomUUID());
        updatedDto.setConstitution(99);

        String json = objectMapper.writeValueAsString(updatedDto);

        mockMvc.perform(put(URI + "/" + VALID_ABILITY_SCORE_ID)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk());

        verify(abilityScoreService, times(1)).updateAbilityScore(eq(VALID_ABILITY_SCORE_ID), any(AbilityScoreDTO.class));
    }

    @Test
    void whenAbilityScoreIdIsInvalid_updateAbilityScore_RespondsBadRequest() throws Exception {
        AbilityScoreDTO updatedDto = new AbilityScoreDTO();
        updatedDto.setId(INVALID_ABILITY_SCORE_ID);
        updatedDto.setFk_campaign_uuid(UUID.randomUUID());
        updatedDto.setCharisma(1);

        String json = objectMapper.writeValueAsString(updatedDto);

        doThrow(new IllegalArgumentException("Unable to update; This item was not found."))
                .when(abilityScoreService).updateAbilityScore(eq(INVALID_ABILITY_SCORE_ID), any(AbilityScoreDTO.class));

        mockMvc.perform(put(URI + "/" + INVALID_ABILITY_SCORE_ID)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isBadRequest())
                .andExpect(content().string(containsString("Unable to update; This item was not found.")));
    }

    @Test
    void whenAbilityScoreAlreadyExists_updateAbilityScore_RespondsBadRequest() throws Exception {
        AbilityScoreDTO invalidDto = new AbilityScoreDTO();
        invalidDto.setId(VALID_ABILITY_SCORE_ID);
        invalidDto.setFk_campaign_uuid(entity.getFk_campaign_uuid());
        invalidDto.setStrength(entity.getStrength());
        invalidDto.setDexterity(entity.getDexterity());
        invalidDto.setConstitution(entity.getConstitution());
        invalidDto.setIntelligence(entity.getIntelligence());
        invalidDto.setWisdom(entity.getWisdom());
        invalidDto.setCharisma(entity.getCharisma());

        String json = objectMapper.writeValueAsString(invalidDto);

        when(abilityScoreService.updateAbilityScore(eq(VALID_ABILITY_SCORE_ID), any(AbilityScoreDTO.class)))
                .thenThrow(new IllegalArgumentException("That combination of ability scores already exists."));

        mockMvc.perform(put(URI + "/" + VALID_ABILITY_SCORE_ID)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isBadRequest())
                .andExpect(content().string(containsString("That combination of ability scores already exists.")));
    }
}
