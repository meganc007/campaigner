package com.mcommings.campaigner.common.services.interfaces;

import com.mcommings.campaigner.common.entities.Government;

import java.util.List;

public interface IGovernment {

    List<Government> getGovernments();

    void saveGovernment(Government government);

    void deleteGovernment(int governmentId);

    void updateGovernment(int governmentId, Government government);
}
