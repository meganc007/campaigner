package com.mcommings.campaigner.interfaces;

import com.mcommings.campaigner.models.Month;

import java.util.List;

public interface IMonth {

    List<Month> getMonths();

    void saveMonth(Month month);

    void deleteMonth(int monthId);

    void updateMonth(int monthId, Month month);

}
