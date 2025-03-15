package com.mcommings.campaigner.items.entities;

import com.mcommings.campaigner.common.entities.BaseEntity;
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

    @Column(name = "max_roll")
    private int max_roll;

    public DiceType() {
        super();
    }

    public DiceType(int id, String name, String description, int max_roll) {
        this.id = id;
        this.setName(name);
        this.setDescription(description);
        this.max_roll = max_roll;
    }
}
