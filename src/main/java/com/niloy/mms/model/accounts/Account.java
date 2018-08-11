package com.niloy.mms.model.accounts;

import com.niloy.mms.model.Address;
import com.niloy.mms.model.Meal;
import com.niloy.mms.model.Name;
import com.niloy.mms.model.Transactions;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Account {
    @Id
    private int accountNumber;
    @ManyToOne
    private Name name;
    @Embedded
    private Address address;
    @OneToMany(mappedBy = "account")
    private List<Transactions> transactionsList;
    @OneToMany(mappedBy = "account")
    private List<Meal> mealList;
    protected double balance;

    public Account() {
        transactionsList = new ArrayList<>();
        mealList = new ArrayList<>();
    }

    public Account(int accountNumber, Name name, Address address, double balance) {
        this.accountNumber = accountNumber;
        this.name = name;
        this.address = address;
        this.balance = balance;
        transactionsList = new ArrayList<>();
        mealList = new ArrayList<>();
    }

    public int getAccountNumber() {
        return accountNumber;
    }

    public Name getName() {
        return name;
    }

    public Address getAddress() {
        return address;
    }

    public double getBalance() {
        return balance;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    public List<Transactions> getTransactionsList() {
        return transactionsList;
    }

    public List<Meal> getMealList() {
        return mealList;
    }

    public void deposit(int amount){
        if (amount > 0)
            balance += amount;
        else System.err.println("Depositing negative amount is not acceptable!");
    }

    public void withdraw(int amount){
        if (amount > 0 && amount <= balance)
            balance -= amount;
        else System.err.println("You don't have sufficient balance!");
    }

    public String getAllData() {
        return "Account{" +
                "accountNumber=" + accountNumber +
                ", name=" + name +
                ", address=" + address +
                ", balance=" + balance +
                '}';
    }

    @Override
    public String toString() {
        return accountNumber + ". " + name;
    }
}
