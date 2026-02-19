package com.fulfilment.application.monolith.warehouses.domain.usecases;

import com.fulfilment.application.monolith.warehouses.domain.models.Warehouse;
import com.fulfilment.application.monolith.warehouses.domain.ports.WarehouseStore;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.junit.jupiter.api.Test;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class CreateWarehouseUseCaseTest {

    @Mock
    WarehouseStore warehouseStore;

    @InjectMocks
    CreateWarehouseUseCase target;

    @Test
    void create(){
        Warehouse warehouse = mock();
        target.create(warehouse);
        verify(warehouseStore).create(warehouse);
    }

}
