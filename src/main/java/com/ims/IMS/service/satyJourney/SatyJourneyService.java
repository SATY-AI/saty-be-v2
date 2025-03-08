package com.ims.IMS.service.satyJourney;

import com.ims.IMS.model.SatyJourney.SatyJourneyData;
import com.ims.IMS.repository.Journey.SatyJourneyDataRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class SatyJourneyService {

    @Autowired
    private SatyJourneyDataRepository satyJourneyDataRepository;
    @Autowired
    private final JdbcTemplate jdbcTemplate;

    public List<SatyJourneyData> getDeoCaDataByCustomIdAndPhoneNumber(String customID, String phoneNumber) {
        return satyJourneyDataRepository.findByCustomIDAndPhoneNumber(customID, phoneNumber);
    }

    // New method to find SatyJourneyData by phone number
    public SatyJourneyData findByPhoneNumber(String phoneNumber) {
        List<SatyJourneyData> results = satyJourneyDataRepository.findByPhoneNumber(phoneNumber);
        return results.isEmpty() ? null : results.get(0); // Return the first result or null if not found
    }

    public boolean hasCustomID(String customID) {
        List<SatyJourneyData> resultsCustomID = satyJourneyDataRepository.findByCustomID(customID);
        return !resultsCustomID.isEmpty();
    }

    public boolean hasPhoneNumber(String phoneNumber) {
        List<SatyJourneyData> resultsPhoneNumber = satyJourneyDataRepository.findByPhoneNumber(phoneNumber);
        return !resultsPhoneNumber.isEmpty();
    }

    // Method to save DeoCaData
    public SatyJourneyData saveDeoCaData(SatyJourneyData deoCaData) {
        return satyJourneyDataRepository.save(deoCaData);
    }

    public List<SatyJourneyData> getAllDeoCaData() {
        // Fetch all DeoCaData entries from the database
        return satyJourneyDataRepository.findAll();
    }

    public SatyJourneyService(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<SatyJourneyData> getFilteredDeoCaData(String phoneNumber, String customID, Float kinhDo, Float viDo,
                                                      Float tocDo, Integer acc, Integer ex1, Integer ex2,
                                                      Integer mucNhienLieu, Float nhietDo, Float doAm,
                                                      Integer rssi, String rtcServer, String maTaiXe,
                                                      Integer trangThaiTaiXe, String sim, String sdCard, int pageSize, int pageOffset) {
        StringBuilder sql = new StringBuilder("SELECT * FROM saty_du_lieu WHERE 1=1");
        List<Object> params = new ArrayList<>();

        // Dynamically add conditions based on non-null parameters
        if (phoneNumber != null) {
            sql.append(" AND phone_number = ?");
            params.add(phoneNumber);
        }
        if (customID != null) {
            sql.append(" AND customID = ?");
            params.add(customID);
        }
        if (kinhDo != null) {
            sql.append(" AND kinh_do = ?");
            params.add(kinhDo);
        }
        if (viDo != null) {
            sql.append(" AND vi_do = ?");
            params.add(viDo);
        }
        if (tocDo != null) {
            sql.append(" AND toc_do = ?");
            params.add(tocDo);
        }
        if (acc != null) {
            sql.append(" AND acc = ?");
            params.add(acc);
        }
        if (ex1 != null) {
            sql.append(" AND ex1 = ?");
            params.add(ex1);
        }
        if (ex2 != null) {
            sql.append(" AND ex2 = ?");
            params.add(ex2);
        }
        if (mucNhienLieu != null) {
            sql.append(" AND muc_nhien_lieu = ?");
            params.add(mucNhienLieu);
        }
        if (nhietDo != null) {
            sql.append(" AND nhiet_do = ?");
            params.add(nhietDo);
        }
        if (doAm != null) {
            sql.append(" AND do_am = ?");
            params.add(doAm);
        }
        if (rssi != null) {
            sql.append(" AND rssi = ?");
            params.add(rssi);
        }
        if (rtcServer != null) {
            sql.append(" AND rtc_server = ?");
            params.add(rtcServer);
        }
        if (maTaiXe != null) {
            sql.append(" AND ma_tai_xe = ?");
            params.add(maTaiXe);
        }
        if (trangThaiTaiXe != null) {
            sql.append(" AND trang_thai_tai_xe = ?");
            params.add(trangThaiTaiXe);
        }
        if (sim != null) {
            sql.append(" AND sim = ?");
            params.add(sim);
        }
        if (sdCard != null) {
            sql.append(" AND sd_card = ?");
            params.add(sdCard);
        }

        // Apply pagination
        sql.append(" LIMIT ? OFFSET ?");
        params.add(pageSize);
        params.add(pageOffset);

        // Execute the query
        return jdbcTemplate.query(sql.toString(), params.toArray(), (rs, rowNum) -> {
            // Map ResultSet to DeoCaData object
            SatyJourneyData data = new SatyJourneyData();
            data.setCustomID(rs.getString("customID"));
            data.setPhoneNumber(rs.getString("phone_number"));
            data.setKinhDo(rs.getFloat("kinh_do"));
            data.setViDo(rs.getFloat("vi_do"));
            data.setTocDo(rs.getFloat("toc_do"));
            data.setAcc(rs.getInt("acc"));
            data.setEx1(rs.getInt("ex1"));
            data.setEx2(rs.getInt("ex2"));
            data.setMucNhienLieu(rs.getInt("muc_nhien_lieu"));
            data.setNhietDo(rs.getFloat("nhiet_do"));
            data.setDoAm(rs.getFloat("do_am"));
            data.setRssi(rs.getInt("rssi"));
            data.setRtcServer(rs.getString("rtc_server"));
            data.setRtcBoard(rs.getString("rtc_board"));
            data.setMaTaiXe(rs.getString("ma_tai_xe"));
            data.setTrangThaiTaiXe(rs.getInt("trang_thai_tai_xe"));
            data.setSim(rs.getString("sim"));
            data.setSdCard(rs.getString("sd_card"));
            return data;
        });
    }



}
