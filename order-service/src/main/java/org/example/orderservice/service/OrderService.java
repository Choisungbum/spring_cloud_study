package org.example.orderservice.service;

import org.example.orderservice.dto.OrderDto;
import org.example.orderservice.entitiy.OrderEntity;

public interface OrderService {
    OrderDto createOrder(OrderDto orderDto);
    OrderDto getOrderByOrderId(String orderId);
    Iterable<OrderEntity> getOrderByUserId(String userId);

}
