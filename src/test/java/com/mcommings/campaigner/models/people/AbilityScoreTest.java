package com.mcommings.campaigner.models.people;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
public class AbilityScoreTest {

    @Test
    public void shouldCreateADefaultAbilityScore() {
        AbilityScore abilityScore = new AbilityScore();
        Assertions.assertNotNull(abilityScore);
        Assertions.assertEquals(0, abilityScore.getId());
        Assertions.assertEquals(0, abilityScore.getStrength());
        Assertions.assertEquals(0, abilityScore.getDexterity());
        Assertions.assertEquals(0, abilityScore.getConstitution());
        Assertions.assertEquals(0, abilityScore.getIntelligence());
        Assertions.assertEquals(0, abilityScore.getWisdom());
        Assertions.assertEquals(0, abilityScore.getCharisma());
    }

    @Test
    public void shouldCreateACustomAbilityScore() {
        int id = 1;
        AbilityScore abilityScore = new AbilityScore(id, 1, 1, 1, 1, 1, 1);

        Assertions.assertEquals(id, abilityScore.getId());
        Assertions.assertEquals(1, abilityScore.getStrength());
        Assertions.assertEquals(1, abilityScore.getDexterity());
        Assertions.assertEquals(1, abilityScore.getConstitution());
        Assertions.assertEquals(1, abilityScore.getIntelligence());
        Assertions.assertEquals(1, abilityScore.getWisdom());
        Assertions.assertEquals(1, abilityScore.getCharisma());
    }
    @Test
    public void shouldConvertAbilityScoreToString() {
        int id = 1;
        AbilityScore abilityScore = new AbilityScore(id, 1, 1, 1, 1, 1, 1);

        String expected = "AbilityScore(id=1, strength=1, dexterity=1, constitution=1, intelligence=1, wisdom=1, charisma=1)";

        Assertions.assertEquals(expected, abilityScore.toString());
    }
}
