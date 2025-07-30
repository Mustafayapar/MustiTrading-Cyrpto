package com.musti.service;

import com.musti.domain.OrderType;
import com.musti.modal.Coin;
import com.musti.modal.Order;
import com.musti.modal.OrderItem;
import com.musti.modal.Users;

import java.util.List;

public interface IOrderService {
    Order createOrder(Users user, OrderItem orderItem, OrderType orderType  );

    Order getOrderById(Long id) throws Exception;

    List<Order> getAllOrderOfUser(Long userId, OrderType orderType, String assetSymbol);

    Order processOrder(Coin coin, double quantity, OrderType orderType, Users user) throws Exception;




}
