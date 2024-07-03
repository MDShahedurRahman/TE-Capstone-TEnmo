package com.techelevator.tenmo.model;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.Positive;
import java.math.BigDecimal;
import java.util.Objects;

public class TransferDto {

    private int transferTypeId;
    private int transferStatusId;
    @Min(1000)
    private int accountFromId;
    @Min(1000)
    private int accountToId;
    @Positive(message = "Transfer amount must be higher greater than $0.00")
    private BigDecimal amount;

    public TransferDto(int transferTypeId, int transferStatusId, int accountFromId, int accountToId, BigDecimal amount) {
        this.transferTypeId = transferTypeId;
        this.transferStatusId = transferStatusId;
        this.accountFromId = accountFromId;
        this.accountToId = accountToId;
        this.amount = amount;
    }

    public int getTransferTypeId() {
        return transferTypeId;
    }

    public void setTransferTypeId(int transferTypeId) {
        this.transferTypeId = transferTypeId;
    }

    public int getTransferStatusId() {
        return transferStatusId;
    }

    public void setTransferStatusId(int transferStatusId) {
        this.transferStatusId = transferStatusId;
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
        TransferDto transferDto = (TransferDto) o;
        return transferTypeId == transferDto.transferTypeId &&
                transferStatusId == transferDto.transferStatusId &&
                accountFromId == transferDto.accountFromId &&
                accountToId == transferDto.accountToId &&
                amount.equals(transferDto.amount);
    }

    @Override
    public int hashCode() {
        return Objects.hash(transferTypeId, transferStatusId, accountFromId, accountToId, amount);
    }

    @Override
    public String toString() {
        return "ValidTransferDto{" +
                "transferTypeId=" + transferTypeId +
                ", transferStatusId=" + transferStatusId +
                ", accountFromId=" + accountFromId +
                ", accountToId=" + accountToId +
                ", amount=" + amount +
                '}';
    }
}