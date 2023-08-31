package com.mcommings.campaigner.models.locations;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class RegionTest {

    @Test
    public void shouldCreateADefaultRegion() {
        Region region = new Region();
        Assertions.assertNotNull(region);
        Assertions.assertEquals(0, region.getId());
        Assertions.assertNull(region.getName());
        Assertions.assertNull(region.getDescription());
        Assertions.assertNull(region.getFk_country());
        Assertions.assertNull(region.getFk_climate());
    }

    @Test
    public void shouldCreateACustomRegionWithNoForeignKeys() {
        int id = 1;
        String name = "Custom Region";
        String description = "This is a custom Region.";

        Region region = new Region(id, name, description);

        Assertions.assertEquals(id, region.getId());
        Assertions.assertEquals(name, region.getName());
        Assertions.assertEquals(description, region.getDescription());
    }

    @Test
    public void shouldConvertRegionToStringWithNoForeignKeys() {
        int id = 1;
        String name = "Custom Region";
        String description = "This is a custom Region.";

        Region region = new Region(id, name, description);
        String expected = "Region(super=BaseEntity(name=Custom Region, description=This is a custom Region.), id=1, fk_country=null, fk_climate=null, country=null, climate=null)";

        Assertions.assertEquals(expected, region.toString());
    }

    @Test
    public void shouldCreateACustomRegionWithForeignKeys() {
        int id = 1;
        String name = "Custom Region";
        String description = "This is a custom Region.";
        Integer fk_country = 2;
        Integer fk_climate = 3;


        Region region = new Region(id, name, description, fk_country, fk_climate);

        Assertions.assertEquals(id, region.getId());
        Assertions.assertEquals(name, region.getName());
        Assertions.assertEquals(description, region.getDescription());
        Assertions.assertEquals(fk_country, region.getFk_country());
        Assertions.assertEquals(fk_climate, region.getFk_climate());
    }

    @Test
    public void shouldConvertRegionToStringWithForeignKeys() {
        int id = 1;
        String name = "Custom Region";
        String description = "This is a custom Region.";
        Integer fk_country = 2;
        Integer fk_climate = 3;

        Region region = new Region(id, name, description, fk_country, fk_climate);
        String expected = "Region(super=BaseEntity(name=Custom Region, description=This is a custom Region.), id=1, fk_country=2, fk_climate=3, country=null, climate=null)";

        Assertions.assertEquals(expected, region.toString());
    }
}
