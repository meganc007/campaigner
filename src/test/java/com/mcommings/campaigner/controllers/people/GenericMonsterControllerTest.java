package com.mcommings.campaigner.controllers.people;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mcommings.campaigner.modules.people.controllers.GenericMonsterController;
import com.mcommings.campaigner.modules.people.dtos.GenericMonsterDTO;
import com.mcommings.campaigner.modules.people.entities.GenericMonster;
import com.mcommings.campaigner.modules.people.services.GenericMonsterService;
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

@WebMvcTest(GenericMonsterController.class)
public class GenericMonsterControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    GenericMonsterService genericMonsterService;

    private static final int VALID_GENERIC_MONSTER_ID = 1;
    private static final int INVALID_GENERIC_MONSTER_ID = 999;
    private static final String URI = "/api/genericMonsters";
    private GenericMonster entity;
    private GenericMonsterDTO dto;
    private Random random = new Random();

    @BeforeEach
    void setUp() {
        entity = new GenericMonster();
        entity.setId(VALID_GENERIC_MONSTER_ID);
        entity.setName("A name.");
        entity.setDescription("A description.");
        entity.setFk_campaign_uuid(UUID.randomUUID());
        entity.setFk_ability_score(random.nextInt(100) + 1);
        entity.setNotes("A note.");
        entity.setTraits("A trait.");

        dto = new GenericMonsterDTO();
        dto.setId(entity.getId());
        dto.setName(entity.getName());
        dto.setDescription(entity.getDescription());
        dto.setFk_campaign_uuid(entity.getFk_campaign_uuid());
        dto.setFk_ability_score(entity.getFk_ability_score());
        dto.setNotes(entity.getNotes());
        dto.setTraits(entity.getTraits());
    }

    @Test
    void whenThereAreGenericMonsters_getGenericMonsters_ReturnsGenericMonsters() throws Exception {
        when(genericMonsterService.getGenericMonsters()).thenReturn(List.of(dto));

        String response = objectMapper.writeValueAsString(List.of(dto));

        mockMvc.perform(get(URI))
                .andExpect(status().isOk())
                .andExpect(content().json(response));
    }

    @Test
    void whenThereAreGenericMonsters_getGenericMonsters_ReturnsEmptyList() throws Exception {
        when(genericMonsterService.getGenericMonsters()).thenReturn(List.of());

        mockMvc.perform(get(URI))
                .andExpect(status().isOk())
                .andExpect(content().json("[]"));
    }

    @Test
    void whenThereIsAGenericMonster_getGenericMonster_ReturnsGenericMonster() throws Exception {
        when(genericMonsterService.getGenericMonster(VALID_GENERIC_MONSTER_ID)).thenReturn(Optional.of(dto));

        mockMvc.perform(get(URI + "/" + VALID_GENERIC_MONSTER_ID))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(VALID_GENERIC_MONSTER_ID))
                .andExpect(jsonPath("$.description").value(dto.getDescription()));
    }

    @Test
    void whenThereIsNotAGenericMonster_getGenericMonster_ThrowsIllegalArgumentException() throws Exception {
        when(genericMonsterService.getGenericMonster(INVALID_GENERIC_MONSTER_ID)).thenReturn(Optional.empty());

        mockMvc.perform(get(URI + "/" + INVALID_GENERIC_MONSTER_ID))
                .andExpect(status().isBadRequest())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof IllegalArgumentException))
                .andExpect(result -> assertEquals(ID_NOT_FOUND.message, result.getResolvedException().getMessage()));
    }

    @Test
    void whenUserEntersWrongIdType_getGenericMonster_ReturnsBadRequest() throws Exception {
        mockMvc.perform(get(URI + "/abc"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void whenServiceFails_getGenericMonster_ReturnsInternalServerError() throws Exception {
        when(genericMonsterService.getGenericMonster(VALID_GENERIC_MONSTER_ID)).thenThrow(new RuntimeException("Database failure"));

        mockMvc.perform(get(URI + "/" + VALID_GENERIC_MONSTER_ID))
                .andExpect(status().isInternalServerError())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof RuntimeException))
                .andExpect(result -> assertEquals("Database failure", result.getResolvedException().getMessage()));
    }

    @Test
    void whenServiceReturnsNull_getGenericMonster_ReturnsInternalServerError() throws Exception {
        when(genericMonsterService.getGenericMonster(VALID_GENERIC_MONSTER_ID)).thenReturn(null);

        mockMvc.perform(get(URI + "/" + VALID_GENERIC_MONSTER_ID))
                .andExpect(status().isInternalServerError());
    }

    @Test
    void whenCampaignUUIDIsValid_getGenericMonstersByCampaignUUID_ReturnsGenericMonsters() throws Exception {
        UUID uuid = UUID.randomUUID();
        when(genericMonsterService.getGenericMonstersByCampaignUUID(uuid)).thenReturn(List.of(dto));

        String response = objectMapper.writeValueAsString(List.of(dto));

        mockMvc.perform(get(URI + "/campaign/" + uuid))
                .andExpect(status().isOk())
                .andExpect(content().json(response));
    }

    @Test
    void whenCampaignUUIDIsValid_getGenericMonstersByCampaignUUID_ReturnsEmptyList() throws Exception {
        UUID uuid = UUID.randomUUID();
        when(genericMonsterService.getGenericMonstersByCampaignUUID(uuid)).thenReturn(List.of());

        mockMvc.perform(get(URI + "/campaign/" + uuid))
                .andExpect(status().isOk())
                .andExpect(content().json("[]"));
    }

    @Test
    void whenUserEntersWrongCampaignUUIDType_getGenericMonster_ReturnsBadRequest() throws Exception {
        mockMvc.perform(get(URI + "/abc"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void whenAbilityScoreIDIsValid_getGenericMonstersByAbilityScore_ReturnsGenericMonsters() throws Exception {
        int abilityScoreId = random.nextInt(100) + 1;
        when(genericMonsterService.getGenericMonstersByAbilityScore(abilityScoreId)).thenReturn(List.of(dto));

        String response = objectMapper.writeValueAsString(List.of(dto));

        mockMvc.perform(get(URI + "/abilityScore/" + abilityScoreId))
                .andExpect(status().isOk())
                .andExpect(content().json(response));
    }

    @Test
    void whenAbilityScoreIDIsNotValid_getGenericMonstersByAbilityScore_ReturnsEmptyList() throws Exception {
        int abilityScoreId = random.nextInt(100) + 1;
        when(genericMonsterService.getGenericMonstersByAbilityScore(abilityScoreId)).thenReturn(List.of());

        mockMvc.perform(get(URI + "/abilityScore/" + abilityScoreId))
                .andExpect(status().isOk())
                .andExpect(content().json("[]"));
    }

    @Test
    void whenGenericMonsterIsValid_saveGenericMonster_RespondsOkRequest() throws Exception {
        UUID uuid = UUID.randomUUID();
        GenericMonsterDTO requestDto = new GenericMonsterDTO();
        requestDto.setId(2);
        requestDto.setName("This is a name");
        requestDto.setDescription("This is a description");
        requestDto.setFk_campaign_uuid(uuid);
        requestDto.setFk_ability_score(4);

        String requestJson = objectMapper.writeValueAsString(requestDto);

        mockMvc.perform(post("/api/genericMonsters")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andExpect(status().isOk());

        verify(genericMonsterService, times(1)).saveGenericMonster(any(GenericMonsterDTO.class));
    }

    @Test
    void whenGenericMonsterIsNotValid_saveGenericMonster_RespondsBadRequest() throws Exception {
        GenericMonsterDTO invalidGenericMonster = new GenericMonsterDTO();
        invalidGenericMonster.setId(2);
        invalidGenericMonster.setDescription("This is a description");
        invalidGenericMonster.setFk_campaign_uuid(null); // Invalid UUID
        invalidGenericMonster.setFk_ability_score(4);

        String requestJson = objectMapper.writeValueAsString(invalidGenericMonster);

        doThrow(new IllegalArgumentException("Campaign UUID cannot be null or empty."))
                .when(genericMonsterService).saveGenericMonster(any(GenericMonsterDTO.class));

        MvcResult result = mockMvc.perform(post("/api/genericMonsters")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andExpect(status().isBadRequest())
                .andReturn();

        String responseBody = result.getResponse().getContentAsString();
        ObjectMapper mapper = new ObjectMapper();
        List<String> errors = mapper.readValue(responseBody, new TypeReference<List<String>>() {
        });

        assertTrue(errors.contains("Monster name cannot be empty"));
        assertTrue(errors.contains("Campaign UUID cannot be null or empty."));

        verify(genericMonsterService, times(0)).saveGenericMonster(any(GenericMonsterDTO.class));
    }

    @Test
    void whenGenericMonsterIdIsValid_deleteGenericMonster_RespondsOkRequest() throws Exception {
        when(genericMonsterService.getGenericMonster(VALID_GENERIC_MONSTER_ID)).thenReturn(Optional.of(dto));

        mockMvc.perform(delete(URI + "/" + VALID_GENERIC_MONSTER_ID))
                .andExpect(status().isOk());

        verify(genericMonsterService, times(1)).deleteGenericMonster(VALID_GENERIC_MONSTER_ID);
    }

    @Test
    void whenGenericMonsterIdIsInvalid_deleteGenericMonster_RespondsBadRequest() throws Exception {
        doThrow(new IllegalArgumentException("Unable to delete; This item was not found."))
                .when(genericMonsterService).deleteGenericMonster(INVALID_GENERIC_MONSTER_ID);

        mockMvc.perform(delete(URI + "/" + INVALID_GENERIC_MONSTER_ID))
                .andExpect(status().isBadRequest())  // Expecting 400 Bad Request
                .andExpect(content().string("Unable to delete; This item was not found."));

        verify(genericMonsterService, times(1)).deleteGenericMonster(INVALID_GENERIC_MONSTER_ID);
    }

    @Test
    void whenGenericMonsterIdIsValid_updateGenericMonster_RespondsOkRequest() throws Exception {
        GenericMonsterDTO updatedDto = new GenericMonsterDTO();
        updatedDto.setId(VALID_GENERIC_MONSTER_ID);
        updatedDto.setDescription("Updated description");
        updatedDto.setFk_campaign_uuid(UUID.randomUUID());
        updatedDto.setFk_ability_score(2);

        String json = objectMapper.writeValueAsString(updatedDto);

        mockMvc.perform(put(URI + "/" + VALID_GENERIC_MONSTER_ID)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk());

        verify(genericMonsterService, times(1)).updateGenericMonster(eq(VALID_GENERIC_MONSTER_ID), any(GenericMonsterDTO.class));
    }

    @Test
    void whenGenericMonsterIdIsInvalid_updateGenericMonster_RespondsBadRequest() throws Exception {
        GenericMonsterDTO updatedDto = new GenericMonsterDTO();
        updatedDto.setId(INVALID_GENERIC_MONSTER_ID);
        updatedDto.setDescription("Some update");
        updatedDto.setFk_campaign_uuid(UUID.randomUUID());
        updatedDto.setFk_ability_score(1);

        String json = objectMapper.writeValueAsString(updatedDto);

        doThrow(new IllegalArgumentException("Unable to update; This item was not found."))
                .when(genericMonsterService).updateGenericMonster(eq(INVALID_GENERIC_MONSTER_ID), any(GenericMonsterDTO.class));

        mockMvc.perform(put(URI + "/" + INVALID_GENERIC_MONSTER_ID)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isBadRequest())
                .andExpect(content().string(containsString("Unable to update; This item was not found.")));
    }

    @Test
    void whenGenericMonsterNameIsInvalid_updateGenericMonster_RespondsBadRequest() throws Exception {
        GenericMonsterDTO invalidDto = new GenericMonsterDTO();
        invalidDto.setId(VALID_GENERIC_MONSTER_ID);
        invalidDto.setDescription("");
        invalidDto.setFk_campaign_uuid(UUID.randomUUID());
        invalidDto.setFk_ability_score(1);

        String json = objectMapper.writeValueAsString(invalidDto);

        when(genericMonsterService.updateGenericMonster(eq(VALID_GENERIC_MONSTER_ID), any(GenericMonsterDTO.class)))
                .thenThrow(new IllegalArgumentException("Item name cannot be null or empty."));

        mockMvc.perform(put(URI + "/" + VALID_GENERIC_MONSTER_ID)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isBadRequest())
                .andExpect(content().string(containsString("Item name cannot be null or empty.")));
    }
}
