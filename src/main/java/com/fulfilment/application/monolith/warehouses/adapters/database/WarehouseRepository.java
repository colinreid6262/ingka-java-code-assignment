package com.fulfilment.application.monolith.warehouses.adapters.database;

import com.fulfilment.application.monolith.location.LocationGateway;
import com.fulfilment.application.monolith.warehouses.WarehouseConverter;
import com.fulfilment.application.monolith.warehouses.domain.models.Location;
import com.fulfilment.application.monolith.warehouses.domain.models.Warehouse;
import com.fulfilment.application.monolith.warehouses.domain.ports.WarehouseStore;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.List;

@ApplicationScoped
public class WarehouseRepository implements WarehouseStore, PanacheRepository<DbWarehouse> {

  private final LocationGateway locationGateway = new LocationGateway();

  @Override
  public void create(Warehouse warehouse) {
    // Check that the businessUnitCode dos not already exist
    throwIfBusinessUnitCodeExists(warehouse.businessUnitCode);
    // Check that the location is valid
    var location = locationGateway.resolveByIdentifier(warehouse.location);
    // Check we are not already at max number of warehouses for this location
    throwIfLocationAtMaxWarehouses(location);
    // Check the total capacity at this location would not be exceeded
    throwIfLocationCapacityWouldBeExceeded(location, warehouse);

    WarehouseConverter.toDbWarehouse(warehouse).persist();
  }

  @Override
  public void update(Warehouse warehouse) {
    DbWarehouse entity = DbWarehouse.find("businessUnitCode", warehouse.businessUnitCode).firstResult();

    // If businessUnitCode has changed, check that the new value does not already exist
    if (!warehouse.businessUnitCode.equals(entity.businessUnitCode)) {
      throwIfBusinessUnitCodeExists(warehouse.businessUnitCode);
    }

    // If location has changed, check that the new value is valid
    if (!warehouse.location.equals(entity.location)) {
      var location = locationGateway.resolveByIdentifier(warehouse.location);
      // Check we are not already at max number of warehouses for this location
      throwIfLocationAtMaxWarehouses(location);
      // Check the total capacity at this location would not be exceeded
      throwIfLocationCapacityWouldBeExceeded(location, warehouse);
    }

    entity.businessUnitCode = warehouse.businessUnitCode;
    entity.location = warehouse.location;
    entity.capacity = warehouse.capacity;
    entity.stock = warehouse.stock;
    entity.createdAt = warehouse.creationAt.toLocalDateTime();
    entity.archivedAt = warehouse.archivedAt.toLocalDateTime();
  }

  @Override
  public void remove(Warehouse warehouse) {
    DbWarehouse entity = DbWarehouse.find("businessUnitCode", warehouse.businessUnitCode).firstResult();
    entity.delete();
  }

  @Override
  public Warehouse findByBusinessUnitCode(String buCode) {
    DbWarehouse entity = DbWarehouse.find("businessUnitCode", buCode).firstResult();
    return WarehouseConverter.toDomainWarehouse(entity);
  }

  private static void throwIfBusinessUnitCodeExists(String businessUnitCode) {
    DbWarehouse.find("businessUnitCode", businessUnitCode).stream().findAny().ifPresent(s -> {
      throw new IllegalArgumentException("businessUnitCode already exists");
    });
  }

  private static void throwIfLocationAtMaxWarehouses(Location location) {
    var warehousesAtLocation = DbWarehouse.find("location", location.identification).count();
    if (warehousesAtLocation >= location.maxNumberOfWarehouses) {
      throw new IllegalArgumentException("Cannot use this location, max number of warehouses reached");
    }
  }

  private static void throwIfLocationCapacityWouldBeExceeded(Location location, Warehouse warehouse) {
    List<DbWarehouse> warehouses = DbWarehouse.list("location", location.identification);
    var existingCapacity = warehouses.stream().mapToInt(s -> s.capacity).sum();
    if ((existingCapacity + warehouse.capacity) >= location.maxCapacity) {
      throw new IllegalArgumentException("Cannot use this location, max capacity would be exceededø");
    }
  }

}
