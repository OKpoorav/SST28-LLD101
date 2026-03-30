package com.bookmyshow.model;

import com.bookmyshow.enums.PaymentStatus;

import java.time.LocalDateTime;

public class Payment {

    private String id;
    private double amount;
    private PaymentStatus status;
    private String paymentMethod;
    private LocalDateTime processedAt;

    public Payment(String id, double amount, String paymentMethod) {
        this.id = id;
        this.amount = amount;
        this.paymentMethod = paymentMethod;
        this.status = PaymentStatus.PENDING;
    }

    public String getId() {
        return id;
    }

    public double getAmount() {
        return amount;
    }

    public PaymentStatus getStatus() {
        return status;
    }

    public void setStatus(PaymentStatus status) {
        this.status = status;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public LocalDateTime getProcessedAt() {
        return processedAt;
    }

    public void setProcessedAt(LocalDateTime time) {
        this.processedAt = time;
    }
}
