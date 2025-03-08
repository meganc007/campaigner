package com.mcommings.campaigner.interfaces;

import com.mcommings.campaigner.entities.Government;

import java.util.List;

public interface IGovernment {

    List<Government> getGovernments();

    void saveGovernment(Government government);

    void deleteGovernment(int governmentId);

    void updateGovernment(int governmentId, Government government);
}
