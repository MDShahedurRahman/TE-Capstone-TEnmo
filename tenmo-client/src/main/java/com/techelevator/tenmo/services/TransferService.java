package com.techelevator.tenmo.services;

import com.techelevator.tenmo.model.AuthenticatedUser;
import com.techelevator.tenmo.model.Transfer;
import com.techelevator.util.BasicLogger;
import org.springframework.http.*;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestClientResponseException;
import org.springframework.web.client.RestTemplate;

public class TransferService {
    private static final String API_BASE_URL = "http://localhost:8080/";
    private final RestTemplate restTemplate = new RestTemplate();

    private String authToken = null;

    public void setAuthToken(String authToken) {
        this.authToken = authToken;
    }


    public Transfer sendTransfer(AuthenticatedUser authenticatedUser, Transfer newTransfer) {
        HttpEntity<Transfer> entity = makeEntity(authenticatedUser, newTransfer);
        Transfer newSendTransfer = null;
        try {
            newSendTransfer = restTemplate.postForObject(API_BASE_URL + "transfers", entity, Transfer.class);
        } catch (RestClientResponseException | ResourceAccessException e) {
            BasicLogger.log(e.getMessage());
        }
        return newSendTransfer;
    }

    // Need to create a method that updates transfer status
     public Transfer updateTransfer(AuthenticatedUser authenticatedUser, int id){
        Transfer transfer
     }

    public String getTransferStatusDescriptionById(AuthenticatedUser authenticatedUser, int id) {
        String transferStatusDescription = "";
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(authenticatedUser.getToken());
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<Integer> entity = new HttpEntity<>(id, headers);
        try {
            ResponseEntity<String> response = restTemplate.exchange(API_BASE_URL + "transfers/transfer_status/" + id, HttpMethod.GET,
                    entity, String.class);
            transferStatusDescription = response.getBody();
        } catch (RestClientResponseException | ResourceAccessException e) {
            BasicLogger.log(e.getMessage());
        }
        return transferStatusDescription;
    }

    // Getting a specific transfer by its Id
    public Transfer getTransferById(AuthenticatedUser authenticatedUser, int id) {
        Transfer transfer = null;
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(authenticatedUser.getToken());
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<Integer> entity = new HttpEntity<>(id, headers);
        try {
            ResponseEntity<Transfer> response = restTemplate.exchange(API_BASE_URL + "transfer/" + id, HttpMethod.GET,
                    entity, Transfer.class);
            transfer = response.getBody();
        } catch (RestClientResponseException | ResourceAccessException e) {
            BasicLogger.log(e.getMessage());
        }
        return transfer;
    }
        // Added new method for getting transfers by account ID
    public Transfer[] getTransfersByAccountId(int accountId) {
        Transfer[] transfers = null;
        try {
            HttpEntity<Void> entity = createAuthEntity();
            ResponseEntity<Transfer[]> response = restTemplate.exchange(
                    API_BASE_URL + "transfers/account/" + accountId,
                    HttpMethod.GET, entity, Transfer[].class);
            transfers = response.getBody();
        } catch (RestClientResponseException | ResourceAccessException e) {
            BasicLogger.log(e.getMessage());
        }
        return transfers;
    }


    private HttpEntity<Transfer> makeEntity(AuthenticatedUser authenticatedUser, Transfer newTransfer) {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(authenticatedUser.getToken());
        headers.setContentType(MediaType.APPLICATION_JSON);
        return new HttpEntity<>(newTransfer, headers);
    }

    private HttpEntity<Void> createAuthEntity() {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(authToken);
        return new HttpEntity<>(headers);
    }
}
