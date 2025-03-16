package com.mcommings.campaigner.services.items;

import com.mcommings.campaigner.modules.items.dtos.WeaponDTO;
import com.mcommings.campaigner.modules.items.entities.Weapon;
import com.mcommings.campaigner.modules.items.mappers.WeaponMapper;
import com.mcommings.campaigner.modules.items.repositories.IWeaponRepository;
import com.mcommings.campaigner.modules.items.services.WeaponService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataIntegrityViolationException;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
public class WeaponTest {

    @Mock
    private WeaponMapper weaponMapper;

    @Mock
    private IWeaponRepository weaponRepository;

    @InjectMocks
    private WeaponService weaponService;

    private Weapon entity;
    private WeaponDTO dto;

    @BeforeEach
    void setUp() {
        Random random = new Random();
        entity = new Weapon();
        entity.setId(1);
        entity.setName("Test Weapon");
        entity.setDescription("A fictional weapon.");
        entity.setFk_campaign_uuid(UUID.randomUUID());
        entity.setRarity("Super rare.");
        entity.setGold_value(random.nextInt(100) + 1);
        entity.setSilver_value(random.nextInt(100) + 1);
        entity.setCopper_value(random.nextInt(100) + 1);
        entity.setWeight(1.0f);
        entity.setFk_weapon_type(random.nextInt(100) + 1);
        entity.setFk_damage_type(random.nextInt(100) + 1);
        entity.setFk_dice_type(random.nextInt(100) + 1);
        entity.setNumber_of_dice(random.nextInt(100) + 1);
        entity.setDamage_modifier(random.nextInt(100) + 1);
        entity.setIsMagical(true);
        entity.setIsCursed(false);
        entity.setNotes("This is a note");

        dto = new WeaponDTO();
        dto.setId(entity.getId());
        dto.setName(entity.getName());
        dto.setDescription(entity.getDescription());
        dto.setFk_campaign_uuid(entity.getFk_campaign_uuid());
        dto.setRarity(entity.getRarity());
        dto.setGold_value(entity.getGold_value());
        dto.setSilver_value(entity.getSilver_value());
        dto.setCopper_value(entity.getCopper_value());
        dto.setWeight(entity.getWeight());
        dto.setFk_weapon_type(entity.getFk_weapon_type());
        dto.setFk_damage_type(entity.getFk_damage_type());
        dto.setFk_dice_type(entity.getFk_dice_type());
        dto.setNumber_of_dice(entity.getNumber_of_dice());
        dto.setDamage_modifier(entity.getDamage_modifier());
        dto.setIsMagical(entity.getIsMagical());
        dto.setIsCursed(entity.getIsCursed());
        dto.setNotes(entity.getNotes());

        when(weaponMapper.mapToWeaponDto(entity)).thenReturn(dto);
        when(weaponMapper.mapFromWeaponDto(dto)).thenReturn(entity);
    }

    @Test
    public void whenThereAreWeapons_getWeapons_ReturnsWeapons() {
        when(weaponRepository.findAll()).thenReturn(List.of(entity));
        List<WeaponDTO> result = weaponService.getWeapons();

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("Test Weapon", result.get(0).getName());
    }

    @Test
    public void whenThereAreNoWeapons_getWeapons_ReturnsEmptyList() {
        when(weaponRepository.findAll()).thenReturn(Collections.emptyList());

        List<WeaponDTO> result = weaponService.getWeapons();

        assertNotNull(result);
        assertTrue(result.isEmpty(), "Expected an empty list when there are no weapons.");
    }

    @Test
    void whenThereIsAWeapon_getWeapon_ReturnsWeaponById() {
        when(weaponRepository.findById(1)).thenReturn(Optional.of(entity));

        Optional<WeaponDTO> result = weaponService.getWeapon(1);

        assertTrue(result.isPresent());
        assertEquals("Test Weapon", result.get().getName());
    }

    @Test
    void whenThereIsNotAWeapon_getWeapon_ReturnsNothing() {
        when(weaponRepository.findById(999)).thenReturn(Optional.empty());

        Optional<WeaponDTO> result = weaponService.getWeapon(999);

        assertTrue(result.isEmpty(), "Expected empty Optional when weapon is not found.");
    }

    @Test
    void whenCampaignUUIDIsValid_getWeaponsByCampaignUUID_ReturnsWeapons() {
        UUID campaignUUID = entity.getFk_campaign_uuid();
        when(weaponRepository.findByfk_campaign_uuid(campaignUUID)).thenReturn(List.of(entity));

        List<WeaponDTO> result = weaponService.getWeaponsByCampaignUUID(campaignUUID);

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(campaignUUID, result.get(0).getFk_campaign_uuid());
    }

    @Test
    void whenCampaignUUIDIsInvalid_getWeaponsByCampaignUUID_ReturnsNothing() {
        UUID campaignUUID = UUID.randomUUID();
        when(weaponRepository.findByfk_campaign_uuid(campaignUUID)).thenReturn(Collections.emptyList());

        List<WeaponDTO> result = weaponService.getWeaponsByCampaignUUID(campaignUUID);

        assertNotNull(result);
        assertTrue(result.isEmpty(), "Expected an empty list when no weapons match the campaign UUID.");
    }

    @Test
    void whenWeaponIsValid_saveWeapon_SavesTheWeapon() {
        when(weaponRepository.save(entity)).thenReturn(entity);

        weaponService.saveWeapon(dto);

        verify(weaponRepository, times(1)).save(entity);
    }

