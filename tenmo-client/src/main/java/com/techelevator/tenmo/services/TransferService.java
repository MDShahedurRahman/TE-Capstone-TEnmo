package com.techelevator.tenmo.services;

import com.techelevator.tenmo.model.AuthenticatedUser;
import com.techelevator.tenmo.model.Transfer;
import com.techelevator.util.BasicLogger;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestClientResponseException;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;

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


    private HttpEntity<Transfer> makeEntity(AuthenticatedUser authenticatedUser, Transfer newTransfer) {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(authenticatedUser.getToken());
        headers.setContentType(MediaType.APPLICATION_JSON);
        return new HttpEntity<>(newTransfer, headers);
    }
}
