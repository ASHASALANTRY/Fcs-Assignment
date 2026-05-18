package com.fulfilment.application.monolith.fulfilment.database;

import com.fulfilment.application.monolith.products.Product;
import com.fulfilment.application.monolith.stores.Store;
import io.quarkus.hibernate.orm.panache.PanacheEntity;
import jakarta.persistence.*;

@Entity
@Table(name="warehouse_fulfilment",
uniqueConstraints = @UniqueConstraint(columnNames = {"store_id","product_id","warehouse_bussiness_unit_code"}))
public class WarehouseFulfilmentEntity extends PanacheEntity {
@ManyToOne(optional = false)
    @JoinColumn(name="id")
    public Store store;

    @ManyToOne(optional = false)
    @JoinColumn(name = "id")
    public Product product;

    @Column(name = "warehouseBusinessUnitCode", nullable = false)
    public String warehouseBusinessUnitCode;
}