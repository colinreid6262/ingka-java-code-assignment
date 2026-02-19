package com.fulfilment.application.monolith.warehouses.domain.usecases;

import com.fulfilment.application.monolith.warehouses.domain.models.Warehouse;
import com.fulfilment.application.monolith.warehouses.domain.ports.WarehouseStore;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ReplaceWarehouseUseCaseTest {

    @Mock
    WarehouseStore warehouseStore;

    @InjectMocks
    ReplaceWarehouseUseCase target;

    @Test
    void replace() {
        Warehouse oldWarehouse = new Warehouse();
        oldWarehouse.businessUnitCode = "12345";
        oldWarehouse.capacity = 5;
        oldWarehouse.stock = 5;

        Warehouse newWarehouse = new Warehouse();
        newWarehouse.businessUnitCode = "12345";
        newWarehouse.capacity = 10;
        newWarehouse.stock = 5;

        when(warehouseStore.findByBusinessUnitCode( "12345")).thenReturn(oldWarehouse);

        target.replace(newWarehouse);
        verify(warehouseStore).update(newWarehouse);
    }

    @Test
    void replaceNonExistingWarehouse() {
        Warehouse newWarehouse = new Warehouse();
        newWarehouse.businessUnitCode = "12345";
        when(warehouseStore.findByBusinessUnitCode("12345")).thenReturn(null);

        var exception = assertThrows(
                IllegalArgumentException.class,
                () -> target.replace(newWarehouse));
        assertEquals("Invalid replacement warehouse, existing warehouse at specified businessUnitCode does not exist", exception.getMessage());
    }

    @Test
    void replaceWithInvalidCapacity() {
        Warehouse oldWarehouse = new Warehouse();
        oldWarehouse.businessUnitCode = "12345";
        oldWarehouse.capacity = 10;
        oldWarehouse.stock = 10;

        Warehouse newWarehouse = new Warehouse();
        newWarehouse.businessUnitCode = "12345";
        newWarehouse.capacity = 5;
        newWarehouse.stock = 5;

        when(warehouseStore.findByBusinessUnitCode("12345")).thenReturn(oldWarehouse);

        var exception = assertThrows(
                IllegalArgumentException.class,
                () -> target.replace(newWarehouse));
        assertEquals("Invalid replacement warehouse, capacity exceeds existing stock levels", exception.getMessage());
    }

    @Test
    void replaceWithUnexpectedStockLevel() {
        Warehouse oldWarehouse = new Warehouse();
        oldWarehouse.businessUnitCode = "12345";
        oldWarehouse.capacity = 10;
        oldWarehouse.stock = 10;

        Warehouse newWarehouse = new Warehouse();
        newWarehouse.businessUnitCode = "12345";
        newWarehouse.capacity = 10;
        newWarehouse.stock = 5;

        when(warehouseStore.findByBusinessUnitCode("12345")).thenReturn(oldWarehouse);

        var exception = assertThrows(
                RuntimeException.class,
                () -> target.replace(newWarehouse));
        assertEquals("Stock level does not match expected value in replacement warehouse", exception.getMessage());
    }

}
