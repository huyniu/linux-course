package com.banking.account;

import java.math.BigDecimal;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record AmountRequest(@NotNull @Positive BigDecimal amount) {
}
