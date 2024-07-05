package com.techelevator.tenmo.controller;


import com.techelevator.tenmo.dao.AccountDao;
import com.techelevator.tenmo.dao.UserDao;
import com.techelevator.tenmo.model.Account;
import com.techelevator.tenmo.model.User;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("/users")
@PreAuthorize("isAuthenticated()")
public class UserController {
    private final UserDao userDao;
    private final AccountDao accountDao;

    public UserController(UserDao userDao, AccountDao accountDao) {
        this.userDao = userDao;
        this.accountDao = accountDao;
    }

    @RequestMapping(path = "", method = RequestMethod.GET)
    public List<User> getUsers() {
        List<User> users = userDao.getUsers();
        if (users.size() < 1) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No users found");
        } else {
            return users;
        }
    }
    @RequestMapping(path = "/{id}", method = RequestMethod.GET)
    public User getUserById(@PathVariable int id) {
        User user = userDao.getUserById(id);
        if (user == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User was not found using the specified ID");
        } else {
            return user;
        }
    }

    @RequestMapping(path = "/{id}/account", method = RequestMethod.GET)
    public Account getAccountByUserId(@PathVariable int id) {
        Account account = accountDao.getAccountByUserId(id);
        if (account == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Account was not found using the specified UserID");
        } else {
            return account;
        }
    }
    @RequestMapping(path = "/account/{id}", method = RequestMethod.GET)
    public String getUsernameByAccountId(@PathVariable int id) {
        String username = accountDao.getUsernameByAccountId(id);
        if (username == null || username.isBlank()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Username not found using specified AccountId");
        } else {
            return username;
        }
    }
}