package com.banking.transaction;

import java.math.BigDecimal;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record TransferRequest(
        @NotBlank String fromAccount,
        @NotBlank String toAccount,
        @NotNull @Positive BigDecimal amount) {
}
