package com.shoppingapp.shoppingapp.service;

import com.shoppingapp.shoppingapp.entity.Order;
import com.shoppingapp.shoppingapp.entity.OrderStatus;
import com.shoppingapp.shoppingapp.entity.PaymentResponse;
import com.shoppingapp.shoppingapp.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.util.*;


@Service
public class OrderService {
    @Autowired
    private OrderRepository orderRepository;

    public Order placeOrder(int userId, int qty, String couponCode) {

        if (qty < 1) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid quantity");
        }
        int totalAmount = 100 * qty;

        int disamount = totalAmount;
        if (couponCode.equals("OFF5")) {
            double discount = 0.05; // 5% discount
            disamount = (int) (totalAmount * (1 - discount));
        } else if (couponCode.equals("OFF10")) {
            double discount = 0.10; // 10% discount
            disamount = (int) (totalAmount * (1 - discount));
        }
        Order order = new Order();
        order.setAmount(disamount);
        order.setUserId(userId);
        order.setQuantity(qty);
        order.setCouponNo(couponCode);
        order.setOrderId(100);

        return orderRepository.save(order);
    }

    public PaymentResponse makePayment(int userId, int orderId, int amount) {

        Random random = new Random();
        int statusCode = random.nextInt(6);
        PaymentResponse paymentResponse = new PaymentResponse();
        paymentResponse.setOrderId(orderId);
        paymentResponse.setUserId(userId);
        paymentResponse.setTransactionId("tran01010000" + (statusCode + 1));

        switch (statusCode) {
            case 0:
                paymentResponse.setStatus("successful");
                break;
            case 1:
                paymentResponse.setStatus("failed");
                paymentResponse.setDescription("Payment Failed as amount is invalid");
                break;
            case 2:
                paymentResponse.setStatus("failed");
                paymentResponse.setDescription("Payment Failed from bank");
                break;
            case 3:
                paymentResponse.setStatus("failed");
                paymentResponse.setDescription("Payment Failed due to invalid order id");
                break;
            case 4:
                paymentResponse.setStatus("failed");
                paymentResponse.setDescription("No response from payment server");
                break;
            case 5:
                paymentResponse.setStatus("failed");
                paymentResponse.setDescription("Order is already paid for");
                break;
        }
        return paymentResponse;
    }

    public List<OrderStatus> getUserOrders(int userId) {

        List<Order> orders = orderRepository.findAllByUserId(userId);
        return convertToOrderStatusList(orders);
    }

    private List<OrderStatus> convertToOrderStatusList(List<Order> orders) {

        List<OrderStatus> orderStatusList = new ArrayList<>();
        LocalDate currentDate = LocalDate.now();
        for (Order order : orders) {
            OrderStatus orderStatus = new OrderStatus();
            orderStatus.setOrderId(order.getOrderId());
            orderStatus.setAmount(order.getAmount());
            orderStatus.setDate(currentDate.toString());
            orderStatus.setCoupon(order.getCouponNo());
            orderStatusList.add(orderStatus);
        }
        return orderStatusList;
    }

    public OrderStatus getOrderDetails(Long orderId) {

        LocalDate currentDate = LocalDate.now();
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Order not found"));

        OrderStatus orderStatus = new OrderStatus();
        orderStatus.setOrderId(order.getOrderId());
        orderStatus.setAmount(order.getAmount());
        orderStatus.setDate(currentDate.toString());
        orderStatus.setCoupon(order.getCouponNo());

        String transactionId = generateTransactionId(orderId);
        String status = getStatusForOrderId(orderId);

        orderStatus.setTransactionId(transactionId);
        orderStatus.setStatus(status);

        return orderStatus;
    }


    private String generateTransactionId(Long orderId) {

        return "tran01010000" + orderId.toString();
    }

    private String getStatusForOrderId(Long orderId) {

        if (orderId % 2 != 0) {
            return "successful";
        } else {
            return "pending";
        }
    }
}