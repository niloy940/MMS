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
    private LocalDate mealDate;
    private LocalTime mealTime;
    private int amount;

    public Meal() {
    }

    public Meal(Account account, LocalDate mealDate, LocalTime mealTime, int amount) {
        this.account = account;
        this.mealDate = mealDate;
        this.mealTime = mealTime;
        this.amount = amount;
    }

    public Account getAccount() {
        return account;
    }

    public LocalDate getMealDate() {
        return mealDate;
    }

    public LocalTime getMealTime() {
        return mealTime;
    }

    public int getAmount() {
        return amount;
    }

    @Override
    public String toString() {
        return "Meal{" +
                "accountNumber" + account.getAccountNumber() +
                "account=" + account +
                ", localDate=" + mealDate +
                ", localTime=" + mealTime +
                ", amount=" + amount +
                '}';
    }
}
