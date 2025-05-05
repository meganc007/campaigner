package com.mcommings.campaigner.modules.common.services.interfaces;

import com.mcommings.campaigner.modules.common.dtos.WealthDTO;

import java.util.List;
import java.util.Optional;

public interface IWealth {

    List<WealthDTO> getWealth();

    void saveWealth(WealthDTO wealth);

    void deleteWealth(int wealthId);

    Optional<WealthDTO> updateWealth(int wealthId, WealthDTO wealth);
}
