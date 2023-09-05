package com.mcommings.campaigner.models.items;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class DamageTypeTest {

    @Test
    public void shouldCreateADefaultDamageType() {
        DamageType damageType = new DamageType();
        Assertions.assertNotNull(damageType);
        Assertions.assertEquals(0, damageType.getId());
        Assertions.assertNull(damageType.getName());
        Assertions.assertNull(damageType.getDescription());
    }

    @Test
    public void shouldCreateACustomDamageType() {
        int id = 1;
        String name = "Custom DamageType";
        String description = "This is a custom DamageType.";

        DamageType damageType = new DamageType(id, name, description);

        Assertions.assertEquals(id, damageType.getId());
        Assertions.assertEquals(name, damageType.getName());
        Assertions.assertEquals(description, damageType.getDescription());
    }

    @Test
    public void shouldConvertDamageTypeToString() {
        int id = 1;
        String name = "Custom DamageType";
        String description = "This is a custom DamageType.";

        DamageType damageType = new DamageType(id, name, description);
        String expected = "DamageType(super=BaseEntity(name=Custom DamageType, description=This is a custom DamageType.), id=1)";

        Assertions.assertEquals(expected, damageType.toString());
    }
}
