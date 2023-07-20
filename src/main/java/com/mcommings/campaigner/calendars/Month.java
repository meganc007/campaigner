package com.mcommings.campaigner.calendars;

import lombok.Data;

import java.util.List;

@Data
public class Month {
    private long id;
    private String name;
    private List<Week> weeks;
}
