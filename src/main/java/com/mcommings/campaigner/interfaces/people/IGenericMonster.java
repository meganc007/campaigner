package com.mcommings.campaigner.interfaces.people;

import com.mcommings.campaigner.models.people.GenericMonster;

import java.util.List;

public interface IGenericMonster {

    List<GenericMonster> getGenericMonsters();

    void saveGenericMonster(GenericMonster genericMonster);

    void deleteGenericMonster(int id);

    void updateGenericMonster(int id, GenericMonster genericMonster);
}
