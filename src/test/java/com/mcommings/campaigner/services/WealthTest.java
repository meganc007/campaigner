package com.mcommings.campaigner.services;

import com.mcommings.campaigner.entities.RepositoryHelper;
import com.mcommings.campaigner.entities.Wealth;
import com.mcommings.campaigner.entities.people.NamedMonster;
import com.mcommings.campaigner.entities.people.Person;
import com.mcommings.campaigner.locations.entities.City;
import com.mcommings.campaigner.locations.repositories.ICityRepository;
import com.mcommings.campaigner.repositories.IWealthRepository;
import com.mcommings.campaigner.repositories.people.INamedMonsterRepository;
import com.mcommings.campaigner.repositories.people.IPersonRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.repository.CrudRepository;

import java.util.*;

import static com.mcommings.campaigner.enums.ForeignKey.FK_WEALTH;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@SpringBootTest
public class WealthTest {

    @Mock
    private IWealthRepository wealthRepository;
    @Mock
    private ICityRepository cityRepository;
    @Mock
    private IPersonRepository personRepository;
    @Mock
    private INamedMonsterRepository namedMonsterRepository;

    @InjectMocks
    private WealthService wealthService;

    @Test
    public void whenThereAreWealth_getWealth_ReturnsWealth() {
        List<Wealth> wealthList = new ArrayList<>();
        wealthList.add(new Wealth(1, "Wealth 1"));
        wealthList.add(new Wealth(2, "Wealth 2"));
        when(wealthRepository.findAll()).thenReturn(wealthList);

        List<Wealth> result = wealthService.getWealth();

        Assertions.assertEquals(2, result.size());
        Assertions.assertEquals(wealthList, result);
    }

    @Test
    public void whenThereAreNoWealth_getWealth_ReturnsNothing() {
        List<Wealth> wealthList = new ArrayList<>();
        when(wealthRepository.findAll()).thenReturn(wealthList);

        List<Wealth> result = wealthService.getWealth();

        Assertions.assertEquals(0, result.size());
        Assertions.assertEquals(wealthList, result);
    }

    @Test
    public void whenWealthIsValid_saveWealth_SavesTheWealth() {
        Wealth wealth = new Wealth(1, "Wealth 1");
        when(wealthRepository.saveAndFlush(wealth)).thenReturn(wealth);

        assertDoesNotThrow(() -> wealthService.saveWealth(wealth));
        verify(wealthRepository, times(1)).saveAndFlush(wealth);
    }

    @Test
    public void whenWealthNameIsInvalid_saveWealth_ThrowsIllegalArgumentException() {
        Wealth wealthWithEmptyName = new Wealth(1, "");
        Wealth wealthWithNullName = new Wealth(2, null);

        assertThrows(IllegalArgumentException.class, () -> wealthService.saveWealth(wealthWithEmptyName));
        assertThrows(IllegalArgumentException.class, () -> wealthService.saveWealth(wealthWithNullName));
    }

    @Test
    public void whenWealthNameAlreadyExists_saveWealth_ThrowsDataIntegrityViolationException() {
        Wealth wealth = new Wealth(1, "Wealth 1");
        Wealth wealthWithDuplicatedName = new Wealth(2, "Wealth 1");
        when(wealthRepository.saveAndFlush(wealth)).thenReturn(wealth);
        when(wealthRepository.saveAndFlush(wealthWithDuplicatedName)).thenThrow(DataIntegrityViolationException.class);

        assertDoesNotThrow(() -> wealthService.saveWealth(wealth));
        assertThrows(DataIntegrityViolationException.class, () -> wealthService.saveWealth(wealthWithDuplicatedName));
    }

    @Test
    public void whenWealthIdExists_deleteWealth_DeletesTheWealth() {
        int wealthId = 1;
        when(wealthRepository.existsById(wealthId)).thenReturn(true);
        assertDoesNotThrow(() -> wealthService.deleteWealth(wealthId));
        verify(wealthRepository, times(1)).deleteById(wealthId);
    }

    @Test
    public void whenWealthIdDoesNotExist_deleteWealth_ThrowsIllegalArgumentException() {
        int wealthId = 9000;
        when(wealthRepository.existsById(wealthId)).thenReturn(false);
        assertThrows(IllegalArgumentException.class, () -> wealthService.deleteWealth(wealthId));
    }

    @Test
    public void whenWealthIdIsAForeignKey_deleteWealth_ThrowsDataIntegrityViolationException() {
        int wealthId = 1;
        City city = new City(1, "City", "Description", UUID.randomUUID(), wealthId, 1, 1, 1, 1);
        List<CrudRepository> repositories = new ArrayList<>(Arrays.asList(cityRepository, personRepository));
        List<City> cities = new ArrayList<>(Arrays.asList(city));

        Person person = new Person(1, "Jane", "Doe", 33, "The Nameless One",
                3, wealthId, 2, true, false, "Personality", "Description", "Notes", UUID.randomUUID());
        List<Person> people = new ArrayList<>(Arrays.asList(person));

        NamedMonster namedMonster = new NamedMonster(1, "First Name", "Last Name", "Title",
                wealthId, 2, 1, false, "Personality", "Description", "Notes", UUID.randomUUID());
        List<NamedMonster> namedMonsters = new ArrayList<>(Arrays.asList(namedMonster));

        when(wealthRepository.existsById(wealthId)).thenReturn(true);
        when(cityRepository.findByfk_wealth(wealthId)).thenReturn(cities);
        when(personRepository.findByfk_wealth(wealthId)).thenReturn(people);
        when(namedMonsterRepository.findByfk_wealth(wealthId)).thenReturn(namedMonsters);

        boolean actual = RepositoryHelper.isForeignKey(repositories, FK_WEALTH.columnName, wealthId);
        Assertions.assertTrue(actual);
        assertThrows(DataIntegrityViolationException.class, () -> wealthService.deleteWealth(wealthId));
    }

    @Test
    public void whenWealthIdIsFound_updateWealth_UpdatesTheWealth() {
        int wealthId = 1;
        Wealth wealth = new Wealth(wealthId, "Old Wealth Name");
        Wealth wealthToUpdate = new Wealth(wealthId, "Updated Wealth Name");

        when(wealthRepository.existsById(wealthId)).thenReturn(true);
        when(wealthRepository.findById(wealthId)).thenReturn(Optional.of(wealth));

        wealthService.updateWealth(wealthId, wealthToUpdate);

        verify(wealthRepository).findById(wealthId);

        Wealth result = wealthRepository.findById(wealthId).get();
        Assertions.assertEquals(wealthToUpdate.getName(), result.getName());
    }

    @Test
    public void whenWealthIdIsNotFound_updateWealth_ThrowsIllegalArgumentException() {
        int wealthId = 1;
        Wealth wealth = new Wealth(wealthId, "Old Wealth Name");

        when(wealthRepository.existsById(wealthId)).thenReturn(false);

        assertThrows(IllegalArgumentException.class, () -> wealthService.updateWealth(wealthId, wealth));
    }
}
