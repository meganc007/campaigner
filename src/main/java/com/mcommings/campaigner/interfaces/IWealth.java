package com.mcommings.campaigner.interfaces;

import com.mcommings.campaigner.entities.Wealth;

import java.util.List;

public interface IWealth {

    List<Wealth> getWealth();

    void saveWealth(Wealth wealth);

    void deleteWealth(int wealthId);

    void updateWealth(int wealthId, Wealth wealth);
}
