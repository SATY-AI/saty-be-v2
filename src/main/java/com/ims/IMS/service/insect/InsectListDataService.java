package com.ims.IMS.service.insect;

import com.ims.IMS.api.InsectListDataResponse;
import com.ims.IMS.mapper.ImsMapping;
import com.ims.IMS.model.insect.InsectListData;
import com.ims.IMS.repository.InsectListDataRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class InsectListDataService {
    private final InsectListDataRepository insectListDataRepository;
    private final ImsMapping imsMapping;

    public List<InsectListDataResponse> getAllInsects() {
        // Fetch all insect data and map to the response DTO
        List<InsectListData> insectList = insectListDataRepository.findAll();
        return imsMapping.toResponseList(insectList);
    }
}
