package com.mcommings.campaigner.common.entities;

import jakarta.persistence.*;
import lombok.Data;
import lombok.ToString;

import java.util.UUID;

@Data
@Entity
@Table(name = "campaigns")
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
@ToString(callSuper = true)
public class Campaign extends BaseEntity {

    @Id
    @Column(name = "campaign_uuid", nullable = false, unique = true, columnDefinition = "varchar(36) default gen_random_uuid()")
    private UUID uuid;

    public Campaign () {
        super();
        this.uuid = UUID.randomUUID();
    }

    public Campaign(String name, String description) {
        this();
        this.setName(name);;
        this.setDescription(description);
    }
}
