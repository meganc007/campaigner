package com.mcommings.campaigner.common.entities;

import jakarta.persistence.*;
import lombok.Data;
import lombok.ToString;

@Data
@Entity
@Table(name = "climates")
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
@ToString(callSuper = true)
public class Climate extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    public Climate () {
        super();
    }

    public Climate(int id, String name, String description) {
        this.id = id;
        super.setName(name);
        super.setDescription(description);
    }
}
