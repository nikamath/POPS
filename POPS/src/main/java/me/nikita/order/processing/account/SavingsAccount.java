package me.nikita.order.processing.account;

import java.util.concurrent.atomic.AtomicInteger;

import me.nikita.order.processing.account.Account;
import me.nikita.order.processing.enums.AccountState;
import me.nikita.order.processing.exceptions.PurchaseOrderProcessingException;
import me.nikita.order.processing.roi.IROI;
import me.nikita.order.processing.transaction.ITransaction;

public class SavingsAccount extends Account implements IROI, ITransaction {
    private static final Integer maxAmountPerDay = 5000;
    private static Double rateOfInterest = 7.5;
    private AtomicInteger amountWithdrawnToday = new AtomicInteger(0);// keeps track of amount withdrawn in a day for this account

    public SavingsAccount(String userName, Integer balance) throws PurchaseOrderProcessingException {
        super(userName, balance);
    }

    @Override
    public synchronized void transferAmount(Account toAccount, Integer amount) throws PurchaseOrderProcessingException {
        withdrawal(amount);
        toAccount.deposit(amount);
    }

    @Override
    public Double getRateOfInterest() {
        return rateOfInterest;
    }

    @Override
    public void withdrawal(Integer amount) throws PurchaseOrderProcessingException {
        if (amountWithdrawnToday.get() + amount >= maxAmountPerDay)
            throw new PurchaseOrderProcessingException("You have exceeded your withdrawal limit!!!");
        if (amount > maxAmountPerDay)
            throw new PurchaseOrderProcessingException("Max Amount that can be withdrawn in a day is " + maxAmountPerDay);
        super.withdrawal(amount);
        amountWithdrawnToday.addAndGet(amount);
    }

    @Override
    public void openAccount() throws PurchaseOrderProcessingException {
        if (isInitialBalanceValid()) {
            setAccountState(AccountState.OPENED);
        } else
            throw new PurchaseOrderProcessingException("Minimum amount not maintained for opening account");
    }

}
