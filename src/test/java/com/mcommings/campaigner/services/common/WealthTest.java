package com.mcommings.campaigner.services.common;

import com.mcommings.campaigner.modules.common.dtos.wealth.CreateWealthDTO;
import com.mcommings.campaigner.modules.common.dtos.wealth.UpdateWealthDTO;
import com.mcommings.campaigner.modules.common.dtos.wealth.ViewWealthDTO;
import com.mcommings.campaigner.modules.common.entities.Wealth;
import com.mcommings.campaigner.modules.common.mappers.WealthMapper;
import com.mcommings.campaigner.modules.common.repositories.IWealthRepository;
import com.mcommings.campaigner.modules.common.services.WealthService;
import com.mcommings.campaigner.setup.common.factories.CommonTestDataFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@SpringBootTest
public class WealthTest {

    @Mock
    private IWealthRepository wealthRepository;

    @Mock
    private WealthMapper wealthMapper;

    @InjectMocks
    private WealthService wealthService;

    private Wealth wealth;
    private ViewWealthDTO viewDto;
    private CreateWealthDTO createDto;
    private UpdateWealthDTO updateDto;

    @BeforeEach
    void setUp() {
        wealth = CommonTestDataFactory.wealth();
        viewDto = CommonTestDataFactory.viewWealthDTO();
        createDto = CommonTestDataFactory.createWealthDTO();
        updateDto = CommonTestDataFactory.updateWealthDTO();
    }

    @Test
    void getAll_returnsMappedDtos() {

        when(wealthRepository.findAll()).thenReturn(List.of(wealth));
        when(wealthMapper.toDto(wealth)).thenReturn(viewDto);

        List<ViewWealthDTO> result = wealthService.getAll();

        assertEquals(1, result.size());
        assertEquals(viewDto, result.get(0));

        verify(wealthRepository).findAll();
        verify(wealthMapper).toDto(wealth);
    }

    @Test
    void getById_whenExists_returnsDto() {

        when(wealthRepository.findById(wealth.getId()))
                .thenReturn(Optional.of(wealth));

        when(wealthMapper.toDto(wealth))
                .thenReturn(viewDto);

        ViewWealthDTO result = wealthService.getById(wealth.getId());

        assertEquals(viewDto, result);

        verify(wealthRepository).findById(wealth.getId());
        verify(wealthMapper).toDto(wealth);
    }

    @Test
    void getById_whenMissing_throwsException() {

        when(wealthRepository.findById(wealth.getId()))
                .thenReturn(Optional.empty());

        assertThrows(
                IllegalArgumentException.class,
                () -> wealthService.getById(wealth.getId())
        );

        verify(wealthRepository).findById(wealth.getId());
    }

    @Test
    void create_whenValid_savesAndReturnsDto() {

        when(wealthMapper.toEntity(createDto)).thenReturn(wealth);

        when(wealthRepository.save(wealth)).thenReturn(wealth);
        when(wealthMapper.toDto(wealth)).thenReturn(viewDto);

        ViewWealthDTO result = wealthService.create(createDto);

        assertEquals(viewDto, result);

        verify(wealthMapper).toEntity(createDto);
        verify(wealthRepository).save(wealth);
        verify(wealthMapper).toDto(wealth);
    }

    @Test
    void update_whenValid_updatesAndReturnsDto() {

        when(wealthRepository.findById(updateDto.getId()))
                .thenReturn(Optional.of(wealth));

        when(wealthRepository.save(wealth)).thenReturn(wealth);
        when(wealthMapper.toDto(wealth)).thenReturn(viewDto);

        ViewWealthDTO result = wealthService.update(updateDto);

        assertEquals(viewDto, result);

        verify(wealthRepository).findById(updateDto.getId());
        verify(wealthMapper).updateWealthFromDto(updateDto, wealth);
        verify(wealthRepository).save(wealth);
        verify(wealthMapper).toDto(wealth);
    }

    @Test
    void update_whenMissing_throwsException() {

        when(wealthRepository.findById(updateDto.getId()))
                .thenReturn(Optional.empty());

        assertThrows(
                IllegalArgumentException.class,
                () -> wealthService.update(updateDto)
        );

        verify(wealthRepository).findById(updateDto.getId());
    }

    @Test
    void delete_callsRepository() {

        wealthService.delete(wealth.getId());

        verify(wealthRepository).deleteById(wealth.getId());
    }
}
