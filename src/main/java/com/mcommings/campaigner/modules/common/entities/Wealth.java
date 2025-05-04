package com.mcommings.campaigner.modules.common.entities;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@Builder
@Entity
@Table(name = "wealth")
@NoArgsConstructor
@AllArgsConstructor
public class Wealth {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column(nullable = false)
    private String name;
}
