package com.mcommings.campaigner.items.entities;

import com.mcommings.campaigner.common.entities.BaseEntity;
import jakarta.persistence.*;
import lombok.Data;
import lombok.ToString;

@Data
@Entity
@Table(name = "damage_types")
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
@ToString(callSuper = true)
public class DamageType extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    public DamageType() {
        super();
    }

    public DamageType(int id, String name, String description) {
        this.id = id;
        this.setName(name);
        this.setDescription(description);
    }
}
