package com.mcommings.campaigner.models.people;

import com.mcommings.campaigner.models.BaseEntity;
import jakarta.persistence.*;
import lombok.Data;
import lombok.ToString;

@Data
@Entity
@Table(name = "generic_monsters")
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
@ToString(callSuper = true)
public class GenericMonster extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "fk_ability_score")
    private Integer fk_ability_score;

    @Column(name = "traits")
    private String traits;

    @Column(name = "notes")
    private String notes;

    @ManyToOne
    @JoinColumn(name = "fk_ability_score", referencedColumnName = "id", updatable = false, insertable = false)
    private AbilityScore abilityScore;

    public GenericMonster() {
        super();
    }

    public GenericMonster(int id, String name, String description) {
        this.id = id;
        this.setName(name);
        this.setDescription(description);
    }

    public GenericMonster(int id, String name, String description, Integer fk_ability_score, String traits, String notes) {
        this.id = id;
        this.setName(name);
        this.setDescription(description);
        this.fk_ability_score = fk_ability_score;
        this.traits = traits;
        this.notes = notes;
    }
}
