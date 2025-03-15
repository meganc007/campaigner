package com.mcommings.campaigner.common.services.interfaces;

import com.mcommings.campaigner.common.entities.Wealth;

import java.util.List;

public interface IWealth {

    List<Wealth> getWealth();

    void saveWealth(Wealth wealth);

    void deleteWealth(int wealthId);

    void updateWealth(int wealthId, Wealth wealth);
}
