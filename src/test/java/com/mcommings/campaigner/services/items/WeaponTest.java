package com.mcommings.campaigner.services.items;

import com.mcommings.campaigner.items.entities.Weapon;
import com.mcommings.campaigner.items.repositories.IDamageTypeRepository;
import com.mcommings.campaigner.items.repositories.IDiceTypeRepository;
import com.mcommings.campaigner.items.repositories.IWeaponRepository;
import com.mcommings.campaigner.items.repositories.IWeaponTypeRepository;
import com.mcommings.campaigner.items.services.WeaponService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataIntegrityViolationException;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@SpringBootTest
public class WeaponTest {

    @Mock
    private IWeaponRepository weaponRepository;
    @Mock
    private IWeaponTypeRepository weaponTypeRepository;
    @Mock
    private IDamageTypeRepository damageTypeRepository;
    @Mock
    private IDiceTypeRepository diceTypeRepository;

    @InjectMocks
    private WeaponService weaponService;

    @Test
    public void whenThereAreWeapons_getWeapons_ReturnsWeapons() {
        List<Weapon> weapons = new ArrayList<>();
        UUID campaign = UUID.randomUUID();
        weapons.add(new Weapon(1, "Weapon 1", "Description 1", campaign));
        weapons.add(new Weapon(2, "Weapon 2", "Description 2", campaign));
        weapons.add(new Weapon(3, "Weapon 3", "Description 3", "Rare", 32, 20,
                12, 20.0f, 2, 2, 2, 6, 0, true, false, "Notes", campaign));
        when(weaponRepository.findAll()).thenReturn(weapons);

        List<Weapon> result = weaponService.getWeapons();

        Assertions.assertEquals(3, result.size());
        Assertions.assertEquals(weapons, result);
    }

    @Test
    public void whenThereAreNoWeapons_getWeapons_ReturnsNothing() {
        List<Weapon> weapons = new ArrayList<>();
        when(weaponRepository.findAll()).thenReturn(weapons);

        List<Weapon> result = weaponService.getWeapons();

        Assertions.assertEquals(0, result.size());
        Assertions.assertEquals(weapons, result);
    }

    @Test
    public void whenCampaignUUIDIsValid_getWeaponsByCampaignUUID_ReturnsWeapons() {
        List<Weapon> weapons = new ArrayList<>();
        UUID campaign = UUID.randomUUID();
        weapons.add(new Weapon(1, "Weapon 1", "Description 1", campaign));
        weapons.add(new Weapon(2, "Weapon 2", "Description 2", campaign));
        weapons.add(new Weapon(3, "Weapon 3", "Description 3", "Rare", 32, 20,
                12, 20.0f, 2, 2, 2, 6, 0,
                true, false, "Notes", campaign));
        when(weaponRepository.findByfk_campaign_uuid(campaign)).thenReturn(weapons);

        List<Weapon> results = weaponService.getWeaponsByCampaignUUID(campaign);

        Assertions.assertEquals(3, results.size());
        Assertions.assertEquals(weapons, results);
    }

    @Test
    public void whenCampaignUUIDIsInvalid_getWeaponsByCampaignUUID_ReturnsNothing() {
        UUID campaign = UUID.randomUUID();
        List<Weapon> weapons = new ArrayList<>();
        when(weaponRepository.findByfk_campaign_uuid(campaign)).thenReturn(weapons);

        List<Weapon> result = weaponService.getWeaponsByCampaignUUID(campaign);

        Assertions.assertEquals(0, result.size());
        Assertions.assertEquals(weapons, result);
    }

    @Test
    public void whenWeaponWithNoForeignKeysIsValid_saveWeapon_SavesTheWeapon() {
        Weapon weapon = new Weapon(1, "Weapon 1", "Description 1", UUID.randomUUID());
        when(weaponRepository.saveAndFlush(weapon)).thenReturn(weapon);

        assertDoesNotThrow(() -> weaponService.saveWeapon(weapon));

        verify(weaponRepository, times(1)).saveAndFlush(weapon);
    }

