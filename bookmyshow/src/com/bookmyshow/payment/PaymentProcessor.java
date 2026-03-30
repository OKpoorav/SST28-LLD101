package com.bookmyshow.payment;

import com.bookmyshow.enums.PaymentStatus;

public interface PaymentProcessor {

    PaymentStatus pay(double amount);

    PaymentStatus refund(double amount);

    String getMethodName();
}
