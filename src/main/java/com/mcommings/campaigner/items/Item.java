package com.mcommings.campaigner.items;

import lombok.Data;

import java.util.List;

@Data
public class Item {
    private long id;
    private String name;
    private String description;
    private List<Currency> value;
}
