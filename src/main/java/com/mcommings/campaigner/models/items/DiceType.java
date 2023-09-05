package com.mcommings.campaigner.models.items;

import com.mcommings.campaigner.models.BaseEntity;
import jakarta.persistence.*;
import lombok.Data;
import lombok.ToString;

@Data
@Entity
@Table(name = "dice_types")
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
@ToString(callSuper = true)
public class DiceType extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "maxRoll")
    private int maxRoll;

    public DiceType() {
        super();
    }

    public DiceType(int id, String name, String description, int maxRoll) {
        this.id = id;
        this.setName(name);
        this.setDescription(description);
        this.maxRoll = maxRoll;
    }
}