    @Test
    public void whenWeaponWithForeignKeysIsValid_saveWeapon_SavesTheWeapon() {
        Weapon weapon = new Weapon(1, "Weapon", "Description", "Rare", 32, 20,
                12, 20.0f, 2, 2, 2, 6, 0,
                true, false, "Notes", UUID.randomUUID());

        when(weaponTypeRepository.existsById(2)).thenReturn(true);
        when(damageTypeRepository.existsById(2)).thenReturn(true);
        when(diceTypeRepository.existsById(2)).thenReturn(true);
        when(weaponRepository.saveAndFlush(weapon)).thenReturn(weapon);

        assertDoesNotThrow(() -> weaponService.saveWeapon(weapon));

        verify(weaponRepository, times(1)).saveAndFlush(weapon);
    }

    @Test
    public void whenWeaponNameIsInvalid_saveWeapon_ThrowsIllegalArgumentException() {
        Weapon weaponWithEmptyName = new Weapon(1, "", "Description 1", UUID.randomUUID());
        Weapon weaponWithNullName = new Weapon(2, null, "Description 2", UUID.randomUUID());

        assertThrows(IllegalArgumentException.class, () -> weaponService.saveWeapon(weaponWithEmptyName));
        assertThrows(IllegalArgumentException.class, () -> weaponService.saveWeapon(weaponWithNullName));
    }

    @Test
    public void whenWeaponNameAlreadyExists_saveWeapon_ThrowsDataIntegrityViolationException() {
        Weapon weapon = new Weapon(1, "Weapon", "Description", "Rare", 32, 20,
                12, 20.0f, 2, 2, 2, 6, 0,
                true, false, "Notes", UUID.randomUUID());
        Weapon weaponWithDuplicatedName = new Weapon(2, "Weapon", "Description", "Rare", 32, 20,
                12, 20.0f, 2, 2, 2, 6, 0,
                true, false, "Notes", UUID.randomUUID());

        when(weaponRepository.existsById(1)).thenReturn(true);
        when(weaponTypeRepository.existsById(2)).thenReturn(true);
        when(damageTypeRepository.existsById(2)).thenReturn(true);
        when(diceTypeRepository.existsById(2)).thenReturn(true);

        when(weaponRepository.saveAndFlush(weapon)).thenReturn(weapon);
        when(weaponRepository.saveAndFlush(weaponWithDuplicatedName)).thenThrow(DataIntegrityViolationException.class);

        assertDoesNotThrow(() -> weaponService.saveWeapon(weapon));
        assertThrows(DataIntegrityViolationException.class, () -> weaponService.saveWeapon(weaponWithDuplicatedName));
    }

    @Test
    public void whenWeaponHasInvalidForeignKeys_saveWeapon_ThrowsDataIntegrityViolationException() {
        Weapon weapon = new Weapon(1, "Weapon", "Description", "Rare", 32, 20,
                12, 20.0f, 2, 2, 2, 6, 0,
                true, false, "Notes", UUID.randomUUID());

        when(weaponTypeRepository.existsById(2)).thenReturn(false);
        when(damageTypeRepository.existsById(2)).thenReturn(true);
        when(diceTypeRepository.existsById(2)).thenReturn(false);
        when(weaponRepository.saveAndFlush(weapon)).thenReturn(weapon);

        assertThrows(DataIntegrityViolationException.class, () -> weaponService.saveWeapon(weapon));
    }

    @Test
    public void whenWeaponIdExists_deleteWeapon_DeletesTheWeapon() {
        int weaponId = 1;
        when(weaponRepository.existsById(weaponId)).thenReturn(true);
        assertDoesNotThrow(() -> weaponService.deleteWeapon(weaponId));
        verify(weaponRepository, times(1)).deleteById(weaponId);
    }

    @Test
    public void whenWeaponIdDoesNotExist_deleteWeapon_ThrowsIllegalArgumentException() {
        int weaponId = 9000;
        when(weaponRepository.existsById(weaponId)).thenReturn(false);
        assertThrows(IllegalArgumentException.class, () -> weaponService.deleteWeapon(weaponId));
    }

