package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.Transfer;

public interface TransferDao {
    public Transfer getTransactionByTransferId(int transferId);

    public Transfer createTransaction(Transfer transfer);

}
