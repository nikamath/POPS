package me.nikita.order.processing.account;

import me.nikita.order.processing.account.Account;
import me.nikita.order.processing.enums.AccountState;
import me.nikita.order.processing.exceptions.PurchaseOrderProcessingException;
import me.nikita.order.processing.roi.IROI;
import me.nikita.order.processing.transaction.ITransaction;

public class CurrentAccount extends Account implements IROI, ITransaction {
    private static final Integer minBalanceRequired = 2000;
    private static Double rateOfInterest = 7.5;

    public CurrentAccount(String userName, Integer balance) throws PurchaseOrderProcessingException {
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
    public void openAccount() throws PurchaseOrderProcessingException {
        if (isInitialBalanceValid()) {
            setAccountState(AccountState.OPENED);
        } else
            throw new PurchaseOrderProcessingException("Minimum amount not maintained for opening account");
    }

    @Override
    public synchronized void withdrawal(Integer amount) throws PurchaseOrderProcessingException {
        if (getBalance() - amount < minBalanceRequired)
            throw new PurchaseOrderProcessingException("Withdrawal failed as minimum balance cannot be maintained!!");
        super.withdrawal(amount);
    }
}
