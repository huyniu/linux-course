package com.quickbite.user;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "user_wallets")
public class UserWallet {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long userId;

    private long balance;

    protected UserWallet() {
    }

    public Long getId() {
        return id;
    }

    public Long getUserId() {
        return userId;
    }

    public long getBalance() {
        return balance;
    }
}
