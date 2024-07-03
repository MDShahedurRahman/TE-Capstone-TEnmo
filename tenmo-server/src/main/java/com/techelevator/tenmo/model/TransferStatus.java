package com.techelevator.tenmo.model;

import java.util.Objects;

public class TransferStatus {
    private int transferStatusId;
    private String transferStatusDescription;

    public TransferStatus(int transferStatusId, String transferStatusDescription) {
        this.transferStatusId = transferStatusId;
        this.transferStatusDescription = transferStatusDescription;
    }

    public int getTransferStatusId() {
        return transferStatusId;
    }

    public void setTransferStatusId(int transferStatusId) {
        this.transferStatusId = transferStatusId;
    }

    public String getTransferStatusDescription() {
        return transferStatusDescription;
    }

    public void setTransferStatusDescription(String transferStatusDescription) {
        this.transferStatusDescription = transferStatusDescription;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TransferStatus that = (TransferStatus) o;
        return transferStatusId == that.transferStatusId &&
                transferStatusDescription.equals(that.transferStatusDescription);
    }

    @Override
    public int hashCode() {
        return Objects.hash(transferStatusId, transferStatusDescription);
    }

    @Override
    public String toString() {
        return "TransferStatus{" +
                "transferStatusId=" + transferStatusId +
                ", transferStatusDescription='" + transferStatusDescription + '\'' +
                '}';
    }
}