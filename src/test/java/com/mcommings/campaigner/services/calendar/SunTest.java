package com.mcommings.campaigner.services.calendar;

import com.mcommings.campaigner.entities.RepositoryHelper;
import com.mcommings.campaigner.entities.calendar.CelestialEvent;
import com.mcommings.campaigner.entities.calendar.Sun;
import com.mcommings.campaigner.repositories.calendar.ICelestialEventRepository;
import com.mcommings.campaigner.repositories.calendar.ISunRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.repository.CrudRepository;

import java.util.*;

import static com.mcommings.campaigner.enums.ForeignKey.FK_SUN;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@SpringBootTest
public class SunTest {

    @Mock
    private ISunRepository sunRepository;
    @Mock
    private ICelestialEventRepository celestialEventRepository;

    @InjectMocks
    private SunService sunService;

    @Test
    public void whenThereAreSuns_getSuns_ReturnsSuns() {
        List<Sun> suns = new ArrayList<>();
        UUID campaign = UUID.randomUUID();
        suns.add(new Sun(1, "Sun 1", "Description 1", campaign));
        suns.add(new Sun(2, "Sun 2", "Description 2", campaign));
        when(sunRepository.findAll()).thenReturn(suns);

        List<Sun> result = sunService.getSuns();

        Assertions.assertEquals(2, result.size());
        Assertions.assertEquals(suns, result);
    }

    @Test
    public void whenThereAreNoSuns_getSuns_ReturnsNothing() {
        List<Sun> suns = new ArrayList<>();
        when(sunRepository.findAll()).thenReturn(suns);

        List<Sun> result = sunService.getSuns();

        Assertions.assertEquals(0, result.size());
        Assertions.assertEquals(suns, result);
    }

    @Test
    public void whenCampaignUUIDIsValid_getSunsByCampaignUUID_ReturnsSuns() {
        List<Sun> suns = new ArrayList<>();
        UUID campaign = UUID.randomUUID();
        suns.add(new Sun(1, "Sun 1", "Description 1", campaign));
        suns.add(new Sun(2, "Sun 2", "Description 2", campaign));
        when(sunRepository.findByfk_campaign_uuid(campaign)).thenReturn(suns);

        List<Sun> results = sunService.getSunsByCampaignUUID(campaign);

        Assertions.assertEquals(2, results.size());
        Assertions.assertEquals(suns, results);
    }

    @Test
    public void whenCampaignUUIDIsInvalid_getSunsByCampaignUUID_ReturnsNothing() {
        UUID campaign = UUID.randomUUID();
        List<Sun> suns = new ArrayList<>();
        when(sunRepository.findByfk_campaign_uuid(campaign)).thenReturn(suns);

        List<Sun> result = sunService.getSunsByCampaignUUID(campaign);

        Assertions.assertEquals(0, result.size());
        Assertions.assertEquals(suns, result);
    }

    @Test
    public void whenSunIsValid_saveSun_SavesTheSun() {
        Sun sun = new Sun(1, "Sun 1", "Description 1", UUID.randomUUID());
        when(sunRepository.saveAndFlush(sun)).thenReturn(sun);

        assertDoesNotThrow(() -> sunService.saveSun(sun));
        verify(sunRepository, times(1)).saveAndFlush(sun);
    }

    @Test
    public void whenSunNameIsInvalid_saveSun_ThrowsIllegalArgumentException() {
        Sun sunWithEmptyName = new Sun(1, "", "Description 1", UUID.randomUUID());
        Sun sunWithNullName = new Sun(2, null, "Description 2", UUID.randomUUID());

        assertThrows(IllegalArgumentException.class, () -> sunService.saveSun(sunWithEmptyName));
        assertThrows(IllegalArgumentException.class, () -> sunService.saveSun(sunWithNullName));
    }

