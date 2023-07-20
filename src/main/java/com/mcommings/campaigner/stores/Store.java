package com.mcommings.campaigner.stores;

import com.mcommings.campaigner.items.Item;
import com.mcommings.campaigner.items.Weapon;
import com.mcommings.campaigner.locations.sublocations.City;
import lombok.Data;

import java.util.List;

@Data
public class Store {
    private long id;
    private String name;
    private String description;
    private List<Item> items;
    private List<Weapon> weapons;
}
