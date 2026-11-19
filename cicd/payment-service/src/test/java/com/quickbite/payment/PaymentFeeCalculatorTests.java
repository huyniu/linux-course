package com.quickbite.payment;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.math.BigDecimal;

import org.junit.jupiter.api.Test;

import com.quickbite.payment.PaymentFeeCalculator.Method;

class PaymentFeeCalculatorTests {

    private final PaymentFeeCalculator calculator = new PaymentFeeCalculator();

    @Test
    void cardFeeIsTwoPercent() {
        assertEquals(new BigDecimal("4000"), calculator.calculateFee(new BigDecimal("200000"), Method.CARD));
    }

    @Test
    void eWalletFeeIsOnePercent() {
        assertEquals(new BigDecimal("2000"), calculator.calculateFee(new BigDecimal("200000"), Method.E_WALLET));
    }

    @Test
    void codFeeIsFlat() {
        assertEquals(new BigDecimal("15000"), calculator.calculateFee(new BigDecimal("50000"), Method.COD));
    }

    @Test
    void feeNeverBelowMinimum() {
        assertEquals(new BigDecimal("1000"), calculator.calculateFee(new BigDecimal("10000"), Method.E_WALLET));
    }

    @Test
    void rejectsNonPositiveAmount() {
        assertThrows(IllegalArgumentException.class,
                () -> calculator.calculateFee(BigDecimal.ZERO, Method.CARD));
    }
}
