package com.mcommings.campaigner.controllers.items;

import com.mcommings.campaigner.controllers.BaseControllerTest;
import com.mcommings.campaigner.modules.items.controllers.WeaponController;
import com.mcommings.campaigner.modules.items.dtos.weapons.CreateWeaponDTO;
import com.mcommings.campaigner.modules.items.dtos.weapons.UpdateWeaponDTO;
import com.mcommings.campaigner.modules.items.dtos.weapons.ViewWeaponDTO;
import com.mcommings.campaigner.modules.items.services.WeaponService;
import com.mcommings.campaigner.setup.items.factories.ItemsTestDataFactory;
import com.mcommings.campaigner.setup.items.fixtures.ItemsTestConstants;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(WeaponController.class)
public class WeaponControllerTest extends BaseControllerTest {

    @MockitoBean
    WeaponService weaponService;

    //GET all
    @Test
    void getWeapons_returnsList() throws Exception {

        when(weaponService.getAll())
                .thenReturn(List.of(ItemsTestDataFactory.viewWeaponDTO()));

        get("/api/weapons")
                .andExpect(status().isOk());

        verify(weaponService).getAll();
    }

    //GET by id
    @Test
    void getWeapon_returnsWeapon() throws Exception {

        ViewWeaponDTO dto = ItemsTestDataFactory.viewWeaponDTO();

        when(weaponService.getById(1)).thenReturn(dto);

        get("/api/weapons/1")
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(dto.getId()));
    }

    //GET by WeaponTypeId
    @Test
    void getWeaponsByWeaponTypeId_returnsWeapons() throws Exception {

        when(weaponService.getWeaponsByWeaponTypeId(ItemsTestConstants.WEAPON_TYPE_ID))
                .thenReturn(List.of(ItemsTestDataFactory.viewWeaponDTO()));

        get("/api/weapons/weaponType/" + ItemsTestConstants.WEAPON_TYPE_ID)
                .andExpect(status().isOk());

        verify(weaponService)
                .getWeaponsByWeaponTypeId(ItemsTestConstants.WEAPON_TYPE_ID);
    }

    //GET by DamageTypeId
    @Test
    void getWeaponsByDamageTypeId_returnsWeapons() throws Exception {

        when(weaponService.getWeaponsByDamageTypeId(ItemsTestConstants.DAMAGE_TYPE_ID))
                .thenReturn(List.of(ItemsTestDataFactory.viewWeaponDTO()));

        get("/api/weapons/damageType/" + ItemsTestConstants.DAMAGE_TYPE_ID)
                .andExpect(status().isOk());

        verify(weaponService)
                .getWeaponsByDamageTypeId(ItemsTestConstants.DAMAGE_TYPE_ID);
    }

    //GET by DiceTypeId
    @Test
    void getWeaponsByDiceTypeId_returnsWeapons() throws Exception {

        when(weaponService.getWeaponsByDiceTypeId(ItemsTestConstants.DICE_TYPE_ID))
                .thenReturn(List.of(ItemsTestDataFactory.viewWeaponDTO()));

        get("/api/weapons/diceType/" + ItemsTestConstants.DICE_TYPE_ID)
                .andExpect(status().isOk());

        verify(weaponService)
                .getWeaponsByDiceTypeId(ItemsTestConstants.DICE_TYPE_ID);
    }

    //GET by CampaignUUID
    @Test
    void getWeaponsByCampaignUUID_returnsWeapons() throws Exception {

        when(weaponService.getWeaponsByCampaignUUID(ItemsTestConstants.CAMPAIGN_UUID))
                .thenReturn(List.of(ItemsTestDataFactory.viewWeaponDTO()));

        get("/api/weapons/campaign/" + ItemsTestConstants.CAMPAIGN_UUID)
                .andExpect(status().isOk());

        verify(weaponService)
                .getWeaponsByCampaignUUID(ItemsTestConstants.CAMPAIGN_UUID);
    }

    //POST
    @Test
    void createWeapon_returnsSaved() throws Exception {

        CreateWeaponDTO create =
                ItemsTestDataFactory.createWeaponDTO();

        ViewWeaponDTO response =
                ItemsTestDataFactory.viewWeaponDTO();

        when(weaponService.create(any())).thenReturn(response);

        post("/api/weapons", create)
                .andExpect(status().isOk());
    }

    //PUT
    @Test
    void updateWeapon_returnsUpdated() throws Exception {

        UpdateWeaponDTO update =
                ItemsTestDataFactory.updateWeaponDTO();

        ViewWeaponDTO response =
                ItemsTestDataFactory.viewWeaponDTO();

        when(weaponService.update(any())).thenReturn(response);

        put("/api/weapons", update)
                .andExpect(status().isOk());
    }

    //DELETE
    @Test
    void deleteWeapon_returnsOk() throws Exception {

        delete("/api/weapons/1")
                .andExpect(status().isOk());

        verify(weaponService).delete(1);
    }
}
