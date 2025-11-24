package com.mipt.olgamallina;

public class Bank {

    public void sendToAccountDeadlock(BankAccount from, BankAccount to, int amount) {
        synchronized (from) {
            synchronized (to) {
                if (from.getBalance() < amount) {
                    throw new IllegalArgumentException("Недостаточно средств");
                }
                from.withdraw(amount);
                to.deposit(amount);
            }
        }
    }

    private static final Object tieLock = new Object();

    public void sendToAccount(BankAccount from, BankAccount to, int amount) {
        if (amount <= 0) {
            throw new IllegalArgumentException("Сумма должна быть положительной");
        }
        if (from == to) return;

        BankAccount first;
        BankAccount second;

        if (from.getId() < to.getId()) {
            first = from;
            second = to;
        } else if (from.getId() > to.getId()) {
            first = to;
            second = from;
        } else {

            synchronized (tieLock) {
                synchronized (from) {
                    synchronized (to) {
                        doTransfer(from, to, amount);
                    }
                }
            }
            return;
        }

        synchronized (first) {
            synchronized (second) {
                doTransfer(from, to, amount);
            }
        }
    }

    private void doTransfer(BankAccount from, BankAccount to, int amount) {
        if (from.getBalance() < amount) {
            throw new IllegalArgumentException("Недостаточно средств");
        }
        from.withdraw(amount);
        to.deposit(amount);
    }
}
