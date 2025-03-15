package com.mcommings.campaigner.items.entities;

import com.mcommings.campaigner.common.entities.BaseEntity;
import com.mcommings.campaigner.common.entities.Campaign;
import jakarta.persistence.*;
import lombok.Data;
import lombok.ToString;

import java.util.UUID;

@Data
@Entity
@Table(name = "weapons")
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
@ToString(callSuper = true)
public class Weapon extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "rarity")
    private String rarity;

    @Column(name = "gold_value")
    private int gold_value;

    @Column(name = "silver_value")
    private int silver_value;

    @Column(name = "copper_value")
    private int copper_value;

    @Column(name = "weight")
    private float weight;

    @Column(name = "fk_weapon_type")
    private Integer fk_weapon_type;

    @Column(name = "fk_damage_type")
    private Integer fk_damage_type;

    @Column(name = "fk_dice_type")
    private Integer fk_dice_type;

    @Column(name = "number_of_dice")
    private int number_of_dice;

    @Column(name = "damage_modifier")
    private int damage_modifier;

    @Column(name = "isMagical")
    private Boolean isMagical;

    @Column(name = "isCursed")
    private Boolean isCursed;

    @Column(name = "notes")
    private String notes;

    @Column(name = "fk_campaign_uuid")
    private UUID fk_campaign_uuid;

    @ManyToOne
    @JoinColumn(name = "fk_campaign_uuid", referencedColumnName = "campaign_uuid", updatable = false, insertable = false)
    private Campaign campaign;

    @ManyToOne
    @JoinColumn(name = "fk_weapon_type", referencedColumnName = "id", updatable = false, insertable = false)
    private WeaponType weaponType;

    @ManyToOne
    @JoinColumn(name = "fk_damage_type", referencedColumnName = "id", updatable = false, insertable = false)
    private DamageType damageType;

    @ManyToOne
    @JoinColumn(name = "fk_dice_type", referencedColumnName = "id", updatable = false, insertable = false)
    private DiceType diceType;

    public Weapon() {
        super();
    }

    public Weapon(int id, String name, String description, UUID fk_campaign_uuid) {
        this.id = id;
        this.setName(name);
        this.setDescription(description);
        this.fk_campaign_uuid = fk_campaign_uuid;
    }

    public Weapon(int id, String name, String description, String rarity, int gold_value, int silver_value,
                  int copper_value, float weight, Integer fk_weapon_type, Integer fk_damage_type, Integer fk_dice_type,
                  int number_of_dice, int damage_modifier, boolean isMagical, boolean isCursed, String notes, UUID fk_campaign_uuid) {
        this.id = id;
        this.setName(name);
        this.setDescription(description);
        this.rarity = rarity;
        this.gold_value = gold_value;
        this.silver_value = silver_value;
        this.copper_value = copper_value;
        this.weight = weight;
        this.fk_weapon_type = fk_weapon_type;
        this.fk_damage_type = fk_damage_type;
        this.fk_dice_type = fk_dice_type;
        this.number_of_dice = number_of_dice;
        this.damage_modifier = damage_modifier;
        this.isMagical = isMagical;
        this.isCursed = isCursed;
        this.notes = notes;
        this.fk_campaign_uuid = fk_campaign_uuid;
    }
}
