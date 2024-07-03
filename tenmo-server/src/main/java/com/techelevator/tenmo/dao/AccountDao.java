package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.Account;

import java.math.BigDecimal;
import java.util.List;

public interface AccountDao {

    BigDecimal getBalance(int userId);
    List<Account> getAccounts();
    Account getAccountByUserId(int userId);
    void updateAccountBalance(Account account);
    String getUsernameByAccountId(int accountId);
    Account getAccountById(int id);
}
