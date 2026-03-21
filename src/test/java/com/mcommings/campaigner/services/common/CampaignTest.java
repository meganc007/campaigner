package com.mcommings.campaigner.services.common;

import com.mcommings.campaigner.modules.common.dtos.campaigns.CreateCampaignDTO;
import com.mcommings.campaigner.modules.common.dtos.campaigns.UpdateCampaignDTO;
import com.mcommings.campaigner.modules.common.dtos.campaigns.ViewCampaignDTO;
import com.mcommings.campaigner.modules.common.entities.Campaign;
import com.mcommings.campaigner.modules.common.mappers.CampaignMapper;
import com.mcommings.campaigner.modules.common.repositories.ICampaignRepository;
import com.mcommings.campaigner.modules.common.services.CampaignService;
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
public class CampaignTest {

    @Mock
    private ICampaignRepository campaignRepository;
    
    @Mock
    private CampaignMapper campaignMapper;
    
    @InjectMocks
    private CampaignService campaignService;

    private Campaign campaign;
    private ViewCampaignDTO viewDto;
    private CreateCampaignDTO createDto;
    private UpdateCampaignDTO updateDto;

    @BeforeEach
    void setUp() {
        campaign = CommonTestDataFactory.campaign();
        viewDto = CommonTestDataFactory.viewCampaignDTO();
        createDto = CommonTestDataFactory.createCampaignDTO();
        updateDto = CommonTestDataFactory.updateCampaignDTO();
    }
    
    @Test
    void getAll_returnsMappedDtos() {

        when(campaignRepository.findAll()).thenReturn(List.of(campaign));
        when(campaignMapper.toDto(campaign)).thenReturn(viewDto);

        List<ViewCampaignDTO> result = campaignService.getAll();

        assertEquals(1, result.size());
        assertEquals(viewDto, result.get(0));

        verify(campaignRepository).findAll();
        verify(campaignMapper).toDto(campaign);
    }

    @Test
    void getById_whenExists_returnsDto() {

        when(campaignRepository.findById(campaign.getUuid()))
                .thenReturn(Optional.of(campaign));

        when(campaignMapper.toDto(campaign))
                .thenReturn(viewDto);

        ViewCampaignDTO result = campaignService.getById(campaign.getUuid());

        assertEquals(viewDto, result);

        verify(campaignRepository).findById(campaign.getUuid());
        verify(campaignMapper).toDto(campaign);
    }

    @Test
    void getById_whenMissing_throwsException() {

        when(campaignRepository.findById(campaign.getUuid()))
                .thenReturn(Optional.empty());

        assertThrows(
                IllegalArgumentException.class,
                () -> campaignService.getById(campaign.getUuid())
        );

        verify(campaignRepository).findById(campaign.getUuid());
    }

    @Test
    void create_whenValid_savesAndReturnsDto() {

        when(campaignMapper.toEntity(createDto)).thenReturn(campaign);

        when(campaignRepository.save(campaign)).thenReturn(campaign);
        when(campaignMapper.toDto(campaign)).thenReturn(viewDto);

        ViewCampaignDTO result = campaignService.create(createDto);

        assertEquals(viewDto, result);

        verify(campaignMapper).toEntity(createDto);
        verify(campaignRepository).save(campaign);
        verify(campaignMapper).toDto(campaign);
    }

    @Test
    void update_whenValid_updatesAndReturnsDto() {

        when(campaignRepository.findById(updateDto.getUuid()))
                .thenReturn(Optional.of(campaign));

        when(campaignRepository.save(campaign)).thenReturn(campaign);
        when(campaignMapper.toDto(campaign)).thenReturn(viewDto);

        ViewCampaignDTO result = campaignService.update(updateDto);

        assertEquals(viewDto, result);

        verify(campaignRepository).findById(updateDto.getUuid());
        verify(campaignRepository).save(campaign);
        verify(campaignMapper).toDto(campaign);
    }

    @Test
    void update_whenMissing_throwsException() {

        when(campaignRepository.findById(updateDto.getUuid()))
                .thenReturn(Optional.empty());

        assertThrows(
                IllegalArgumentException.class,
                () -> campaignService.update(updateDto)
        );

        verify(campaignRepository).findById(updateDto.getUuid());
    }

    @Test
    void delete_callsRepository() {

        campaignService.delete(campaign.getUuid());

        verify(campaignRepository).deleteById(campaign.getUuid());
    }
}
