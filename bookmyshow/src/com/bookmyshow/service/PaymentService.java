package com.bookmyshow.service;

import com.bookmyshow.enums.PaymentStatus;
import com.bookmyshow.model.Payment;
import com.bookmyshow.payment.PaymentProcessor;

import java.time.LocalDateTime;
import java.util.UUID;

public class PaymentService {

    public Payment process(double amount, PaymentProcessor processor) {
        String id = UUID.randomUUID().toString();
        Payment payment = new Payment(id, amount, processor.getMethodName());
        PaymentStatus status = processor.pay(amount);
        payment.setStatus(status);
        payment.setProcessedAt(LocalDateTime.now());
        System.out.println("Payment " + status + " for Rs." + amount + " via " + processor.getMethodName() + ".");
        return payment;
    }

    public PaymentStatus refund(Payment payment, PaymentProcessor processor) {
        return processor.refund(payment.getAmount());
    }
}
