package com.fulfilment.application.monolith.warehouses;

import com.fulfilment.application.monolith.warehouses.adapters.database.DbWarehouse;

import java.time.ZoneId;

public class WarehouseConverter {

    public static com.fulfilment.application.monolith.warehouses.domain.models.Warehouse toDomainWarehouse(DbWarehouse toConvert) {
        var converted = new com.fulfilment.application.monolith.warehouses.domain.models.Warehouse();
        converted.businessUnitCode = toConvert.businessUnitCode;
        converted.location = toConvert.location;
        converted.capacity = toConvert.capacity;
        converted.stock = toConvert.stock;
        converted.creationAt = toConvert.createdAt.atZone(ZoneId.systemDefault());
        converted.archivedAt = toConvert.archivedAt.atZone(ZoneId.systemDefault());
        return converted;
    }

    public static com.fulfilment.application.monolith.warehouses.domain.models.Warehouse toDomainWarehouse(com.warehouse.api.beans.Warehouse toConvert) {
        var converted = new com.fulfilment.application.monolith.warehouses.domain.models.Warehouse();
        converted.businessUnitCode = toConvert.getId();
        converted.location = toConvert.getLocation();
        converted.capacity = toConvert.getCapacity();
        converted.stock = toConvert.getStock();
        return converted;
    }

    public static DbWarehouse toDbWarehouse(com.fulfilment.application.monolith.warehouses.domain.models.Warehouse toConvert) {
        var converted = new DbWarehouse();
        converted.businessUnitCode = toConvert.businessUnitCode;
        converted.location = toConvert.location;
        converted.capacity = toConvert.capacity;
        converted.stock = toConvert.stock;
        converted.createdAt = toConvert.creationAt.toLocalDateTime();
        converted.archivedAt = toConvert.archivedAt.toLocalDateTime();
        return converted;
    }

    public static com.warehouse.api.beans.Warehouse toJsonWarehouse(DbWarehouse toConvert) {
        var converted = new com.warehouse.api.beans.Warehouse();
        converted.setId(toConvert.businessUnitCode);
        converted.setLocation(toConvert.location);
        converted.setCapacity(toConvert.capacity);
        converted.setStock(toConvert.stock);
        return converted;
    }

    public static com.warehouse.api.beans.Warehouse toJsonWarehouse(com.fulfilment.application.monolith.warehouses.domain.models.Warehouse toConvert) {
        var converted = new com.warehouse.api.beans.Warehouse();
        converted.setId(toConvert.businessUnitCode);
        converted.setLocation(toConvert.location);
        converted.setCapacity(toConvert.capacity);
        converted.setStock(toConvert.stock);
        return converted;
    }
}
