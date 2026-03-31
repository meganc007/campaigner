package com.mcommings.campaigner.services.items;

import com.mcommings.campaigner.modules.common.entities.Campaign;
import com.mcommings.campaigner.modules.common.repositories.ICampaignRepository;
import com.mcommings.campaigner.modules.items.dtos.weapons.CreateWeaponDTO;
import com.mcommings.campaigner.modules.items.dtos.weapons.UpdateWeaponDTO;
import com.mcommings.campaigner.modules.items.dtos.weapons.ViewWeaponDTO;
import com.mcommings.campaigner.modules.items.entities.Weapon;
import com.mcommings.campaigner.modules.items.mappers.WeaponMapper;
import com.mcommings.campaigner.modules.items.repositories.IWeaponRepository;
import com.mcommings.campaigner.modules.items.services.WeaponService;
import com.mcommings.campaigner.setup.items.factories.ItemsTestDataFactory;
import com.mcommings.campaigner.setup.items.fixtures.ItemsTestConstants;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class WeaponTest {

    @Mock
    private IWeaponRepository weaponRepository;

    @Mock
    private ICampaignRepository campaignRepository;

    @Mock
    private WeaponMapper weaponMapper;

    @InjectMocks
    private WeaponService weaponService;

    private Weapon weapon;
    private ViewWeaponDTO viewDto;
    private CreateWeaponDTO createDto;
    private UpdateWeaponDTO updateDto;

    @BeforeEach
    void setUp() {
        weapon = ItemsTestDataFactory.weapon();
        viewDto = ItemsTestDataFactory.viewWeaponDTO();
        createDto = ItemsTestDataFactory.createWeaponDTO();
        updateDto = ItemsTestDataFactory.updateWeaponDTO();
    }

    @Test
    void getAll_returnsMappedDtos() {

        when(weaponRepository.findAll()).thenReturn(List.of(weapon));
        when(weaponMapper.toDto(weapon)).thenReturn(viewDto);

        List<ViewWeaponDTO> result = weaponService.getAll();

        assertEquals(1, result.size());
        assertEquals(viewDto, result.get(0));

        verify(weaponRepository).findAll();
        verify(weaponMapper).toDto(weapon);
    }

    @Test
    void getWeaponsByCampaignUUID_returnsMappedList() {

        when(weaponRepository.findByCampaign_Uuid(ItemsTestConstants.CAMPAIGN_UUID))
                .thenReturn(List.of(weapon));

        when(weaponMapper.toDto(weapon))
                .thenReturn(viewDto);

        List<ViewWeaponDTO> result =
                weaponService.getWeaponsByCampaignUUID(
                        ItemsTestConstants.CAMPAIGN_UUID);

        assertEquals(1, result.size());
        assertEquals(viewDto, result.get(0));

        verify(weaponRepository)
                .findByCampaign_Uuid(ItemsTestConstants.CAMPAIGN_UUID);

        verify(weaponMapper).toDto(weapon);
    }

    @Test
    void getWeaponsByWeaponTypeId_returnsMappedList() {
        when(weaponRepository.findByWeaponType_Id(ItemsTestConstants.WEAPON_TYPE_ID))
                .thenReturn(List.of(weapon));

        when(weaponMapper.toDto(weapon))
                .thenReturn(viewDto);

        List<ViewWeaponDTO> result =
                weaponService.getWeaponsByWeaponTypeId(
                        ItemsTestConstants.WEAPON_TYPE_ID);

        assertEquals(1, result.size());
        assertEquals(viewDto, result.get(0));

        verify(weaponRepository)
                .findByWeaponType_Id((ItemsTestConstants.WEAPON_TYPE_ID));

        verify(weaponMapper).toDto(weapon);
    }

    @Test
    void getWeaponsByDamageTypeId_returnsMappedList() {
        when(weaponRepository.findByDamageType_Id(ItemsTestConstants.DAMAGE_TYPE_ID))
                .thenReturn(List.of(weapon));

        when(weaponMapper.toDto(weapon))
                .thenReturn(viewDto);

        List<ViewWeaponDTO> result =
                weaponService.getWeaponsByDamageTypeId(
                        ItemsTestConstants.WEAPON_TYPE_ID);

        assertEquals(1, result.size());
        assertEquals(viewDto, result.get(0));

        verify(weaponRepository)
                .findByDamageType_Id((ItemsTestConstants.DAMAGE_TYPE_ID));

        verify(weaponMapper).toDto(weapon);
    }

    @Test
    void getWeaponsByDiceTypeId_returnsMappedList() {
        when(weaponRepository.findByDiceType_Id(ItemsTestConstants.DICE_TYPE_ID))
                .thenReturn(List.of(weapon));

        when(weaponMapper.toDto(weapon))
                .thenReturn(viewDto);

        List<ViewWeaponDTO> result =
                weaponService.getWeaponsByDiceTypeId(
                        ItemsTestConstants.WEAPON_TYPE_ID);

        assertEquals(1, result.size());
        assertEquals(viewDto, result.get(0));

        verify(weaponRepository)
                .findByDiceType_Id((ItemsTestConstants.DICE_TYPE_ID));

        verify(weaponMapper).toDto(weapon);
    }

    @Test
    void getById_whenExists_returnsDto() {

        when(weaponRepository.findById(weapon.getId()))
                .thenReturn(Optional.of(weapon));

        when(weaponMapper.toDto(weapon))
                .thenReturn(viewDto);

        ViewWeaponDTO result = weaponService.getById(weapon.getId());

        assertEquals(viewDto, result);

        verify(weaponRepository).findById(weapon.getId());
        verify(weaponMapper).toDto(weapon);
    }

    @Test
    void getById_whenMissing_throwsException() {

        when(weaponRepository.findById(weapon.getId()))
                .thenReturn(Optional.empty());

        assertThrows(
                IllegalArgumentException.class,
                () -> weaponService.getById(weapon.getId())
        );

        verify(weaponRepository).findById(weapon.getId());
    }

    @Test
    void create_whenValid_savesAndReturnsDto() {

        Campaign campaign = new Campaign();
        campaign.setUuid(createDto.getCampaignUuid());

        when(weaponMapper.toEntity(createDto)).thenReturn(weapon);
        when(campaignRepository.getReferenceById(createDto.getCampaignUuid()))
                .thenReturn(campaign);

        when(weaponRepository.save(weapon)).thenReturn(weapon);
        when(weaponMapper.toDto(weapon)).thenReturn(viewDto);

        ViewWeaponDTO result = weaponService.create(createDto);

        assertEquals(viewDto, result);

        verify(weaponMapper).toEntity(createDto);
        verify(campaignRepository).getReferenceById(createDto.getCampaignUuid());
        verify(weaponRepository).save(weapon);
        verify(weaponMapper).toDto(weapon);
    }

    @Test
    void update_whenValid_updatesAndReturnsDto() {

        Campaign campaign = new Campaign();
        campaign.setUuid(updateDto.getCampaignUuid());

        when(weaponRepository.findById(updateDto.getId()))
                .thenReturn(Optional.of(weapon));

        when(campaignRepository.getReferenceById(updateDto.getCampaignUuid()))
                .thenReturn(campaign);

        when(weaponRepository.save(weapon)).thenReturn(weapon);
        when(weaponMapper.toDto(weapon)).thenReturn(viewDto);

        ViewWeaponDTO result = weaponService.update(updateDto);

        assertEquals(viewDto, result);

        verify(weaponRepository).findById(updateDto.getId());
        verify(weaponMapper).updateWeaponFromDto(updateDto, weapon);
        verify(campaignRepository).getReferenceById(updateDto.getCampaignUuid());
        verify(weaponRepository).save(weapon);
        verify(weaponMapper).toDto(weapon);
    }

    @Test
    void update_whenMissing_throwsException() {

        when(weaponRepository.findById(updateDto.getId()))
                .thenReturn(Optional.empty());

        assertThrows(
                IllegalArgumentException.class,
                () -> weaponService.update(updateDto)
        );

        verify(weaponRepository).findById(updateDto.getId());
    }

    @Test
    void delete_callsRepository() {

        weaponService.delete(weapon.getId());

        verify(weaponRepository).deleteById(weapon.getId());
    }
}
