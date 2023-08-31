package com.mcommings.campaigner.models.locations;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class SettlementTypeTest {

    @Test
    public void shouldCreateADefaultSettlementType() {
        SettlementType settlementType = new SettlementType();
        Assertions.assertNotNull(settlementType);
        Assertions.assertEquals(0, settlementType.getId());
        Assertions.assertNull(settlementType.getName());
        Assertions.assertNull(settlementType.getDescription());
    }

    @Test
    public void shouldCreateACustomSettlementType() {
        int id = 1;
        String name = "Custom SettlementType";
        String description = "This is a custom SettlementType.";

        SettlementType settlementType = new SettlementType(id, name, description);

        Assertions.assertEquals(id, settlementType.getId());
        Assertions.assertEquals(name, settlementType.getName());
        Assertions.assertEquals(description, settlementType.getDescription());
    }
    @Test
    public void shouldConvertSettlementTypeToString() {
        int id = 1;
        String name = "Custom SettlementType";
        String description = "This is a custom SettlementType.";

        SettlementType settlementType = new SettlementType(id, name, description);
        String expected = "SettlementType(super=BaseEntity(name=Custom SettlementType, description=This is a custom SettlementType.), id=1)";

        Assertions.assertEquals(expected, settlementType.toString());
    }
}
