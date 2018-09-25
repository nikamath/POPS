package me.nikita.order.processing.transaction;

import me.nikita.order.processing.account.Account;
import me.nikita.order.processing.exceptions.PurchaseOrderProcessingException;

public interface ITransaction {

    public void transferAmount(Account toAccount, Integer amount) throws PurchaseOrderProcessingException;

}
