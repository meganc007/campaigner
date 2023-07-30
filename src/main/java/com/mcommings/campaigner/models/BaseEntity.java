package com.mcommings.campaigner.models;

import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import lombok.Data;

@Data
@MappedSuperclass
public abstract class BaseEntity {

    @Column(nullable = false)
    private String name;

    @Column
    private String description;

}
