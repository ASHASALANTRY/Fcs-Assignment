package com.fulfilment.application.monolith.fulfilment.database;

import com.fulfilment.application.monolith.products.Product;
import com.fulfilment.application.monolith.stores.Store;
import io.quarkus.hibernate.orm.panache.PanacheEntity;
import jakarta.persistence.*;

@Entity
@Table(name="warehouse_fulfilment" )
public class WarehouseFulfilmentEntity {
    @Id @GeneratedValue public Long fulfilment_id;

    @ManyToOne(optional = false)
    @JoinColumn(name="store_id",nullable = false)
    public Store store;

    @ManyToOne(optional = false)
    @JoinColumn(name = "product_id", nullable = false)
    public Product product;

    @Column(name = "warehouseBusinessUnitCode", nullable = false)
    public String warehouseBusinessUnitCode;
}