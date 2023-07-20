package com.mcommings.campaigner.calendars;

import lombok.Data;

import java.util.List;

@Data
public class Calendar {
    private long id;
    private int year;
    private List<Month> months;
    private List<Moon> moons;
    private List<Sun> suns;

}
