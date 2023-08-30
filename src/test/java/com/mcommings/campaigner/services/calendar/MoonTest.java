package com.mcommings.campaigner.services.calendar;

import com.mcommings.campaigner.models.calendar.Moon;
import com.mcommings.campaigner.models.repositories.calendar.IMoonRepository;
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
public class MoonTest {
    
    @Mock
    private IMoonRepository moonRepository;
    
    @InjectMocks
    private MoonService  moonService;
    
    @Test
    public void whenThereAreMoons_getMoons_ReturnsMoons() {
        List<Moon> moons = new ArrayList<>();
        moons.add(new Moon(1, "Moon 1", "Description 1"));
        moons.add(new Moon(2, "Moon 2", "Description 2"));
        when(moonRepository.findAll()).thenReturn(moons);

        List<Moon> result = moonService.getMoons();

        Assertions.assertEquals(2, result.size());
        Assertions.assertEquals(moons, result);
    }

    @Test
    public void whenThereAreNoMoons_getMoons_ReturnsNothing() {
        List<Moon> moons = new ArrayList<>();
        when(moonRepository.findAll()).thenReturn(moons);

        List<Moon> result = moonService.getMoons();

        Assertions.assertEquals(0, result.size());
        Assertions.assertEquals(moons, result);
    }

    @Test
    public void whenMoonIsValid_saveMoon_SavesTheMoon() {
        Moon moon = new Moon(1, "Moon 1", "Description 1");
        when(moonRepository.saveAndFlush(moon)).thenReturn(moon);

        assertDoesNotThrow(() -> moonService.saveMoon(moon));
        verify(moonRepository, times(1)).saveAndFlush(moon);
    }

    @Test
    public void whenMoonNameIsInvalid_saveMoon_ThrowsIllegalArgumentException() {
        Moon moonWithEmptyName = new Moon(1, "", "Description 1");
        Moon moonWithNullName = new Moon(2, null, "Description 2");

        assertThrows(IllegalArgumentException.class, () -> moonService.saveMoon(moonWithEmptyName));
        assertThrows(IllegalArgumentException.class, () -> moonService.saveMoon(moonWithNullName));
    }

    @Test
    public void whenMoonNameAlreadyExists_saveMoon_ThrowsDataIntegrityViolationException() {
        Moon moon = new Moon(1, "Moon 1", "Description 1");
        Moon moonWithDuplicatedName = new Moon(2, "Moon 1", "Description 2");
        when(moonRepository.saveAndFlush(moon)).thenReturn(moon);
        when(moonRepository.saveAndFlush(moonWithDuplicatedName)).thenThrow(DataIntegrityViolationException.class);

        assertDoesNotThrow(() -> moonService.saveMoon(moon));
        assertThrows(DataIntegrityViolationException.class, () -> moonService.saveMoon(moonWithDuplicatedName));
    }

    @Test
    public void whenMoonIdExists_deleteMoon_DeletesTheMoon() {
        int moonId = 1;
        when(moonRepository.existsById(moonId)).thenReturn(true);
        assertDoesNotThrow(() -> moonService.deleteMoon(moonId));
        verify(moonRepository, times(1)).deleteById(moonId);
    }

    @Test
    public void whenMoonIdDoesNotExist_deleteMoon_ThrowsIllegalArgumentException() {
        int moonId = 9000;
        when(moonRepository.existsById(moonId)).thenReturn(false);
        assertThrows(IllegalArgumentException.class, () -> moonService.deleteMoon(moonId));
    }

    //TODO: test that deleteMoon doesn't delete when it's a foreign key in Celestial Events
    
    @Test
    public void whenMoonIdIsFound_updateMoon_UpdatesTheMoon() {
        int moonId = 1;
        Moon moon = new Moon(moonId, "Old Moon Name", "Old Description");
        Moon moonToUpdate = new Moon(moonId, "Updated Moon Name", "Updated Description");

        when(moonRepository.existsById(moonId)).thenReturn(true);
        when(moonRepository.findById(moonId)).thenReturn(Optional.of(moon));

        moonService.updateMoon(moonId, moonToUpdate);

        verify(moonRepository).findById(moonId);

        Moon result = moonRepository.findById(moonId).get();
        Assertions.assertEquals(moonToUpdate.getName(), result.getName());
        Assertions.assertEquals(moonToUpdate.getDescription(), result.getDescription());
    }

    @Test
    public void whenMoonIdIsNotFound_updateMoon_ThrowsIllegalArgumentException() {
        int moonId = 1;
        Moon moon = new Moon(moonId, "Old Moon Name", "Old Description");

        when(moonRepository.existsById(moonId)).thenReturn(false);

        assertThrows(IllegalArgumentException.class, () -> moonService.updateMoon(moonId, moon));
    }
}
