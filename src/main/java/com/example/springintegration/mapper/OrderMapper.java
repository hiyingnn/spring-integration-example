package com.example.springintegration.mapper;

import com.example.springintegration.model.Order;
import org.springframework.batch.item.file.mapping.FieldSetMapper;
import org.springframework.batch.item.file.transform.FieldSet;

public class OrderMapper implements FieldSetMapper<Order> {
    public Order mapFieldSet(FieldSet fs) {

       return Order.builder()
               .referenceCode(fs.readString("referenceCode"))
               .name(fs.readString("name"))
               .firstUpdated(fs.readDate("firstUpdated"))
               .lastUpdated(fs.readDate("lastUpdated"))
               .build();
    }
}