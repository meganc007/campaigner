package com.mcommings.campaigner.governments;

import lombok.Data;

@Data
public class Government {
    private long id;
    private String name;
    private String description;

    public Government(long id, String name, String description) {
        this.id = id;
        this.name = name;
        this.description = description;
    }

}
