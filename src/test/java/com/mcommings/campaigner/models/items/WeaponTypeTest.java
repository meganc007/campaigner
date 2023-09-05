package com.mcommings.campaigner.models.items;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class WeaponTypeTest {

    @Test
    public void shouldCreateADefaultWeaponType() {
        WeaponType weaponType = new WeaponType();
        Assertions.assertNotNull(weaponType);
        Assertions.assertEquals(0, weaponType.getId());
        Assertions.assertNull(weaponType.getName());
        Assertions.assertNull(weaponType.getDescription());
    }

    @Test
    public void shouldCreateACustomWeaponType() {
        int id = 1;
        String name = "Custom WeaponType";
        String description = "This is a custom WeaponType.";

        WeaponType weaponType = new WeaponType(id, name, description);

        Assertions.assertEquals(id, weaponType.getId());
        Assertions.assertEquals(name, weaponType.getName());
        Assertions.assertEquals(description, weaponType.getDescription());
    }

    @Test
    public void shouldConvertWeaponTypeToString() {
        int id = 1;
        String name = "Custom WeaponType";
        String description = "This is a custom WeaponType.";

        WeaponType weaponType = new WeaponType(id, name, description);
        String expected = "WeaponType(super=BaseEntity(name=Custom WeaponType, description=This is a custom WeaponType.), id=1)";

        Assertions.assertEquals(expected, weaponType.toString());
    }
}
