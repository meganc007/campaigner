package com.mcommings.campaigner.interfaces.people;

import com.mcommings.campaigner.models.people.NamedMonster;

import java.util.List;

public interface INamedMonster {

    List<NamedMonster> getNamedMonsters();

    void saveNamedMonster(NamedMonster namedMonster);

    void deleteNamedMonster(int id);

    void updateNamedMonster(int id, NamedMonster namedMonster);
}
