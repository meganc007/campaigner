package com.mcommings.campaigner.services;

import com.mcommings.campaigner.models.SettlementType;
import com.mcommings.campaigner.models.repositories.ISettlementTypeRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.when;

@SpringBootTest
public class SettlementTypeTest {

    @Mock
    private ISettlementTypeRepository settlementTypeRepository;

    @InjectMocks
    private SettlementTypeService settlementTypeService;

    @Test
    public void whenThereAreSettlementTypes_getSettlementTypes_ReturnsSettlementTypes() {
        List<SettlementType> settlementTypes = new ArrayList<>();
        settlementTypes.add(new SettlementType(1, "Settlement Type 1", "Description 1"));
        settlementTypes.add(new SettlementType(2, "Settlement Type 2", "Description 2"));
        when(settlementTypeRepository.findAll()).thenReturn(settlementTypes);

        List<SettlementType> result = settlementTypeService.getSettlementTypes();

        Assertions.assertEquals(2, result.size());
        Assertions.assertEquals(settlementTypes, result);
    }

    @Test
    public void whenThereAreNoSettlementTypes_getSettlementTypes_ReturnsNothing() {
        List<SettlementType> settlementTypes = new ArrayList<>();
        when(settlementTypeRepository.findAll()).thenReturn(settlementTypes);

        List<SettlementType> result = settlementTypeService.getSettlementTypes();

        Assertions.assertEquals(0, result.size());
        Assertions.assertEquals(settlementTypes, result);

    }
}
