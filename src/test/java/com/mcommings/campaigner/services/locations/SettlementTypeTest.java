package com.mcommings.campaigner.services.locations;

import com.mcommings.campaigner.models.RepositoryHelper;
import com.mcommings.campaigner.models.locations.City;
import com.mcommings.campaigner.models.locations.SettlementType;
import com.mcommings.campaigner.repositories.locations.ICityRepository;
import com.mcommings.campaigner.repositories.locations.ISettlementTypeRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.repository.CrudRepository;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static com.mcommings.campaigner.enums.ForeignKey.FK_SETTLEMENT;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@SpringBootTest
public class SettlementTypeTest {

    @Mock
    private ISettlementTypeRepository settlementTypeRepository;

    @Mock
    private ICityRepository cityRepository;

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

    @Test
    public void whenSettlementTypeIsValid_saveSettlementType_SavesTheSettlementType() {
        SettlementType settlementType = new SettlementType(1, "SettlementType 1", "Description 1");
        when(settlementTypeRepository.saveAndFlush(settlementType)).thenReturn(settlementType);

        assertDoesNotThrow(() -> settlementTypeService.saveSettlementType(settlementType));
        verify(settlementTypeRepository, times(1)).saveAndFlush(settlementType);
    }

    @Test
    public void whenSettlementTypeNameIsInvalid_saveSettlementType_ThrowsIllegalArgumentException() {
        SettlementType settlementTypeWithEmptyName = new SettlementType(1, "", "Description 1");
        SettlementType settlementTypeWithNullName = new SettlementType(2, null, "Description 2");

        assertThrows(IllegalArgumentException.class, () -> settlementTypeService.saveSettlementType(settlementTypeWithEmptyName));
        assertThrows(IllegalArgumentException.class, () -> settlementTypeService.saveSettlementType(settlementTypeWithNullName));
    }

    @Test
    public void whenSettlementTypeNameAlreadyExists_saveSettlementType_ThrowsDataIntegrityViolationException() {
        SettlementType settlementType = new SettlementType(1, "SettlementType 1", "Description 1");
        SettlementType settlementTypeWithDuplicatedName = new SettlementType(2, "SettlementType 1", "Description 2");
        when(settlementTypeRepository.saveAndFlush(settlementType)).thenReturn(settlementType);
        when(settlementTypeRepository.saveAndFlush(settlementTypeWithDuplicatedName)).thenThrow(DataIntegrityViolationException.class);

        assertDoesNotThrow(() -> settlementTypeService.saveSettlementType(settlementType));
        assertThrows(DataIntegrityViolationException.class, () -> settlementTypeService.saveSettlementType(settlementTypeWithDuplicatedName));
    }

    @Test
    public void whenSettlementTypeIdExists_deleteSettlementType_DeletesTheSettlementType() {
        int settlementTypeId = 1;
        when(settlementTypeRepository.existsById(settlementTypeId)).thenReturn(true);
        assertDoesNotThrow(() -> settlementTypeService.deleteSettlementType(settlementTypeId));
        verify(settlementTypeRepository, times(1)).deleteById(settlementTypeId);
    }

    @Test
    public void whenSettlementTypeIdDoesNotExist_deleteSettlementType_ThrowsIllegalArgumentException() {
        int settlementTypeId = 9000;
        when(settlementTypeRepository.existsById(settlementTypeId)).thenReturn(false);
        assertThrows(IllegalArgumentException.class, () -> settlementTypeService.deleteSettlementType(settlementTypeId));
    }

    @Test
    public void whenSettlementTypeIdIsAForeignKey_deleteSettlementType_ThrowsDataIntegrityViolationException() {
        int settlementTypeId = 1;
        City city = new City(1, "City", "Description", 1, 1, settlementTypeId, 1,1);
        List<CrudRepository> repositories = new ArrayList<>(Arrays.asList(cityRepository));
        List<City> cities = new ArrayList<>(Arrays.asList(city));

        when(settlementTypeRepository.existsById(settlementTypeId)).thenReturn(true);
        when(cityRepository.findByfk_settlement(settlementTypeId)).thenReturn(cities);

        boolean actual = RepositoryHelper.isForeignKey(repositories, FK_SETTLEMENT.columnName, settlementTypeId);
        Assertions.assertTrue(actual);
        assertThrows(DataIntegrityViolationException.class, () -> settlementTypeService.deleteSettlementType(settlementTypeId));
    }

    @Test
    public void whenSettlementTypeIdIsFound_updateSettlementType_UpdatesTheSettlementType() {
        int settlementTypeId = 1;
        SettlementType settlementType = new SettlementType(settlementTypeId, "Old SettlementType Name", "Old Description");
        SettlementType settlementTypeToUpdate = new SettlementType(settlementTypeId, "Updated SettlementType Name", "Updated Description");

        when(settlementTypeRepository.existsById(settlementTypeId)).thenReturn(true);
        when(settlementTypeRepository.findById(settlementTypeId)).thenReturn(Optional.of(settlementType));

        settlementTypeService.updateSettlementType(settlementTypeId, settlementTypeToUpdate);

        verify(settlementTypeRepository).findById(settlementTypeId);

        SettlementType result = settlementTypeRepository.findById(settlementTypeId).get();
        Assertions.assertEquals(settlementTypeToUpdate.getName(), result.getName());
        Assertions.assertEquals(settlementTypeToUpdate.getDescription(), result.getDescription());
    }

    @Test
    public void whenSettlementTypeIdIsNotFound_updateSettlementType_ThrowsIllegalArgumentException() {
        int settlementTypeId = 1;
        SettlementType settlementType = new SettlementType(settlementTypeId, "Old SettlementType Name", "Old Description");

        when(settlementTypeRepository.existsById(settlementTypeId)).thenReturn(false);

        assertThrows(IllegalArgumentException.class, () -> settlementTypeService.updateSettlementType(settlementTypeId, settlementType));
    }
}
