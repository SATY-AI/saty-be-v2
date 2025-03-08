package com.ims.IMS.service.qrCodeService;


import com.ims.IMS.decode.AESUtil;
import com.ims.IMS.qrCode.QRCodeGenerator;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class QrCodeService {
//    private static final String SECRET_KEY = "G7k!d9Pq@3xT#vB5";  // Should be 16 characters for AES
    public String generateQRCodeBase64(String SECRET_KEY, String deviceID, String email, String phoneNumber) throws Exception {
        // Create JSON string from data
        String jsonData = createJsonString(deviceID, email, phoneNumber);
        // Encrypt the JSON data
        String encryptedData = AESUtil.encrypt(jsonData, SECRET_KEY);
        // Generate the QR code and convert it to Base64
        return QRCodeGenerator.generateQRCodeBase64(encryptedData);
    }
    private String createJsonString(String deviceID, String email, String phoneNumber) throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        Map<String, String> data = new HashMap<>();
        data.put("deviceID", deviceID);
        data.put("email", email);
        data.put("phoneNumber", phoneNumber);
        return mapper.writeValueAsString(data);
    }


}

