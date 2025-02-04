package com.mcommings.campaigner.services.locations;

import com.mcommings.campaigner.entities.Event;
import com.mcommings.campaigner.entities.RepositoryHelper;
import com.mcommings.campaigner.entities.locations.Continent;
import com.mcommings.campaigner.entities.locations.Country;
import com.mcommings.campaigner.repositories.IEventRepository;
import com.mcommings.campaigner.repositories.locations.IContinentRepository;
import com.mcommings.campaigner.repositories.locations.ICountryRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.repository.CrudRepository;

import java.util.*;

import static com.mcommings.campaigner.enums.ForeignKey.FK_CONTINENT;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@SpringBootTest
public class ContinentTest {
    @Mock
    private IContinentRepository continentRepository;
    @Mock
    private ICountryRepository countryRepository;
    @Mock
    private IEventRepository eventRepository;

    @InjectMocks
    private ContinentService continentService;

    @Test
    public void whenThereAreContinents_getContinents_ReturnsContinents() {
        List<Continent> continents = new ArrayList<>();
        continents.add(new Continent(1, "Continent 1", "Description 1", UUID.randomUUID()));
        continents.add(new Continent(2, "Continent 2", "Description 2", UUID.randomUUID()));
        when(continentRepository.findAll()).thenReturn(continents);

        List<Continent> result = continentService.getContinents();

        Assertions.assertEquals(2, result.size());
        Assertions.assertEquals(continents, result);
    }

    @Test
    public void whenThereAreNoContinents_getContinents_ReturnsNothing() {
        List<Continent> continents = new ArrayList<>();
        when(continentRepository.findAll()).thenReturn(continents);

        List<Continent> result = continentService.getContinents();

        Assertions.assertEquals(0, result.size());
        Assertions.assertEquals(continents, result);
    }

    @Test
    public void whenThereIsAContinent_getContinent_ReturnsContinent() {
        Continent continent = new Continent(1, "Continent 1", "Description 1", UUID.randomUUID());
        when(continentRepository.findById(1)).thenReturn(Optional.of(continent));

        Continent result = continentService.getContinent(1);

        Assertions.assertEquals(continent, result);
    }

    @Test
    public void whenThereIsNotAContinent_getContinent_ReturnsNothing() {
        when(continentRepository.findById(9000)).thenReturn(Optional.empty());
        Assertions.assertThrows(NoSuchElementException.class, () -> continentService.getContinent(9000));
    }

    @Test
    public void whenCampaignUUIDIsValid_getContinentsByCampaignUUID_ReturnsContinents() {
        UUID campaign = UUID.randomUUID();
        List<Continent> continents = new ArrayList<>();
        continents.add(new Continent(1, "Continent 1", "Description 1", campaign));
        continents.add(new Continent(2, "Continent 2", "Description 2", campaign));

        when(continentRepository.findByfk_campaign_uuid(campaign)).thenReturn(continents);

        List<Continent> results = continentService.getContinentsByCampaignUUID(campaign);

        Assertions.assertEquals(2, results.size());
        Assertions.assertEquals(continents, results);
    }

    @Test
    public void whenCampaignUUIDIsInvalid_getContinentsByCampaignUUID_ReturnsNothing() {
        UUID campaign = UUID.randomUUID();
        List<Continent> continents = new ArrayList<>();
        when(continentRepository.findByfk_campaign_uuid(campaign)).thenReturn(continents);

        List<Continent> result = continentService.getContinentsByCampaignUUID(campaign);

        Assertions.assertEquals(0, result.size());
        Assertions.assertEquals(continents, result);
    }

    @Test
    public void whenContinentIsValid_saveContinent_SavesTheContinent() {
        Continent continent = new Continent(1, "Continent 1", "Description 1", UUID.randomUUID());
        when(continentRepository.saveAndFlush(continent)).thenReturn(continent);

        assertDoesNotThrow(() -> continentService.saveContinent(continent));
        verify(continentRepository, times(1)).saveAndFlush(continent);
    }

    @Test
    public void whenContinentNameIsInvalid_saveContinent_ThrowsIllegalArgumentException() {
        Continent continentWithEmptyName = new Continent(1, "", "Description 1", UUID.randomUUID());
        Continent continentWithNullName = new Continent(2, null, "Description 2", UUID.randomUUID());

        assertThrows(IllegalArgumentException.class, () -> continentService.saveContinent(continentWithEmptyName));
        assertThrows(IllegalArgumentException.class, () -> continentService.saveContinent(continentWithNullName));
    }

