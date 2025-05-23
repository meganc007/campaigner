package com.mcommings.campaigner.controllers.quests;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mcommings.campaigner.modules.quests.controllers.RewardController;
import com.mcommings.campaigner.modules.quests.dtos.RewardDTO;
import com.mcommings.campaigner.modules.quests.entities.Reward;
import com.mcommings.campaigner.modules.quests.services.RewardService;
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
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(RewardController.class)
public class RewardControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    RewardService rewardService;

    private static final int VALID_REWARD_ID = 1;
    private static final int INVALID_REWARD_ID = 999;
    private static final String URI = "/api/rewards";
    private Reward entity;
    private RewardDTO dto;
    private Random random = new Random();

    @BeforeEach
    void setUp() {
        entity = new Reward();
        entity.setId(1);
        entity.setDescription("This is a description.");
        entity.setNotes("This is a note.");
        entity.setFk_campaign_uuid(UUID.randomUUID());
        entity.setGold_value(random.nextInt(100) + 1);
        entity.setSilver_value(random.nextInt(100) + 1);
        entity.setCopper_value(random.nextInt(100) + 1);
        entity.setFk_item(random.nextInt(100) + 1);
        entity.setFk_weapon(random.nextInt(100) + 1);

        dto = new RewardDTO();
        dto.setId(entity.getId());
        dto.setDescription(entity.getDescription());
        dto.setNotes(entity.getNotes());
        dto.setFk_campaign_uuid(entity.getFk_campaign_uuid());
        dto.setGold_value(entity.getGold_value());
        dto.setSilver_value(entity.getSilver_value());
        dto.setCopper_value(entity.getCopper_value());
        dto.setFk_item(entity.getFk_item());
        dto.setFk_weapon(entity.getFk_weapon());
    }

    @Test
    void whenThereAreRewards_getRewards_ReturnsRewards() throws Exception {
        when(rewardService.getRewards()).thenReturn(List.of(dto));

        String response = objectMapper.writeValueAsString(List.of(dto));

        mockMvc.perform(get(URI))
                .andExpect(status().isOk())
                .andExpect(content().json(response));
    }

    @Test
    void whenThereAreRewards_getRewards_ReturnsEmptyList() throws Exception {
        when(rewardService.getRewards()).thenReturn(List.of());

        mockMvc.perform(get(URI))
                .andExpect(status().isOk())
                .andExpect(content().json("[]"));
    }

    @Test
    void whenThereIsAReward_getReward_ReturnsReward() throws Exception {
        when(rewardService.getReward(VALID_REWARD_ID)).thenReturn(Optional.of(dto));

        mockMvc.perform(get(URI + "/" + VALID_REWARD_ID))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(VALID_REWARD_ID))
                .andExpect(jsonPath("$.description").value(dto.getDescription()));
    }

    @Test
    void whenThereIsNotAReward_getReward_ThrowsIllegalArgumentException() throws Exception {
        when(rewardService.getReward(INVALID_REWARD_ID)).thenReturn(Optional.empty());

        mockMvc.perform(get(URI + "/" + INVALID_REWARD_ID))
                .andExpect(status().isBadRequest())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof IllegalArgumentException))
                .andExpect(result -> assertEquals(ID_NOT_FOUND.message, result.getResolvedException().getMessage()));
    }

    @Test
    void whenUserEntersWrongIdType_getReward_ReturnsBadRequest() throws Exception {
        mockMvc.perform(get(URI + "/abc"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void whenServiceFails_getReward_ReturnsInternalServerError() throws Exception {
        when(rewardService.getReward(VALID_REWARD_ID)).thenThrow(new RuntimeException("Database failure"));

        mockMvc.perform(get(URI + "/" + VALID_REWARD_ID))
                .andExpect(status().isInternalServerError())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof RuntimeException))
                .andExpect(result -> assertEquals("Database failure", result.getResolvedException().getMessage()));
    }

    @Test
    void whenServiceReturnsNull_getReward_ReturnsInternalServerError() throws Exception {
        when(rewardService.getReward(VALID_REWARD_ID)).thenReturn(null);

        mockMvc.perform(get(URI + "/" + VALID_REWARD_ID))
                .andExpect(status().isInternalServerError());
    }

    @Test
    void whenCampaignUUIDIsValid_getRewardsByCampaignUUID_ReturnsRewards() throws Exception {
        UUID uuid = UUID.randomUUID();
        when(rewardService.getRewardsByCampaignUUID(uuid)).thenReturn(List.of(dto));

        String response = objectMapper.writeValueAsString(List.of(dto));

        mockMvc.perform(get(URI + "/campaign/" + uuid))
                .andExpect(status().isOk())
                .andExpect(content().json(response));
    }

    @Test
    void whenCampaignUUIDIsValid_getRewardsByCampaignUUID_ReturnsEmptyList() throws Exception {
        UUID uuid = UUID.randomUUID();
        when(rewardService.getRewardsByCampaignUUID(uuid)).thenReturn(List.of());

        mockMvc.perform(get(URI + "/campaign/" + uuid))
                .andExpect(status().isOk())
                .andExpect(content().json("[]"));
    }

    @Test
    void whenUserEntersWrongCampaignUUIDType_getReward_ReturnsBadRequest() throws Exception {
        mockMvc.perform(get(URI + "/abc"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void whenKeywordIsValid_getRewardsWhereDescriptionContainsKeyword_ReturnsRewards() throws Exception {
        UUID uuid = UUID.randomUUID();
        String keyword = "magic sword";

        RewardDTO reward1 = new RewardDTO();
        reward1.setId(1);
        reward1.setDescription("A villager found a magic sword.");
        reward1.setFk_campaign_uuid(uuid);

        RewardDTO reward2 = new RewardDTO();
        reward2.setId(2);
        reward2.setDescription("A noble wants the party to retrieve their magic sword.");
        reward2.setFk_campaign_uuid(uuid);

        RewardDTO reward3 = new RewardDTO();
        reward3.setId(2);
        reward3.setDescription("You come across a winged cat hunting carrier pigeons.");
        reward3.setFk_campaign_uuid(uuid);

        List<RewardDTO> rewards = List.of(reward1, reward2);

        when(rewardService.getRewardsWhereDescriptionContainsKeyword(keyword)).thenReturn(rewards);

        MvcResult result = mockMvc.perform(get(URI + "/description/" + keyword))
                .andExpect(status().isOk())
                .andReturn();

        String responseJson = result.getResponse().getContentAsString();

        ObjectMapper objectMapper = new ObjectMapper();
        List<RewardDTO> returnedRewards = objectMapper.readValue(responseJson,
                objectMapper.getTypeFactory().constructCollectionType(List.class, RewardDTO.class));

        assertTrue(returnedRewards.contains(reward1));
        assertTrue(returnedRewards.contains(reward2));
        assertFalse(returnedRewards.contains(reward3));
    }

    @Test
    void whenKeywordIsValidButThereAreNoMatches_getRewardsWhereDescriptionContainsKeyword_ReturnsEmptyList() throws Exception {
        UUID uuid = UUID.randomUUID();
        String keyword = "dog";

        RewardDTO reward1 = new RewardDTO();
        reward1.setId(1);
        reward1.setDescription("A villager found a magic sword.");
        reward1.setFk_campaign_uuid(uuid);

        RewardDTO reward2 = new RewardDTO();
        reward2.setId(2);
        reward2.setDescription("A noble wants the party to retrieve their magic sword.");
        reward2.setFk_campaign_uuid(uuid);

        RewardDTO reward3 = new RewardDTO();
        reward3.setId(2);
        reward3.setDescription("You come across a winged cat hunting carrier pigeons.");
        reward3.setFk_campaign_uuid(uuid);

        when(rewardService.getRewardsWhereDescriptionContainsKeyword(keyword)).thenReturn(List.of());

        MvcResult result = mockMvc.perform(get(URI + "/description/" + keyword))
                .andExpect(status().isOk())
                .andReturn();

        String responseJson = result.getResponse().getContentAsString();

        ObjectMapper objectMapper = new ObjectMapper();
        List<RewardDTO> returnedRewards = objectMapper.readValue(responseJson,
                objectMapper.getTypeFactory().constructCollectionType(List.class, RewardDTO.class));

        assertFalse(returnedRewards.contains(reward1));
        assertFalse(returnedRewards.contains(reward2));
        assertFalse(returnedRewards.contains(reward3));
    }

    @Test
    void whenItemIDIsValid_getRewardsByItemId_ReturnsRewards() throws Exception {
        int itemId = random.nextInt(100) + 1;
        when(rewardService.getRewardsByItemId(itemId)).thenReturn(List.of(dto));

        String response = objectMapper.writeValueAsString(List.of(dto));

        mockMvc.perform(get(URI + "/item/" + itemId))
                .andExpect(status().isOk())
                .andExpect(content().json(response));
    }

    @Test
    void whenItemIDIsNotValid_getRewardsByItemId_ReturnsEmptyList() throws Exception {
        int itemId = random.nextInt(100) + 1;
        when(rewardService.getRewardsByItemId(itemId)).thenReturn(List.of());

        mockMvc.perform(get(URI + "/item/" + itemId))
                .andExpect(status().isOk())
                .andExpect(content().json("[]"));
    }

    @Test
    void whenWeaponIDIsValid_getRewardsByWeaponId_ReturnsRewards() throws Exception {
        int weaponId = random.nextInt(100) + 1;
        when(rewardService.getRewardsByWeaponId(weaponId)).thenReturn(List.of(dto));

        String response = objectMapper.writeValueAsString(List.of(dto));

        mockMvc.perform(get(URI + "/weapon/" + weaponId))
                .andExpect(status().isOk())
                .andExpect(content().json(response));
    }

    @Test
    void whenWeaponIDIsNotValid_getRewardsByWeaponId_ReturnsEmptyList() throws Exception {
        int weaponId = random.nextInt(100) + 1;
        when(rewardService.getRewardsByWeaponId(weaponId)).thenReturn(List.of());

        mockMvc.perform(get(URI + "/weapon/" + weaponId))
                .andExpect(status().isOk())
                .andExpect(content().json("[]"));
    }

    @Test
    void whenRewardIsValid_saveReward_RespondsOkRequest() throws Exception {
        UUID uuid = UUID.randomUUID();
        RewardDTO requestDto = new RewardDTO();
        requestDto.setId(2);
        requestDto.setDescription("This is a description");
        requestDto.setFk_campaign_uuid(uuid);

        String requestJson = objectMapper.writeValueAsString(requestDto);

        mockMvc.perform(post("/api/rewards")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andExpect(status().isOk());

        verify(rewardService, times(1)).saveReward(any(RewardDTO.class));
    }

    @Test
    void whenRewardIsNotValid_saveReward_RespondsBadRequest() throws Exception {
        RewardDTO invalidReward = new RewardDTO();
        invalidReward.setId(2);
        invalidReward.setDescription("");
        invalidReward.setFk_campaign_uuid(null); // Invalid UUID

        String requestJson = objectMapper.writeValueAsString(invalidReward);

        doThrow(new IllegalArgumentException("Campaign UUID cannot be null or empty."))
                .when(rewardService).saveReward(any(RewardDTO.class));

        MvcResult result = mockMvc.perform(post("/api/rewards")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andExpect(status().isBadRequest())
                .andReturn();

        String responseBody = result.getResponse().getContentAsString();
        ObjectMapper mapper = new ObjectMapper();
        List<String> errors = mapper.readValue(responseBody, new TypeReference<List<String>>() {
        });

        assertTrue(errors.contains("Reward description cannot be empty"));
        assertTrue(errors.contains("Campaign UUID cannot be null or empty."));

        verify(rewardService, times(0)).saveReward(any(RewardDTO.class));
    }

    @Test
    void whenRewardIdIsValid_deleteReward_RespondsOkRequest() throws Exception {
        when(rewardService.getReward(VALID_REWARD_ID)).thenReturn(Optional.of(dto));

        mockMvc.perform(delete(URI + "/" + VALID_REWARD_ID))
                .andExpect(status().isOk());

        verify(rewardService, times(1)).deleteReward(VALID_REWARD_ID);
    }

    @Test
    void whenRewardIdIsInvalid_deleteReward_RespondsBadRequest() throws Exception {
        doThrow(new IllegalArgumentException("Unable to delete; This item was not found."))
                .when(rewardService).deleteReward(INVALID_REWARD_ID);

        mockMvc.perform(delete(URI + "/" + INVALID_REWARD_ID))
                .andExpect(status().isBadRequest())  // Expecting 400 Bad Request
                .andExpect(content().string("Unable to delete; This item was not found."));

        verify(rewardService, times(1)).deleteReward(INVALID_REWARD_ID);
    }

    @Test
    void whenRewardIdIsValid_updateReward_RespondsOkRequest() throws Exception {
        RewardDTO updatedDto = new RewardDTO();
        updatedDto.setId(VALID_REWARD_ID);
        updatedDto.setDescription("Updated description");
        updatedDto.setFk_campaign_uuid(UUID.randomUUID());

        String json = objectMapper.writeValueAsString(updatedDto);

        mockMvc.perform(put(URI + "/" + VALID_REWARD_ID)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk());

        verify(rewardService, times(1)).updateReward(eq(VALID_REWARD_ID), any(RewardDTO.class));
    }

    @Test
    void whenRewardIdIsInvalid_updateReward_RespondsBadRequest() throws Exception {
        RewardDTO updatedDto = new RewardDTO();
        updatedDto.setId(INVALID_REWARD_ID);
        updatedDto.setDescription("Some update");
        updatedDto.setFk_campaign_uuid(UUID.randomUUID());

        String json = objectMapper.writeValueAsString(updatedDto);

        doThrow(new IllegalArgumentException("Unable to update; This item was not found."))
                .when(rewardService).updateReward(eq(INVALID_REWARD_ID), any(RewardDTO.class));

        mockMvc.perform(put(URI + "/" + INVALID_REWARD_ID)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isBadRequest())
                .andExpect(content().string(containsString("Unable to update; This item was not found.")));
    }

    @Test
    void whenRewardDescriptionIsInvalid_updateReward_RespondsBadRequest() throws Exception {
        RewardDTO invalidDto = new RewardDTO();
        invalidDto.setId(VALID_REWARD_ID);
        invalidDto.setDescription("");
        invalidDto.setFk_campaign_uuid(UUID.randomUUID());

        String json = objectMapper.writeValueAsString(invalidDto);

        when(rewardService.updateReward(eq(VALID_REWARD_ID), any(RewardDTO.class)))
                .thenThrow(new IllegalArgumentException("Item name cannot be null or empty."));

        mockMvc.perform(put(URI + "/" + VALID_REWARD_ID)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isBadRequest())
                .andExpect(content().string(containsString("Item name cannot be null or empty.")));
    }
}
