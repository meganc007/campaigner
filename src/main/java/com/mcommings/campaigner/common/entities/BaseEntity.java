package com.mcommings.campaigner.common.entities;

import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import lombok.Data;

@Data
@MappedSuperclass
public abstract class BaseEntity {

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "description")
    private String description;

}
