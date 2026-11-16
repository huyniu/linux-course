package com.banking.account;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/accounts")
public class AccountController {

    private static final Logger log = LoggerFactory.getLogger(AccountController.class);

    private final AccountRepository accountRepository;

    public AccountController(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Account create(@Valid @RequestBody CreateAccountRequest request) {
        if (accountRepository.existsByAccountNumber(request.accountNumber())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT,
                    "So tai khoan da ton tai: " + request.accountNumber());
        }
        Account account = accountRepository.save(
                new Account(request.accountNumber(), request.ownerName(), request.initialBalance()));
        log.info("Da tao tai khoan {} cho {}", account.getAccountNumber(), account.getOwnerName());
        return account;
    }

    @GetMapping
    public List<Account> findAll() {
        return accountRepository.findAll();
    }

    @GetMapping("/{accountNumber}")
    public Account findByAccountNumber(@PathVariable String accountNumber) {
        log.info("Truy van tai khoan {}", accountNumber);
        return accountRepository.findByAccountNumber(accountNumber)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "Khong tim thay tai khoan: " + accountNumber));
    }
}
