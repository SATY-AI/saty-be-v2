package com.ims.IMS.swagger;

import com.ims.IMS.api.satyjourney.data.MobileHistoryLoginRequest;
import com.ims.IMS.model.SatyJourney.HistoryBoardLogin;
import com.ims.IMS.model.SatyJourney.SatyDevice;
import com.ims.IMS.model.SatyJourney.SatyJourneyData;
import com.ims.IMS.repository.Journey.HistoryBoardLoginRepository;
import com.ims.IMS.repository.Journey.SatyDeviceRepository;
import com.ims.IMS.repository.Journey.SatyJourneyDataRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class HistoryLoginService {
    private final HistoryBoardLoginRepository historyBoardLoginRepository;
    @Autowired
    private SatyDeviceRepository satyDeviceRepository;
    @Autowired
    private SatyJourneyDataRepository satyJourneyDataRepository;

    @Autowired
    public HistoryLoginService(HistoryBoardLoginRepository historyBoardLoginRepository) {
        this.historyBoardLoginRepository = historyBoardLoginRepository;
    }

    public String saveOrUpdateHistory(MobileHistoryLoginRequest request) {
        // Check if BienSoXe exists in SatyDevice table
        Optional<SatyDevice> satyDeviceOptional = satyDeviceRepository.findByBienSoXe(request.BienSoXe());
        if (satyDeviceOptional.isEmpty()) {
            return "BienSoXe not found in the SatyDevice table!";
        }

        // Check if MaTaiXe exists in SatyJourneyData table
        Optional<SatyJourneyData> satyJourneyDataOptional = satyJourneyDataRepository.findByMaTaiXe(request.MaTaiXe());
        if (satyJourneyDataOptional.isEmpty()) {
            return "MaTaiXe not found in the SatyJourneyData table!";
        }

        if ("1".equals(request.CODE())) {
            // Insert new record
            HistoryBoardLogin newHistory = HistoryBoardLogin.builder()
                    .maTaiXe(request.MaTaiXe())
                    .bienSoXe(request.BienSoXe())
                    .ngayGioDangNhap(request.NgayGioDangNhap()) // Set the parsed ZonedDateTime
                    .toaDoDangNhap(request.ToaDoDangNhap())
                    .build();
            historyBoardLoginRepository.save(newHistory);
            return "Data Login Update successfully!";
        } else if ("2".equals(request.CODE())) {
            // Update existing record
            List<HistoryBoardLogin> existingRecords = historyBoardLoginRepository
                    .findByMaTaiXeAndBienSoXe(request.MaTaiXe(), request.BienSoXe());
            if (!existingRecords.isEmpty()) {
                HistoryBoardLogin existingRecord = existingRecords.get(0); // Get the first match
                existingRecord.setNgayGioDangXuat(request.NgayGioDangXuat());
                existingRecord.setToaDoDangXuat(request.ToaDoDangXuat());
                historyBoardLoginRepository.save(existingRecord);
                return "Data Logout Update successfully!";
            } else {
                return "Data not found for update!";
            }
        }
        return "Invalid CODE!";
    }
}
