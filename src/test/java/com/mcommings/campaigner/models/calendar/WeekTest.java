package com.mcommings.campaigner.models.calendar;

import com.mcommings.campaigner.models.calendar.Week;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class WeekTest {

    @Test
    public void shouldCreateADefaultWeek() {
        Week week = new Week();
        Assertions.assertNotNull(week);
        Assertions.assertEquals(0, week.getId());
        Assertions.assertNull(week.getDescription());
        Assertions.assertNull(week.getWeek_number());
        Assertions.assertNull(week.getFk_month());
    }

    @Test
    public void shouldCreateACustomWeek() {
        int id = 1;
        String description = "This is a custom Week.";
        Integer week_number = 1;
        Integer fk_month = 2;

        Week week = new Week(id, description, week_number, fk_month);

        Assertions.assertEquals(id, week.getId());
        Assertions.assertEquals(description, week.getDescription());
        Assertions.assertEquals(week_number, week.getWeek_number());
        Assertions.assertEquals(fk_month, week.getFk_month());
    }

    @Test
    public void shouldConvertWeekToStringWithForeignKeys() {
        int id = 1;
        String description = "This is a custom Week.";
        Integer week_number = 1;
        Integer fk_month = 2;

        Week week = new Week(id, description, week_number, fk_month);
        String expected = "Week(id=1, description=This is a custom Week., week_number=1, fk_month=2, month=null)";

        Assertions.assertEquals(expected, week.toString());
    }
}
