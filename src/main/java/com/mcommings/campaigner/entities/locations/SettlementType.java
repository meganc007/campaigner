package com.mcommings.campaigner.entities.locations;

import com.mcommings.campaigner.entities.BaseEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@Entity
@Table(name = "settlement_types")
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
@ToString(callSuper = true)
public class SettlementType extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    public SettlementType() {
        super();
    }

    public SettlementType(int id, String name, String description) {
        this.id = id;
        this.setName(name);
        this.setDescription(description);
    }
}
