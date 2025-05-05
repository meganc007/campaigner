package com.mcommings.campaigner.services.common;

import com.mcommings.campaigner.modules.common.dtos.WealthDTO;
import com.mcommings.campaigner.modules.common.entities.Wealth;
import com.mcommings.campaigner.modules.common.mappers.WealthMapper;
import com.mcommings.campaigner.modules.common.repositories.IWealthRepository;
import com.mcommings.campaigner.modules.common.services.WealthService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataIntegrityViolationException;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
public class WealthTest {

    @Mock
    private WealthMapper wealthMapper;

    @Mock
    private IWealthRepository wealthRepository;

    @InjectMocks
    private WealthService wealthService;

    private Wealth entity;
    private WealthDTO dto;

    @BeforeEach
    void setUp() {
        entity = new Wealth();
        entity.setId(1);
        entity.setName("Test Wealth");

        dto = new WealthDTO();
        dto.setId(entity.getId());
        dto.setName(entity.getName());

        when(wealthMapper.mapToWealthDto(entity)).thenReturn(dto);
        when(wealthMapper.mapFromWealthDto(dto)).thenReturn(entity);
    }

    @Test
    public void whenThereAreWealth_getWealth_ReturnsWealth() {
        when(wealthRepository.findAll()).thenReturn(List.of(entity));
        List<WealthDTO> result = wealthService.getWealth();

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("Test Wealth", result.get(0).getName());
    }

    @Test
    public void whenThereAreNoWealth_getWealth_ReturnsEmptyList() {
        when(wealthRepository.findAll()).thenReturn(Collections.emptyList());

        List<WealthDTO> result = wealthService.getWealth();

        assertNotNull(result);
        assertTrue(result.isEmpty(), "Expected an empty list when there are no wealth.");
    }

    @Test
    void whenWealthIsValid_saveWealth_SavesTheWealth() {
        when(wealthRepository.save(entity)).thenReturn(entity);

        wealthService.saveWealth(dto);

        verify(wealthRepository, times(1)).save(entity);
    }

    @Test
    public void whenWealthNameIsInvalid_saveWealth_ThrowsIllegalArgumentException() {
        WealthDTO wealthWithEmptyName = new WealthDTO();
        wealthWithEmptyName.setId(1);
        wealthWithEmptyName.setName("");

        WealthDTO wealthWithNullName = new WealthDTO();
        wealthWithNullName.setId(1);
        wealthWithNullName.setName(null);

        assertThrows(IllegalArgumentException.class, () -> wealthService.saveWealth(wealthWithEmptyName));
        assertThrows(IllegalArgumentException.class, () -> wealthService.saveWealth(wealthWithNullName));
    }

    @Test
    public void whenWealthNameAlreadyExists_saveWealth_ThrowsDataIntegrityViolationException() {
        when(wealthRepository.findByName(dto.getName())).thenReturn(Optional.of(entity));
        assertThrows(DataIntegrityViolationException.class, () -> wealthService.saveWealth(dto));
        verify(wealthRepository, times(1)).findByName(dto.getName());
        verify(wealthRepository, never()).save(any(Wealth.class));
    }

    @Test
    void whenWealthIdExists_deleteWealth_DeletesTheWealth() {
        when(wealthRepository.existsById(1)).thenReturn(true);
        wealthService.deleteWealth(1);
        verify(wealthRepository, times(1)).deleteById(1);
    }

    @Test
    void whenWealthIdDoesNotExist_deleteWealth_ThrowsIllegalArgumentException() {
        when(wealthRepository.existsById(999)).thenReturn(false);
        assertThrows(IllegalArgumentException.class, () -> wealthService.deleteWealth(999));
    }

    @Test
    void whenDeleteWealthFails_deleteWealth_ThrowsException() {
        when(wealthRepository.existsById(1)).thenReturn(true);
        doThrow(new RuntimeException("Database error")).when(wealthRepository).deleteById(1);

        assertThrows(RuntimeException.class, () -> wealthService.deleteWealth(1));
    }

    @Test
    void whenWealthIdIsFound_updateWealth_UpdatesTheWealth() {
        WealthDTO updateDTO = new WealthDTO();
        updateDTO.setName("Updated Name");

        when(wealthRepository.findById(1)).thenReturn(Optional.of(entity));
        when(wealthRepository.existsById(1)).thenReturn(true);
        when(wealthRepository.save(entity)).thenReturn(entity);
        when(wealthMapper.mapToWealthDto(entity)).thenReturn(updateDTO);

        Optional<WealthDTO> result = wealthService.updateWealth(1, updateDTO);

        assertTrue(result.isPresent());
        assertEquals("Updated Name", result.get().getName());
    }

    @Test
    void whenWealthIdIsNotFound_updateWealth_ReturnsEmptyOptional() {
        WealthDTO updateDTO = new WealthDTO();
        updateDTO.setName("Updated Name");

        when(wealthRepository.findById(999)).thenReturn(Optional.empty());
        assertThrows(IllegalArgumentException.class, () -> wealthService.updateWealth(999, updateDTO));
    }

    @Test
    public void whenWealthNameIsInvalid_updateWealth_ThrowsIllegalArgumentException() {
        WealthDTO updateEmptyName = new WealthDTO();
        updateEmptyName.setName("");

        WealthDTO updateNullName = new WealthDTO();
        updateNullName.setName(null);

        when(wealthRepository.existsById(1)).thenReturn(true);

        assertThrows(IllegalArgumentException.class, () -> wealthService.updateWealth(1, updateEmptyName));
        assertThrows(IllegalArgumentException.class, () -> wealthService.updateWealth(1, updateNullName));
    }

    @Test
    public void whenWealthNameAlreadyExists_updateWealth_ThrowsDataIntegrityViolationException() {
        when(wealthRepository.findByName(dto.getName())).thenReturn(Optional.of(entity));

        assertThrows(IllegalArgumentException.class, () -> wealthService.updateWealth(entity.getId(), dto));
    }
}
