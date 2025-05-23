package com.mcommings.campaigner.services.people;


import com.mcommings.campaigner.modules.people.dtos.PersonDTO;
import com.mcommings.campaigner.modules.people.entities.Person;
import com.mcommings.campaigner.modules.people.mappers.PersonMapper;
import com.mcommings.campaigner.modules.people.repositories.IPersonRepository;
import com.mcommings.campaigner.modules.people.services.PersonService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataIntegrityViolationException;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
public class PersonTest {

    @Mock
    private PersonMapper personMapper;

    @Mock
    private IPersonRepository personRepository;

    @InjectMocks
    private PersonService personService;

    private Person entity;
    private PersonDTO dto;

    @BeforeEach
    void setUp() {
        Random random = new Random();
        entity = new Person();
        entity.setId(1);
        entity.setFirstName("Jane");
        entity.setLastName("Doe");
        entity.setAge(random.nextInt(100) + 1);
        entity.setTitle("A random person");
        entity.setFk_race(random.nextInt(100) + 1);
        entity.setFk_wealth(random.nextInt(100) + 1);
        entity.setFk_ability_score(random.nextInt(100) + 1);
        entity.setIsNPC(true);
        entity.setIsEnemy(false);
        entity.setPersonality("This is a personality");
        entity.setDescription("This is a description");
        entity.setNotes("This is a note");
        entity.setFk_campaign_uuid(UUID.randomUUID());

        dto = new PersonDTO();
        dto.setId(entity.getId());
        dto.setFirstName(entity.getFirstName());
        dto.setLastName(entity.getLastName());
        dto.setAge(entity.getAge());
        dto.setTitle(entity.getTitle());
        dto.setFk_race(entity.getFk_race());
        dto.setFk_wealth(entity.getFk_wealth());
        dto.setFk_ability_score(entity.getFk_ability_score());
        dto.setIsNPC(entity.getIsNPC());
        dto.setIsEnemy(entity.getIsEnemy());
        dto.setPersonality(entity.getPersonality());
        dto.setDescription(entity.getDescription());
        dto.setNotes(entity.getNotes());
        dto.setFk_campaign_uuid(entity.getFk_campaign_uuid());

        when(personMapper.mapToPersonDto(entity)).thenReturn(dto);
        when(personMapper.mapFromPersonDto(dto)).thenReturn(entity);
    }

    @Test
    public void whenThereArePeople_getPeople_ReturnsPeople() {
        when(personRepository.findAll()).thenReturn(List.of(entity));
        List<PersonDTO> result = personService.getPeople();

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("Jane", result.get(0).getFirstName());
    }

    @Test
    public void whenThereAreNoPeople_getPeople_ReturnsNothing() {
        when(personRepository.findAll()).thenReturn(Collections.emptyList());

        List<PersonDTO> result = personService.getPeople();

        assertNotNull(result);
        assertTrue(result.isEmpty(), "Expected an empty list when there are no persons.");
    }

    @Test
    public void whenThereIsAPerson_getPerson_ReturnsPeople() {
        when(personRepository.findById(1)).thenReturn(Optional.of(entity));

        Optional<PersonDTO> result = personService.getPerson(1);

        assertTrue(result.isPresent());
        assertEquals("Doe", result.get().getLastName());
    }

    @Test
    public void whenThereIsNotAPerson_getPerson_ReturnsPerson() {
        when(personRepository.findById(999)).thenReturn(Optional.empty());

        Optional<PersonDTO> result = personService.getPerson(999);

        assertTrue(result.isEmpty(), "Expected empty Optional when person is not found.");
    }

    @Test
    public void whenCampaignUUIDIsValid_getPeopleByCampaignUUID_ReturnsPersons() {
        UUID campaignUUID = entity.getFk_campaign_uuid();
        when(personRepository.findByfk_campaign_uuid(campaignUUID)).thenReturn(List.of(entity));

        List<PersonDTO> result = personService.getPeopleByCampaignUUID(campaignUUID);

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(campaignUUID, result.get(0).getFk_campaign_uuid());
    }

    @Test
    public void whenCampaignUUIDIsInvalid_getPeopleByCampaignUUID_ReturnsNothing() {
        UUID campaignUUID = UUID.randomUUID();
        when(personRepository.findByfk_campaign_uuid(campaignUUID)).thenReturn(Collections.emptyList());

        List<PersonDTO> result = personService.getPeopleByCampaignUUID(campaignUUID);

        assertNotNull(result);
        assertTrue(result.isEmpty(), "Expected an empty list when no persons match the campaign UUID.");
    }

    @Test
    public void whenPersonIsValid_savePerson_SavesThePerson() {
        when(personRepository.save(entity)).thenReturn(entity);

        personService.savePerson(dto);

        verify(personRepository, times(1)).save(entity);
    }

