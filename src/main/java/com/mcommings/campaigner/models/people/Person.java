package com.mcommings.campaigner.models.people;

import com.mcommings.campaigner.models.Wealth;
import jakarta.persistence.*;
import lombok.Data;
import lombok.ToString;

@Data
@Entity
@Table(name = "people")
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
@ToString(callSuper = true)
public class Person extends SentientBeingEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "age")
    private int age;

    @Column(name = "fk_race")
    private Integer fk_race;

    @Column(name = "fk_wealth")
    private Integer fk_wealth;

    @Column(name = "fk_ability_score")
    private Integer fk_ability_score;

    @Column(name = "isNPC")
    private Boolean isNPC;

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
        super();
    }

    public Person(int id, String firstName, String lastName, int age, String title, Boolean isNPC, Boolean isEnemy,
                  String personality, String description, String notes) {
        this.id = id;
        this.setFirstName(firstName);
        this.setLastName(lastName);
        this.age = age;
        this.setTitle(title);
        this.isNPC = isNPC;
        this.setIsEnemy(isEnemy);
        this.setPersonality(personality);
        this.setDescription(description);
        this.setNotes(notes);
    }

    public Person(int id, String firstName, String lastName, int age, String title, Integer fk_race, Integer fk_wealth,
                  Integer fk_ability_score, Boolean isNPC, Boolean isEnemy, String personality, String description,
                  String notes) {
        this.id = id;
        this.setFirstName(firstName);
        this.setLastName(lastName);
        this.age = age;
        this.setTitle(title);
        this.fk_race = fk_race;
        this.setFk_wealth(fk_wealth);
        this.setFk_ability_score(fk_ability_score);
        this.isNPC = isNPC;
        this.setIsEnemy(isEnemy);
        this.setPersonality(personality);
        this.setDescription(description);
        this.setNotes(notes);
    }
}
