package com.shoppingapp.shoppingapp.repository;

import com.shoppingapp.shoppingapp.entity.Coupon;
import com.shoppingapp.shoppingapp.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Long> {
    List<Order> findAllByCoupon(Coupon coupon);
    List<Order> findAllByUserId(int userId);
}
