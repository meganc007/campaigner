package com.mcommings.campaigner.modules.locations.entities;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@Builder
@Entity
@Table(name = "settlement_types")
@NoArgsConstructor
@AllArgsConstructor
public class SettlementType {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column(nullable = false, unique = true)
    private String name;
    private String description;
}
