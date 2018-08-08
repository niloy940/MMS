package com.niloy.mms.model;

import com.niloy.mms.model.accounts.Account;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalTime;

@Entity
public class Meal {
    @Id
    @GeneratedValue
    @Column(name = "m_Id", nullable = false, columnDefinition = "INT(11) UNSIGNED")
    private int mealId;
    @ManyToOne
    private Account account;
    private LocalDate localDate;
    private LocalTime localTime;
    private int amount;

    public Meal() {
    }

    public Meal(Account account, LocalDate localDate, LocalTime localTime, int amount) {
        this.account = account;
        this.localDate = localDate;
        this.localTime = localTime;
        this.amount = amount;
    }

    public Account getAccount() {
        return account;
    }

    public LocalDate getLocalDate() {
        return localDate;
    }

    public LocalTime getLocalTime() {
        return localTime;
    }

    public int getAmount() {
        return amount;
    }

    @Override
    public String toString() {
        return "Meal{" +
                "account=" + account +
                ", localDate=" + localDate +
                ", localTime=" + localTime +
                ", amount=" + amount +
                '}';
    }
}
