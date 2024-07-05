package com.techelevator.tenmo.services;


import com.techelevator.tenmo.model.AuthenticatedUser;
import com.techelevator.tenmo.model.Transfer;
import com.techelevator.tenmo.model.User;
import com.techelevator.tenmo.model.UserCredentials;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;
import java.util.Scanner;

public class ConsoleService {

    private final Scanner scanner = new Scanner(System.in);

    public int promptForMenuSelection(String prompt) {
        int menuSelection;
        System.out.print(prompt);
        try {
            menuSelection = Integer.parseInt(scanner.nextLine());
        } catch (NumberFormatException e) {
            menuSelection = -1;
        }
        return menuSelection;
    }

    public void printGreeting() {
        System.out.println("*********************");
        System.out.println("* Welcome to TEnmo! *");
        System.out.println("*********************");
    }

    public void printLoginMenu() {
        System.out.println();
        System.out.println("1: Register");
        System.out.println("2: Login");
        System.out.println("0: Exit");
        System.out.println();
    }

    public void printMainMenu() {
        System.out.println();
        System.out.println("1: View your current balance");
        System.out.println("2: View your past transfers");
        System.out.println("3: View your pending requests");
        System.out.println("4: Send TE bucks");
        System.out.println("5: Request TE bucks");
        System.out.println("0: Exit");
        System.out.println();
    }

    public void printSendRequestBanner() {
        System.out.println("-------------------------------------------");
        System.out.printf("%-10s %n", "Users");
        System.out.printf(" %-10s  %-10s %n", "ID", "Name");
        System.out.println("-------------------------------------------");
    }

    public void printNotEnoughBalanceForTransfer() {
        System.out.println();
        System.out.println("**************************************************************");
        System.out.println("Your available balance is not high enough for the transaction.");
        System.out.println("**************************************************************");
    }


    public void printUserIdsAndNames(AuthenticatedUser authenticatedUser, User[] users) {
        for (User user : users) {
            if (user.getId() != authenticatedUser.getUser().getId()) {
                System.out.println(user.getId() + "\t\t" + user.getUsername());
            }
        }
        System.out.println("---------");
    }

    public UserCredentials promptForCredentials() {
        String username = promptForString("Username: ");
        String password = promptForString("Password: ");
        return new UserCredentials(username, password);
    }

    public String promptForString(String prompt) {
        System.out.print(prompt);
        return scanner.nextLine();
    }

    public int promptForInt(String prompt) {
        System.out.print(prompt);
        while (true) {
            try {
                return Integer.parseInt(scanner.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("Please enter a number.");
            }
        }
    }

    public BigDecimal promptForBigDecimal(String prompt) {
        System.out.print(prompt);
        while (true) {
            try {
                return new BigDecimal(scanner.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("Please enter a decimal number.");
            }
        }
    }

    public void pause() {
        System.out.println("\nPress Enter to continue...");
        scanner.nextLine();
    }

    public void printErrorMessage() {
        System.out.println("An error occurred. Check the log for details.");
    }

    public void printBalance(BigDecimal balance) {
        NumberFormat currencyFormat = NumberFormat.getCurrencyInstance(Locale.US);
        String formattedBalance = currencyFormat.format(balance);
        System.out.println("Your current account balance is: " + formattedBalance);
    }

    public void printUsersIdAndName(User[] users){
        System.out.println("-------------------------------------------");
        System.out.printf("%-10s %n", "Users");
        System.out.printf(" %-10s  %-10s %n", "ID", "Name");
        System.out.println("-------------------------------------------");
        for(User user:users){
            System.out.printf(" %-10s  %-10s %n", user.getId(), user.getUsername());
        }
        System.out.println("---------");
    }
    private void viewPendingRequestText(){
        System.out.println();
    }
    private void prettyPrintTransferInfo(Transfer transfer){
        System.out.println("-------------------------------------------- \n Transfer Details \n--------------------------------------------");
        System.out.println("Id: " + transfer.getId());
        System.out.println("From: " + transfer.getAccountFromId());
        System.out.println("To: " + transfer.getAccountFromId());
        System.out.println("");
    }
}
