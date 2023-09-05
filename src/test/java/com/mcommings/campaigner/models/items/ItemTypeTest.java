package com.mcommings.campaigner.models.items;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class ItemTypeTest {

    @Test
    public void shouldCreateADefaultItemType() {
        ItemType itemType = new ItemType();
        Assertions.assertNotNull(itemType);
        Assertions.assertEquals(0, itemType.getId());
        Assertions.assertNull(itemType.getName());
        Assertions.assertNull(itemType.getDescription());
    }

    @Test
    public void shouldCreateACustomItemType() {
        int id = 1;
        String name = "Custom ItemType";
        String description = "This is a custom ItemType.";

        ItemType itemType = new ItemType(id, name, description);

        Assertions.assertEquals(id, itemType.getId());
        Assertions.assertEquals(name, itemType.getName());
        Assertions.assertEquals(description, itemType.getDescription());
    }

    @Test
    public void shouldConvertItemTypeToString() {
        int id = 1;
        String name = "Custom ItemType";
        String description = "This is a custom ItemType.";

        ItemType itemType = new ItemType(id, name, description);
        String expected = "ItemType(super=BaseEntity(name=Custom ItemType, description=This is a custom ItemType.), id=1)";

        Assertions.assertEquals(expected, itemType.toString());
    }
}
