package com.fulfilment.application.monolith.warehouses.adapters.restapi;

import com.fulfilment.application.monolith.warehouses.WarehouseConverter;
import com.fulfilment.application.monolith.warehouses.adapters.database.WarehouseRepository;
import com.warehouse.api.WarehouseResource;
import com.warehouse.api.beans.Warehouse;
import jakarta.inject.Inject;
import jakarta.validation.constraints.NotNull;
import java.util.List;

public class WarehouseResourceImpl implements WarehouseResource {

  @Inject WarehouseRepository warehouseRepository;

  @Override
  public List<Warehouse> listAllWarehousesUnits() {
    return warehouseRepository.findAll().stream().map(WarehouseConverter::toJsonWarehouse).toList();
  }

  @Override
  public Warehouse createANewWarehouseUnit(@NotNull Warehouse data) {
    warehouseRepository.create(WarehouseConverter.toDomainWarehouse(data));
    return data;
  }

  @Override
  public Warehouse getAWarehouseUnitByID(String id) {
    return WarehouseConverter.toJsonWarehouse(warehouseRepository.findByBusinessUnitCode(id));
  }

  @Override
  public void archiveAWarehouseUnitByID(String id) {
    var toDelete = warehouseRepository.findByBusinessUnitCode(id);
    warehouseRepository.remove(toDelete);
  }
}
