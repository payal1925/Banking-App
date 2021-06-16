package com.example.bankingapp.Models;

public class User {
    private long id;
    private String name;
    private String email;
    private double current_balance;

    public User() {
    }

    public User(long id, String name, String email, double current_balance) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.current_balance = current_balance;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public double getCurrent_balance() {
        return current_balance;
    }

    public void setCurrent_balance(double current_balance) {
        this.current_balance = current_balance;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", current_balance=" + current_balance +
                '}';
    }
}
