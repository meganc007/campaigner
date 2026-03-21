package com.mcommings.campaigner.modules.common.services;

import com.mcommings.campaigner.config.BaseService;
import com.mcommings.campaigner.modules.common.dtos.wealth.CreateWealthDTO;
import com.mcommings.campaigner.modules.common.dtos.wealth.UpdateWealthDTO;
import com.mcommings.campaigner.modules.common.dtos.wealth.ViewWealthDTO;
import com.mcommings.campaigner.modules.common.entities.Wealth;
import com.mcommings.campaigner.modules.common.mappers.WealthMapper;
import com.mcommings.campaigner.modules.common.repositories.IWealthRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class WealthService extends BaseService<
        Wealth,
        Integer,
        ViewWealthDTO,
        CreateWealthDTO,
        UpdateWealthDTO> {

    private final IWealthRepository wealthRepository;
    private final WealthMapper wealthMapper;

    @Override
    protected JpaRepository<Wealth, Integer> getRepository() {
        return wealthRepository;
    }

    @Override
    protected ViewWealthDTO toViewDto(Wealth entity) {
        return wealthMapper.toDto(entity);
    }

    @Override
    protected Wealth toEntity(CreateWealthDTO dto) {

        Wealth entity = wealthMapper.toEntity(dto);

        return entity;
    }

    @Override
    protected void updateEntity(UpdateWealthDTO dto, Wealth entity) {

        wealthMapper.updateWealthFromDto(dto, entity);
    }

    @Override
    protected Integer getId(UpdateWealthDTO dto) {
        return dto.getId();
    }
}
