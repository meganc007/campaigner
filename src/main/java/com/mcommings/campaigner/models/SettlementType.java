package com.mcommings.campaigner.models;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "settlement_types")
public class SettlementType {

    @Id
    @SequenceGenerator(name = "settlement_types_id_seq", sequenceName = "settlement_types_id_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "settlement_types_id_seq")
    private int id;
    private String name;
    private String description;

    public SettlementType() {}

    public SettlementType(int id, String name, String description) {
        this.id = id;
        this.name = name;
        this.description = description;
    }
}
