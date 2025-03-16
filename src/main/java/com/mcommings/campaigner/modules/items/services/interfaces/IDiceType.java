package com.mcommings.campaigner.modules.items.services.interfaces;

import com.mcommings.campaigner.modules.items.dtos.DiceTypeDTO;

import java.util.List;
import java.util.Optional;

public interface IDiceType {

    List<DiceTypeDTO> getDiceTypes();

    Optional<DiceTypeDTO> getDiceType(int diceTypeId);

    void saveDiceType(DiceTypeDTO diceType);

    void deleteDiceType(int diceTypeId);

    Optional<DiceTypeDTO> updateDiceType(int diceTypeId, DiceTypeDTO diceType);
}
