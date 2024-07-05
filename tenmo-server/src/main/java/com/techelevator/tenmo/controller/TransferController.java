package com.techelevator.tenmo.controller;

import com.techelevator.tenmo.dao.*;
import com.techelevator.tenmo.exception.DaoException;
import com.techelevator.tenmo.dao.TransferDao;
import com.techelevator.tenmo.dao.TransferStatusDao;
import com.techelevator.tenmo.dao.TransferTypeDao;
import com.techelevator.tenmo.model.Transfer;
import com.techelevator.tenmo.model.TransferDto;
import com.techelevator.tenmo.model.TransferStatus;
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

    public TransferController(TransferDao transferDao, TransferStatusDao transferStatusDao, TransferTypeDao transferTypeDao) {
        this.transferDao = transferDao;
        this.transferStatusDao = transferStatusDao;
        this.transferTypeDao = transferTypeDao;
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

    @RequestMapping(path = "/transfer/{id}", method = RequestMethod.GET)
    public Transfer getTransfer(@PathVariable int id) {
        Transfer transfer;
        try {
            transfer = transferDao.getTransferById(id);
        } catch (DaoException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return transfer;
    }

    // new end point to get all transfers both to and from the user based off their account id
    @RequestMapping(path = "/account/{accountId}", method = RequestMethod.GET)
    public List<Transfer> getTransfersByAccountId(@PathVariable int accountId) {
        List<Transfer> transfers;
        try {
            transfers = transferDao.getTransfersByAccountId(accountId);
        } catch (DaoException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return transfers;
    }

    @ResponseStatus(HttpStatus.CREATED)
    @RequestMapping(path = "", method = RequestMethod.POST)
    public Transfer addTransfer(@Valid @RequestBody TransferDto newTransfer) {
        try {
            Transfer transfer = transferDao.createTransfer(newTransfer);
            if (transfer == null) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Transfer creation failed.");
            }
            return transferDao.getTransferById(transfer.getId());
        } catch (DaoException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Transfer creation failed.", e.getCause());
        }
    }

    @RequestMapping(path = "/{id}", method = RequestMethod.PUT)
    public Transfer updateTransfer(@Valid @RequestBody Transfer transfer, @PathVariable int id) {
        if (transfer.getId() != id) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Could not find transfer with specified ID");
        }
        return transferDao.updateTransfer(transfer);
    }

    @RequestMapping(path = "/transfer_status/{id}", method = RequestMethod.GET)
    public String getTransferStatusDescriptionById(@PathVariable int id) {
        TransferStatus transferStatus = transferStatusDao.getTransferStatusById(id);
        String transferStatusDescription = transferStatus.getTransferStatusDescription();
        if (transferStatusDescription.isBlank()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Could not find transfer status description with specified ID");
        } else {
            return transferStatusDescription;
        }
    }
}
