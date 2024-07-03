package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.Transfer;
import com.techelevator.tenmo.model.TransferDto;

import java.util.List;

public interface TransferDao {
    Transfer getTransferById(int transferId);
    Transfer createTransfer(TransferDto transfer);
    List<Transfer> getTransfers();
    Transfer updateTransfer(Transfer transfer);
}