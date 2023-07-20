package com.mcommings.campaigner.calendars;

import lombok.Data;

import java.util.List;

@Data
public class Week {
    private long id;
    private String name;
    private List<Day> days;
}
