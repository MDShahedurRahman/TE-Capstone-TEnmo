package com.techelevator.tenmo;

import com.techelevator.tenmo.model.*;
import com.techelevator.tenmo.services.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

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
        transferService.setAuthToken(currentUser.getToken());

        Scanner scanner = new Scanner(System.in);
        System.out.println("-------------------------------------------------------------");
        System.out.println("Transfers");
        System.out.printf("%-20s %-30s %-10s", "ID", "From/To", "Amount");
        System.out.println();
        System.out.println("-------------------------------------------------------------");
        // Getting user Account info
        Account userAccount = accountService.getAccountByUserId(currentUser, currentUser.getUser().getId());
        Transfer[] transfers = transferService.getTransfersByAccountId(userAccount.getAccountId());
        // Check if transfers array is null
        if (transfers != null && transfers.length > 0) {
            for (Transfer transfer : transfers) {
                if (transfer.getTransferTypeId() == 2 && transfer.getAccountFromId() == userAccount.getAccountId()){
                    String fromTo = "To: " + userService.getUserByAccountId(currentUser, transfer.getAccountToId());
                    System.out.printf("%-20s %-30s %-10s%n", transfer.getId(), fromTo, "$"+transfer.getAmount());
                } else if(transfer.getTransferTypeId() == 2 && transfer.getAccountToId() == userAccount.getAccountId()){
                    String fromTo = "From: " + userService.getUserByAccountId(currentUser, transfer.getAccountFromId());
                    System.out.printf("%-20s %-30s %-10s%n", transfer.getId(), fromTo, "$"+transfer.getAmount());
                } else if (transfer.getTransferTypeId() == 1 && transfer.getAccountFromId() == userAccount.getAccountId()
                        && transfer.getTransferStatusId() == 1) {
                    String fromTo = "Request From: " + userService.getUserByAccountId(currentUser, transfer.getAccountToId());
                    System.out.printf("%-20s %-30s %-10s%n", transfer.getId()+"(Pending)", fromTo, "$"+transfer.getAmount());
                } else if (transfer.getTransferTypeId() == 1 && transfer.getAccountToId() == userAccount.getAccountId()
                        && transfer.getTransferStatusId() == 1){
                    String fromTo = "Request To: " + userService.getUserByAccountId(currentUser, transfer.getAccountFromId());
                    System.out.printf("%-20s %-30s %-10s%n", transfer.getId()+"(Pending)", fromTo, "$"+transfer.getAmount());
                } else if (transfer.getTransferTypeId() == 1 && transfer.getAccountFromId() == userAccount.getAccountId()
                        && transfer.getTransferStatusId() == 2){
                    String fromTo = "Request From: " + userService.getUserByAccountId(currentUser, transfer.getAccountToId());
                    System.out.printf("%-20s %-30s %-10s%n", transfer.getId()+"(Approved)", fromTo, "$"+transfer.getAmount());
                } else if (transfer.getTransferTypeId() == 1 && transfer.getAccountToId() == userAccount.getAccountId()
                        && transfer.getTransferStatusId() == 2){
                    String fromTo = "Request To: " + userService.getUserByAccountId(currentUser, transfer.getAccountFromId());
                    System.out.printf("%-20s %-30s %-10s%n", transfer.getId()+"(Approved)", fromTo, "$"+transfer.getAmount());
                } else if (transfer.getTransferTypeId() == 1 && transfer.getAccountFromId() == userAccount.getAccountId()
                        && transfer.getTransferStatusId() == 3){
                    String fromTo = "Request From: " + userService.getUserByAccountId(currentUser, transfer.getAccountToId());
                    System.out.printf("%-20s %-30s %-10s%n", transfer.getId()+"(Rejected)", fromTo, "$"+transfer.getAmount());
                } else if (transfer.getTransferTypeId() == 1 && transfer.getAccountToId() == userAccount.getAccountId()
                        && transfer.getTransferStatusId() == 3){
                    String fromTo = "Request To: " + userService.getUserByAccountId(currentUser, transfer.getAccountFromId());
                    System.out.printf("%-20s %-30s %-10s%n", transfer.getId()+"(Rejected)", fromTo, "$"+transfer.getAmount());
                }
            }
        } else {
            System.out.println();
            System.out.println("No transfers found.");
        }
        System.out.println("---------");
        int transferId = consoleService.promptForInt("Please enter transfer ID to view details (0 to cancel): ");
        if (transferId == 0) {
            System.out.println("Canceling...");
            return;
        }


        int transferCount = 0;
        for (Transfer transfer:transfers) {
            if (transfer.getId() == transferId) {
                System.out.println("-------------------------------------------- \n Transfer Details \n--------------------------------------------");
                System.out.println("Id: " + transfer.getId());
                System.out.println("From: " + userService.getUserByAccountId(currentUser, transfer.getAccountFromId()));
                System.out.println("To: " + userService.getUserByAccountId(currentUser, transfer.getAccountToId()));
                if (transfer.getTransferTypeId() == 1) {
                    System.out.println("Type: Request");
                } else {
                    System.out.println("Type: Send");
                }
                if (transfer.getTransferStatusId() == 1) {
                    System.out.println("Status: Pending");
                } else if (transfer.getTransferStatusId() == 2) {
                    System.out.println("Status: Approved");
                } else {
                    System.out.println("Status: Rejected");
                }
                System.out.println("Amount: $" + transfer.getAmount());
                transferCount++;
            }
        }

        if (transferCount == 0) {
            System.out.println("Transfer ID not found.");
        }
    }


	private void viewPendingRequests() {
        List<Transfer> pendingRequestList = new ArrayList<>();
        transferService.setAuthToken(currentUser.getToken());
        System.out.println("---------------------------------------------------");
        System.out.println("Pending Transfers");
        System.out.printf("%-18s %-24s %-10s", "ID", "To", "Amount");
        System.out.println();
        System.out.println("---------------------------------------------------");
        Account userAccount = accountService.getAccountByUserId(currentUser, currentUser.getUser().getId());
        Transfer[] transfers = transferService.getTransfersByAccountId(userAccount.getAccountId());
        for (Transfer transfer: transfers) {
            if (transfer.getAccountFromId() == userAccount.getAccountId() &&
                    transfer.getTransferTypeId() == 1 && transfer.getTransferStatusId() == 1) {
                pendingRequestList.add(transfer);
                System.out.printf("%-18s %-24s %-10s%n", transfer.getId(), userService.getUserByAccountId(currentUser, transfer.getAccountToId()), "$"+transfer.getAmount());
            }
        }
        System.out.println("---------");
        int transferId = consoleService.promptForInt("Please enter transfer ID to approve/reject (0 to cancel): ");
        if (transferId == 0){
            System.out.println("Canceled...");
            return;
        }

        Transfer transfer = new Transfer();
        try {
            transfer = transferService.getTransferById(currentUser, transferId);
        } catch (NullPointerException e) {
            System.out.println("Invalid transfer ID not matches on the list.");
        }

        if (pendingRequestList.contains(transfer)) {
            System.out.println();
            System.out.println("Id: " + transfer.getId());
            System.out.println("Transfer Request From: " + userService.getUserByAccountId(currentUser, transfer.getAccountToId()));
            System.out.println("Type: Request");
            System.out.println("Status: Pending");
            System.out.println("Amount: $" + transfer.getAmount());
            System.out.println();

            System.out.println("1: Approve\n" +
                    "2: Reject\n" +
                    "0: Don't approve or reject\n" +
                    "---------\n");
            int userInput = consoleService.promptForInt("Please choose an option: ");
            if (userInput == 0) {
                return;
            }

            switch (userInput) {
                case 1:
                    BigDecimal balance = userAccount.getBalance();
                    BigDecimal transferAmount = transfer.getAmount();
                    if (balance.subtract(transferAmount).compareTo(BigDecimal.ZERO) >= 0) {
                        transfer.setTransferStatusId(2);
                        transfer = transferService.updateTransfer(currentUser, transfer);
                        if (accountService.updateAccount(currentUser, transfer, userAccount.getAccountId())) {
                            System.out.print("Request approved.");
                        } else {
                            System.out.println("Failed to approve.");
                        }
                    } else {
                        consoleService.printTransferFailed();
                    }
                    break;
                case 2:
                    transfer.setTransferStatusId(3);
                    transfer = transferService.updateTransfer(currentUser, transfer);
                    if (transferService.getTransferById(currentUser, transfer.getId()).getTransferStatusId() == 3) {
                        System.out.println("Request rejected.");
                    } else {
                        System.out.println("Failed to approve.");
                    }
                    break;
                default:
                    System.out.println("Not a valid option.");
                    break;
            }
        } else {
            System.out.println("Pending request with specified transfer ID Could not be found.");
        }
	}

	private void sendBucks() {
		// TODO Auto-generated method stub
        consoleService.printSendRequestBanner();
        consoleService.printUserIdsAndNames(currentUser, userService.getUsers(currentUser));
        int receivingUserId = consoleService.promptForInt("Enter ID of user you are sending to (0 to cancel): ");

        if (receivingUserId == currentUser.getUser().getId()) {
            System.out.println("Cannot send money to yourself.");
        }

        if (receivingUserId != 0 && receivingUserId != currentUser.getUser().getId()) {
            Account currentUserAccount = accountService.getAccountByUserId(currentUser, currentUser.getUser().getId());
            Account receivingUserAccount = accountService.getAccountByUserId(currentUser, receivingUserId);
            if (receivingUserAccount == null) {
                System.out.println("User ID is not matching on the list and try again.");
                return;
            }

            BigDecimal amountToSend = consoleService.promptForBigDecimal("Enter amount: $");
            if (accountService.getAccountByUserId(currentUser, currentUser.getUser().getId()).getBalance().subtract(amountToSend).compareTo(BigDecimal.ZERO) >= 0) {
                int currentUserAccountId = currentUserAccount.getAccountId();
                int receivingUserAccountId = receivingUserAccount.getAccountId();
                Transfer newTransfer = new Transfer(2, 2, currentUserAccountId,
                        receivingUserAccountId, amountToSend);
                Transfer sentTransfer = transferService.sendTransfer(currentUser, newTransfer);

                if (accountService.updateAccount(currentUser, sentTransfer, currentUserAccountId)) {
                    System.out.println("Transfer complete.");
                } else {
                    System.out.println("Transfer failed, user ID is not matching on the list and try again.");
                }

            } else {
                consoleService.printTransferFailed();
            }
        }
	}

	private void requestBucks() {
		// TODO Auto-generated method stub
        consoleService.printSendRequestBanner();
        consoleService.printUserIdsAndNames(currentUser, userService.getUsers(currentUser));
        int requestingUserId = consoleService.promptForInt("Enter ID of user you are requesting from (0 to cancel): ");

        if (requestingUserId == currentUser.getUser().getId()) {
            System.out.println("Cannot request money from yourself.");
        }

        if (requestingUserId != 0 && requestingUserId != currentUser.getUser().getId()) {
            Account currentUserAccount = accountService.getAccountByUserId(currentUser, currentUser.getUser().getId());
            Account requestingUserAccount = accountService.getAccountByUserId(currentUser, requestingUserId);
            if (requestingUserAccount == null) {
                System.out.println("User ID is not matching on the list and try again.");
                return;
            }

            BigDecimal amountToRequest = consoleService.promptForBigDecimal("Enter amount: $");
            Transfer transfer = new Transfer(1, 1, requestingUserAccount.getAccountId(),
                    currentUserAccount.getAccountId(), amountToRequest);
            Transfer requestedTransfer = transferService.sendTransfer(currentUser, transfer);
            try {
                if (transferService.getTransferStatusById(currentUser,
                        requestedTransfer.getTransferStatusId()).equals("Pending")) {
                    System.out.println("Request sent and is now pending for approval.");
                } else {
                    System.out.println("Request failed.");
                }
            } catch (NullPointerException e) {
                System.out.println("User Could not be found with the ID.");
            }
        }
		
	}

}
