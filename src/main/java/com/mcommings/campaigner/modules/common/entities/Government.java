package com.mcommings.campaigner.modules.common.entities;

import jakarta.persistence.*;
import lombok.Data;
import lombok.ToString;

@Data
@Entity
@Table(name = "governments")
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
@ToString(callSuper = true)
public class Government extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    public Government () {
        super();
    }

    public Government(int id, String name, String description) {
        this.id = id;
        this.setName(name);
        this.setDescription(description);
    }

}
