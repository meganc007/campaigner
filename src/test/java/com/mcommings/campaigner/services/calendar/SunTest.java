package com.mcommings.campaigner.services.calendar;

import com.mcommings.campaigner.models.calendar.Sun;
import com.mcommings.campaigner.models.repositories.calendar.ISunRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataIntegrityViolationException;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@SpringBootTest
public class SunTest {

    @Mock
    private ISunRepository sunRepository;

    @InjectMocks
    private SunService sunService;

    @Test
    public void whenThereAreSuns_getSuns_ReturnsSuns() {
        List<Sun> suns = new ArrayList<>();
        suns.add(new Sun(1, "Sun 1", "Description 1"));
        suns.add(new Sun(2, "Sun 2", "Description 2"));
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
    public void whenSunIsValid_saveSun_SavesTheSun() {
        Sun sun = new Sun(1, "Sun 1", "Description 1");
        when(sunRepository.saveAndFlush(sun)).thenReturn(sun);

        assertDoesNotThrow(() -> sunService.saveSun(sun));
        verify(sunRepository, times(1)).saveAndFlush(sun);
    }

    @Test
    public void whenSunNameIsInvalid_saveSun_ThrowsIllegalArgumentException() {
        Sun sunWithEmptyName = new Sun(1, "", "Description 1");
        Sun sunWithNullName = new Sun(2, null, "Description 2");

        assertThrows(IllegalArgumentException.class, () -> sunService.saveSun(sunWithEmptyName));
        assertThrows(IllegalArgumentException.class, () -> sunService.saveSun(sunWithNullName));
    }

    @Test
    public void whenSunNameAlreadyExists_saveSun_ThrowsDataIntegrityViolationException() {
        Sun sun = new Sun(1, "Sun 1", "Description 1");
        Sun sunWithDuplicatedName = new Sun(2, "Sun 1", "Description 2");
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

    //TODO: test that deleteSun doesn't delete when it's a foreign key in Celestial Events

    @Test
    public void whenSunIdIsFound_updateSun_UpdatesTheSun() {
        int sunId = 1;
        Sun sun = new Sun(sunId, "Old Sun Name", "Old Description");
        Sun sunToUpdate = new Sun(sunId, "Updated Sun Name", "Updated Description");

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
        Sun sun = new Sun(sunId, "Old Sun Name", "Old Description");

        when(sunRepository.existsById(sunId)).thenReturn(false);

        assertThrows(IllegalArgumentException.class, () -> sunService.updateSun(sunId, sun));
    }
}
