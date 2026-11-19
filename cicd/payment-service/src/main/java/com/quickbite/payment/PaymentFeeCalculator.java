package com.quickbite.payment;

import java.math.BigDecimal;
import java.math.RoundingMode;

import org.springframework.stereotype.Component;

// Tinh phi giao dich theo phuong thuc thanh toan
@Component
public class PaymentFeeCalculator {

    public enum Method {
        CARD, E_WALLET, COD
    }

    private static final BigDecimal CARD_RATE = new BigDecimal("0.02");
    private static final BigDecimal E_WALLET_RATE = new BigDecimal("0.01");
    private static final BigDecimal COD_FLAT_FEE = new BigDecimal("15000");
    private static final BigDecimal MIN_FEE = new BigDecimal("1000");

    public BigDecimal calculateFee(BigDecimal amount, Method method) {
        if (amount == null || amount.signum() <= 0) {
            throw new IllegalArgumentException("So tien thanh toan phai lon hon 0");
        }
        BigDecimal fee = switch (method) {
            case CARD -> amount.multiply(CARD_RATE);
            case E_WALLET -> amount.multiply(E_WALLET_RATE);
            case COD -> COD_FLAT_FEE;
        };
        return fee.max(MIN_FEE).setScale(0, RoundingMode.HALF_UP);
    }
}
