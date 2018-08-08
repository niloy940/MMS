package com.niloy.mms.model;

import com.niloy.mms.model.accounts.Account;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalTime;

@Entity
public class Transactions {
//    private int accountNumber;
    @Id
    @GeneratedValue
    @Column(name = "t_Id", nullable = false, columnDefinition = "INT(11) UNSIGNED")
    private int transactionId;
    @ManyToOne
    private Account account;
    private TransactionType transactionType;
    private LocalDate transactionDate;
    private LocalTime transactionTime;
    private double amount;

    public Transactions() {
    }

    public Transactions(Account account, TransactionType transactionType, LocalDate transactionDate, LocalTime transactionTime, double amount) {
        this.account = account;
        this.transactionType = transactionType;
        this.transactionDate = transactionDate;
        this.transactionTime = transactionTime;
        this.amount = amount;
    }

    /*public Transactions(int accountNumber, Account account, TransactionType transactionType, LocalDate transactionDate, LocalTime transactionTime, double amount) {
        this.accountNumber = accountNumber;
        this.account = account;
        this.transactionType = transactionType;
        this.transactionDate = transactionDate;
        this.transactionTime = transactionTime;
        this.amount = amount;
    }*/

    /*public int getAccountNumber() {
        return accountNumber;
    }
*/
    public Account getAccount() {
        return account;
    }

    public TransactionType getTransactionType() {
        return transactionType;
    }

    public LocalDate getTransactionDate() {
        return transactionDate;
    }

    public LocalTime getTransactionTime() {
        return transactionTime;
    }

    public double getAmount() {
        return amount;
    }

    public int getAccountNumber(){
        return account.getAccountNumber();
    }

    public Name getName(){
        return account.getName();
    }

    @Override
    public String toString() {
        return "Transactions{" +
                "accountNumber=" + account.getAccountNumber() +
                ", account=" + account +
                ", transactionType=" + transactionType.toString() +
                ", transactionDate=" + transactionDate +
                ", transactionTime=" + transactionTime +
                ", amount=" + amount +
                '}';
    }
}
