package com.mcommings.campaigner.models.people;

import com.mcommings.campaigner.models.Wealth;
import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "people")
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
public class Person {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "firstName")
    private String firstName;

    @Column(name = "lastName")
    private String lastName;

    @Column(name = "age")
    private int age;

    @Column(name = "title")
    private String title;

    @Column(name = "fk_race")
    private Integer fk_race;

    @Column(name = "fk_wealth")
    private Integer fk_wealth;

    @Column(name = "fk_ability_score")
    private Integer fk_ability_score;

    @Column(name = "isNPC")
    private Boolean isNPC;

    @Column(name = "isEnemy")
    private Boolean isEnemy;

    @Column(name = "personality")
    private String personality;

    @Column(name = "description")
    private String description;

    @Column(name = "notes")
    private String notes;

    @ManyToOne
    @JoinColumn(name = "fk_race", referencedColumnName = "id", updatable = false, insertable = false)
    private Race race;

    @ManyToOne
    @JoinColumn(name = "fk_wealth", referencedColumnName = "id", updatable = false, insertable = false)
    private Wealth wealth;

    @ManyToOne
    @JoinColumn(name = "fk_ability_score", referencedColumnName = "id", updatable = false, insertable = false)
    private AbilityScore abilityScore;

    public Person() {
    }

    public Person(int id, String firstName, String lastName, int age, String title, Boolean isNPC, Boolean isEnemy,
                  String personality, String description, String notes) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.age = age;
        this.title = title;
        this.isNPC = isNPC;
        this.isEnemy = isEnemy;
        this.personality = personality;
        this.description = description;
        this.notes = notes;
    }

    public Person(int id, String firstName, String lastName, int age, String title, Integer fk_race, Integer fk_wealth,
                  Integer fk_ability_score, Boolean isNPC, Boolean isEnemy, String personality, String description,
                  String notes) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.age = age;
        this.title = title;
        this.fk_race = fk_race;
        this.fk_wealth = fk_wealth;
        this.fk_ability_score = fk_ability_score;
        this.isNPC = isNPC;
        this.isEnemy = isEnemy;
        this.personality = personality;
        this.description = description;
        this.notes = notes;
    }
}