    @Test
    public void whenPersonNameIsInvalid_savePerson_ThrowsIllegalArgumentException() {
        PersonDTO personWithEmptyName = new PersonDTO();
        personWithEmptyName.setId(1);
        personWithEmptyName.setFirstName("");
        personWithEmptyName.setDescription("A fictional person.");
        personWithEmptyName.setFk_campaign_uuid(UUID.randomUUID());

        PersonDTO personWithNullName = new PersonDTO();
        personWithNullName.setId(1);
        personWithNullName.setFirstName(null);
        personWithNullName.setDescription("A fictional city.");
        personWithNullName.setFk_campaign_uuid(UUID.randomUUID());

        assertThrows(IllegalArgumentException.class, () -> personService.savePerson(personWithEmptyName));
        assertThrows(IllegalArgumentException.class, () -> personService.savePerson(personWithNullName));
    }

    @Test
    public void whenPersonNameAlreadyExists_savePerson_ThrowsDataIntegrityViolationException() {
        when(personMapper.mapFromPersonDto(dto)).thenReturn(entity);
        when(personRepository.personExists(any(Person.class))).thenReturn(Optional.of(entity));

        assertThrows(DataIntegrityViolationException.class, () -> personService.savePerson(dto));
        verify(personRepository, times(1)).personExists(personMapper.mapFromPersonDto(dto));
        verify(personRepository, never()).save(any(Person.class));
    }

    @Test
    public void whenPersonIdExists_deletePerson_DeletesThePerson() {
        when(personRepository.existsById(1)).thenReturn(true);
        personService.deletePerson(1);
        verify(personRepository, times(1)).deleteById(1);
    }

    @Test
    public void whenPersonIdDoesNotExist_deletePerson_ThrowsIllegalArgumentException() {
        when(personRepository.existsById(999)).thenReturn(false);
        assertThrows(IllegalArgumentException.class, () -> personService.deletePerson(999));
    }

    @Test
    public void whenDeletePersonFails_deletePerson_ThrowsException() {
        when(personRepository.existsById(1)).thenReturn(true);
        doThrow(new RuntimeException("Database error")).when(personRepository).deleteById(1);

        assertThrows(RuntimeException.class, () -> personService.deletePerson(1));
    }

    @Test
    public void whenPersonIdIsFound_updatePerson_UpdatesThePerson() {
        PersonDTO updateDTO = new PersonDTO();
        updateDTO.setFirstName("John");

        when(personRepository.findById(1)).thenReturn(Optional.of(entity));
        when(personRepository.existsById(1)).thenReturn(true);
        when(personRepository.save(entity)).thenReturn(entity);
        when(personMapper.mapToPersonDto(entity)).thenReturn(updateDTO);

        Optional<PersonDTO> result = personService.updatePerson(1, updateDTO);

        assertTrue(result.isPresent());
        assertEquals("John", result.get().getFirstName());
    }

    @Test
    public void whenPersonIdIsNotFound_updatePerson_ReturnsEmptyOptional() {
        PersonDTO updateDTO = new PersonDTO();
        updateDTO.setFirstName("John");

        when(personRepository.findById(999)).thenReturn(Optional.empty());
        assertThrows(IllegalArgumentException.class, () -> personService.updatePerson(999, updateDTO));
    }

    @Test
    public void whenPersonNameIsInvalid_updatePerson_ThrowsIllegalArgumentException() {
        PersonDTO updateEmptyName = new PersonDTO();
        updateEmptyName.setFirstName("");

        PersonDTO updateNullName = new PersonDTO();
        updateNullName.setFirstName(null);

        when(personRepository.existsById(1)).thenReturn(true);

        assertThrows(IllegalArgumentException.class, () -> personService.updatePerson(1, updateEmptyName));
        assertThrows(IllegalArgumentException.class, () -> personService.updatePerson(1, updateNullName));
    }

    @Test
    public void whenPersonNameAlreadyExists_updatePerson_ThrowsDataIntegrityViolationException() {
        when(personRepository.existsById(dto.getId())).thenReturn(true);
        when(personMapper.mapFromPersonDto(dto)).thenReturn(entity);
        when(personRepository.findByFirstNameAndLastName(dto.getFirstName(), dto.getLastName()))
                .thenReturn(Optional.of(entity));

        assertThrows(DataIntegrityViolationException.class, () -> personService.updatePerson(dto.getId(), dto));

        verify(personRepository, times(1)).existsById(dto.getId());
        verify(personRepository, times(1)).findByFirstNameAndLastName(dto.getFirstName(), dto.getLastName());
        verify(personRepository, never()).save(any(Person.class));
    }
}
