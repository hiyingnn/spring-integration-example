package com.example.springintegration.mapper;

import com.example.springintegration.model.OrderRecord;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Optional;

@Mapper
public interface OrderRecordMapper {
  Optional<OrderRecord> selectOrderRecordByReferenceId(String referenceId);

  List<OrderRecord> selectOrderRecords();

  void updateOrderRecord(OrderRecord orderRecord);

  void insertOrderRecord(OrderRecord orderRecord);
}
