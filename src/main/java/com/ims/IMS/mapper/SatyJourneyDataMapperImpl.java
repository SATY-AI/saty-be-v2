package com.ims.IMS.mapper;

import com.ims.IMS.api.satyjourney.data.SatyJourneyDataRequest;
import com.ims.IMS.api.satyjourney.device.SatyDeviceInformationRequest;
import com.ims.IMS.model.SatyJourney.SatyDevice;
import com.ims.IMS.model.SatyJourney.SatyJourneyData;
import org.springframework.stereotype.Component;

@Component
public class SatyJourneyDataMapperImpl implements SatyJourneyDataMapper {
    @Override
    public SatyDevice toEntity(SatyDeviceInformationRequest request) {
        return SatyDevice.builder()
                .bienSoXe(request.bienSoXe())
                .Ten_VN(request.Ten_VN())
                .Ten_EN(request.Ten_EN())
                .Ten_JP(request.Ten_JP())
                .Ten_CN(request.Ten_CN())
                .MoTa_VN(request.MoTa_VN())
                .MoTa_EN(request.MoTa_EN())
                .MoTa_JP(request.MoTa_JP())
                .MoTa_CN(request.MoTa_CN())
                .KinhDo(request.KinhDo())
                .ViDo(request.ViDo())
                .ViTri(request.ViTri())
                .build();
    }

    @Override
    public SatyDeviceInformationRequest toRequest(SatyDevice entity) {
        return new SatyDeviceInformationRequest(
                entity.getId(),
                entity.getBienSoXe(),
                entity.getTen_VN(),
                entity.getTen_EN(),
                entity.getTen_JP(),
                entity.getTen_CN(),
                entity.getMoTa_VN(),
                entity.getMoTa_EN(),
                entity.getMoTa_JP(),
                entity.getMoTa_CN(),
                entity.getKinhDo(),
                entity.getViDo(),
                entity.getViTri()
        );
    }
    // Map from SatyJourneyData entity to SatyJourneyDataRequest DTO
    @Override
    public SatyJourneyDataRequest toSatyJourneyDataRequest(SatyJourneyData satyJourneyData) {
        return new SatyJourneyDataRequest(
                satyJourneyData.getCustomID(),
                satyJourneyData.getPhoneNumber(),
                satyJourneyData.getKinhDo(),
                satyJourneyData.getViDo(),
                satyJourneyData.getTocDo(),
                satyJourneyData.getAcc(),
                satyJourneyData.getEx1(),
                satyJourneyData.getEx2(),
                satyJourneyData.getMucNhienLieu(),
                satyJourneyData.getNhietDo(),
                satyJourneyData.getDoAm(),
                satyJourneyData.getRssi(),
                satyJourneyData.getRtcServer(),
                satyJourneyData.getRtcBoard(),
                satyJourneyData.getMaTaiXe(),
                satyJourneyData.getTrangThaiTaiXe(),
                satyJourneyData.getSim(),
                satyJourneyData.getSdCard()
        );
    }

    // Map from SatyJourneyDataRequest DTO to SatyJourneyData entity
    @Override
    public SatyJourneyData toSatyJourneyData(SatyJourneyDataRequest satyJourneyDataRequest) {
        return SatyJourneyData.builder()
                .customID(satyJourneyDataRequest.customID())
                .phoneNumber(satyJourneyDataRequest.phoneNumber())
                .kinhDo(satyJourneyDataRequest.KinhDo())
                .viDo(satyJourneyDataRequest.ViDo())
                .tocDo(satyJourneyDataRequest.TocDo())
                .acc(satyJourneyDataRequest.ACC())
                .ex1(satyJourneyDataRequest.Ex1())
                .ex2(satyJourneyDataRequest.Ex2())
                .mucNhienLieu(satyJourneyDataRequest.MucNhienLieu())
                .nhietDo(satyJourneyDataRequest.NhietDo())
                .doAm(satyJourneyDataRequest.DoAm())
                .rssi(satyJourneyDataRequest.RSSI())
                .rtcServer(satyJourneyDataRequest.RTC_Server())
                .rtcBoard(satyJourneyDataRequest.RTC_Board())
                .maTaiXe(satyJourneyDataRequest.MaTaiXe())
                .trangThaiTaiXe(satyJourneyDataRequest.TrangThaiTaiXe())
                .sim(satyJourneyDataRequest.SIM())
                .sdCard(satyJourneyDataRequest.SDCard())
                .build();
    }
}
