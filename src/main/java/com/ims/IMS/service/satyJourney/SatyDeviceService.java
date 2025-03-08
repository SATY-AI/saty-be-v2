package com.ims.IMS.service.satyJourney;

import com.ims.IMS.api.satyjourney.device.SatyDeviceInformationResponse;
import com.ims.IMS.model.SatyJourney.SatyDevice;
import com.ims.IMS.repository.Journey.SatyDeviceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class SatyDeviceService {
    private final SatyDeviceRepository satyDeviceRepository;

    @Autowired
    public SatyDeviceService(SatyDeviceRepository satyDeviceRepository) {
        this.satyDeviceRepository = satyDeviceRepository;
    }

    // Get device information
    public Optional<SatyDeviceInformationResponse> getDeviceInformation(String bienSoXe) {
        // Find the device by bienSoXe
        return satyDeviceRepository.findByBienSoXe(bienSoXe)
                .map(device -> new SatyDeviceInformationResponse(
                        device.getBienSoXe(),
                        device.getTen_VN(),
                        device.getTen_EN(),
                        device.getTen_JP(),
                        device.getTen_CN(),
                        device.getMoTa_VN(),
                        device.getMoTa_EN(),
                        device.getMoTa_JP(),
                        device.getMoTa_CN(),
                        device.getKinhDo(),
                        device.getViDo(),
                        device.getViTri()
                ));
    }

    // Create a new SatyDevice
    public SatyDevice createSatyDevice(SatyDevice satyDevice) {
        // Check if bienSoXe is unique
        if (satyDeviceRepository.existsByBienSoXe(satyDevice.getBienSoXe())) {
            throw new IllegalArgumentException("BienSoXe already exists");
        }
        return satyDeviceRepository.save(satyDevice);
    }

    // Get all SatyDevices
    public List<SatyDevice> getAllSatyDevices() {
        return satyDeviceRepository.findAll();
    }

    // Get a SatyDevice by ID
    public Optional<SatyDevice> getSatyDeviceById(Integer id) {
        return satyDeviceRepository.findById(id);
    }

    // Update an existing SatyDevice
    public Optional<SatyDevice> updateSatyDevice(Integer id, SatyDevice updatedDevice) {
        return satyDeviceRepository.findById(id).map(existingDevice -> {
            existingDevice.setBienSoXe(updatedDevice.getBienSoXe());
            existingDevice.setTen_VN(updatedDevice.getTen_VN());
            existingDevice.setTen_EN(updatedDevice.getTen_EN());
            existingDevice.setTen_JP(updatedDevice.getTen_JP());
            existingDevice.setTen_CN(updatedDevice.getTen_CN());
            existingDevice.setMoTa_VN(updatedDevice.getMoTa_VN());
            existingDevice.setMoTa_EN(updatedDevice.getMoTa_EN());
            existingDevice.setMoTa_JP(updatedDevice.getMoTa_JP());
            existingDevice.setMoTa_CN(updatedDevice.getMoTa_CN());
            existingDevice.setKinhDo(updatedDevice.getKinhDo());
            existingDevice.setViDo(updatedDevice.getViDo());
            existingDevice.setViTri(updatedDevice.getViTri());
            return satyDeviceRepository.save(existingDevice);
        });
    }

    // Delete a SatyDevice by ID
    public boolean deleteSatyDeviceById(Integer id) {
        if (satyDeviceRepository.existsById(id)) {
            satyDeviceRepository.deleteById(id);
            return true;
        } else {
            return false;
        }
    }
}
