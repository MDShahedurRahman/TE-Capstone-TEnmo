package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.Transfer;

public interface TransferDao {
    public Transfer getTransferByTransferId(int transferId);

    public Transfer createTransfer(Transfer transfer);

}
