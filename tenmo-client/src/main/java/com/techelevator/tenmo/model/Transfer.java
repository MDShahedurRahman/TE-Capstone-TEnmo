package com.techelevator.tenmo.model;

import java.math.BigDecimal;
import java.util.Objects;

public class Transfer {
    private int id;
    private int transferStatusId;
    private int transferTypeId;
    private int accountFromId;
    private int accountToId;
    private BigDecimal amount;

    public Transfer() { }

    public Transfer(int id, int transferTypeId, int transferStatusId, int accountFromId, int accountToId, BigDecimal amount) {
        this.id = id;
        this.transferTypeId = transferTypeId;
        this.transferStatusId = transferStatusId;
        this.accountFromId = accountFromId;
        this.accountToId = accountToId;
        this.amount = amount;
    }

    public Transfer(int transferTypeId, int transferStatusId, int accountFromId, int accountToId, BigDecimal amount) {
        this.transferTypeId = transferTypeId;
        this.transferStatusId = transferStatusId;
        this.accountFromId = accountFromId;
        this.accountToId = accountToId;
        this.amount = amount;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getTransferStatusId() {
        return transferStatusId;
    }

    public void setTransferStatusId(int transferStatusId) {
        this.transferStatusId = transferStatusId;
    }

    public int getTransferTypeId() {
        return transferTypeId;
    }

    public void setTransferTypeId(int transferTypeId) {
        this.transferTypeId = transferTypeId;
    }

    public int getAccountFromId() {
        return accountFromId;
    }

    public void setAccountFromId(int accountFromId) {
        this.accountFromId = accountFromId;
    }

    public int getAccountToId() {
        return accountToId;
    }

    public void setAccountToId(int accountToId) {
        this.accountToId = accountToId;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Transfer transfer = (Transfer) o;
        return id == transfer.id &&
                transferStatusId == transfer.transferStatusId &&
                transferTypeId == transfer.transferTypeId &&
                accountFromId == transfer.accountFromId &&
                accountToId == transfer.accountToId &&
                amount.equals(transfer.amount);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, transferStatusId, transferTypeId, accountFromId, accountToId, amount);
    }

    @Override
    public String toString() {
        return "Transfer{" +
                "id=" + id +
                ", transferStatusId=" + transferStatusId +
                ", transferTypeId=" + transferTypeId +
                ", accountFromId=" + accountFromId +
                ", accountToId=" + accountToId +
                ", amount=" + amount +
                '}';
    }
}