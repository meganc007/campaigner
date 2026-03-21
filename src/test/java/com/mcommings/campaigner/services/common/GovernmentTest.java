package com.mcommings.campaigner.services.common;

import com.mcommings.campaigner.modules.common.dtos.government.CreateGovernmentDTO;
import com.mcommings.campaigner.modules.common.dtos.government.UpdateGovernmentDTO;
import com.mcommings.campaigner.modules.common.dtos.government.ViewGovernmentDTO;
import com.mcommings.campaigner.modules.common.entities.Government;
import com.mcommings.campaigner.modules.common.mappers.GovernmentMapper;
import com.mcommings.campaigner.modules.common.repositories.IGovernmentRepository;
import com.mcommings.campaigner.modules.common.services.GovernmentService;
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
public class GovernmentTest {

    @Mock
    private IGovernmentRepository governmentRepository;
    
    @Mock
    private GovernmentMapper governmentMapper;

    @InjectMocks
    private GovernmentService governmentService;

    private Government government;
    private ViewGovernmentDTO viewDto;
    private CreateGovernmentDTO createDto;
    private UpdateGovernmentDTO updateDto;

    @BeforeEach
    void setUp() {
        government = CommonTestDataFactory.government();
        viewDto = CommonTestDataFactory.viewGovernmentDTO();
        createDto = CommonTestDataFactory.createGovernmentDTO();
        updateDto = CommonTestDataFactory.updateGovernmentDTO();
    }

    @Test
    void getAll_returnsMappedDtos() {

        when(governmentRepository.findAll()).thenReturn(List.of(government));
        when(governmentMapper.toDto(government)).thenReturn(viewDto);

        List<ViewGovernmentDTO> result = governmentService.getAll();

        assertEquals(1, result.size());
        assertEquals(viewDto, result.get(0));

        verify(governmentRepository).findAll();
        verify(governmentMapper).toDto(government);
    }

    @Test
    void getById_whenExists_returnsDto() {

        when(governmentRepository.findById(government.getId()))
                .thenReturn(Optional.of(government));

        when(governmentMapper.toDto(government))
                .thenReturn(viewDto);

        ViewGovernmentDTO result = governmentService.getById(government.getId());

        assertEquals(viewDto, result);

        verify(governmentRepository).findById(government.getId());
        verify(governmentMapper).toDto(government);
    }

    @Test
    void getById_whenMissing_throwsException() {

        when(governmentRepository.findById(government.getId()))
                .thenReturn(Optional.empty());

        assertThrows(
                IllegalArgumentException.class,
                () -> governmentService.getById(government.getId())
        );

        verify(governmentRepository).findById(government.getId());
    }

    @Test
    void create_whenValid_savesAndReturnsDto() {

        when(governmentMapper.toEntity(createDto)).thenReturn(government);

        when(governmentRepository.save(government)).thenReturn(government);
        when(governmentMapper.toDto(government)).thenReturn(viewDto);

        ViewGovernmentDTO result = governmentService.create(createDto);

        assertEquals(viewDto, result);

        verify(governmentMapper).toEntity(createDto);
        verify(governmentRepository).save(government);
        verify(governmentMapper).toDto(government);
    }

    @Test
    void update_whenValid_updatesAndReturnsDto() {

        when(governmentRepository.findById(updateDto.getId()))
                .thenReturn(Optional.of(government));

        when(governmentRepository.save(government)).thenReturn(government);
        when(governmentMapper.toDto(government)).thenReturn(viewDto);

        ViewGovernmentDTO result = governmentService.update(updateDto);

        assertEquals(viewDto, result);

        verify(governmentRepository).findById(updateDto.getId());
        verify(governmentMapper).updateGovernmentFromDto(updateDto, government);
        verify(governmentRepository).save(government);
        verify(governmentMapper).toDto(government);
    }

    @Test
    void update_whenMissing_throwsException() {

        when(governmentRepository.findById(updateDto.getId()))
                .thenReturn(Optional.empty());

        assertThrows(
                IllegalArgumentException.class,
                () -> governmentService.update(updateDto)
        );

        verify(governmentRepository).findById(updateDto.getId());
    }

    @Test
    void delete_callsRepository() {

        governmentService.delete(government.getId());

        verify(governmentRepository).deleteById(government.getId());
    }
}
