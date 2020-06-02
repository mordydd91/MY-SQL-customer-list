
//Author: Mordy dabah


package com.example.mysql;

public class Customer {

    private String firstName, lastName;
    private String city;
    private int avg_shopping_price;
    private long customerId;

    public Customer(long customerId, String firstName, String lastName, String city, int avg_shopping_price) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.city = city;
        this.avg_shopping_price = avg_shopping_price;
        this.customerId = customerId;
    }

    public Customer(String firstName, String lastName, String city, int avg_shopping_price) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.city = city;
        this.avg_shopping_price = avg_shopping_price;
        this.customerId = 0;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Customer customer = (Customer) o;
        return firstName.equals(customer.getFirstName()) && lastName.equals(customer.getLastName()) && city.equals(customer.getCity());
    }

    @Override
    public String toString() {
        return "Customer{" +
                "firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", city='" + city + '\'' +
                ", avg_shopping_price=" + avg_shopping_price +
                ", customerId=" + customerId +
                '}';
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public int getAvg_shopping_price() {
        return avg_shopping_price;
    }

    public void setAvg_shopping_price(int avg_shopping_price) {
        this.avg_shopping_price = avg_shopping_price;
    }

    public long getCustomerId() {
        return customerId;
    }

    public void setCustomerId(long customerId) {
        this.customerId = customerId;
    }

}
