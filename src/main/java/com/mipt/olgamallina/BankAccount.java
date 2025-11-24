package com.mipt.olgamallina;

public class BankAccount {
    private final long id;
    private int balance;

    public BankAccount(long id, int balance) {
        this.id = id;
        this.balance = balance;
    }

    public long getId() {
        return id;
    }

    public int getBalance() {
        return balance;
    }

    void deposit(int amount) {
        balance += amount;
    }

    void withdraw(int amount) {
        balance -= amount;
    }

    @Override
    public String toString() {
        return "Account{id=" + id + ", balance=" + balance + "}";
    }
}
