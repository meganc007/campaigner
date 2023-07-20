package com.mcommings.campaigner.items;

import lombok.Data;

import java.util.List;

@Data
public class Weapon {

    private long id;
    private String name;
    private String description;
    private boolean isMagical;
    private List<Currency> value;
}
