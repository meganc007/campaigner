package com.mcommings.campaigner.modules.items.entities;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@Builder
@Entity
@Table(name = "dice_types")
@NoArgsConstructor
@AllArgsConstructor
public class DiceType {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column(nullable = false, unique = true)
    private String name;
    private String description;
    private int max_roll;
}
