package com.mcommings.campaigner.controllers.people;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mcommings.campaigner.modules.people.controllers.NamedMonsterController;
import com.mcommings.campaigner.modules.people.dtos.NamedMonsterDTO;
import com.mcommings.campaigner.modules.people.entities.NamedMonster;
import com.mcommings.campaigner.modules.people.services.NamedMonsterService;
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

@WebMvcTest(NamedMonsterController.class)
public class NamedMonsterControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    NamedMonsterService namedMonsterService;

    private static final int VALID_NAMED_MONSTER_ID = 1;
    private static final int INVALID_NAMED_MONSTER_ID = 999;
    private static final String URI = "/api/namedMonsters";
    private NamedMonster entity;
    private NamedMonsterDTO dto;
    private Random random = new Random();

    @BeforeEach
    void setUp() {
        entity = new NamedMonster();
        entity.setId(1);
        entity.setFirstName("Monster");
        entity.setLastName("Mash");
        entity.setTitle("The biggest, baddest monster of them all.");
        entity.setFk_wealth(random.nextInt(100) + 1);
        entity.setFk_ability_score(random.nextInt(100) + 1);
        entity.setIsEnemy(false);
        entity.setPersonality("This is a namedMonsterality");
        entity.setDescription("This is a description");
        entity.setNotes("This is a note");
        entity.setFk_campaign_uuid(UUID.randomUUID());

        dto = new NamedMonsterDTO();
        dto.setId(entity.getId());
        dto.setFirstName(entity.getFirstName());
        dto.setLastName(entity.getLastName());
        dto.setTitle(entity.getTitle());
        dto.setFk_wealth(entity.getFk_wealth());
        dto.setFk_ability_score(entity.getFk_ability_score());
        dto.setIsEnemy(entity.getIsEnemy());
        dto.setPersonality(entity.getPersonality());
        dto.setDescription(entity.getDescription());
        dto.setNotes(entity.getNotes());
        dto.setFk_campaign_uuid(entity.getFk_campaign_uuid());
    }

    @Test
    void whenThereAreNamedMonsters_getNamedMonsters_ReturnsNamedMonsters() throws Exception {
        when(namedMonsterService.getNamedMonsters()).thenReturn(List.of(dto));

        String response = objectMapper.writeValueAsString(List.of(dto));

        mockMvc.perform(get(URI))
                .andExpect(status().isOk())
                .andExpect(content().json(response));
    }

    @Test
    void whenThereAreNamedMonsters_getNamedMonsters_ReturnsEmptyList() throws Exception {
        when(namedMonsterService.getNamedMonsters()).thenReturn(List.of());

        mockMvc.perform(get(URI))
                .andExpect(status().isOk())
                .andExpect(content().json("[]"));
    }

    @Test
    void whenThereIsANamedMonster_getNamedMonster_ReturnsNamedMonster() throws Exception {
        when(namedMonsterService.getNamedMonster(VALID_NAMED_MONSTER_ID)).thenReturn(Optional.of(dto));

        mockMvc.perform(get(URI + "/" + VALID_NAMED_MONSTER_ID))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(VALID_NAMED_MONSTER_ID))
                .andExpect(jsonPath("$.description").value(dto.getDescription()));
    }

    @Test
    void whenThereIsNotANamedMonster_getNamedMonster_ThrowsIllegalArgumentException() throws Exception {
        when(namedMonsterService.getNamedMonster(INVALID_NAMED_MONSTER_ID)).thenReturn(Optional.empty());

        mockMvc.perform(get(URI + "/" + INVALID_NAMED_MONSTER_ID))
                .andExpect(status().isBadRequest())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof IllegalArgumentException))
                .andExpect(result -> assertEquals(ID_NOT_FOUND.message, result.getResolvedException().getMessage()));
    }

    @Test
    void whenUserEntersWrongIdType_getNamedMonster_ReturnsBadRequest() throws Exception {
        mockMvc.perform(get(URI + "/abc"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void whenServiceFails_getNamedMonster_ReturnsInternalServerError() throws Exception {
        when(namedMonsterService.getNamedMonster(VALID_NAMED_MONSTER_ID)).thenThrow(new RuntimeException("Database failure"));

        mockMvc.perform(get(URI + "/" + VALID_NAMED_MONSTER_ID))
                .andExpect(status().isInternalServerError())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof RuntimeException))
                .andExpect(result -> assertEquals("Database failure", result.getResolvedException().getMessage()));
    }

    @Test
    void whenServiceReturnsNull_getNamedMonster_ReturnsInternalServerError() throws Exception {
        when(namedMonsterService.getNamedMonster(VALID_NAMED_MONSTER_ID)).thenReturn(null);

        mockMvc.perform(get(URI + "/" + VALID_NAMED_MONSTER_ID))
                .andExpect(status().isInternalServerError());
    }

    @Test
    void whenCampaignUUIDIsValid_getNamedMonstersByCampaignUUID_ReturnsNamedMonsters() throws Exception {
        UUID uuid = UUID.randomUUID();
        when(namedMonsterService.getNamedMonstersByCampaignUUID(uuid)).thenReturn(List.of(dto));

        String response = objectMapper.writeValueAsString(List.of(dto));

        mockMvc.perform(get(URI + "/campaign/" + uuid))
                .andExpect(status().isOk())
                .andExpect(content().json(response));
    }

    @Test
    void whenCampaignUUIDIsValid_getNamedMonstersByCampaignUUID_ReturnsEmptyList() throws Exception {
        UUID uuid = UUID.randomUUID();
        when(namedMonsterService.getNamedMonstersByCampaignUUID(uuid)).thenReturn(List.of());

        mockMvc.perform(get(URI + "/campaign/" + uuid))
                .andExpect(status().isOk())
                .andExpect(content().json("[]"));
    }

    @Test
    void whenUserEntersWrongCampaignUUIDType_getNamedMonster_ReturnsBadRequest() throws Exception {
        mockMvc.perform(get(URI + "/abc"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void whenAbilityScoreIdIsValid_getNamedMonstersByAbilityScoreId_ReturnsNamedMonsters() throws Exception {
        int abilityScoreId = random.nextInt(100) + 1;
        when(namedMonsterService.getNamedMonstersByAbilityScore(abilityScoreId)).thenReturn(List.of(dto));

        String response = objectMapper.writeValueAsString(List.of(dto));

        mockMvc.perform(get(URI + "/abilityScore/" + abilityScoreId))
                .andExpect(status().isOk())
                .andExpect(content().json(response));
    }

    @Test
    void whenAbilityScoreIdIsNotValid_getNamedMonstersByAbilityScore_ReturnsEmptyList() throws Exception {
        int abilityScoreId = random.nextInt(100) + 1;
        when(namedMonsterService.getNamedMonstersByAbilityScore(abilityScoreId)).thenReturn(List.of());

        mockMvc.perform(get(URI + "/abilityScore/" + abilityScoreId))
                .andExpect(status().isOk())
                .andExpect(content().json("[]"));
    }

    @Test
    void whenGenericMonsterIdIsValid_getNamedMonstersByGenericMonster_ReturnsNamedMonsters() throws Exception {
        int genericMonsterId = random.nextInt(100) + 1;
        when(namedMonsterService.getNamedMonstersByGenericMonster(genericMonsterId)).thenReturn(List.of(dto));

        String response = objectMapper.writeValueAsString(List.of(dto));

        mockMvc.perform(get(URI + "/genericMonster/" + genericMonsterId))
                .andExpect(status().isOk())
                .andExpect(content().json(response));
    }

    @Test
    void whenGenericMonsterIdIsNotValid_getNamedMonstersByGenericMonster_ReturnsEmptyList() throws Exception {
        int genericMonsterId = random.nextInt(100) + 1;
        when(namedMonsterService.getNamedMonstersByGenericMonster(genericMonsterId)).thenReturn(List.of());

        mockMvc.perform(get(URI + "/genericMonster/" + genericMonsterId))
                .andExpect(status().isOk())
                .andExpect(content().json("[]"));
    }

    @Test
    void whenIsEnemy_getNamedMonstersByEnemyStatus_ReturnsEnemies() throws Exception {
        NamedMonsterDTO enemy1 = new NamedMonsterDTO();
        enemy1.setId(1);
        enemy1.setFirstName("Enemy");
        enemy1.setLastName("One");
        enemy1.setIsEnemy(true);

        NamedMonsterDTO enemy2 = new NamedMonsterDTO();
        enemy2.setId(2);
        enemy2.setFirstName("Enemy");
        enemy2.setLastName("Two");
        enemy2.setIsEnemy(true);

        NamedMonsterDTO nonEnemy = new NamedMonsterDTO();
        nonEnemy.setId(3);
        nonEnemy.setFirstName("Friend");
        nonEnemy.setLastName("Three");
        nonEnemy.setIsEnemy(false);

        List<NamedMonsterDTO> expectedEnemies = List.of(enemy1, enemy2);
        when(namedMonsterService.getNamedMonstersByEnemyStatus(true)).thenReturn(expectedEnemies);

        String expectedJson = objectMapper.writeValueAsString(expectedEnemies);

        mockMvc.perform(get(URI + "/enemy/true"))
                .andExpect(status().isOk())
                .andExpect(content().json(expectedJson));

        verify(namedMonsterService, times(1)).getNamedMonstersByEnemyStatus(true);
    }

    @Test
    void whenIsNotEnemy_getNamedMonstersByEnemyStatus_ReturnsNonEnemies() throws Exception {
        NamedMonsterDTO enemy1 = new NamedMonsterDTO();
        enemy1.setId(1);
        enemy1.setFirstName("Enemy");
        enemy1.setLastName("One");
        enemy1.setIsEnemy(true);

        NamedMonsterDTO enemy2 = new NamedMonsterDTO();
        enemy2.setId(2);
        enemy2.setFirstName("Enemy");
        enemy2.setLastName("Two");
        enemy2.setIsEnemy(true);

        NamedMonsterDTO nonEnemy = new NamedMonsterDTO();
        nonEnemy.setId(3);
        nonEnemy.setFirstName("Friend");
        nonEnemy.setLastName("Three");
        nonEnemy.setIsEnemy(false);

        List<NamedMonsterDTO> expectedEnemies = List.of(nonEnemy);
        when(namedMonsterService.getNamedMonstersByEnemyStatus(false)).thenReturn(expectedEnemies);

        String expectedJson = objectMapper.writeValueAsString(expectedEnemies);

        mockMvc.perform(get(URI + "/enemy/false"))
                .andExpect(status().isOk())
                .andExpect(content().json(expectedJson));

        verify(namedMonsterService, times(1)).getNamedMonstersByEnemyStatus(false);
    }

    @Test
    void whenNamedMonsterIsValid_saveNamedMonster_RespondsOkRequest() throws Exception {
        UUID uuid = UUID.randomUUID();
        NamedMonsterDTO requestDto = new NamedMonsterDTO();
        requestDto.setId(2);
        requestDto.setFirstName("This is a name");
        requestDto.setDescription("This is a description");
        requestDto.setFk_campaign_uuid(uuid);
        requestDto.setIsEnemy(false);

        String requestJson = objectMapper.writeValueAsString(requestDto);

        mockMvc.perform(post("/api/namedMonsters")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andExpect(status().isOk());

        verify(namedMonsterService, times(1)).saveNamedMonster(any(NamedMonsterDTO.class));
    }

    @Test
    void whenNamedMonsterIsNotValid_saveNamedMonster_RespondsBadRequest() throws Exception {
        NamedMonsterDTO invalidNamedMonster = new NamedMonsterDTO();
        invalidNamedMonster.setId(2);
        invalidNamedMonster.setDescription("This is a description");
        invalidNamedMonster.setFk_campaign_uuid(null);
        invalidNamedMonster.setIsEnemy(true);

        String requestJson = objectMapper.writeValueAsString(invalidNamedMonster);

        doThrow(new IllegalArgumentException("Campaign UUID cannot be null or empty."))
                .when(namedMonsterService).saveNamedMonster(any(NamedMonsterDTO.class));

        MvcResult result = mockMvc.perform(post("/api/namedMonsters")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andExpect(status().isBadRequest())
                .andReturn();

        String responseBody = result.getResponse().getContentAsString();
        ObjectMapper mapper = new ObjectMapper();
        List<String> errors = mapper.readValue(responseBody, new TypeReference<List<String>>() {
        });

        assertTrue(errors.contains("First name cannot be empty."));
        assertTrue(errors.contains("Campaign UUID cannot be null or empty."));

        verify(namedMonsterService, times(0)).saveNamedMonster(any(NamedMonsterDTO.class));
    }

    @Test
    void whenNamedMonsterIdIsValid_deleteNamedMonster_RespondsOkRequest() throws Exception {
        when(namedMonsterService.getNamedMonster(VALID_NAMED_MONSTER_ID)).thenReturn(Optional.of(dto));

        mockMvc.perform(delete(URI + "/" + VALID_NAMED_MONSTER_ID))
                .andExpect(status().isOk());

        verify(namedMonsterService, times(1)).deleteNamedMonster(VALID_NAMED_MONSTER_ID);
    }

    @Test
    void whenNamedMonsterIdIsInvalid_deleteNamedMonster_RespondsBadRequest() throws Exception {
        doThrow(new IllegalArgumentException("Unable to delete; This item was not found."))
                .when(namedMonsterService).deleteNamedMonster(INVALID_NAMED_MONSTER_ID);

        mockMvc.perform(delete(URI + "/" + INVALID_NAMED_MONSTER_ID))
                .andExpect(status().isBadRequest())  // Expecting 400 Bad Request
                .andExpect(content().string("Unable to delete; This item was not found."));

        verify(namedMonsterService, times(1)).deleteNamedMonster(INVALID_NAMED_MONSTER_ID);
    }

    @Test
    void whenNamedMonsterIdIsValid_updateNamedMonster_RespondsOkRequest() throws Exception {
        NamedMonsterDTO updatedDto = new NamedMonsterDTO();
        updatedDto.setId(VALID_NAMED_MONSTER_ID);
        updatedDto.setDescription("Updated description");
        updatedDto.setFk_campaign_uuid(UUID.randomUUID());

        String json = objectMapper.writeValueAsString(updatedDto);

        mockMvc.perform(put(URI + "/" + VALID_NAMED_MONSTER_ID)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk());

        verify(namedMonsterService, times(1)).updateNamedMonster(eq(VALID_NAMED_MONSTER_ID), any(NamedMonsterDTO.class));
    }

    @Test
    void whenNamedMonsterIdIsInvalid_updateNamedMonster_RespondsBadRequest() throws Exception {
        NamedMonsterDTO updatedDto = new NamedMonsterDTO();
        updatedDto.setId(INVALID_NAMED_MONSTER_ID);
        updatedDto.setDescription("Some update");
        updatedDto.setFk_campaign_uuid(UUID.randomUUID());

        String json = objectMapper.writeValueAsString(updatedDto);

        doThrow(new IllegalArgumentException("Unable to update; This item was not found."))
                .when(namedMonsterService).updateNamedMonster(eq(INVALID_NAMED_MONSTER_ID), any(NamedMonsterDTO.class));

        mockMvc.perform(put(URI + "/" + INVALID_NAMED_MONSTER_ID)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isBadRequest())
                .andExpect(content().string(containsString("Unable to update; This item was not found.")));
    }

    @Test
    void whenNamedMonsterNameIsInvalid_updateNamedMonster_RespondsBadRequest() throws Exception {
        NamedMonsterDTO invalidDto = new NamedMonsterDTO();
        invalidDto.setId(VALID_NAMED_MONSTER_ID);
        invalidDto.setDescription("");
        invalidDto.setFk_campaign_uuid(UUID.randomUUID());

        String json = objectMapper.writeValueAsString(invalidDto);

        when(namedMonsterService.updateNamedMonster(eq(VALID_NAMED_MONSTER_ID), any(NamedMonsterDTO.class)))
                .thenThrow(new IllegalArgumentException("Item name cannot be null or empty."));

        mockMvc.perform(put(URI + "/" + VALID_NAMED_MONSTER_ID)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isBadRequest())
                .andExpect(content().string(containsString("Item name cannot be null or empty.")));
    }
}
