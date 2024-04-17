package com.shoppingapp.shoppingapp.service;

import com.shoppingapp.shoppingapp.entity.Coupon;
import com.shoppingapp.shoppingapp.entity.Order;
import com.shoppingapp.shoppingapp.repository.CouponRepository;
import com.shoppingapp.shoppingapp.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class CouponService {
    @Autowired
    private CouponRepository couponRepository;

    @Autowired
    private OrderRepository orderRepository;

    public Map<String, Integer> getAllCoupons() {

        Map<String, Integer> coupons = new HashMap<>();
        coupons.put("OFF5", 5);
        coupons.put("OFF10", 10);
        return coupons;
    }

    public Coupon getCouponByCode(String code) {

        return couponRepository.findByCode(code)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Coupon not found"));
    }

    public boolean hasUserUsedCoupon(String code, int userId) {

        Coupon coupon = getCouponByCode(code);
        if(coupon != null) {
            List<Order> userOrders = orderRepository.findAllByCoupon(coupon);
            for (Order order : userOrders) {
                if (order.getAmount() > 0 && order.getUserId() == userId) {
                    return true;
                }
            }
        }
        return false;
    }
}