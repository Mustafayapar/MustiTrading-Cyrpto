package com.musti.controller;

import com.musti.domain.OrderType;
import com.musti.modal.Coin;
import com.musti.modal.Order;
import com.musti.modal.Users;
import com.musti.request.CreateOrderRequest;
import com.musti.service.CoinServiceImpl;
import com.musti.service.OrderServiceImpl;
import com.musti.service.UserServiceImpl;
import com.musti.service.WalletServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/orders")
public class OrderController {

    @Autowired
    private OrderServiceImpl orderService;

    @Autowired
    private UserServiceImpl userService;
    @Autowired
    private CoinServiceImpl coinService;

//    @Autowired
//    private WalletTransactionServiceImpl walletTransactionService;

    @PostMapping("/pay")
    public ResponseEntity<Order> payOrderPayment(
            @RequestHeader("Authorization") String token,
            @RequestBody CreateOrderRequest req
    ) throws Exception {
        Users user= userService.findUserProfileByJwt(token);
        Coin coin =coinService.findById(req.getCoinId());

        Order order =orderService.processOrder(coin,req.getQuantity(),req.getOrderType(),user);

        return ResponseEntity.ok(order);
    }

    @GetMapping
    public ResponseEntity<Order> getOrderById(
            @RequestHeader("Authorization") String token,
            @PathVariable Long orderId
    )throws Exception{


        Users user = userService.findUserProfileByJwt(token);
        Order order = orderService.getOrderById(orderId);
    if (order.getUser().getId() == user.getId()){
            return ResponseEntity.ok(order);
        }
        else{
            throw new Exception("Order not found");
        }
    }


    @GetMapping
    public ResponseEntity<List<Order>> getAllOrders(
            @RequestHeader("Authorization") String jwt,
            @RequestParam(required = false) OrderType order_type,
            @RequestParam(required = false) String asset_symbol
    )throws Exception{

        Long userId = userService.findUserProfileByJwt(jwt).getId();
        List<Order> userOrders = orderService.getAllOrderOfUser(userId,order_type,asset_symbol);


        return ResponseEntity.ok(userOrders);
    }





}
