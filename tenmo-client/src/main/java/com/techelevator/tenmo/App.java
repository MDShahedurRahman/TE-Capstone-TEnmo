package com.techelevator.tenmo;

import com.techelevator.tenmo.model.*;
import com.techelevator.tenmo.services.*;

import java.math.BigDecimal;

public class App {

    private static final String API_BASE_URL = "http://localhost:8080/";

    private final ConsoleService consoleService = new ConsoleService();
    private final AuthenticationService authenticationService = new AuthenticationService(API_BASE_URL);
    private final AccountService accountService = new AccountService();
    private final UserService userService = new UserService();
    private final TransferService transferService = new TransferService();

    private AuthenticatedUser currentUser;

    public static void main(String[] args) {
        App app = new App();
        app.run();
    }

    private void run() {
        consoleService.printGreeting();
        loginMenu();
        if (currentUser != null) {
            mainMenu();
        }
    }
    private void loginMenu() {
        int menuSelection = -1;
        while (menuSelection != 0 && currentUser == null) {
            consoleService.printLoginMenu();
            menuSelection = consoleService.promptForMenuSelection("Please choose an option: ");
            if (menuSelection == 1) {
                handleRegister();
            } else if (menuSelection == 2) {
                handleLogin();
            } else if (menuSelection != 0) {
                System.out.println("Invalid Selection");
                consoleService.pause();
            }
        }
    }

    private void handleRegister() {
        System.out.println("Please register a new user account");
        UserCredentials credentials = consoleService.promptForCredentials();
        if (authenticationService.register(credentials)) {
            System.out.println("Registration successful. You can now login.");
        } else {
            consoleService.printErrorMessage();
        }
    }

    private void handleLogin() {
        UserCredentials credentials = consoleService.promptForCredentials();
        currentUser = authenticationService.login(credentials);
        if (currentUser == null) {
            consoleService.printErrorMessage();
        }
    }

    private void mainMenu() {
        int menuSelection = -1;
        while (menuSelection != 0) {
            consoleService.printMainMenu();
            menuSelection = consoleService.promptForMenuSelection("Please choose an option: ");
            if (menuSelection == 1) {
                viewCurrentBalance();
            } else if (menuSelection == 2) {
                viewTransferHistory();
            } else if (menuSelection == 3) {
                viewPendingRequests();
            } else if (menuSelection == 4) {
                sendBucks();
            } else if (menuSelection == 5) {
                requestBucks();
            } else if (menuSelection == 0) {
                continue;
            } else {
                System.out.println("Invalid Selection");
            }
            consoleService.pause();
        }
    }

	private void viewCurrentBalance() {
		// TODO Auto-generated method stub
        AccountService accountService = new AccountService();
        // Prints a formatted version of the users current balance (see console services)
        consoleService.printBalance(accountService.getAccountByUserId(currentUser, currentUser.getUser().getId()).getBalance());
	}

	private void viewTransferHistory() {
		// TODO Auto-generated method stub
		
	}

	private void viewPendingRequests() {
		// TODO Auto-generated method stub
		
	}

	private void sendBucks() {
		// TODO Auto-generated method stub
        consoleService.printSendRequestBanner();
        consoleService.printUserIdsAndNames(currentUser, userService.getUsers(currentUser));
        int receivingUserId = consoleService.promptForInt("Enter ID of user you are sending money to (0 to cancel): ");

        if (receivingUserId == currentUser.getUser().getId()) {
            System.out.println("You cannot send money to yourself.");
        }

        if (receivingUserId != 0 && receivingUserId != currentUser.getUser().getId()) {
            BigDecimal amountToSend = consoleService.promptForBigDecimal("Enter amount: $");
            if (accountService.getAccountByUserId(currentUser, currentUser.getUser().getId()).getBalance().subtract(amountToSend).compareTo(BigDecimal.ZERO) >= 0) {
                int currentUserAccountId = accountService.getAccountByUserId(currentUser, currentUser.getUser().getId()).getAccountId();
                int receivingUserAccountId = accountService.getAccountByUserId(currentUser, receivingUserId).getAccountId();
                Transfer transfer = new Transfer(2, 2, currentUserAccountId, receivingUserAccountId, amountToSend);
                Transfer sentTransfer = transferService.sendTransfer(currentUser, transfer);

                if (accountService.updateAccount(currentUser, sentTransfer, currentUserAccountId)) {
                    System.out.println("Transfer complete.");
                } else {
                    System.out.println("Transfer failed, make sure the user ID matches on the list and try again.");
                }

            } else {
                consoleService.printNotEnoughBalanceForTransfer();
            }
        }
	}

	private void requestBucks() {
		// TODO Auto-generated method stub
        consoleService.printSendRequestBanner();
        consoleService.printUserIdsAndNames(currentUser, userService.getUsers(currentUser));
        int requestingUserId = consoleService.promptForInt("Enter ID of user you are requesting money from (0 to cancel): ");

        if (requestingUserId == currentUser.getUser().getId()) {
            System.out.println("You cannot request money from yourself.");
        }

        if (requestingUserId != 0 && requestingUserId != currentUser.getUser().getId()) {
            BigDecimal amountToRequest = consoleService.promptForBigDecimal("Enter amount: $");
            Transfer transfer = new Transfer(1, 1,
                    accountService.getAccountByUserId(currentUser, requestingUserId).getAccountId(),
                    accountService.getAccountByUserId(currentUser, currentUser.getUser().getId()).getAccountId(),
                    amountToRequest);
            Transfer requestedTransfer = transferService.sendTransfer(currentUser, transfer);
            try {
                if (transferService.getTransferStatusDescriptionById(currentUser,
                        requestedTransfer.getTransferStatusId()).equals("Pending")) {
                    System.out.println("Request was sent and is pending for approval from user.");
                } else {
                    System.out.println("Request failed.");
                }
            } catch (NullPointerException e) {
                System.out.println("Could not find user with specified ID.");
            }
        }
		
	}

}
