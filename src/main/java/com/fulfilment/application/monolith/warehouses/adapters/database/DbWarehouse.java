package com.fulfilment.application.monolith.warehouses.adapters.database;

import io.quarkus.hibernate.orm.panache.PanacheEntity;
import jakarta.persistence.Cacheable;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import java.time.LocalDateTime;

@Entity
@Table(name = "warehouse")
@Cacheable
public class DbWarehouse extends PanacheEntity {

  public String businessUnitCode;

  public String location;

  public Integer capacity;

  public Integer stock;

  public LocalDateTime createdAt;

  public LocalDateTime archivedAt;

  public DbWarehouse() {}
}
