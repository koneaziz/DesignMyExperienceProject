package com.aa.designmyexperience.Models;

import java.time.LocalDate;

public class Order {
    private int orderId;
    private int userId;
    private int eventId;
    private LocalDate bookingDate;
    private int ticketQuantity;
    private double totalPrice;
    private String orderStatus;
    private String eventName;

    // Default constructor
    public Order() {
    }

    // Constructor with orderId (if needed)
    public Order(int orderId, int userId, int eventId, LocalDate bookingDate, int ticketQuantity, double totalPrice, String orderStatus) {
        this.orderId = orderId;
        this.userId = userId;
        this.eventId = eventId;
        this.bookingDate = bookingDate;
        this.ticketQuantity = ticketQuantity;
        this.totalPrice = totalPrice;
        this.orderStatus = orderStatus;
    }

    // Constructor without orderId (for inserts, orderId being auto-generated)
    public Order(int userId, int eventId, LocalDate bookingDate, int ticketQuantity, double totalPrice, String orderStatus) {
        this.userId = userId;
        this.eventId = eventId;
        this.bookingDate = bookingDate;
        this.ticketQuantity = ticketQuantity;
        this.totalPrice = totalPrice;
        this.orderStatus = orderStatus;
    }

    // Getters and setters

    public int getOrderId() {
        return orderId;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getEventId() {
        return eventId;
    }

    public void setEventId(int eventId) {
        this.eventId = eventId;
    }

    public LocalDate getBookingDate() {
        return bookingDate;
    }

    public void setBookingDate(LocalDate bookingDate) {
        this.bookingDate = bookingDate;
    }

    public int getTicketQuantity() {
        return ticketQuantity;
    }

    public void setTicketQuantity(int ticketQuantity) {
        this.ticketQuantity = ticketQuantity;
    }

    public double getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(double totalPrice) {
        this.totalPrice = totalPrice;
    }

    public String getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(String orderStatus) {
        this.orderStatus = orderStatus;
    }

    public String getEventName() {
        return eventName;
    }

    public void setEventName(String eventName) {
        this.eventName = eventName;
    }

}
