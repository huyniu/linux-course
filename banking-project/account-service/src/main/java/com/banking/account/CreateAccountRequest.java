package com.banking.account;

import java.math.BigDecimal;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.PositiveOrZero;

public record CreateAccountRequest(
        @NotBlank @Pattern(regexp = "[0-9]{6,20}", message = "phai gom 6-20 chu so") String accountNumber,
        @NotBlank String ownerName,
        @NotNull @PositiveOrZero BigDecimal initialBalance) {
}
