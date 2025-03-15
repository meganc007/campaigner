package com.mcommings.campaigner.modules.people.entities;

import com.mcommings.campaigner.modules.common.entities.Campaign;
import com.mcommings.campaigner.modules.common.entities.Wealth;
import jakarta.persistence.*;
import lombok.Data;
import lombok.ToString;

import java.util.UUID;

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

    @Column(name = "fk_campaign_uuid")
    private UUID fk_campaign_uuid;

    @ManyToOne
    @JoinColumn(name = "fk_campaign_uuid", referencedColumnName = "campaign_uuid", updatable = false, insertable = false)
    private Campaign campaign;

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
                        String description, String notes, UUID fk_campaign_uuid) {
        this.id = id;
        this.setFirstName(firstName);
        this.setLastName(lastName);
        this.setTitle(title);
        this.setIsEnemy(isEnemy);
        this.setPersonality(personality);
        this.setDescription(description);
        this.setNotes(notes);
        this.fk_campaign_uuid = fk_campaign_uuid;
    }

    public NamedMonster(int id, String firstName, String lastName, String title, Integer fk_wealth, Integer fk_ability_score,
                        Integer fk_generic_monster, Boolean isEnemy, String personality, String description, String notes, UUID fk_campaign_uuid) {
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
        this.fk_campaign_uuid = fk_campaign_uuid;
    }
}
