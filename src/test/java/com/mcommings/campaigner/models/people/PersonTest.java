package com.mcommings.campaigner.models.people;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class PersonTest {

    @Test
    public void shouldCreateADefaultPerson() {
        Person person = new Person();
        Assertions.assertNotNull(person);
        Assertions.assertEquals(0, person.getId());
        Assertions.assertNull(person.getFirstName());
        Assertions.assertNull(person.getLastName());
        Assertions.assertEquals(0, person.getAge());
        Assertions.assertNull(person.getTitle());
        Assertions.assertNull(person.getFk_race());
        Assertions.assertNull(person.getFk_wealth());
        Assertions.assertNull(person.getFk_ability_score());
        Assertions.assertNull(person.getIsNPC());
        Assertions.assertNull(person.getIsEnemy());
        Assertions.assertNull(person.getPersonality());
        Assertions.assertNull(person.getDescription());
        Assertions.assertNull(person.getNotes());
    }

    @Test
    public void shouldCreateACustomPersonWithNoForeignKeys() {
        int id = 1;
        String firstName = "Jane";
        String lastName = "Doe";
        int age = 22;
        String title = "The Nameless";
        Boolean isNPC = true;
        Boolean isEnemy = false;
        String personality = "She rarely speaks";
        String description = "Non-distinct";
        String notes = "She's a shapeshifter";

        Person person = new Person(id, firstName, lastName, age, title, isNPC, isEnemy, personality, description, notes);

        Assertions.assertEquals(id, person.getId());
        Assertions.assertEquals(firstName, person.getFirstName());
        Assertions.assertEquals(lastName, person.getLastName());
        Assertions.assertEquals(age, person.getAge());
        Assertions.assertEquals(title, person.getTitle());
        Assertions.assertEquals(isNPC, person.getIsNPC());
        Assertions.assertEquals(isEnemy, person.getIsEnemy());
        Assertions.assertEquals(personality, person.getPersonality());
        Assertions.assertEquals(description, person.getDescription());
        Assertions.assertEquals(notes, person.getNotes());
    }

    @Test
    public void shouldConvertPersonToStringWithNoForeignKeys() {
        int id = 1;
        String firstName = "Jane";
        String lastName = "Doe";
        int age = 22;
        String title = "The Nameless";
        Boolean isNPC = true;
        Boolean isEnemy = false;
        String personality = "She rarely speaks";
        String description = "Non-distinct";
        String notes = "She's a shapeshifter";

        Person person = new Person(id, firstName, lastName, age, title, isNPC, isEnemy, personality, description, notes);
        String expected = "Person(id=1, firstName=Jane, lastName=Doe, age=22, title=The Nameless, fk_race=null, fk_wealth=null, fk_ability_score=null, isNPC=true, isEnemy=false, personality=She rarely speaks, description=Non-distinct, notes=She's a shapeshifter, race=null, wealth=null, abilityScore=null)";

        Assertions.assertEquals(expected, person.toString());
    }

    @Test
    public void shouldCreateACustomPersonWithForeignKeys() {
        int id = 1;
        String firstName = "Jane";
        String lastName = "Doe";
        int age = 22;
        String title = "The Nameless";
        Integer fk_race = 2;
        Integer fk_wealth = 2;
        Integer fk_ability_score = 2;
        Boolean isNPC = true;
        Boolean isEnemy = false;
        String personality = "She rarely speaks";
        String description = "Non-distinct";
        String notes = "She's a shapeshifter";

        Person person = new Person(id, firstName, lastName, age, title, fk_race, fk_wealth, fk_ability_score, isNPC, isEnemy, personality, description, notes);

        Assertions.assertEquals(id, person.getId());
        Assertions.assertEquals(firstName, person.getFirstName());
        Assertions.assertEquals(lastName, person.getLastName());
        Assertions.assertEquals(age, person.getAge());
        Assertions.assertEquals(title, person.getTitle());
        Assertions.assertEquals(fk_race, person.getFk_race());
        Assertions.assertEquals(fk_wealth, person.getFk_wealth());
        Assertions.assertEquals(fk_ability_score, person.getFk_ability_score());
        Assertions.assertEquals(isNPC, person.getIsNPC());
        Assertions.assertEquals(isEnemy, person.getIsEnemy());
        Assertions.assertEquals(personality, person.getPersonality());
        Assertions.assertEquals(description, person.getDescription());
        Assertions.assertEquals(notes, person.getNotes());
    }

    @Test
    public void shouldConvertPersonToStringWithForeignKeys() {
        int id = 1;
        String firstName = "Jane";
        String lastName = "Doe";
        int age = 22;
        String title = "The Nameless";
        Integer fk_race = 2;
        Integer fk_wealth = 2;
        Integer fk_ability_score = 2;
        Boolean isNPC = true;
        Boolean isEnemy = false;
        String personality = "She rarely speaks";
        String description = "Non-distinct";
        String notes = "She's a shapeshifter";

        Person person = new Person(id, firstName, lastName, age, title, fk_race, fk_wealth, fk_ability_score, isNPC, isEnemy, personality, description, notes);
        String expected = "Person(id=1, firstName=Jane, lastName=Doe, age=22, title=The Nameless, fk_race=2, fk_wealth=2, fk_ability_score=2, isNPC=true, isEnemy=false, personality=She rarely speaks, description=Non-distinct, notes=She's a shapeshifter, race=null, wealth=null, abilityScore=null)";

        Assertions.assertEquals(expected, person.toString());
    }
}
