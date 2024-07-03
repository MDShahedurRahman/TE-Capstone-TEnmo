package com.techelevator.tenmo.controller;

import com.techelevator.tenmo.dao.AccountDao;
import com.techelevator.tenmo.model.Account;
import com.techelevator.tenmo.model.Transfer;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import com.techelevator.tenmo.dao.TransferStatusDao;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;


@RestController
@PreAuthorize("isAuthenticated()")
public class AccountController {
    private final AccountDao accountDao;
    private final TransferStatusDao transferStatusDao;


    public AccountController(AccountDao accountDao, TransferStatusDao transferStatusDao) {
        this.accountDao = accountDao;
        this.transferStatusDao = transferStatusDao;
    }


    // Return a List of all accounts. Not sure if this is needed.
    @RequestMapping(path = "/account", method = RequestMethod.GET)
    public List<Account> list() {
        return accountDao.getAccounts();
    }

    @RequestMapping(path = "/account/{userId}", method = RequestMethod.GET)
    public Account get(@PathVariable int userId) {
        Account account = accountDao.getAccountByUserId(userId);
        if (account == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Account not found.");
        } else {
            return account;
        }
    }


    @RequestMapping(path = "/account/balance/{id}", method = RequestMethod.PUT)
    public void updateAccountBalance(@RequestBody Transfer transfer) {
        Account accountTo = accountDao.getAccountById(transfer.getAccountToId());
        Account accountFrom = accountDao.getAccountById(transfer.getAccountFromId());
        BigDecimal amountTransferred = transfer.getAmount();
        if (transfer.getTransferStatusId() == transferStatusDao.getTransferStatusByDescription("Approved").getTransferStatusId()) {
            accountFrom.setBalance(accountFrom.getBalance().subtract(amountTransferred));
            accountTo.setBalance(accountTo.getBalance().add(amountTransferred));
            accountDao.updateAccountBalance(accountFrom);
            accountDao.updateAccountBalance(accountTo);
        }
    }
}
