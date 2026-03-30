package com.bookmyshow.payment;

import com.bookmyshow.enums.PaymentStatus;

public class UPIPayment implements PaymentProcessor {

    private String upiId;

    public UPIPayment(String upiId) {
        this.upiId = upiId;
    }

    @Override
    public PaymentStatus pay(double amount) {
        System.out.println("Processing UPI payment of Rs." + amount + " from " + upiId + ".");
        return PaymentStatus.SUCCESS;
    }

    @Override
    public PaymentStatus refund(double amount) {
        System.out.println("Refunding Rs." + amount + " to UPI id " + upiId + ".");
        return PaymentStatus.SUCCESS;
    }

    @Override
    public String getMethodName() {
        return "UPI";
    }
}
