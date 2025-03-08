package com.ims.IMS.mapper;

import com.ims.IMS.api.fertilizer.response.GetAllFertilizerResponse;
import com.ims.IMS.api.plant.GetAllPlantDataResponseID;
import com.ims.IMS.api.plant.GetPlantDataResponse;
import com.ims.IMS.model.fertilizerProduct.Fertilizer;
import com.ims.IMS.model.plantProduct.Plant;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;
import java.util.List;
import java.util.stream.Collectors;

@Component
@Primary
public class ProductAgriMappingImpl implements ProductAgriMapping {

    @Override
    public GetAllFertilizerResponse toGetAllFertilizerResponse(Fertilizer fertilizer) {
        return new GetAllFertilizerResponse(
                fertilizer.getId(),
                fertilizer.getName(),
                fertilizer.getUniqueCode(),
                fertilizer.getPricePerKg(),
                fertilizer.getStockQuantity(),
                fertilizer.getDescription(),
                fertilizer.getManufacturer()
        );
    }

    @Override
    public List<GetAllFertilizerResponse> toGetAllFertilizerResponseList(List<Fertilizer> fertilizers) {
        return fertilizers.stream()
                .map(this::toGetAllFertilizerResponse)
                .collect(Collectors.toList());
    }

    @Override
    public GetPlantDataResponse toGetPlantDataResponse(Plant plant) {
        return new GetPlantDataResponse(
                plant.getName(),
                plant.getType(),
                plant.getHeight(),
                plant.getPrice(),
                plant.getFertilizerRecommendations()
        );
    }

    @Override
    public GetAllPlantDataResponseID toGetPlantDataResponseID(Plant plant) {
        return new GetAllPlantDataResponseID(
                plant.getId(),
                plant.getName(),
                plant.getType(),
                plant.getHeight(),
                plant.getPrice(),
                plant.getFertilizerRecommendations()
        );
    }


    @Override
    public List<GetPlantDataResponse> toGetPlantDataResponseList(List<Plant> plants) {
        return plants.stream()
                .map(this::toGetPlantDataResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<GetAllPlantDataResponseID> toGetPlantDataResponseListID(List<Plant> plants) {
        return plants.stream()
                .map(this::toGetPlantDataResponseID)
                .collect(Collectors.toList());
    }

}
