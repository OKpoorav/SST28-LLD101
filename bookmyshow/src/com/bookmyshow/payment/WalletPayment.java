package com.bookmyshow.payment;

import com.bookmyshow.enums.PaymentStatus;

public class WalletPayment implements PaymentProcessor {

    private String walletId;
    private double walletBalance;

    public WalletPayment(String walletId, double walletBalance) {
        this.walletId = walletId;
        this.walletBalance = walletBalance;
    }

    @Override
    public PaymentStatus pay(double amount) {
        if (walletBalance < amount) {
            System.out.println("Insufficient wallet balance.");
            return PaymentStatus.FAILED;
        }
        walletBalance -= amount;
        System.out.println("Paid Rs." + amount + " from wallet.");
        return PaymentStatus.SUCCESS;
    }

    @Override
    public PaymentStatus refund(double amount) {
        walletBalance += amount;
        System.out.println("Refunded Rs." + amount + " to wallet.");
        return PaymentStatus.SUCCESS;
    }

    @Override
    public String getMethodName() {
        return "WALLET";
    }
}
