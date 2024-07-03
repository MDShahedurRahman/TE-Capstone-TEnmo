package com.techelevator.tenmo.model;

import java.util.Objects;

public class TransferType {
    private int transferTypeId;
    private String transferTypeDescription;

    public TransferType(int transferTypeId, String transferTypeDescription) {
        this.transferTypeId = transferTypeId;
        this.transferTypeDescription = transferTypeDescription;
    }

    public int getTransferTypeId() {
        return transferTypeId;
    }

    public void setTransferTypeId(int transferTypeId) {
        this.transferTypeId = transferTypeId;
    }

    public String getTransferTypeDescription() {
        return transferTypeDescription;
    }

    public void setTransferTypeDescription(String transferTypeDescription) {
        this.transferTypeDescription = transferTypeDescription;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TransferType that = (TransferType) o;
        return transferTypeId == that.transferTypeId &&
                transferTypeDescription.equals(that.transferTypeDescription);
    }

    @Override
    public int hashCode() {
        return Objects.hash(transferTypeId, transferTypeDescription);
    }

    @Override
    public String toString() {
        return "TransferType{" +
                "transferTypeId=" + transferTypeId +
                ", transferTypeDescription='" + transferTypeDescription + '\'' +
                '}';
    }
}