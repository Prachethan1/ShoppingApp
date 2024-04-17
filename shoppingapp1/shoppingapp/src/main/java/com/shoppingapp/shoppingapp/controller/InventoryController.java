package com.shoppingapp.shoppingapp.controller;

import com.shoppingapp.shoppingapp.entity.*;
import com.shoppingapp.shoppingapp.service.CouponService;
import com.shoppingapp.shoppingapp.service.OrderService;
import com.shoppingapp.shoppingapp.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
public class InventoryController {
    @Autowired
    private ProductService productService;

    @Autowired
    private CouponService couponService;

    @Autowired
    private OrderService orderService;

    @PostMapping("/add")
    public Product add(Product product){
        return productService.add(product);
    }
    @GetMapping("/inventory")
    public Product getInventory(@RequestParam(name = "pid") Integer pid) {

        return productService.getProduct(pid);
    }

    @GetMapping("/fetchCoupons")
    public Map<String,Integer> fetchCoupons() {

        return couponService.getAllCoupons();
    }
    @PostMapping("/{userId}/order")
    public Order placeOrder(@PathVariable int userId,
                                        @RequestParam int qty,
                                        @RequestParam(name = "coupon") String couponCode) {

        return orderService.placeOrder(userId, qty, couponCode);
    }

    @PostMapping("/{userId}/{orderId}/pay")
    public ResponseEntity<PaymentResponse> makePayment(@PathVariable int userId,
                                                       @PathVariable int orderId,
                                                       @RequestParam int amount) {

        return ResponseEntity.ok(orderService.makePayment(userId,orderId, amount));
    }

    @GetMapping("/{userId}/orders")
    public List<OrderStatus> getUserOrders(@PathVariable int userId) {

        return orderService.getUserOrders(userId);
    }

    @GetMapping("/{userId}/orders/{orderId}")
    public ResponseEntity<?> getOrderDetails(@PathVariable Long orderId) {

        return ResponseEntity.ok(orderService.getOrderDetails(orderId));
    }

}
