package com.techelevator.tenmo.controller;

import com.techelevator.tenmo.dao.AccountDao;
import com.techelevator.tenmo.dao.TransferDao;
import com.techelevator.tenmo.dao.UserDao;
import com.techelevator.tenmo.exception.DaoException;
import com.techelevator.tenmo.model.Transfer;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.security.Principal;

@RestController
@RequestMapping("/transfer")

public class TransferController {
    private final TransferDao transferDao;
    private UserDao userDao;
    private AccountDao accountDao;

    public TransferController(TransferDao transferDao, UserDao userDao, AccountDao accountDao) {
        this.transferDao = transferDao;
        this.userDao = userDao;
        this.accountDao = accountDao;
    }

    @RequestMapping(path = "/{id}", method = RequestMethod.GET)
    public Transfer getTransaction(@PathVariable int id, Principal principal) {
        Transfer transfer;
        try {
            transfer = transferDao.getTransactionByTransferId(id);
        } catch (DaoException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return transfer;

    }

    @RequestMapping(path = "", method = RequestMethod.POST)
    public Transfer addTransaction(@RequestBody Transfer transfer) {
        return transferDao.createTransaction(transfer);
    }


}
