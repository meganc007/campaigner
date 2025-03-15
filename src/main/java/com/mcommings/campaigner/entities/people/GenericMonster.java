package com.mcommings.campaigner.entities.people;

import com.mcommings.campaigner.common.entities.BaseEntity;
import com.mcommings.campaigner.common.entities.Campaign;
import jakarta.persistence.*;
import lombok.Data;
import lombok.ToString;

import java.util.UUID;

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

    @Column(name = "fk_campaign_uuid")
    private UUID fk_campaign_uuid;

    @ManyToOne
    @JoinColumn(name = "fk_campaign_uuid", referencedColumnName = "campaign_uuid", updatable = false, insertable = false)
    private Campaign campaign;

    @ManyToOne
    @JoinColumn(name = "fk_ability_score", referencedColumnName = "id", updatable = false, insertable = false)
    private AbilityScore abilityScore;

    public GenericMonster() {
        super();
    }

    public GenericMonster(int id, String name, String description, UUID fk_campaign_uuid) {
        this.id = id;
        this.setName(name);
        this.setDescription(description);
        this.fk_campaign_uuid = fk_campaign_uuid;
    }

    public GenericMonster(int id, String name, String description, Integer fk_ability_score, String traits, String notes, UUID fk_campaign_uuid) {
        this.id = id;
        this.setName(name);
        this.setDescription(description);
        this.fk_ability_score = fk_ability_score;
        this.traits = traits;
        this.notes = notes;
        this.fk_campaign_uuid = fk_campaign_uuid;
    }
}
