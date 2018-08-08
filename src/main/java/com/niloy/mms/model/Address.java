package com.niloy.mms.model;

import javax.persistence.Embeddable;

@Embeddable
public class Address {
    private String streetAddress;
    private String city;
    private String country;
    private String postalCode;

    public Address() {
    }

    public Address(String streetAddress, String city, String country, String postalCode) {
        this.streetAddress = streetAddress;
        this.city = city;
        this.country = country;
        this.postalCode = postalCode;
    }

    public String getStreetAddress() {
        return streetAddress;
    }

    public String getCity() {
        return city;
    }

    public String getCountry() {
        return country;
    }

    public String getPostalCode() {
        return postalCode;
    }

    @Override
    public String toString() {
        return "Address{" +
                "streetAddree='" + streetAddress + '\'' +
                ", city='" + city + '\'' +
                ", country='" + country + '\'' +
                ", postalCode='" + postalCode + '\'' +
                '}';
    }
}
