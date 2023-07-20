package com.mcommings.campaigner.people;

import com.mcommings.campaigner.items.Currency;
import com.mcommings.campaigner.items.Item;
import com.mcommings.campaigner.items.Weapon;
import com.mcommings.campaigner.statblocks.Statblock;
import lombok.Data;
import com.mcommings.campaigner.locations.Location;

import java.util.List;

@Data
public class Person {
    private long id;
    private String name;
    private String description;
    private int age;
    private Type type;
//    private List<Relationship> relationships;
    private Location location;
    private Race race;
    private String job;
    private List<Item> items;
    private List<Weapon> weapons;
    private List<Currency> moneyOwned;
    private List<String> skills;
    private Statblock statblock;
    private String alignment;
    private List<String> motives;
    private List<String> bonds;
    private String deity;

    enum Type {
        PLAYER_CHARACTER,
        NPC,
        ENEMY
    };

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
