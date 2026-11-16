package com.banking.transaction;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClientException;
import org.springframework.web.server.ResponseStatusException;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/transactions")
public class TransferController {

    private static final Logger log = LoggerFactory.getLogger(TransferController.class);

    private final AccountClient accountClient;
    private final TransactionRepository transactionRepository;

    public TransferController(AccountClient accountClient, TransactionRepository transactionRepository) {
        this.accountClient = accountClient;
        this.transactionRepository = transactionRepository;
    }

    @PostMapping("/transfer")
    @ResponseStatus(HttpStatus.CREATED)
    public Transaction transfer(@Valid @RequestBody TransferRequest request) {
        log.info("Nhan yeu cau chuyen {} tu {} sang {}", request.amount(), request.fromAccount(),
                request.toAccount());

        if (request.fromAccount().equals(request.toAccount())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "Tai khoan nguon va tai khoan dich phai khac nhau");
        }

        // Buoc 1: hoi account-service xem tai khoan nguon co ton tai khong
        AccountResponse source = accountClient.findAccount(request.fromAccount())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "Tai khoan nguon khong ton tai: " + request.fromAccount()));

        // Buoc 2: kiem tra tai khoan dich
        accountClient.findAccount(request.toAccount())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "Tai khoan dich khong ton tai: " + request.toAccount()));

        // Buoc 3: kiem tra so du
        if (source.balance().compareTo(request.amount()) < 0) {
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY,
                    "So du khong du: hien co " + source.balance() + ", can " + request.amount());
        }

        // Buoc 4: tru tien nguon roi cong tien dich
        try {
            accountClient.debit(request.fromAccount(), request.amount());
        } catch (HttpClientErrorException.Conflict e) {
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, "So du khong du");
        }
        try {
            accountClient.credit(request.toAccount(), request.amount());
        } catch (RestClientException e) {
            // Cong tien that bai: hoan lai tien cho tai khoan nguon
            log.error("Cong tien that bai, hoan {} ve tai khoan {}", request.amount(), request.fromAccount(), e);
            accountClient.credit(request.fromAccount(), request.amount());
            throw new ResponseStatusException(HttpStatus.BAD_GATEWAY,
                    "Chuyen tien that bai, da hoan tien ve tai khoan nguon");
        }

        Transaction transaction = transactionRepository.save(
                new Transaction(request.fromAccount(), request.toAccount(), request.amount(), "SUCCESS"));
        log.info("Chuyen tien thanh cong, ma giao dich {}", transaction.getId());
        return transaction;
    }

    @GetMapping
    public List<Transaction> findAll() {
        return transactionRepository.findAll();
    }
}
