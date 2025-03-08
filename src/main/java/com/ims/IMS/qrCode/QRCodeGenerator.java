package com.ims.IMS.qrCode;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.ims.IMS.decode.AESUtil;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

public class QRCodeGenerator {
    public static String generateQRCodeBase64(String data) throws Exception {
        int width = 300;
        int height = 300;
        String fileType = "png";

        // Set encoding hints
        Map<EncodeHintType, Object> hintMap = new HashMap<>();
        hintMap.put(EncodeHintType.CHARACTER_SET, "UTF-8");

        QRCodeWriter qrCodeWriter = new QRCodeWriter();
        BitMatrix bitMatrix = qrCodeWriter.encode(data, BarcodeFormat.QR_CODE, width, height, hintMap);

        // Convert BitMatrix to BufferedImage
        BufferedImage bufferedImage = MatrixToImageWriter.toBufferedImage(bitMatrix);

        // Convert BufferedImage to Base64
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        MatrixToImageWriter.writeToStream(bitMatrix, fileType, outputStream);
        byte[] qrImageBytes = outputStream.toByteArray();

        return Base64.getEncoder().encodeToString(qrImageBytes);
    }

//    public static void main(String[] args) {
//        final String SECRET_KEY = "C+XTO1wK/HRz6P6SNEzXGw==";  // Should be 16 characters for AES
//         // The encrypted data from the QR code
//            String encryptedData = "hSTH/Sl3aaO76yYhib3C4HLVEpG8FFsFImHk1clmMH/Vj7UQ4JEu6jX8zFjkFudni70yrjOBUHyoVCgFWQ1Xacrayj6YUHFyMGbNQijQNZ9xyG/ya6wkkhpUhN+tMCmJ";
//
//            try {
//                // Decrypt the data
//                String decryptedData = AESUtil.decrypt(encryptedData, SECRET_KEY);
//                System.out.println("Decrypted Data: " + decryptedData);
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//        }

}

