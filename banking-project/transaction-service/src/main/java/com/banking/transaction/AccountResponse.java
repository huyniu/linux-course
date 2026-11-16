package com.banking.transaction;

import java.math.BigDecimal;

// Du lieu tai khoan nhan ve tu account-service
public record AccountResponse(String accountNumber, String ownerName, BigDecimal balance) {
}
