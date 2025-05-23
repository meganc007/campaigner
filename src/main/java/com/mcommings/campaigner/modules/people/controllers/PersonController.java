package com.mcommings.campaigner.modules.people.controllers;

import com.mcommings.campaigner.modules.people.dtos.PersonDTO;
import com.mcommings.campaigner.modules.people.services.interfaces.IPerson;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

import static com.mcommings.campaigner.enums.ErrorMessage.ID_NOT_FOUND;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "api/people")
public class PersonController {

    private final IPerson personService;

    @GetMapping
    public List<PersonDTO> getPeople() {
        return personService.getPeople();
    }

    @GetMapping(path = "/{personId}")
    public PersonDTO getPerson(@PathVariable("personId") int personId) {
        return personService.getPerson(personId).orElseThrow(() -> new IllegalArgumentException(ID_NOT_FOUND.message));
    }

    @GetMapping(path = "/campaign/{uuid}")
    public List<PersonDTO> getPeopleByCampaignUUID(@PathVariable("uuid") UUID uuid) {
        return personService.getPeopleByCampaignUUID(uuid);
    }

    @GetMapping(path = "/race/{raceId}")
    public List<PersonDTO> getPeopleByRace(@PathVariable("raceId") int raceId) {
        return personService.getPeopleByRace(raceId);
    }

    @GetMapping(path = "/abilityScore/{abilityScoreId}")
    public List<PersonDTO> getPeopleByAbilityScore(@PathVariable("abilityScoreId") int abilityScoreId) {
        return personService.getPeopleByAbilityScore(abilityScoreId);
    }

    @GetMapping(path = "/enemy/{isEnemy}")
    public List<PersonDTO> getPeopleByEnemyStatus(@PathVariable("isEnemy") boolean isEnemy) {
        return personService.getPeopleByEnemyStatus(isEnemy);
    }

    @GetMapping(path = "/npc/{isNPC}")
    public List<PersonDTO> getPeopleByNPCStatus(@PathVariable("isNPC") boolean isNPC) {
        return personService.getPeopleByNPCStatus(isNPC);
    }

    @PostMapping
    public void savePerson(@Valid @RequestBody PersonDTO person) {
        personService.savePerson(person);
    }

    @DeleteMapping(path = "{personId}")
    public void deletePerson(@PathVariable("personId") int personId) {
        personService.deletePerson(personId);
    }

    @PutMapping(path = "{personId}")
    public void updatePerson(@PathVariable("personId") int personId, @RequestBody PersonDTO person) {
        personService.updatePerson(personId, person);
    }
}
