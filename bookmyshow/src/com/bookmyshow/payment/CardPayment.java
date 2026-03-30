package com.bookmyshow.payment;

import com.bookmyshow.enums.PaymentStatus;

public class CardPayment implements PaymentProcessor {

    private String cardLastFourDigits;

    public CardPayment(String cardLastFourDigits) {
        this.cardLastFourDigits = cardLastFourDigits;
    }

    @Override
    public PaymentStatus pay(double amount) {
        System.out.println("Processing card payment of Rs." + amount + " for card ending " + cardLastFourDigits + ".");
        return PaymentStatus.SUCCESS;
    }

    @Override
    public PaymentStatus refund(double amount) {
        System.out.println("Refunding Rs." + amount + " to card ending " + cardLastFourDigits + ".");
        return PaymentStatus.SUCCESS;
    }

    @Override
    public String getMethodName() {
        return "CARD";
    }
}
