package com.mcommings.campaigner.models.people;

import com.mcommings.campaigner.models.Wealth;
import jakarta.persistence.*;
import lombok.Data;
import lombok.ToString;

@Data
@Entity
@Table(name = "named_monsters")
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
@ToString(callSuper = true)
public class NamedMonster extends SentientBeingEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "fk_wealth")
    private Integer fk_wealth;

    @Column(name = "fk_ability_score")
    private Integer fk_ability_score;

    @Column(name = "fk_generic_monster")
    private Integer fk_generic_monster;

    @ManyToOne
    @JoinColumn(name = "fk_wealth", referencedColumnName = "id", updatable = false, insertable = false)
    private Wealth wealth;

    @ManyToOne
    @JoinColumn(name = "fk_ability_score", referencedColumnName = "id", updatable = false, insertable = false)
    private AbilityScore abilityScore;

    @ManyToOne
    @JoinColumn(name = "fk_generic_monster", referencedColumnName = "id", updatable = false, insertable = false)
    private GenericMonster genericMonster;

    public NamedMonster() {
        super();
    }

    public NamedMonster(int id, String firstName, String lastName, String title, Boolean isEnemy, String personality,
                        String description, String notes) {
        this.id = id;
        this.setFirstName(firstName);
        this.setLastName(lastName);
        this.setTitle(title);
        this.setIsEnemy(isEnemy);
        this.setPersonality(personality);
        this.setDescription(description);
        this.setNotes(notes);
    }

    public NamedMonster(int id, String firstName, String lastName, String title, Integer fk_wealth, Integer fk_ability_score,
                        Integer fk_generic_monster, Boolean isEnemy, String personality, String description, String notes) {
        this.id = id;
        this.setFirstName(firstName);
        this.setLastName(lastName);
        this.setTitle(title);
        this.setFk_wealth(fk_wealth);
        this.setFk_ability_score(fk_ability_score);
        this.setFk_generic_monster(fk_generic_monster);
        this.setIsEnemy(isEnemy);
        this.setPersonality(personality);
        this.setDescription(description);
        this.setNotes(notes);
    }
}
