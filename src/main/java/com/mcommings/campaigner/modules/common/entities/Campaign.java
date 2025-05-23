package com.mcommings.campaigner.modules.common.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;

import java.util.UUID;

@Getter
@Setter
@Builder
@Entity
@Table(name = "campaigns")
@NoArgsConstructor
@AllArgsConstructor
public class Campaign {

    @Id
    @Column(name = "campaign_uuid", nullable = false, unique = true, columnDefinition = "varchar(36) default gen_random_uuid()")
    private UUID uuid;
    @Column(nullable = false)
    private String name;
    private String description;
}
