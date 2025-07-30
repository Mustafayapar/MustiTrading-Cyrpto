package com.musti.service;

import com.musti.domain.ORDER_STATUS;
import com.musti.domain.OrderType;
import com.musti.modal.Coin;
import com.musti.modal.Order;
import com.musti.modal.OrderItem;
import com.musti.modal.Users;
import com.musti.repository.IOrderItemRepository;
import com.musti.repository.IOrderRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
public class OrderServiceImpl implements IOrderService{

    @Autowired
    private IOrderRepository orderRepository;

    @Autowired
    private WalletServiceImpl walletService;

    @Autowired
    private IOrderItemRepository orderItemRepository;

    @Override
    public Order createOrder(Users user, OrderItem orderItem, OrderType orderType) {

        double price = orderItem.getCoin().getCurrentPrice()*orderItem.getQuantity();

        Order order = new Order();
        order.setUser(user);
        order.setUser(user);
        order.setOrderItem(orderItem);
        order.setOrderType(orderType);
        order.setPrice(BigDecimal.valueOf(price));
        order.setOrderStatus(ORDER_STATUS.PENDING);

        return orderRepository.save(order);
    }

    @Override
    public Order getOrderById(Long id) throws Exception {
        return orderRepository.findById(id).orElseThrow(()->new Exception("order not found"));
    }

    @Override
    public List<Order> getAllOrderOfUser(Long userId, OrderType orderType, String assetSymbol) {
        return orderRepository.findByUserId(userId);
    }

    private OrderItem createOrderItem(Coin coin, double quantity,
                                      double buyPrice, double sellPrice) {
        OrderItem orderItem = new OrderItem();
        orderItem.setCoin(coin);
        orderItem.setQuantity(quantity);
        orderItem.setBuyPrice(buyPrice);
        orderItem.setSellPrice(sellPrice);
        return orderItemRepository.save(orderItem);

    }

    @Transactional
    public Order buyAsset(Coin coin , double quantity, Users user) throws Exception {
        if (quantity <= 0) {
            throw new Exception("quantity should be greater than 0");
        }
        double buyPrice = coin.getCurrentPrice();
        OrderItem orderItem = createOrderItem(coin, quantity, buyPrice, 0);

        Order order = createOrder(user,orderItem,OrderType.BUY);
        orderItem.setOrder(order);
        walletService.payOrderPayment(order,user);

        order.setOrderStatus(ORDER_STATUS.SUCCESS);
        order.setOrderType(OrderType.BUY);
        Order savedOrder = orderRepository.save(order);
        return savedOrder;

    }
    @Transactional
    public Order sellAsset(Coin coin , double quantity, Users user) throws Exception {
        if (quantity <= 0) {
            throw new Exception("quantity should be greater than 0");
        }
        double sellPrice = coin.getCurrentPrice();
        double buyPrice = assetToSell.getPrice();

        OrderItem orderItem = createOrderItem(coin, quantity, buyPrice, sellPrice);

        Order order = createOrder(user,orderItem,OrderType.SELL);
        orderItem.setOrder(order);

        if (assetToSell.getQuantity()>=quantity){
            order.setOrderStatus(ORDER_STATUS.SUCCESS);
            order.setOrderType(OrderType.SELL);
            Order savedOrder = orderRepository.save(order);

            walletService.payOrderPayment(order,user);

            Asset updatedAsset= assetService.updateAsset(assetToSell.getId(), -quantity);
            if (updatedAsset.getQuantity()*coin.getCurrentPrice()<=1){
                assetService.deleteAsset(updatedAsset.getId());
            }
            return savedOrder;
        }
        throw new Exception("quantity should be greater than 0");
    }
    @Override
    @Transactional
    public Order processOrder(Coin coin, double quantity, OrderType orderType, Users user) throws Exception {

        if (orderType.equals(OrderType.BUY)) {
            return buyAsset(coin, quantity,user);
        } else if (orderType.equals(OrderType.SELL)) {
            return sellAsset(coin,quantity,user);
        }
        throw  new Exception("invalid order type");

    }
}
