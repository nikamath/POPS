package me.nikita.order.processing.account;

import java.util.concurrent.atomic.AtomicLong;

import me.nikita.order.processing.enums.AccountState;
import me.nikita.order.processing.exceptions.PurchaseOrderProcessingException;

public abstract class Account {

    private final Long accountNumber;
    private String userName;
    private Integer balance;
    private AccountState accountState; // defines the state of the account
    private static AtomicLong accountNumberCounter = new AtomicLong(100000000000L); //used to generate account number

    public Account(String userName, Integer balance) {
        super();
        this.accountNumber = accountNumberCounter.incrementAndGet();
        this.userName = userName;
        this.balance = balance;
        this.accountState = AccountState.CREATED;
    }

    @Override
    public void finalize() {
        this.userName = null;
        this.balance = null;
        System.out.println("Account object getting destroyed");
    }

    protected boolean isInitialBalanceValid() {
        return balance < 1000 ? false : true;
    }

    private boolean isAccountCloseable() {
        return balance == 0 ? true : false;
    }

    public abstract void openAccount() throws PurchaseOrderProcessingException;

    public void closeAccount() throws PurchaseOrderProcessingException {
        if (isAccountCloseable()) {
            accountState = AccountState.CLOSED;
        } else
            throw new PurchaseOrderProcessingException("Account balance should be 0 for closing");

    }

    public void editAccount(String userName) {
        this.userName = userName;
    }

    public synchronized void deposit(Integer amount) throws PurchaseOrderProcessingException {
        if (amount < 0)
            throw new PurchaseOrderProcessingException("Invalid amount to deposit");
        balance = balance + amount;
    }

    public synchronized void withdrawal(Integer amount) throws PurchaseOrderProcessingException {
        if (amount < 0)
            throw new PurchaseOrderProcessingException("Incorrect amount!!!");

        else if (balance < amount) {
            throw new PurchaseOrderProcessingException("Insufficient balance!!!");
        } else
            balance = balance - amount;
    }

    public Integer checkBalance() {
        return balance;
    }

    public String getAccountDetails() {
        return toString();
    }

    public Integer getBalance() {
        return balance;
    }

    @Override
    public String toString() {
        return "Account [accountNumber=" + accountNumber + ", userName=" + userName + ", balance=" + balance + "]";
    }

    public void setAccountState(AccountState accountState) {
        this.accountState = accountState;
    }

}
