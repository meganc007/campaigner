package com.mcommings.campaigner.modules.common.services.interfaces;

import com.mcommings.campaigner.modules.common.dtos.GovernmentDTO;

import java.util.List;
import java.util.Optional;

public interface IGovernment {

    List<GovernmentDTO> getGovernments();

    Optional<GovernmentDTO> getGovernment(int governmentId);

    void saveGovernment(GovernmentDTO government);

    void deleteGovernment(int governmentId);

    Optional<GovernmentDTO> updateGovernment(int governmentId, GovernmentDTO government);
}