    //    TODO: uncomment / fix test when classes that use Weapon as a fk are added
//    @Test
//    public void whenWeaponIdIsAForeignKey_deleteWeapon_ThrowsDataIntegrityViolationException() {
//        int weaponId = 1;
//        Place place = new Place(1, "Place", "Description", 1, weaponId, 1, 1, 1);
//        List<CrudRepository> repositories = new ArrayList<>(Arrays.asList(placeRepository, eventRepository));
//        List<Place> places = new ArrayList<>(Arrays.asList(place));
//
//        Event event = new Event(1, "Event", "Description", 1, 1, 1, 1, weaponId, 1, 1);
//        List<Event> events = new ArrayList<>(Arrays.asList(event));
//
//        when(weaponRepository.existsById(weaponId)).thenReturn(true);
//        when(placeRepository.findByfk_weapon(weaponId)).thenReturn(places);
//        when(eventRepository.findByfk_weapon(weaponId)).thenReturn(events);
//
//        boolean actual = RepositoryHelper.isForeignKey(repositories, FK_WEAPON.columnName, weaponId);
//        Assertions.assertTrue(actual);
//        assertThrows(DataIntegrityViolationException.class, () -> weaponService.deleteWeapon(weaponId));
//    }

    @Test
    public void whenWeaponIdWithNoFKIsFound_updateWeapon_UpdatesTheWeapon() {
        int weaponId = 1;
        UUID campaign = UUID.randomUUID();

        Weapon weapon = new Weapon(weaponId, "Old Weapon Name", "Old Description", campaign);
        Weapon updateNoFK = new Weapon(weaponId, "Updated Weapon Name", "Updated Description", campaign);

        when(weaponRepository.existsById(weaponId)).thenReturn(true);
        when(weaponRepository.findById(weaponId)).thenReturn(Optional.of(weapon));

        weaponService.updateWeapon(weaponId, updateNoFK);

        verify(weaponRepository).findById(weaponId);

        Weapon result1 = weaponRepository.findById(weaponId).get();
        Assertions.assertEquals(updateNoFK.getName(), result1.getName());
        Assertions.assertEquals(updateNoFK.getDescription(), result1.getDescription());
    }

    @Test
    public void whenWeaponIdWithValidFKIsFound_updateWeapon_UpdatesTheWeapon() {
        int weaponId = 2;
        UUID campaign = UUID.randomUUID();

        Weapon weapon = new Weapon(weaponId, "Weapon", "Description", "Rare", 32, 20,
                12, 20.0f, 2, 2, 2, 6, 0,
                true, false, "Notes", campaign);
        Weapon update = new Weapon(weaponId, "Weapon", "Description", "Rare", 3200, 2000,
                12, 10.0f, 2, 2, 2, 12, 5,
                true, false, "Notes", campaign);

        when(weaponRepository.existsById(weaponId)).thenReturn(true);
        when(weaponRepository.findById(weaponId)).thenReturn(Optional.of(weapon));
        when(weaponTypeRepository.existsById(2)).thenReturn(true);
        when(damageTypeRepository.existsById(2)).thenReturn(true);
        when(diceTypeRepository.existsById(2)).thenReturn(true);

        weaponService.updateWeapon(weaponId, update);

        verify(weaponRepository).findById(weaponId);

        Weapon result = weaponRepository.findById(weaponId).get();
        Assertions.assertEquals(update.getName(), result.getName());
        Assertions.assertEquals(update.getDescription(), result.getDescription());
        Assertions.assertEquals(update.getRarity(), result.getRarity());
        Assertions.assertEquals(update.getGold_value(), result.getGold_value());
        Assertions.assertEquals(update.getSilver_value(), result.getSilver_value());
        Assertions.assertEquals(update.getCopper_value(), result.getCopper_value());
        Assertions.assertEquals(update.getWeight(), result.getWeight());
        Assertions.assertEquals(update.getFk_weapon_type(), result.getFk_weapon_type());
        Assertions.assertEquals(update.getFk_damage_type(), result.getFk_damage_type());
        Assertions.assertEquals(update.getFk_dice_type(), result.getFk_dice_type());
        Assertions.assertEquals(update.getNumber_of_dice(), result.getNumber_of_dice());
        Assertions.assertEquals(update.getDamage_modifier(), result.getDamage_modifier());
        Assertions.assertEquals(update.getIsMagical(), result.getIsMagical());
        Assertions.assertEquals(update.getIsCursed(), result.getIsCursed());
        Assertions.assertEquals(update.getNotes(), result.getNotes());
    }

