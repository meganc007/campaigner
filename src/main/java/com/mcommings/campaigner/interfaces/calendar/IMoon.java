package com.mcommings.campaigner.interfaces.calendar;

import com.mcommings.campaigner.models.calendar.Moon;

import java.util.List;

public interface IMoon {

    List<Moon> getMoons();

    void saveMoon(Moon moon);

    void deleteMoon(int moonId);

    void updateMoon(int moonId, Moon moon);
}
