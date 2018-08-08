package com.niloy.mms.model;

import com.niloy.mms.model.accounts.Account;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Name {
    @Id
    @GeneratedValue
    @Column(name = "Id", nullable = false, columnDefinition = "INT(11) UNSIGNED")
    private int nameId;
    private String firstName;
    private String lastName;
    private String middleName;
    @OneToMany(mappedBy = "name")
    private List<Account> accountList;

    public Name() {
        accountList = new ArrayList<>();
    }

    public Name(String firstName, String lastName, String middleName) {
        this.firstName = firstName;
        this.middleName = middleName;
        this.lastName = lastName;
        accountList = new ArrayList<>();
    }

    public String getFirstName() {
        return firstName;
    }

    public String getMiddleName() {
        return middleName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getAllData() {
        return "Name{" +
                "firstName='" + firstName + '\'' +
                ", middleName='" + middleName + '\'' +
                ", lastName='" + lastName + '\'' +
                '}';
    }

    @Override
    public String toString() {
        return firstName + " " + middleName + " " + lastName;
    }
}