    @Test
    public void whenWeaponIdWithInvalidFKIsFound_updateWeapon_ThrowsDataIntegrityViolationException() {
        int weaponId = 2;
        UUID campaign = UUID.randomUUID();

        Weapon weapon = new Weapon(weaponId, "Weapon", "Description", "Rare", 32, 20,
                12, 20.0f, 2, 2, 2, 6, 0,
                true, false, "Notes", campaign);
        Weapon update = new Weapon(weaponId, "Weapon", "Description", "Rare", 3200, 2000,
                12, 10.0f, 2, 2, 2, 12, 5,
                true, false, "Notes", campaign);

        when(weaponRepository.existsById(weaponId)).thenReturn(true);
        when(weaponRepository.findById(weaponId)).thenReturn(Optional.of(weapon));
        when(weaponTypeRepository.existsById(2)).thenReturn(false);

        assertThrows(DataIntegrityViolationException.class, () -> weaponService.updateWeapon(weaponId, update));
    }

    @Test
    public void whenWeaponIdIsNotFound_updateWeapon_ThrowsIllegalArgumentException() {
        int weaponId = 1;
        Weapon weapon = new Weapon(weaponId, "Old Weapon Name", "Old Description", UUID.randomUUID());

        when(weaponRepository.existsById(weaponId)).thenReturn(false);

        assertThrows(IllegalArgumentException.class, () -> weaponService.updateWeapon(weaponId, weapon));
    }

    @Test
    public void whenSomeWeaponFieldsChanged_updateWeapon_OnlyUpdatesChangedFields() {
        int weaponId = 1;
        Weapon weapon = new Weapon(weaponId, "Weapon", "Description", "Rare", 32, 20,
                12, 20.0f, 2, 2, 2, 6, 0,
                true, false, "Notes", UUID.randomUUID());

        int newSilverValue = 0;
        Boolean newIsMagical = false;
        String newNotes = "This weapon was found at the base of a mountain.";

        Weapon update = new Weapon();
        update.setSilver_value(newSilverValue);
        update.setIsMagical(newIsMagical);
        update.setNotes(newNotes);

        when(weaponRepository.existsById(weaponId)).thenReturn(true);
        when(weaponTypeRepository.existsById(2)).thenReturn(true);
        when(damageTypeRepository.existsById(2)).thenReturn(true);
        when(diceTypeRepository.existsById(2)).thenReturn(true);
        when(weaponRepository.findById(weaponId)).thenReturn(Optional.of(weapon));

        weaponService.updateWeapon(weaponId, update);

        verify(weaponRepository).findById(weaponId);

        Weapon result = weaponRepository.findById(weaponId).get();

        Assertions.assertEquals(weapon.getName(), result.getName());
        Assertions.assertEquals(weapon.getDescription(), result.getDescription());
        Assertions.assertEquals(weapon.getRarity(), result.getRarity());
        Assertions.assertEquals(weapon.getGold_value(), result.getGold_value());
        Assertions.assertEquals(newSilverValue, result.getSilver_value());
        Assertions.assertEquals(weapon.getCopper_value(), result.getCopper_value());
        Assertions.assertEquals(weapon.getWeight(), result.getWeight());
        Assertions.assertEquals(weapon.getFk_weapon_type(), result.getFk_weapon_type());
        Assertions.assertEquals(weapon.getFk_damage_type(), result.getFk_damage_type());
        Assertions.assertEquals(weapon.getFk_dice_type(), result.getFk_dice_type());
        Assertions.assertEquals(weapon.getNumber_of_dice(), result.getNumber_of_dice());
        Assertions.assertEquals(weapon.getDamage_modifier(), result.getDamage_modifier());
        Assertions.assertEquals(newIsMagical, result.getIsMagical());
        Assertions.assertEquals(weapon.getIsCursed(), result.getIsCursed());
        Assertions.assertEquals(newNotes, result.getNotes());
    }
}
