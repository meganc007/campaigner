package com.mcommings.campaigner.people;

import com.mcommings.campaigner.items.Currency;
import com.mcommings.campaigner.items.Item;
import com.mcommings.campaigner.items.Weapon;
import lombok.Data;
import com.mcommings.campaigner.locations.Location;

import java.util.List;

@Data
public class Person {
    private long id;
    private String firstName;
    private String lastName;
    private int age;
    private String title;
    private Race race;
    private boolean isNPC;
    private boolean isEnemy;
    private String personality;
    private String description;
    private String notes;

    enum Race {
        HUMAN,
        ELF,
        HALF_ELF,
        DWARF,
        ORC,
        HALF_ORC,
        DRAGONBORN,
        HALFLING,
        GNOME
    }
}
