package com.ims.IMS.service.warehouse;

import com.ims.IMS.api.warehouse.*;
import com.ims.IMS.exception.*;
import com.ims.IMS.mapper.SatyRiceWarehouseMapping;
import com.ims.IMS.model.warehouse.WareHouseData;
import com.ims.IMS.repository.Rice.RiceProductRepository;
import com.ims.IMS.repository.WareHouse.WareHouseDataRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class WareHouseDataService {
    private final WareHouseDataRepository wareHouseDataRepository;
    private final SatyRiceWarehouseMapping satyRiceWarehouseMapping;
    private final RiceProductRepository riceProductRepository;

    public WareHouseDataResponse getWarehouseById(Integer id) {
        WareHouseData wareHouseData = wareHouseDataRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Warehouse not found"));
        return satyRiceWarehouseMapping.toWareHouseDataResponse(wareHouseData);
    }

    public WareHouseDataResponse createWarehouse(WareHouseDataRegisterRequest request) {
        if (request.uniqueCodeWareHouse() == null || request.uniqueCodeWareHouse().isEmpty()) {
            throw new WarehouseValidationException("Unique code warehouse must not be empty");
        }

        boolean existsByUniqueCodeProductinRiceProduct = riceProductRepository.existsByUniqueCode(request.uniqueCodeProduct());

        boolean exists = wareHouseDataRepository.existsByUniqueCodeWareHouse(request.uniqueCodeWareHouse());
        boolean existsByUniqueCode = wareHouseDataRepository.existsByUniqueCodeProduct(request.uniqueCodeProduct());

        if (!existsByUniqueCodeProductinRiceProduct) {
            throw new RiceProductValidationException("Unique Code Product " + request.uniqueCodeProduct() + " does not exist in Rice Product Table");
        }

        boolean existsByBoth = wareHouseDataRepository.existsByUniqueCodeWareHouseAndUniqueCodeProduct(
                request.uniqueCodeWareHouse(), request.uniqueCodeProduct()
        );

        if (existsByBoth) {
            throw new CodeProductValidationException("Code Product " + request.uniqueCodeProduct() +
                    " and Code Warehouse " + request.uniqueCodeWareHouse() + " already exist in the same record");
        }

        try {
            WareHouseData wareHouseData = satyRiceWarehouseMapping.toWareHouseData(request);
            wareHouseDataRepository.save(wareHouseData);
            return satyRiceWarehouseMapping.toWareHouseDataResponse(wareHouseData);
        } catch (Exception e) {
            throw new WarehouseProcessingException("An unexpected error occurred while creating the warehouse", e);
        }
    }

    public WareHouseDetailResponse detailWareHouse(WareHouseDetailRequest request) {
        if (request.wareHouseCode() == null || request.wareHouseCode().isEmpty()) {
            throw new WarehouseValidationException("Unique Code WareHouse must not be empty");
        }

        // Fetch all warehouse data that match the given unique code
        List<WareHouseData> wareHouseDataList = wareHouseDataRepository.findAllByUniqueCodeWareHouse(request.wareHouseCode());

        if (wareHouseDataList.isEmpty()) {
            throw new WarehouseNotFoundException("Warehouse with the given unique code does not exist");
        }

        // Select the first warehouse or apply filtering logic
        WareHouseData selectedWarehouse = wareHouseDataList.get(0);

        return satyRiceWarehouseMapping.toWareHouseDetailResponse(selectedWarehouse);
    }

    public WareHouseDataUpdateResponse updateWarehouse(String uniqueCodeWarehouse, WareHouseDataUpdateRequest request) {
        List<WareHouseData> wareHouseDataList = wareHouseDataRepository.findAllByUniqueCodeWareHouse(uniqueCodeWarehouse);
        if (wareHouseDataList.isEmpty()) {
            throw new RuntimeException("Warehouse not found");
        }
        // Choose the first warehouse or add selection logic
        WareHouseData selectedWarehouse = wareHouseDataList.get(0);
        satyRiceWarehouseMapping.updateWareHouseDataFromRequest(request, selectedWarehouse);
        wareHouseDataRepository.save(selectedWarehouse);
        return satyRiceWarehouseMapping.toWareHouseDataUpdateResponse(selectedWarehouse, "Warehouse updated successfully");
    }

    public List<WareHouseDataResponse> getAllWarehouses(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<WareHouseData> warehousesPage = wareHouseDataRepository.findAll(pageable);
        return warehousesPage.getContent().stream()
                .map(satyRiceWarehouseMapping::toWareHouseDataResponse)
                .collect(Collectors.toList());
    }

    public void deleteWarehouse(String uniqueCodeWarehouse) {
        List<WareHouseData> wareHouseDataList = wareHouseDataRepository.findAllByUniqueCodeWareHouse(uniqueCodeWarehouse);
        if (wareHouseDataList.isEmpty()) {
            throw new RuntimeException("Warehouse not found");
        }
        // Delete all warehouses with the given unique code
        wareHouseDataRepository.deleteAll(wareHouseDataList);
    }
}
