package com.example.springintegration.process;

import com.example.springintegration.mapper.OrderRecordMapper;
import com.example.springintegration.model.Order;
import com.example.springintegration.model.OrderRecord;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
@Slf4j
public class OrderProcessor implements ItemProcessor<Order, OrderRecord> {

  private final OrderRecordMapper orderRecordMapper;

  @Override
  public OrderRecord process(Order order) {
    OrderRecord orderRecordFromFile = OrderRecord.builder()
      .referenceId(order.referenceId())
      .name(order.name())
      .firstUpdated(order.firstUpdated())
      .lastUpdated(order.lastUpdated())
      .isDeleted(false)
      .build();
    Optional<OrderRecord> orderRecordOptionalFromDb = orderRecordMapper.selectOrderRecordByReferenceId(order.referenceId());

    // if empty, insert
    if (orderRecordOptionalFromDb.isEmpty()) {
       orderRecordMapper.insertOrderRecord(orderRecordFromFile);
       return orderRecordFromFile;
    }

    OrderRecord orderRecordFromDb = orderRecordOptionalFromDb.get();

    // if present, update
    if (!orderRecordFromDb.equals(orderRecordFromFile)) {
      orderRecordMapper.updateOrderRecord(orderRecordFromFile);
      return orderRecordFromFile;
    }
    return null;
  }
}