    @Test
    public void whenContinentNameAlreadyExists_saveContinent_ThrowsDataIntegrityViolationException() {
        Continent continent = new Continent(1, "Continent 1", "Description 1", UUID.randomUUID());
        Continent continentWithDuplicatedName = new Continent(2, "Continent 1", "Description 2", UUID.randomUUID());
        when(continentRepository.saveAndFlush(continent)).thenReturn(continent);
        when(continentRepository.saveAndFlush(continentWithDuplicatedName)).thenThrow(DataIntegrityViolationException.class);

        assertDoesNotThrow(() -> continentService.saveContinent(continent));
        assertThrows(DataIntegrityViolationException.class, () -> continentService.saveContinent(continentWithDuplicatedName));
    }

    @Test
    public void whenContinentIdExists_deleteContinent_DeletesTheContinent() {
        int continentId = 1;
        when(continentRepository.existsById(continentId)).thenReturn(true);
        assertDoesNotThrow(() -> continentService.deleteContinent(continentId));
        verify(continentRepository, times(1)).deleteById(continentId);
    }

    @Test
    public void whenContinentIdDoesNotExist_deleteContinent_ThrowsIllegalArgumentException() {
        int continentId = 9000;
        when(continentRepository.existsById(continentId)).thenReturn(false);
        assertThrows(IllegalArgumentException.class, () -> continentService.deleteContinent(continentId));
    }

    @Test
    public void whenContinentIdIsAForeignKey_deleteContinent_ThrowsDataIntegrityViolationException() {
        int continentId = 1;
        Country country = new Country(1, "Country", "Description", UUID.randomUUID(), continentId, 1);
        List<CrudRepository> repositories = new ArrayList<>(Arrays.asList(countryRepository, eventRepository));
        List<Country> countries = new ArrayList<>(Arrays.asList(country));

        Event event = new Event(1, "Name", "Description", 1, 1, 1, 1,
                1, continentId, 1, UUID.randomUUID());
        List<Event> events = new ArrayList<>(Arrays.asList(event));

        when(continentRepository.existsById(continentId)).thenReturn(true);
        when(countryRepository.findByfk_continent(continentId)).thenReturn(countries);
        when(eventRepository.findByfk_continent(continentId)).thenReturn(events);

        boolean actual = RepositoryHelper.isForeignKey(repositories, FK_CONTINENT.columnName, continentId);
        Assertions.assertTrue(actual);
        assertThrows(DataIntegrityViolationException.class, () -> continentService.deleteContinent(continentId));
    }

    @Test
    public void whenContinentIdIsFound_updateContinent_UpdatesTheContinent() {
        int continentId = 1;
        Continent continent = new Continent(continentId, "Old Continent Name", "Old Description", UUID.randomUUID());
        Continent continentToUpdate = new Continent(continentId, "Updated Continent Name", "Updated Description", UUID.randomUUID());

        when(continentRepository.existsById(continentId)).thenReturn(true);
        when(continentRepository.findById(continentId)).thenReturn(Optional.of(continent));

        continentService.updateContinent(continentId, continentToUpdate);

        verify(continentRepository).findById(continentId);

        Continent result = continentRepository.findById(continentId).get();
        Assertions.assertEquals(continentToUpdate.getName(), result.getName());
        Assertions.assertEquals(continentToUpdate.getDescription(), result.getDescription());
    }

    @Test
    public void whenContinentIdIsNotFound_updateContinent_ThrowsIllegalArgumentException() {
        int continentId = 1;
        Continent continent = new Continent(continentId, "Old Continent Name", "Old Description", UUID.randomUUID());

        when(continentRepository.existsById(continentId)).thenReturn(false);

        assertThrows(IllegalArgumentException.class, () -> continentService.updateContinent(continentId, continent));
    }

    @Test
    public void whenSomeContinentFieldsChanged_updateContinent_OnlyUpdatesChangedFields() {
        int continentId = 1;
        Continent continent = new Continent(continentId, "Name", "Old Continent Description", UUID.randomUUID());

        String newDescription = "New Continent description";

        Continent continentToUpdate = new Continent();
        continentToUpdate.setDescription(newDescription);

        when(continentRepository.existsById(continentId)).thenReturn(true);
        when(continentRepository.findById(continentId)).thenReturn(Optional.of(continent));

        continentService.updateContinent(continentId, continentToUpdate);

        verify(continentRepository).findById(continentId);

        Continent result = continentRepository.findById(continentId).get();
        Assertions.assertEquals(continent.getName(), result.getName());
        Assertions.assertEquals(newDescription, result.getDescription());
    }
}
