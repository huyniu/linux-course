package com.banking.transaction;

import java.math.BigDecimal;
import java.util.Map;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

// Goi API noi bo cua account-service qua DNS cua Docker (http://account-service:8080)
@Component
public class AccountClient {

    private static final Logger log = LoggerFactory.getLogger(AccountClient.class);

    private final RestTemplate restTemplate;
    private final String baseUrl;

    public AccountClient(RestTemplate restTemplate, @Value("${account-service.url}") String baseUrl) {
        this.restTemplate = restTemplate;
        this.baseUrl = baseUrl;
    }

    public Optional<AccountResponse> findAccount(String accountNumber) {
        log.info("Goi account-service: GET {}/api/accounts/{}", baseUrl, accountNumber);
        try {
            return Optional.ofNullable(restTemplate.getForObject(
                    baseUrl + "/api/accounts/{accountNumber}", AccountResponse.class, accountNumber));
        } catch (HttpClientErrorException.NotFound e) {
            log.warn("account-service tra ve 404 cho tai khoan {}", accountNumber);
            return Optional.empty();
        }
    }

    public void debit(String accountNumber, BigDecimal amount) {
        log.info("Goi account-service: POST {}/api/accounts/{}/debit amount={}", baseUrl, accountNumber, amount);
        restTemplate.postForObject(baseUrl + "/api/accounts/{accountNumber}/debit",
                Map.of("amount", amount), AccountResponse.class, accountNumber);
    }

    public void credit(String accountNumber, BigDecimal amount) {
        log.info("Goi account-service: POST {}/api/accounts/{}/credit amount={}", baseUrl, accountNumber, amount);
        restTemplate.postForObject(baseUrl + "/api/accounts/{accountNumber}/credit",
                Map.of("amount", amount), AccountResponse.class, accountNumber);
    }
}
