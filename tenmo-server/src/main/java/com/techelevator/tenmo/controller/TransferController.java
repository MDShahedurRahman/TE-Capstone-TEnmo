package com.techelevator.tenmo.controller;

import com.techelevator.tenmo.dao.*;
import com.techelevator.tenmo.exception.DaoException;
import com.techelevator.tenmo.dao.TransferDao;
import com.techelevator.tenmo.dao.TransferStatusDao;
import com.techelevator.tenmo.dao.TransferTypeDao;
import com.techelevator.tenmo.model.Transfer;
import com.techelevator.tenmo.model.TransferDto;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/transfers")
@PreAuthorize("isAuthenticated()")
public class TransferController {
    private final TransferDao transferDao;
    private final TransferStatusDao transferStatusDao;
    private final TransferTypeDao transferTypeDao;
    private UserDao userDao;
    private AccountDao accountDao;

    public TransferController(TransferDao transferDao, TransferStatusDao transferStatusDao, TransferTypeDao transferTypeDao, UserDao userDao, AccountDao accountDao) {
        this.transferDao = transferDao;
        this.transferStatusDao = transferStatusDao;
        this.transferTypeDao = transferTypeDao;
        this.userDao = userDao;
        this.accountDao = accountDao;
    }

    @RequestMapping(path = "", method = RequestMethod.GET)
    public List<Transfer> getTransfers() {
        List<Transfer> transfers;
        try {
            transfers = transferDao.getTransfers();
        } catch (DaoException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return transfers;
    }

    @RequestMapping(path = "/{id}", method = RequestMethod.GET)
    public Transfer getTransfer(@PathVariable int id) {
        Transfer transfer;
        try {
            transfer = transferDao.getTransferById(id);
        } catch (DaoException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return transfer;
    }

    @ResponseStatus(HttpStatus.CREATED)
    @RequestMapping(path = "", method = RequestMethod.POST)
    public Transfer addTransfer(@Valid @RequestBody TransferDto transferdto) {
        Transfer transfer;
        try {
            transfer = transferDao.createTransfer(transferdto);
        } catch (DaoException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Transfer creation failed.", e.getCause());
        }

        return transfer;
    }
}
