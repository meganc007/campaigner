package com.mcommings.campaigner.controllers.people;

import com.mcommings.campaigner.entities.people.Person;
import com.mcommings.campaigner.services.people.PersonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping(path = "api/people/people")
public class PersonController {

    private final PersonService personService;

    @Autowired
    public PersonController(PersonService personService) {
        this.personService = personService;
    }

    @GetMapping
    public List<Person> getPeople() {
        return personService.getPeople();
    }

    @GetMapping(path = "/campaign/{uuid}")
    public List<Person> getPeopleByCampaignUUID(@PathVariable("uuid") UUID uuid) {
        return personService.getPeopleByCampaignUUID(uuid);
    }

    @PostMapping
    public void savePerson(@RequestBody Person person) {
        personService.savePerson(person);
    }

    @DeleteMapping(path = "{personId}")
    public void deletePerson(@PathVariable("personId") int personId) {
        personService.deletePerson(personId);
    }

    @PutMapping(path = "{personId}")
    public void updatePerson(@PathVariable("personId") int personId, @RequestBody Person person) {
        personService.updatePerson(personId, person);
    }
}