    @Test
    public void whenWeaponNameIsInvalid_saveWeapon_ThrowsIllegalArgumentException() {
        WeaponDTO weaponWithEmptyName = new WeaponDTO();
        weaponWithEmptyName.setId(1);
        weaponWithEmptyName.setName("");
        weaponWithEmptyName.setDescription("A fictional weapon.");
        weaponWithEmptyName.setFk_campaign_uuid(UUID.randomUUID());
        weaponWithEmptyName.setRarity("rare");
        weaponWithEmptyName.setGold_value(1);
        weaponWithEmptyName.setSilver_value(1);
        weaponWithEmptyName.setCopper_value(1);
        weaponWithEmptyName.setWeight(1f);
        weaponWithEmptyName.setFk_weapon_type(1);
        weaponWithEmptyName.setFk_damage_type(1);
        weaponWithEmptyName.setFk_dice_type(1);
        weaponWithEmptyName.setNumber_of_dice(1);
        weaponWithEmptyName.setDamage_modifier(1);
        weaponWithEmptyName.setIsMagical(true);
        weaponWithEmptyName.setIsCursed(true);
        weaponWithEmptyName.setNotes("note");

        WeaponDTO weaponWithNullName = new WeaponDTO();
        weaponWithNullName.setId(1);
        weaponWithNullName.setName(null);
        weaponWithNullName.setDescription("A fictional weapon.");
        weaponWithNullName.setFk_campaign_uuid(UUID.randomUUID());
        weaponWithNullName.setRarity("rare");
        weaponWithNullName.setGold_value(1);
        weaponWithNullName.setSilver_value(1);
        weaponWithNullName.setCopper_value(1);
        weaponWithNullName.setWeight(1f);
        weaponWithNullName.setFk_weapon_type(1);
        weaponWithNullName.setFk_damage_type(1);
        weaponWithNullName.setFk_dice_type(1);
        weaponWithNullName.setNumber_of_dice(1);
        weaponWithNullName.setDamage_modifier(1);
        weaponWithNullName.setIsMagical(true);
        weaponWithNullName.setIsCursed(true);
        weaponWithNullName.setNotes("note");

        assertThrows(IllegalArgumentException.class, () -> weaponService.saveWeapon(weaponWithEmptyName));
        assertThrows(IllegalArgumentException.class, () -> weaponService.saveWeapon(weaponWithNullName));
    }

    @Test
    public void whenWeaponNameAlreadyExists_saveWeapon_ThrowsDataIntegrityViolationException() {
        when(weaponRepository.findByName(dto.getName())).thenReturn(Optional.of(entity));
        assertThrows(DataIntegrityViolationException.class, () -> weaponService.saveWeapon(dto));
        verify(weaponRepository, times(1)).findByName(dto.getName());
        verify(weaponRepository, never()).save(any(Weapon.class));
    }

    @Test
    void whenWeaponIdExists_deleteWeapon_DeletesTheWeapon() {
        when(weaponRepository.existsById(1)).thenReturn(true);
        weaponService.deleteWeapon(1);
        verify(weaponRepository, times(1)).deleteById(1);
    }

    @Test
    void whenWeaponIdDoesNotExist_deleteWeapon_ThrowsIllegalArgumentException() {
        when(weaponRepository.existsById(999)).thenReturn(false);
        assertThrows(IllegalArgumentException.class, () -> weaponService.deleteWeapon(999));
    }

    @Test
    void whenDeleteWeaponFails_deleteWeapon_ThrowsException() {
        when(weaponRepository.existsById(1)).thenReturn(true);
        doThrow(new RuntimeException("Database error")).when(weaponRepository).deleteById(1);

        assertThrows(RuntimeException.class, () -> weaponService.deleteWeapon(1));
    }

    @Test
    void whenWeaponIdIsFound_updateWeapon_UpdatesTheWeapon() {
        WeaponDTO updateDTO = new WeaponDTO();
        updateDTO.setName("Updated Name");

        when(weaponRepository.findById(1)).thenReturn(Optional.of(entity));
        when(weaponRepository.existsById(1)).thenReturn(true);
        when(weaponRepository.save(entity)).thenReturn(entity);
        when(weaponMapper.mapToWeaponDto(entity)).thenReturn(updateDTO);

        Optional<WeaponDTO> result = weaponService.updateWeapon(1, updateDTO);

        assertTrue(result.isPresent());
        assertEquals("Updated Name", result.get().getName());
    }

    @Test
    void whenWeaponIdIsNotFound_updateWeapon_ReturnsEmptyOptional() {
        WeaponDTO updateDTO = new WeaponDTO();
        updateDTO.setName("Updated Name");

        when(weaponRepository.findById(999)).thenReturn(Optional.empty());
        assertThrows(IllegalArgumentException.class, () -> weaponService.updateWeapon(999, updateDTO));
    }

    @Test
    public void whenWeaponNameIsInvalid_updateWeapon_ThrowsIllegalArgumentException() {
        WeaponDTO updateEmptyName = new WeaponDTO();
        updateEmptyName.setName("");

        WeaponDTO updateNullName = new WeaponDTO();
        updateNullName.setName(null);

        when(weaponRepository.existsById(1)).thenReturn(true);

        assertThrows(IllegalArgumentException.class, () -> weaponService.updateWeapon(1, updateEmptyName));
        assertThrows(IllegalArgumentException.class, () -> weaponService.updateWeapon(1, updateNullName));
    }

    @Test
    public void whenWeaponNameAlreadyExists_updateWeapon_ThrowsDataIntegrityViolationException() {
        when(weaponRepository.findByName(dto.getName())).thenReturn(Optional.of(entity));

        assertThrows(IllegalArgumentException.class, () -> weaponService.updateWeapon(entity.getId(), dto));
    }
}
