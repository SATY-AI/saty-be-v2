package com.ims.IMS.service;


import com.ims.IMS.model.imsprocessing.Device;
import com.ims.IMS.repository.DeviceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DeviceService {

    @Autowired
    private DeviceRepository deviceRepository;

    public List<Device> getDevicesByLocationID(String locationID) {
        return deviceRepository.findByLocationID(locationID);
    }
}
