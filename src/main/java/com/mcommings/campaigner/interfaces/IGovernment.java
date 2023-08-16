package com.mcommings.campaigner.interfaces;

import com.mcommings.campaigner.models.Government;

import java.util.List;

public interface IGovernment {

    public List<Government> getGovernments();

    public void saveGovernment(Government government);

    public void deleteGovernment(int governmentId);

    public void updateGovernment(int governmentId, Government government);
}
