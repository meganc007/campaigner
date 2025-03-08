package com.mcommings.campaigner.interfaces.items;

import com.mcommings.campaigner.entities.items.DiceType;

import java.util.List;

public interface IDiceType {

    List<DiceType> getDiceTypes();

    void saveDiceType(DiceType diceType);

    void deleteDiceType(int diceTypeId);

    void updateDiceType(int diceTypeId, DiceType diceType);
}
