package com.mcommings.campaigner.controllers.people;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mcommings.campaigner.modules.people.controllers.PersonController;
import com.mcommings.campaigner.modules.people.dtos.PersonDTO;
import com.mcommings.campaigner.modules.people.entities.Person;
import com.mcommings.campaigner.modules.people.services.PersonService;
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

@WebMvcTest(PersonController.class)
public class PersonControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    PersonService personService;

    private static final int VALID_PERSON_ID = 1;
    private static final int INVALID_PERSON_ID = 999;
    private static final String URI = "/api/people";
    private Person entity;
    private PersonDTO dto;
    private Random random = new Random();

    @BeforeEach
    void setUp() {
        entity = new Person();
        entity.setId(VALID_PERSON_ID);
        entity.setFirstName("Jane");
        entity.setLastName("Doe");
        entity.setAge(random.nextInt(100) + 1);
        entity.setTitle("A random person");
        entity.setFk_race(random.nextInt(100) + 1);
        entity.setFk_wealth(random.nextInt(100) + 1);
        entity.setFk_ability_score(random.nextInt(100) + 1);
        entity.setIsNPC(true);
        entity.setIsEnemy(false);
        entity.setPersonality("This is a personality");
        entity.setDescription("This is a description");
        entity.setNotes("This is a note");
        entity.setFk_campaign_uuid(UUID.randomUUID());

        dto = new PersonDTO();
        dto.setId(entity.getId());
        dto.setFirstName(entity.getFirstName());
        dto.setLastName(entity.getLastName());
        dto.setAge(entity.getAge());
        dto.setTitle(entity.getTitle());
        dto.setFk_race(entity.getFk_race());
        dto.setFk_wealth(entity.getFk_wealth());
        dto.setFk_ability_score(entity.getFk_ability_score());
        dto.setIsNPC(entity.getIsNPC());
        dto.setIsEnemy(entity.getIsEnemy());
        dto.setPersonality(entity.getPersonality());
        dto.setDescription(entity.getDescription());
        dto.setNotes(entity.getNotes());
        dto.setFk_campaign_uuid(entity.getFk_campaign_uuid());
    }

    @Test
    void whenThereArePeople_getPeople_ReturnsPeople() throws Exception {
        when(personService.getPeople()).thenReturn(List.of(dto));

        String response = objectMapper.writeValueAsString(List.of(dto));

        mockMvc.perform(get(URI))
                .andExpect(status().isOk())
                .andExpect(content().json(response));
    }

    @Test
    void whenThereArePeople_getPeople_ReturnsEmptyList() throws Exception {
        when(personService.getPeople()).thenReturn(List.of());

        mockMvc.perform(get(URI))
                .andExpect(status().isOk())
                .andExpect(content().json("[]"));
    }

    @Test
    void whenThereIsAPerson_getPerson_ReturnsPerson() throws Exception {
        when(personService.getPerson(VALID_PERSON_ID)).thenReturn(Optional.of(dto));

        mockMvc.perform(get(URI + "/" + VALID_PERSON_ID))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(VALID_PERSON_ID))
                .andExpect(jsonPath("$.description").value(dto.getDescription()));
    }

    @Test
    void whenThereIsNotAPerson_getPerson_ThrowsIllegalArgumentException() throws Exception {
        when(personService.getPerson(INVALID_PERSON_ID)).thenReturn(Optional.empty());

        mockMvc.perform(get(URI + "/" + INVALID_PERSON_ID))
                .andExpect(status().isBadRequest())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof IllegalArgumentException))
                .andExpect(result -> assertEquals(ID_NOT_FOUND.message, result.getResolvedException().getMessage()));
    }

    @Test
    void whenUserEntersWrongIdType_getPerson_ReturnsBadRequest() throws Exception {
        mockMvc.perform(get(URI + "/abc"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void whenServiceFails_getPerson_ReturnsInternalServerError() throws Exception {
        when(personService.getPerson(VALID_PERSON_ID)).thenThrow(new RuntimeException("Database failure"));

        mockMvc.perform(get(URI + "/" + VALID_PERSON_ID))
                .andExpect(status().isInternalServerError())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof RuntimeException))
                .andExpect(result -> assertEquals("Database failure", result.getResolvedException().getMessage()));
    }

    @Test
    void whenServiceReturnsNull_getPerson_ReturnsInternalServerError() throws Exception {
        when(personService.getPerson(VALID_PERSON_ID)).thenReturn(null);

        mockMvc.perform(get(URI + "/" + VALID_PERSON_ID))
                .andExpect(status().isInternalServerError());
    }

    @Test
    void whenCampaignUUIDIsValid_getPeopleByCampaignUUID_ReturnsPeople() throws Exception {
        UUID uuid = UUID.randomUUID();
        when(personService.getPeopleByCampaignUUID(uuid)).thenReturn(List.of(dto));

        String response = objectMapper.writeValueAsString(List.of(dto));

        mockMvc.perform(get(URI + "/campaign/" + uuid))
                .andExpect(status().isOk())
                .andExpect(content().json(response));
    }

    @Test
    void whenCampaignUUIDIsValid_getPeopleByCampaignUUID_ReturnsEmptyList() throws Exception {
        UUID uuid = UUID.randomUUID();
        when(personService.getPeopleByCampaignUUID(uuid)).thenReturn(List.of());

        mockMvc.perform(get(URI + "/campaign/" + uuid))
                .andExpect(status().isOk())
                .andExpect(content().json("[]"));
    }

    @Test
    void whenUserEntersWrongCampaignUUIDType_getPerson_ReturnsBadRequest() throws Exception {
        mockMvc.perform(get(URI + "/abc"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void whenRaceIDIsValid_getPeopleByRace_ReturnsPeople() throws Exception {
        int raceId = random.nextInt(100) + 1;
        when(personService.getPeopleByRace(raceId)).thenReturn(List.of(dto));

        String response = objectMapper.writeValueAsString(List.of(dto));

        mockMvc.perform(get(URI + "/race/" + raceId))
                .andExpect(status().isOk())
                .andExpect(content().json(response));
    }

    @Test
    void whenRaceIDIsNotValid_getPeopleByRace_ReturnsEmptyList() throws Exception {
        int raceId = random.nextInt(100) + 1;
        when(personService.getPeopleByRace(raceId)).thenReturn(List.of());

        mockMvc.perform(get(URI + "/race/" + raceId))
                .andExpect(status().isOk())
                .andExpect(content().json("[]"));
    }

    @Test
    void whenAbilityScoreIdIsValid_getPeopleByAbilityScoreId_ReturnsPeople() throws Exception {
        int abilityScoreId = random.nextInt(100) + 1;
        when(personService.getPeopleByAbilityScore(abilityScoreId)).thenReturn(List.of(dto));

        String response = objectMapper.writeValueAsString(List.of(dto));

        mockMvc.perform(get(URI + "/abilityScore/" + abilityScoreId))
                .andExpect(status().isOk())
                .andExpect(content().json(response));
    }

    @Test
    void whenAbilityScoreIdIsNotValid_getPeopleByAbilityScore_ReturnsEmptyList() throws Exception {
        int abilityScoreId = random.nextInt(100) + 1;
        when(personService.getPeopleByAbilityScore(abilityScoreId)).thenReturn(List.of());

        mockMvc.perform(get(URI + "/abilityScore/" + abilityScoreId))
                .andExpect(status().isOk())
                .andExpect(content().json("[]"));
    }

    @Test
    void whenIsEnemy_getPeopleByEnemyStatus_ReturnsEnemies() throws Exception {
        PersonDTO enemy1 = new PersonDTO();
        enemy1.setId(1);
        enemy1.setFirstName("Enemy");
        enemy1.setLastName("One");
        enemy1.setIsEnemy(true);

        PersonDTO enemy2 = new PersonDTO();
        enemy2.setId(2);
        enemy2.setFirstName("Enemy");
        enemy2.setLastName("Two");
        enemy2.setIsEnemy(true);

        PersonDTO nonEnemy = new PersonDTO();
        nonEnemy.setId(3);
        nonEnemy.setFirstName("Friend");
        nonEnemy.setLastName("Three");
        nonEnemy.setIsEnemy(false);

        List<PersonDTO> expectedEnemies = List.of(enemy1, enemy2);
        when(personService.getPeopleByEnemyStatus(true)).thenReturn(expectedEnemies);

        String expectedJson = objectMapper.writeValueAsString(expectedEnemies);

        mockMvc.perform(get(URI + "/enemy/true"))
                .andExpect(status().isOk())
                .andExpect(content().json(expectedJson));

        verify(personService, times(1)).getPeopleByEnemyStatus(true);
    }

    @Test
    void whenIsNotEnemy_getPeopleByEnemyStatus_ReturnsNonEnemies() throws Exception {
        PersonDTO enemy1 = new PersonDTO();
        enemy1.setId(1);
        enemy1.setFirstName("Enemy");
        enemy1.setLastName("One");
        enemy1.setIsEnemy(true);

        PersonDTO enemy2 = new PersonDTO();
        enemy2.setId(2);
        enemy2.setFirstName("Enemy");
        enemy2.setLastName("Two");
        enemy2.setIsEnemy(true);

        PersonDTO nonEnemy = new PersonDTO();
        nonEnemy.setId(3);
        nonEnemy.setFirstName("Friend");
        nonEnemy.setLastName("Three");
        nonEnemy.setIsEnemy(false);

        List<PersonDTO> expectedEnemies = List.of(nonEnemy);
        when(personService.getPeopleByEnemyStatus(false)).thenReturn(expectedEnemies);

        String expectedJson = objectMapper.writeValueAsString(expectedEnemies);

        mockMvc.perform(get(URI + "/enemy/false"))
                .andExpect(status().isOk())
                .andExpect(content().json(expectedJson));

        verify(personService, times(1)).getPeopleByEnemyStatus(false);
    }

    @Test
    void whenIsNPC_getPeopleByNPCStatus_ReturnsNPCs() throws Exception {
        PersonDTO npc1 = new PersonDTO();
        npc1.setId(1);
        npc1.setFirstName("NPC");
        npc1.setLastName("One");
        npc1.setIsNPC(true);

        PersonDTO npc2 = new PersonDTO();
        npc2.setId(2);
        npc2.setFirstName("NPC");
        npc2.setLastName("Two");
        npc2.setIsNPC(true);

        PersonDTO playerCharacter = new PersonDTO();
        playerCharacter.setId(3);
        playerCharacter.setFirstName("Character");
        playerCharacter.setLastName("Three");
        playerCharacter.setIsNPC(false);

        List<PersonDTO> expectedNPCs = List.of(npc1, npc2);
        when(personService.getPeopleByNPCStatus(true)).thenReturn(expectedNPCs);

        String expectedJson = objectMapper.writeValueAsString(expectedNPCs);

        mockMvc.perform(get(URI + "/npc/true"))
                .andExpect(status().isOk())
                .andExpect(content().json(expectedJson));

        verify(personService, times(1)).getPeopleByNPCStatus(true);
    }

    @Test
    void whenIsNotNPC_getPeopleByNPCStatus_ReturnsPlayerCharacters() throws Exception {
        PersonDTO npc1 = new PersonDTO();
        npc1.setId(1);
        npc1.setFirstName("NPC");
        npc1.setLastName("One");
        npc1.setIsNPC(true);

        PersonDTO npc2 = new PersonDTO();
        npc2.setId(2);
        npc2.setFirstName("NPC");
        npc2.setLastName("Two");
        npc2.setIsNPC(true);

        PersonDTO playerCharacter = new PersonDTO();
        playerCharacter.setId(3);
        playerCharacter.setFirstName("Character");
        playerCharacter.setLastName("Three");
        playerCharacter.setIsNPC(false);

        List<PersonDTO> playerCharacters = List.of(npc1, npc2);
        when(personService.getPeopleByNPCStatus(false)).thenReturn(playerCharacters);

        String expectedJson = objectMapper.writeValueAsString(playerCharacters);

        mockMvc.perform(get(URI + "/npc/false"))
                .andExpect(status().isOk())
                .andExpect(content().json(expectedJson));

        verify(personService, times(1)).getPeopleByNPCStatus(false);
    }

    @Test
    void whenPersonIsValid_savePerson_RespondsOkRequest() throws Exception {
        UUID uuid = UUID.randomUUID();
        PersonDTO requestDto = new PersonDTO();
        requestDto.setId(2);
        requestDto.setFirstName("This is a name");
        requestDto.setDescription("This is a description");
        requestDto.setFk_campaign_uuid(uuid);
        requestDto.setIsEnemy(false);
        requestDto.setIsNPC(true);

        String requestJson = objectMapper.writeValueAsString(requestDto);

        mockMvc.perform(post("/api/people")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andExpect(status().isOk());

        verify(personService, times(1)).savePerson(any(PersonDTO.class));
    }

    @Test
    void whenPersonIsNotValid_savePerson_RespondsBadRequest() throws Exception {
        PersonDTO invalidPerson = new PersonDTO();
        invalidPerson.setId(2);
        invalidPerson.setDescription("This is a description");
        invalidPerson.setFk_campaign_uuid(null);
        invalidPerson.setIsEnemy(true);
        invalidPerson.setIsNPC(true);

        String requestJson = objectMapper.writeValueAsString(invalidPerson);

        doThrow(new IllegalArgumentException("Campaign UUID cannot be null or empty."))
                .when(personService).savePerson(any(PersonDTO.class));

        MvcResult result = mockMvc.perform(post("/api/people")
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

        verify(personService, times(0)).savePerson(any(PersonDTO.class));
    }

    @Test
    void whenPersonIdIsValid_deletePerson_RespondsOkRequest() throws Exception {
        when(personService.getPerson(VALID_PERSON_ID)).thenReturn(Optional.of(dto));

        mockMvc.perform(delete(URI + "/" + VALID_PERSON_ID))
                .andExpect(status().isOk());

        verify(personService, times(1)).deletePerson(VALID_PERSON_ID);
    }

    @Test
    void whenPersonIdIsInvalid_deletePerson_RespondsBadRequest() throws Exception {
        doThrow(new IllegalArgumentException("Unable to delete; This item was not found."))
                .when(personService).deletePerson(INVALID_PERSON_ID);

        mockMvc.perform(delete(URI + "/" + INVALID_PERSON_ID))
                .andExpect(status().isBadRequest())  // Expecting 400 Bad Request
                .andExpect(content().string("Unable to delete; This item was not found."));

        verify(personService, times(1)).deletePerson(INVALID_PERSON_ID);
    }

    @Test
    void whenPersonIdIsValid_updatePerson_RespondsOkRequest() throws Exception {
        PersonDTO updatedDto = new PersonDTO();
        updatedDto.setId(VALID_PERSON_ID);
        updatedDto.setDescription("Updated description");
        updatedDto.setFk_campaign_uuid(UUID.randomUUID());

        String json = objectMapper.writeValueAsString(updatedDto);

        mockMvc.perform(put(URI + "/" + VALID_PERSON_ID)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk());

        verify(personService, times(1)).updatePerson(eq(VALID_PERSON_ID), any(PersonDTO.class));
    }

    @Test
    void whenPersonIdIsInvalid_updatePerson_RespondsBadRequest() throws Exception {
        PersonDTO updatedDto = new PersonDTO();
        updatedDto.setId(INVALID_PERSON_ID);
        updatedDto.setDescription("Some update");
        updatedDto.setFk_campaign_uuid(UUID.randomUUID());

        String json = objectMapper.writeValueAsString(updatedDto);

        doThrow(new IllegalArgumentException("Unable to update; This item was not found."))
                .when(personService).updatePerson(eq(INVALID_PERSON_ID), any(PersonDTO.class));

        mockMvc.perform(put(URI + "/" + INVALID_PERSON_ID)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isBadRequest())
                .andExpect(content().string(containsString("Unable to update; This item was not found.")));
    }

    @Test
    void whenPersonNameIsInvalid_updatePerson_RespondsBadRequest() throws Exception {
        PersonDTO invalidDto = new PersonDTO();
        invalidDto.setId(VALID_PERSON_ID);
        invalidDto.setDescription("");
        invalidDto.setFk_campaign_uuid(UUID.randomUUID());

        String json = objectMapper.writeValueAsString(invalidDto);

        when(personService.updatePerson(eq(VALID_PERSON_ID), any(PersonDTO.class)))
                .thenThrow(new IllegalArgumentException("Item name cannot be null or empty."));

        mockMvc.perform(put(URI + "/" + VALID_PERSON_ID)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isBadRequest())
                .andExpect(content().string(containsString("Item name cannot be null or empty.")));
    }
}
