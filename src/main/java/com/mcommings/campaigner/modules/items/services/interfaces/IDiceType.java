package com.mcommings.campaigner.modules.items.services.interfaces;

import com.mcommings.campaigner.modules.items.entities.DiceType;

import java.util.List;

public interface IDiceType {

    List<DiceType> getDiceTypes();

    void saveDiceType(DiceType diceType);

    void deleteDiceType(int diceTypeId);

    void updateDiceType(int diceTypeId, DiceType diceType);
}
