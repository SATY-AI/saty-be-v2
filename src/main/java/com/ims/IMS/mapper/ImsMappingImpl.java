package com.ims.IMS.mapper;

import com.ims.IMS.api.CustomerImageDetailResponse;
import com.ims.IMS.api.InsectListDataResponse;
import com.ims.IMS.api.satyjourney.data.SatyDataRequest;
import com.ims.IMS.api.satyjourney.data.SatyDataResponse;
import com.ims.IMS.api.device.ListDeviceResponse;
import com.ims.IMS.api.image.ListImageResponse;
import com.ims.IMS.api.location.ListLocationResponse;
import com.ims.IMS.api.location.LocationIDResponse;
import com.ims.IMS.model.SatyJourney.SatyJourneyData;
import com.ims.IMS.model.insect.InsectListData;
import com.ims.IMS.model.imsprocessing.Device;
import com.ims.IMS.model.imsprocessing.Image;
import com.ims.IMS.model.imsprocessing.ImageDetail;
import com.ims.IMS.model.imsprocessing.Location;
import com.ims.IMS.service.qrCodeService.QrCodeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Component
@Primary
public class ImsMappingImpl implements ImsMapping {
    @Autowired
    private QrCodeService qrCodeService;

    public ListDeviceResponse mapFromDevice(Device device, String secretKey, String email, String phoneNumber) {
        // Generate QR code for the device ID
        String qrCodeBase64;
        try {
            qrCodeBase64 = qrCodeService.generateQRCodeBase64(secretKey, device.getDeviceID(), email, phoneNumber);
        } catch (Exception e) {
            // Log the exception and provide a default value for the QR code
            // Use a logging framework or replace with System.out.println for simplicity
            // logger.error("Error generating QR code", e);
            qrCodeBase64 = ""; // Use an empty string or a placeholder if QR code generation fails
        }
        // Create the response object
        return new ListDeviceResponse(
                device.getId(),
                device.getDeviceID(),
                device.getDescription(),
                device.getCreated_at(),
                device.getUpdated_at(),
                device.getMissionDescription(),
                device.getLocationX(),
                device.getLocationY(),
                device.getLocationID(),
                email, // Use the email parameter passed to the method
                qrCodeBase64
        );
    }



    @Override
    public LocationIDResponse mapToLocationID(Location location) {
        return new LocationIDResponse(location.getLocationID());
    }

    // Method to map Location to ListLocationResponse (if needed separately)
    public ListLocationResponse mapFromLocation(Location location) {
        // You can customize this if necessary
        return new ListLocationResponse(
                location.getId(),
                location.getLocationID(),
                location.getLocationProvince().toString(),
                location.getDescription(),
                location.getUrlLocationImage(),
                Collections.emptyList() // No devices here if used separately
        );
    }

    @Override
    public CustomerImageDetailResponse mapToCustomerImageDetailResponse(ImageDetail imageDetail) {
        if (imageDetail == null) {
            return null;
        }
        return new CustomerImageDetailResponse(
                imageDetail.getImageName(),
                imageDetail.getDescription(),
                imageDetail.getNumber(),
                imageDetail.getLength()
        );
    }

    @Override
    public ListImageResponse mapFromImage(Image image) {
        return new ListImageResponse(
                image.getId(),
                image.getImageBucket(),
                image.getDescription(),
                image.getTopic(),
                image.getImageList(),
                image.getDeviceID(),
                image.getCreated_at(),
                image.getUpdated_at());
    }

    @Override
    public List<InsectListDataResponse> toResponseList(List<InsectListData> insectListDataList) {
        // Check if the input list is null or empty
        if (insectListDataList == null || insectListDataList.isEmpty()) {
            return Collections.emptyList(); // Return an empty list if input is null or empty
        }
        // Convert each InsectListData to InsectListDataResponse
        return insectListDataList.stream()
                .map(insectListData -> new InsectListDataResponse(
                        insectListData.getId(),
                        insectListData.getVnName(),
                        insectListData.getEnName()
                ))
                .collect(Collectors.toList()); // Collect results into a list
    }

    @Override
    public SatyJourneyData mapToSatyJourneyData(SatyDataRequest satyDataRequest) {
        // Mapping s0 to s14 to fields of DeoCaData
        String[] fields = satyDataRequest.S().split(";");
        return SatyJourneyData.builder()
                .customID(satyDataRequest.ID())
                .phoneNumber(satyDataRequest.PhoneNumber())    // s-1: PhoneNumber
                .kinhDo(Float.parseFloat(fields[0]))            // s0: KinhDo
                .viDo(Float.parseFloat(fields[1]))              // s1: ViDo
                .tocDo(Float.parseFloat(fields[2]))             // s2: TocDo
                .acc(Integer.parseInt(fields[3]))               // s3: ACC
                .ex1(Integer.parseInt(fields[4]))               // s4: Ex1
                .ex2(Integer.parseInt(fields[5]))               // s5: Ex2
                .mucNhienLieu(Integer.parseInt(fields[6]))       // s6: MucNhienLieu
                .nhietDo(Float.parseFloat(fields[7]))            // s7: NhietDo
                .doAm(Float.parseFloat(fields[8]))              // s8: DoAm
                .rssi(Integer.parseInt(fields[9]))              // s9: RSSI
                .rtcBoard(fields[10])                          // s10: RTC_Board
                .maTaiXe(fields[11])                            // s11: MaTaiXe
                .trangThaiTaiXe(Integer.parseInt(fields[12]))    // s12: TrangThaiLamViec
                .sim(fields[13])                                // s13: SIM
                .sdCard(fields[14])                             // s14: SDCard
                .rtcServer(String.valueOf(LocalDateTime.now()))                // Setting the server time as the current time
                .build();
    }

    @Override
    public SatyDataResponse mapToSatyDataResponse(SatyJourneyData deoCaData) {
        return new SatyDataResponse(
                deoCaData.getCustomID(),
                deoCaData.getPhoneNumber(),
                deoCaData.getKinhDo(),
                deoCaData.getViDo(),
                deoCaData.getTocDo(),
                deoCaData.getAcc(),
                deoCaData.getEx1(),
                deoCaData.getEx2(),
                deoCaData.getMucNhienLieu(),
                deoCaData.getNhietDo(),
                deoCaData.getDoAm(),
                deoCaData.getRssi(),
                deoCaData.getRtcServer(),
                deoCaData.getRtcBoard(),
                deoCaData.getMaTaiXe(),
                deoCaData.getTrangThaiTaiXe(),
                deoCaData.getSim(),
                deoCaData.getSdCard()
        );
    }
}