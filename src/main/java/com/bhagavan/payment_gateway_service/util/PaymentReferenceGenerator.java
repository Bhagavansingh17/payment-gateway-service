package com.bhagavan.payment_gateway_service.util;

import java.util.UUID;

public class PaymentReferenceGenerator {

    public static String generatePaymentRef() {
        return "PAY_" + UUID.randomUUID().toString().replace("-", "").substring(0, 12).toUpperCase();
    }
}