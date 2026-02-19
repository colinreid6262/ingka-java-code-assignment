package com.fulfilment.application.monolith.warehouses.domain.usecases;

import com.fulfilment.application.monolith.warehouses.domain.models.Warehouse;
import com.fulfilment.application.monolith.warehouses.domain.ports.ReplaceWarehouseOperation;
import com.fulfilment.application.monolith.warehouses.domain.ports.WarehouseStore;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.Objects;

@ApplicationScoped
public class ReplaceWarehouseUseCase implements ReplaceWarehouseOperation {

  private final WarehouseStore warehouseStore;

  public ReplaceWarehouseUseCase(WarehouseStore warehouseStore) {
    this.warehouseStore = warehouseStore;
  }

  @Override
  public void replace(Warehouse newWarehouse) {
    // It must be the same businessUnitCode, otherwise it isn't a 'replacement'
    var existingWarehouse = warehouseStore.findByBusinessUnitCode(newWarehouse.businessUnitCode);
    if (Objects.isNull(existingWarehouse)) {
      throw new IllegalArgumentException("Invalid replacement warehouse, existing warehouse at specified businessUnitCode does not exist");
    }
    // Check that new warehouse capacity can handle the existingWarehouse Stock
    Integer stock = existingWarehouse.stock;
    if (stock > newWarehouse.capacity) {
      throw new IllegalArgumentException("Invalid replacement warehouse, capacity exceeds existing stock levels");
    }
    warehouseStore.update(newWarehouse);

    // Check that stock in new warehouse equals stock in old warehouse
    if (!newWarehouse.stock.equals(stock)) {
      throw new RuntimeException("Stock level does not match expected value in replacement warehouse");
    }
  }
}