    @Test
    public void whenSunNameAlreadyExists_saveSun_ThrowsDataIntegrityViolationException() {
        Sun sun = new Sun(1, "Sun 1", "Description 1", UUID.randomUUID());
        Sun sunWithDuplicatedName = new Sun(2, "Sun 1", "Description 2", UUID.randomUUID());
        when(sunRepository.saveAndFlush(sun)).thenReturn(sun);
        when(sunRepository.saveAndFlush(sunWithDuplicatedName)).thenThrow(DataIntegrityViolationException.class);

        assertDoesNotThrow(() -> sunService.saveSun(sun));
        assertThrows(DataIntegrityViolationException.class, () -> sunService.saveSun(sunWithDuplicatedName));
    }

    @Test
    public void whenSunIdExists_deleteSun_DeletesTheSun() {
        int sunId = 1;
        when(sunRepository.existsById(sunId)).thenReturn(true);
        assertDoesNotThrow(() -> sunService.deleteSun(sunId));
        verify(sunRepository, times(1)).deleteById(sunId);
    }

    @Test
    public void whenSunIdDoesNotExist_deleteSun_ThrowsIllegalArgumentException() {
        int sunId = 9000;
        when(sunRepository.existsById(sunId)).thenReturn(false);
        assertThrows(IllegalArgumentException.class, () -> sunService.deleteSun(sunId));
    }

    @Test
    public void whenSunIdIsAForeignKey_deleteSun_ThrowsDataIntegrityViolationException() {
        int sunId = 1;
        CelestialEvent celestialEvent = new CelestialEvent(1, "CelestialEvent", "Description", 1, sunId, 1, 1, 1, 1, UUID.randomUUID());
        List<CrudRepository> repositories = new ArrayList<>(Arrays.asList(celestialEventRepository));
        List<CelestialEvent> celestialEvents = new ArrayList<>(Arrays.asList(celestialEvent));

        when(sunRepository.existsById(sunId)).thenReturn(true);
        when(celestialEventRepository.findByfk_sun(sunId)).thenReturn(celestialEvents);

        boolean actual = RepositoryHelper.isForeignKey(repositories, FK_SUN.columnName, sunId);
        Assertions.assertTrue(actual);
        assertThrows(DataIntegrityViolationException.class, () -> sunService.deleteSun(sunId));
    }
    @Test
    public void whenSunIdIsFound_updateSun_UpdatesTheSun() {
        int sunId = 1;
        UUID campaign = UUID.randomUUID();
        Sun sun = new Sun(sunId, "Old Sun Name", "Old Description", campaign);
        Sun sunToUpdate = new Sun(sunId, "Updated Sun Name", "Updated Description", campaign);

        when(sunRepository.existsById(sunId)).thenReturn(true);
        when(sunRepository.findById(sunId)).thenReturn(Optional.of(sun));

        sunService.updateSun(sunId, sunToUpdate);

        verify(sunRepository).findById(sunId);

        Sun result = sunRepository.findById(sunId).get();
        Assertions.assertEquals(sunToUpdate.getName(), result.getName());
        Assertions.assertEquals(sunToUpdate.getDescription(), result.getDescription());
    }

    @Test
    public void whenSunIdIsNotFound_updateSun_ThrowsIllegalArgumentException() {
        int sunId = 1;
        Sun sun = new Sun(sunId, "Old Sun Name", "Old Description", UUID.randomUUID());

        when(sunRepository.existsById(sunId)).thenReturn(false);

        assertThrows(IllegalArgumentException.class, () -> sunService.updateSun(sunId, sun));
    }

    @Test
    public void whenSomeSunFieldsChanged_updateSun_OnlyUpdatesChangedFields() {
        int sunId = 1;
        Sun sun = new Sun(sunId, "Old Sun Name", "Old Description", UUID.randomUUID());

        String newName = "New Sun";

        Sun sunToUpdate = new Sun();
        sunToUpdate.setName(newName);

        when(sunRepository.existsById(sunId)).thenReturn(true);
        when(sunRepository.findById(sunId)).thenReturn(Optional.of(sun));

        sunService.updateSun(sunId, sunToUpdate);

        verify(sunRepository).findById(sunId);

        Sun result = sunRepository.findById(sunId).get();
        Assertions.assertEquals(sun.getDescription(), result.getDescription());
        Assertions.assertEquals(newName, result.getName());
    }
}
