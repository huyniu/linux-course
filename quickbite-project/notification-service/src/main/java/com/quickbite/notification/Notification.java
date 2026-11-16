package com.quickbite.notification;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "notifications")
public class Notification {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long userId;

    private String title;

    private String message;

    private boolean seen;

    protected Notification() {
    }

    public Long getId() {
        return id;
    }

    public Long getUserId() {
        return userId;
    }

    public String getTitle() {
        return title;
    }

    public String getMessage() {
        return message;
    }

    public boolean getSeen() {
        return seen;
    }
}
