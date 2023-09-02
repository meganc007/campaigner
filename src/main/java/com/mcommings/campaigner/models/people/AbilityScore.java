package com.mcommings.campaigner.models.people;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "ability_scores")
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
public class AbilityScore {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    int id;

    @Column(name = "strength")
    private int strength;

    @Column(name = "dexterity")
    private int dexterity;

    @Column(name = "constitution")
    private int constitution;

    @Column(name = "intelligence")
    private int intelligence;

    @Column(name = "wisdom")
    private int wisdom;

    @Column(name = "charisma")
    private int charisma;

    public AbilityScore() {}

    public AbilityScore(int id, int strength, int dexterity, int constitution, int intelligence, int wisdom, int charisma) {
        this.id = id;
        this.strength = strength;
        this.dexterity = dexterity;
        this.constitution = constitution;
        this.intelligence = intelligence;
        this.wisdom = wisdom;
        this.charisma = charisma;
    }
}
