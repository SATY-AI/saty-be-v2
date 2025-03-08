package com.ims.IMS.service.redeemProductService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.*;

import java.util.Map;

@Service
public class ProductRedeemService {

    @Autowired
    private RestTemplate restTemplate;

    public boolean checkUserExistence(String email, String password) {
        String url = "https://admin.trungmua.vn/api/user/check-existence";

        // Create request body
        Map<String, String> requestBody = Map.of(
                "email", email,
                "password", password
        );

        // Set headers
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<Map<String, String>> requestEntity = new HttpEntity<>(requestBody, headers);

        try {
            ResponseEntity<Map> response = restTemplate.postForEntity(url, requestEntity, Map.class);
            if (response.getBody() != null && response.getBody().containsKey("data")) {
                return (Boolean) response.getBody().get("data");
            }
        } catch (Exception e) {
            System.err.println("Error calling checkUserExistence API: " + e.getMessage());
        }

        return false; // Default to false if an error occurs
    }
}
