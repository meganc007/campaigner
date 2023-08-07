package com.mcommings.campaigner.models;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class WealthTest {
    @Test
    public void shouldCreateADefaultWealth() {
        Wealth wealth = new Wealth();
        Assertions.assertNotNull(wealth);
        Assertions.assertEquals(0, wealth.getId());
        Assertions.assertNull(wealth.getName());
    }

    @Test
    public void shouldCreateACustomWealth() {
        int id = 1;
        String name = "Custom Wealth";

        Wealth wealth = new Wealth(id, name);

        Assertions.assertEquals(id, wealth.getId());
        Assertions.assertEquals(name, wealth.getName());
    }
    @Test
    public void shouldConvertWealthToString() {
        int id = 1;
        String name = "Custom Wealth";

        Wealth wealth = new Wealth(id, name);
        String expected = "Wealth(id=1, name=Custom Wealth)";

        Assertions.assertEquals(expected, wealth.toString());
    }
}
