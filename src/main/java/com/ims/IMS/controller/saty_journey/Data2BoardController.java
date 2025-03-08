package com.ims.IMS.controller.saty_journey;

import com.ims.IMS.api.satyjourney.data.BoardSendToServerRequest;
import com.ims.IMS.api.satyjourney.data.BoardSendToServerResponse;
import com.ims.IMS.api.satyjourney.data.Data2DeviceResponse;
import com.ims.IMS.api.satyjourney.data.HistoryLoginBoardResponse;
import com.ims.IMS.api.satyjourney.data.MobileHistoryLoginRequest;
import com.ims.IMS.api.satyjourney.data.MobileHistoryLoginResponse;
import com.ims.IMS.api.satyjourney.data.MobileSendToServerRequest;
import com.ims.IMS.api.satyjourney.data.MobileSendToServerResponse;
import com.ims.IMS.lib.api.ResponseApi;
import com.ims.IMS.model.SatyJourney.Data2Board;
import com.ims.IMS.model.SatyJourney.HistoryBoardLogin;
import com.ims.IMS.repository.Journey.Data2BoardRepository;
import com.ims.IMS.repository.Journey.HistoryBoardLoginRepository;
import com.ims.IMS.swagger.HistoryLoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/journey")
public class Data2BoardController {
    @Autowired
    private Data2BoardRepository data2BoardRepository;
    @Autowired
    private HistoryBoardLoginRepository historyBoardLoginRepository;
    @Autowired
    private HistoryLoginService historyLoginService;

    @GetMapping("/data-from-board")
    public ResponseApi<Data2DeviceResponse> getDataFromBoard(
            @RequestParam String CheDo,
            @RequestParam String key) {
        // Check if CheDo is equal to 1
        if (!"1".equals(CheDo)) {
            // Return error message if CheDo is not 1
            return ResponseApi.error("02", "Invalid CheDo value: " + CheDo + ". CheDo must be 1.");
        }
        // Find data by customID
        Optional<Data2Board> data2BoardOptional = data2BoardRepository.findByCustomID(key);
        // Check if the data exists
        if (data2BoardOptional.isPresent()) {
            Data2Board data2Board = data2BoardOptional.get();
            // Map Data2Board to Data2DeviceResponse
            Data2DeviceResponse response = new Data2DeviceResponse(data2Board.getCustomID(), data2Board.getS());
            // Return success response
            return ResponseApi.success(response);
        } else {
            // Return error response if not found
            return ResponseApi.error("01", "Data not found for customID: " + key);
        }
    }

    @PostMapping("/mobile-2-server")
    public ResponseApi<MobileSendToServerResponse> sendData2Server(
            @RequestBody MobileSendToServerRequest mobileSendToServerRequest) {
        try {
            // Find the data by customID (ID from the request)
            Optional<Data2Board> data2BoardOptional = data2BoardRepository.findByCustomID(mobileSendToServerRequest.ID());
            // Check if the data exists
            if (data2BoardOptional.isPresent()) {
                Data2Board data2Board = data2BoardOptional.get();
                // Update the S field
                data2Board.setS(mobileSendToServerRequest.S());
                // Save the updated entity back to the database
                data2BoardRepository.save(data2Board);
                // Prepare the success response
                MobileSendToServerResponse response = new MobileSendToServerResponse("Data successfully updated", data2Board.getCustomID(), data2Board.getS());
                return ResponseApi.success(response);
            } else {
                // Return an error if the customID (ID) is not found
                return ResponseApi.error("01", "Data not found for ID: " + mobileSendToServerRequest.ID());
            }
        } catch (Exception e) {
            // Handle exceptions and return an error response
            return ResponseApi.error("02", "Failed to update data: " + e.getMessage());
        }
    }

