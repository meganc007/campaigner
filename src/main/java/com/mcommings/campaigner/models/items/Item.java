package com.mcommings.campaigner.models.items;

import com.mcommings.campaigner.models.BaseEntity;
import com.mcommings.campaigner.models.Campaign;
import jakarta.persistence.*;
import lombok.Data;
import lombok.ToString;

import java.util.UUID;

@Data
@Entity
@Table(name = "items")
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
@ToString(callSuper = true)
public class Item extends BaseEntity {

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

    @Column(name = "fk_item_type")
    private Integer fk_item_type;

    @Column(name = "is_magical")
    private Boolean isMagical;

    @Column(name = "is_cursed")
    private Boolean isCursed;

    @Column(name = "notes")
    private String notes;

    @Column(name = "fk_campaign_uuid")
    private UUID fk_campaign_uuid;

    @ManyToOne
    @JoinColumn(name = "fk_campaign_uuid", referencedColumnName = "campaign_uuid", updatable = false, insertable = false)
    private Campaign campaign;

    @ManyToOne
    @JoinColumn(name = "fk_item_type", referencedColumnName = "id", updatable = false, insertable = false)
    private ItemType itemType;


    public Item() {
        super();
    }

    public Item(int id, String name, String description, UUID fk_campaign_uuid) {
        this.id = id;
        this.setName(name);
        this.setDescription(description);
        this.fk_campaign_uuid = fk_campaign_uuid;
    }

    public Item(int id, String name, String description, String rarity, int gold_value, int silver_value,
                int copper_value, float weight, Integer fk_item_type, boolean isMagical, boolean isCursed, String notes, UUID fk_campaign_uuid) {
        this.id = id;
        this.setName(name);
        this.setDescription(description);
        this.rarity = rarity;
        this.gold_value = gold_value;
        this.silver_value = silver_value;
        this.copper_value = copper_value;
        this.weight = weight;
        this.fk_item_type = fk_item_type;
        this.isMagical = isMagical;
        this.isCursed = isCursed;
        this.notes = notes;
        this.fk_campaign_uuid = fk_campaign_uuid;
    }
}
