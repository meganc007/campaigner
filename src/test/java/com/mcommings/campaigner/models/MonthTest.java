package com.mcommings.campaigner.models;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class MonthTest {
    
    @Test
    public void shouldCreateADefaultMonth() {
        Month month = new Month();
        Assertions.assertNotNull(month);
        Assertions.assertEquals(0, month.getId());
        Assertions.assertNull(month.getName());
        Assertions.assertNull(month.getDescription());
    }

    @Test
    public void shouldCreateACustomMonth() {
        int id = 1;
        String name = "Custom Month";
        String description = "This is a custom Month.";
        String season = "summer";

        Month month = new Month(id, name, description, season);

        Assertions.assertEquals(id, month.getId());
        Assertions.assertEquals(name, month.getName());
        Assertions.assertEquals(description, month.getDescription());
        Assertions.assertEquals(season, month.getSeason());
    }

    @Test
    public void shouldConvertMonthToString() {
        int id = 1;
        String name = "Custom Month";
        String description = "This is a custom Month.";
        String season = "summer";

        Month month = new Month(id, name, description, season);

        String expected = "Month(super=BaseEntity(name=Custom Month, description=This is a custom Month.), id=1, season=summer)";

        Assertions.assertEquals(expected, month.toString());
    }
}
