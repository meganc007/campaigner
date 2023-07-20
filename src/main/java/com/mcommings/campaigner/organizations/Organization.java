package com.mcommings.campaigner.organizations;

import com.mcommings.campaigner.people.Person;
import lombok.Data;

import java.util.List;

@Data
public class Organization {
    private long id;
    private String name;
    private String description;
    private List<Person> members;
}