    @PostMapping("/board-2-server")
    public ResponseApi<BoardSendToServerResponse> boardSendToServerResponseResponseApi(
            @RequestBody BoardSendToServerRequest boardSendToServerRequest) {
        try {
            // Extract customID and S from the request
            String customID = boardSendToServerRequest.ID();
            String S = boardSendToServerRequest.S();

            // Split the S string by semicolons
            String[] sParts = S.split(";");

            // Check if the string has at least 4 parts
            if (sParts.length < 4) {
                return ResponseApi.error("01", "Invalid S format, it should contain 4 parts separated by ';'");
            }

            // Extract the individual values
            String statusOfChecking = sParts[0];    // e.g., "61"
            String maTheNFC = sParts[1];            // e.g., "MaTheNFC"
            String dateTimeStr = sParts[2];         // e.g., "2024-09-27T09:00:55.556316"
            String statusWorking = sParts[3];       // e.g., "1"

            // Find the Data2Board record by customID
            Optional<Data2Board> data2BoardOptional = data2BoardRepository.findByCustomID(customID);
            if (data2BoardOptional.isPresent()) {
                Data2Board data2Board = data2BoardOptional.get();
                // Update MaTheNFC, dateTimeFromBoard, and statusWorking
                data2Board.setMaTheNFC(maTheNFC);
                data2Board.setDateTimeFromBoard(dateTimeStr);
                data2Board.setStatusWorking(statusWorking);
                // Update the first element in the string S to "0" and preserve the rest
                String updatedS = "0;" + sParts[1];
                data2Board.setS(updatedS);
                // Save the updated entity to the database
                data2BoardRepository.save(data2Board);
                // Create the response object
                BoardSendToServerResponse response = new BoardSendToServerResponse(statusOfChecking,
                        maTheNFC, String.valueOf(dateTimeStr), statusWorking);
                return ResponseApi.success(response);
            } else {
                // Return error if the record is not found
                return ResponseApi.error("02", "Data not found for customID: " + customID);
            }
        } catch (Exception e) {
            // Handle exceptions and return an error response
            return ResponseApi.error("03", "Failed to update data: " + e.getMessage());
        }
    }

    @GetMapping("/history-board-login")
    public ResponseApi<List<HistoryLoginBoardResponse>> historyBoardLogin(
            @RequestParam String CheDo,
            @RequestParam String MaTaiXe,
            @RequestParam String BienSoXe) {

        // Check if CheDo is equal to 1
        if (!"1".equals(CheDo)) {
            // Return error message if CheDo is not 1
            return ResponseApi.error("00", "Invalid CheDo value: " + CheDo + ". CheDo must be Valid.");
        }
        // Fetch data based on MaTaiXe and BienSoXe
        List<HistoryBoardLogin> historyBoardLogins = historyBoardLoginRepository.findByMaTaiXeAndBienSoXe(MaTaiXe, BienSoXe);

        // Check if data exists
        if (historyBoardLogins.isEmpty()) {
            // Return error response if no data found
            return ResponseApi.error("01", "No history found for MaTaiXe: " + MaTaiXe + " and BienSoXe: " + BienSoXe);
        }

        // Convert list of HistoryBoardLogin to HistoryLoginBoardResponse
        List<HistoryLoginBoardResponse> responseList = historyBoardLogins.stream()
                .map(login -> new HistoryLoginBoardResponse(
                        login.getMaTaiXe(),
                        login.getBienSoXe(),
                        login.getNgayGioDangNhap(),
                        login.getNgayGioDangXuat(),
                        login.getToaDoDangNhap(),
                        login.getToaDoDangXuat()
                ))
                .collect(Collectors.toList());

        // Return success response with the mapped data
        return ResponseApi.success(responseList);
    }

    @PostMapping("/mobile-save-data")
    public ResponseApi<MobileHistoryLoginResponse> saveHistoryLogin(@RequestBody MobileHistoryLoginRequest request) {
        String result = historyLoginService.saveOrUpdateHistory(request);
        return ResponseApi.success(new MobileHistoryLoginResponse(result));
    }
}
