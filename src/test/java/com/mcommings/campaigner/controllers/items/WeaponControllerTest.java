package com.mcommings.campaigner.controllers.items;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mcommings.campaigner.modules.items.controllers.WeaponController;
import com.mcommings.campaigner.modules.items.dtos.WeaponDTO;
import com.mcommings.campaigner.modules.items.entities.Weapon;
import com.mcommings.campaigner.modules.items.services.WeaponService;
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

@WebMvcTest(WeaponController.class)
public class WeaponControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    WeaponService weaponService;

    private static final int VALID_WEAPON_ID = 1;
    private static final int INVALID_WEAPON_ID = 999;
    private static final String URI = "/api/weapons";
    private Weapon entity;
    private WeaponDTO dto;
    private Random random = new Random();

    @BeforeEach
    void setUp() {
        entity = new Weapon();
        entity.setId(VALID_WEAPON_ID);
        entity.setName("A name.");
        entity.setDescription("A description.");
        entity.setFk_campaign_uuid(UUID.randomUUID());
        entity.setFk_weapon_type(random.nextInt(100) + 1);
        entity.setRarity("Common.");
        entity.setGold_value(random.nextInt(100) + 1);
        entity.setSilver_value(random.nextInt(100) + 1);
        entity.setCopper_value(random.nextInt(100) + 1);
        entity.setCopper_value(random.nextInt(100) + 1);
        entity.setWeight(random.nextFloat(100) + 1);
        entity.setIsMagical(true);
        entity.setIsCursed(false);
        entity.setNotes("Notes.");
        entity.setFk_weapon_type(random.nextInt(100) + 1);
        entity.setFk_damage_type(random.nextInt(100) + 1);
        entity.setFk_dice_type(random.nextInt(100) + 1);
        entity.setNumber_of_dice(random.nextInt(100) + 1);
        entity.setDamage_modifier(random.nextInt(100) + 1);


        dto = new WeaponDTO();
        dto.setId(entity.getId());
        dto.setName(entity.getName());
        dto.setDescription(entity.getDescription());
        dto.setFk_campaign_uuid(entity.getFk_campaign_uuid());
        dto.setFk_weapon_type(entity.getFk_weapon_type());
        dto.setGold_value(entity.getGold_value());
        dto.setSilver_value(entity.getSilver_value());
        dto.setCopper_value(entity.getCopper_value());
        dto.setWeight(entity.getWeight());
        dto.setIsMagical(entity.getIsMagical());
        dto.setIsCursed(entity.getIsCursed());
        dto.setNotes(entity.getNotes());
        dto.setFk_weapon_type(entity.getFk_weapon_type());
        dto.setFk_damage_type(entity.getFk_damage_type());
        dto.setFk_dice_type(entity.getFk_dice_type());
        dto.setNumber_of_dice(entity.getNumber_of_dice());
        dto.setDamage_modifier(entity.getDamage_modifier());
    }

    @Test
    void whenThereAreWeapons_getWeapons_ReturnsWeapons() throws Exception {
        when(weaponService.getWeapons()).thenReturn(List.of(dto));

        String response = objectMapper.writeValueAsString(List.of(dto));

        mockMvc.perform(get(URI))
                .andExpect(status().isOk())
                .andExpect(content().json(response));
    }

    @Test
    void whenThereAreWeapons_getWeapons_ReturnsEmptyList() throws Exception {
        when(weaponService.getWeapons()).thenReturn(List.of());

        mockMvc.perform(get(URI))
                .andExpect(status().isOk())
                .andExpect(content().json("[]"));
    }

    @Test
    void whenThereIsAWeapon_getWeapon_ReturnsWeapon() throws Exception {
        when(weaponService.getWeapon(VALID_WEAPON_ID)).thenReturn(Optional.of(dto));

        mockMvc.perform(get(URI + "/" + VALID_WEAPON_ID))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(VALID_WEAPON_ID))
                .andExpect(jsonPath("$.description").value(dto.getDescription()));
    }

    @Test
    void whenThereIsNotAWeapon_getWeapon_ThrowsIllegalArgumentException() throws Exception {
        when(weaponService.getWeapon(INVALID_WEAPON_ID)).thenReturn(Optional.empty());

        mockMvc.perform(get(URI + "/" + INVALID_WEAPON_ID))
                .andExpect(status().isBadRequest())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof IllegalArgumentException))
                .andExpect(result -> assertEquals(ID_NOT_FOUND.message, result.getResolvedException().getMessage()));
    }

    @Test
    void whenUserEntersWrongIdType_getWeapon_ReturnsBadRequest() throws Exception {
        mockMvc.perform(get(URI + "/abc"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void whenServiceFails_getWeapon_ReturnsInternalServerError() throws Exception {
        when(weaponService.getWeapon(VALID_WEAPON_ID)).thenThrow(new RuntimeException("Database failure"));

        mockMvc.perform(get(URI + "/" + VALID_WEAPON_ID))
                .andExpect(status().isInternalServerError())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof RuntimeException))
                .andExpect(result -> assertEquals("Database failure", result.getResolvedException().getMessage()));
    }

    @Test
    void whenServiceReturnsNull_getWeapon_ReturnsInternalServerError() throws Exception {
        when(weaponService.getWeapon(VALID_WEAPON_ID)).thenReturn(null);

        mockMvc.perform(get(URI + "/" + VALID_WEAPON_ID))
                .andExpect(status().isInternalServerError());
    }

    @Test
    void whenCampaignUUIDIsValid_getWeaponsByCampaignUUID_ReturnsWeapons() throws Exception {
        UUID uuid = UUID.randomUUID();
        when(weaponService.getWeaponsByCampaignUUID(uuid)).thenReturn(List.of(dto));

        String response = objectMapper.writeValueAsString(List.of(dto));

        mockMvc.perform(get(URI + "/campaign/" + uuid))
                .andExpect(status().isOk())
                .andExpect(content().json(response));
    }

    @Test
    void whenCampaignUUIDIsValid_getWeaponsByCampaignUUID_ReturnsEmptyList() throws Exception {
        UUID uuid = UUID.randomUUID();
        when(weaponService.getWeaponsByCampaignUUID(uuid)).thenReturn(List.of());

        mockMvc.perform(get(URI + "/campaign/" + uuid))
                .andExpect(status().isOk())
                .andExpect(content().json("[]"));
    }

    @Test
    void whenUserEntersWrongCampaignUUIDType_getWeapon_ReturnsBadRequest() throws Exception {
        mockMvc.perform(get(URI + "/abc"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void whenWeaponTypeIDIsValid_getWeaponsByWeaponType_ReturnsWeapons() throws Exception {
        int weaponTypeId = random.nextInt(100) + 1;
        when(weaponService.getWeaponsByWeaponType(weaponTypeId)).thenReturn(List.of(dto));

        String response = objectMapper.writeValueAsString(List.of(dto));

        mockMvc.perform(get(URI + "/weapontype/" + weaponTypeId))
                .andExpect(status().isOk())
                .andExpect(content().json(response));
    }

    @Test
    void whenWeaponTypeIDIsNotValid_getWeaponsByWeaponType_ReturnsEmptyList() throws Exception {
        int weaponTypeId = random.nextInt(100) + 1;
        when(weaponService.getWeaponsByWeaponType(weaponTypeId)).thenReturn(List.of());

        mockMvc.perform(get(URI + "/weapontype/" + weaponTypeId))
                .andExpect(status().isOk())
                .andExpect(content().json("[]"));
    }

    @Test
    void whenDamageTypeIDIsValid_getWeaponsByDamageType_ReturnsWeapons() throws Exception {
        int damageTypeId = random.nextInt(100) + 1;
        when(weaponService.getWeaponsByDamageType(damageTypeId)).thenReturn(List.of(dto));

        String response = objectMapper.writeValueAsString(List.of(dto));

        mockMvc.perform(get(URI + "/damagetype/" + damageTypeId))
                .andExpect(status().isOk())
                .andExpect(content().json(response));
    }

    @Test
    void whenDamageTypeIDIsNotValid_getWeaponsByDamageType_ReturnsEmptyList() throws Exception {
        int damageTypeId = random.nextInt(100) + 1;
        when(weaponService.getWeaponsByDamageType(damageTypeId)).thenReturn(List.of());

        mockMvc.perform(get(URI + "/damagetype/" + damageTypeId))
                .andExpect(status().isOk())
                .andExpect(content().json("[]"));
    }

    @Test
    void whenDiceTypeIDIsValid_getWeaponsByDiceType_ReturnsWeapons() throws Exception {
        int diceTypeId = random.nextInt(100) + 1;
        when(weaponService.getWeaponsByDiceType(diceTypeId)).thenReturn(List.of(dto));

        String response = objectMapper.writeValueAsString(List.of(dto));

        mockMvc.perform(get(URI + "/dicetype/" + diceTypeId))
                .andExpect(status().isOk())
                .andExpect(content().json(response));
    }

    @Test
    void whenDiceTypeIDIsNotValid_getWeaponsByDiceType_ReturnsEmptyList() throws Exception {
        int diceTypeId = random.nextInt(100) + 1;
        when(weaponService.getWeaponsByDiceType(diceTypeId)).thenReturn(List.of());

        mockMvc.perform(get(URI + "/dicetype/" + diceTypeId))
                .andExpect(status().isOk())
                .andExpect(content().json("[]"));
    }

    @Test
    void whenWeaponIsValid_saveWeapon_RespondsOkRequest() throws Exception {
        UUID uuid = UUID.randomUUID();
        WeaponDTO requestDto = new WeaponDTO();
        requestDto.setId(2);
        requestDto.setName("This is a name");
        requestDto.setDescription("This is a description");
        requestDto.setFk_campaign_uuid(uuid);
        requestDto.setFk_weapon_type(4);

        String requestJson = objectMapper.writeValueAsString(requestDto);

        mockMvc.perform(post("/api/weapons")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andExpect(status().isOk());

        verify(weaponService, times(1)).saveWeapon(any(WeaponDTO.class));
    }

    @Test
    void whenWeaponIsNotValid_saveWeapon_RespondsBadRequest() throws Exception {
        WeaponDTO invalidWeapon = new WeaponDTO();
        invalidWeapon.setId(2);
        invalidWeapon.setDescription("This is a description");
        invalidWeapon.setFk_campaign_uuid(null); // Invalid UUID
        invalidWeapon.setFk_weapon_type(4);

        String requestJson = objectMapper.writeValueAsString(invalidWeapon);

        doThrow(new IllegalArgumentException("Campaign UUID cannot be null or empty."))
                .when(weaponService).saveWeapon(any(WeaponDTO.class));

        MvcResult result = mockMvc.perform(post("/api/weapons")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andExpect(status().isBadRequest())
                .andReturn();

        String responseBody = result.getResponse().getContentAsString();
        ObjectMapper mapper = new ObjectMapper();
        List<String> errors = mapper.readValue(responseBody, new TypeReference<List<String>>() {
        });

        assertTrue(errors.contains("Weapon name cannot be empty"));
        assertTrue(errors.contains("Campaign UUID cannot be null or empty."));

        verify(weaponService, times(0)).saveWeapon(any(WeaponDTO.class));
    }

    @Test
    void whenWeaponIdIsValid_deleteWeapon_RespondsOkRequest() throws Exception {
        when(weaponService.getWeapon(VALID_WEAPON_ID)).thenReturn(Optional.of(dto));

        mockMvc.perform(delete(URI + "/" + VALID_WEAPON_ID))
                .andExpect(status().isOk());

        verify(weaponService, times(1)).deleteWeapon(VALID_WEAPON_ID);
    }

    @Test
    void whenWeaponIdIsInvalid_deleteWeapon_RespondsBadRequest() throws Exception {
        doThrow(new IllegalArgumentException("Unable to delete; This weapon was not found."))
                .when(weaponService).deleteWeapon(INVALID_WEAPON_ID);

        mockMvc.perform(delete(URI + "/" + INVALID_WEAPON_ID))
                .andExpect(status().isBadRequest())  // Expecting 400 Bad Request
                .andExpect(content().string("Unable to delete; This weapon was not found."));

        verify(weaponService, times(1)).deleteWeapon(INVALID_WEAPON_ID);
    }

    @Test
    void whenWeaponIdIsValid_updateWeapon_RespondsOkRequest() throws Exception {
        WeaponDTO updatedDto = new WeaponDTO();
        updatedDto.setId(VALID_WEAPON_ID);
        updatedDto.setDescription("Updated description");
        updatedDto.setFk_campaign_uuid(UUID.randomUUID());
        updatedDto.setFk_weapon_type(2);

        String json = objectMapper.writeValueAsString(updatedDto);

        mockMvc.perform(put(URI + "/" + VALID_WEAPON_ID)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk());

        verify(weaponService, times(1)).updateWeapon(eq(VALID_WEAPON_ID), any(WeaponDTO.class));
    }

    @Test
    void whenWeaponIdIsInvalid_updateWeapon_RespondsBadRequest() throws Exception {
        WeaponDTO updatedDto = new WeaponDTO();
        updatedDto.setId(INVALID_WEAPON_ID);
        updatedDto.setDescription("Some update");
        updatedDto.setFk_campaign_uuid(UUID.randomUUID());
        updatedDto.setFk_weapon_type(1);

        String json = objectMapper.writeValueAsString(updatedDto);

        doThrow(new IllegalArgumentException("Unable to update; This weapon was not found."))
                .when(weaponService).updateWeapon(eq(INVALID_WEAPON_ID), any(WeaponDTO.class));

        mockMvc.perform(put(URI + "/" + INVALID_WEAPON_ID)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isBadRequest())
                .andExpect(content().string(containsString("Unable to update; This weapon was not found.")));
    }

    @Test
    void whenWeaponNameIsInvalid_updateWeapon_RespondsBadRequest() throws Exception {
        WeaponDTO invalidDto = new WeaponDTO();
        invalidDto.setId(VALID_WEAPON_ID);
        invalidDto.setDescription("");
        invalidDto.setFk_campaign_uuid(UUID.randomUUID());
        invalidDto.setFk_weapon_type(1);

        String json = objectMapper.writeValueAsString(invalidDto);

        when(weaponService.updateWeapon(eq(VALID_WEAPON_ID), any(WeaponDTO.class)))
                .thenThrow(new IllegalArgumentException("Weapon name cannot be null or empty."));

        mockMvc.perform(put(URI + "/" + VALID_WEAPON_ID)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isBadRequest())
                .andExpect(content().string(containsString("Weapon name cannot be null or empty.")));
    }
}
