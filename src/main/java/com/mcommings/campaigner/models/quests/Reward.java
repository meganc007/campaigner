package com.mcommings.campaigner.models.quests;

import com.mcommings.campaigner.models.items.Item;
import com.mcommings.campaigner.models.items.Weapon;
import jakarta.persistence.*;
import lombok.Data;
import lombok.ToString;

@Data
@Entity
@Table(name = "rewards")
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
@ToString(callSuper = true)
public class Reward extends CommonQuestEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "gold_value")
    private int gold_value;

    @Column(name = "silver_value")
    private int silver_value;

    @Column(name = "copper_value")
    private int copper_value;

    @Column(name = "fk_item")
    private Integer fk_item;

    @Column(name = "fk_weapon")
    private Integer fk_weapon;

    @ManyToOne
    @JoinColumn(name = "fk_item", referencedColumnName = "id", updatable = false, insertable = false)
    private Item item;

    @ManyToOne
    @JoinColumn(name = "fk_weapon", referencedColumnName = "id", updatable = false, insertable = false)
    private Weapon weapon;

    public Reward() {
        super();
    }

    public Reward(int id, String description, String notes) {
        this.id = id;
        this.setDescription(description);
        this.setNotes(notes);
    }

    public Reward(int id, String description, String notes, int gold_value, int silver_value, int copper_value) {
        this.id = id;
        this.setDescription(description);
        this.setNotes(notes);
        this.gold_value = gold_value;
        this.silver_value = silver_value;
        this.copper_value = copper_value;
    }

}
