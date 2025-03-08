package com.ims.IMS.mapper;

import com.ims.IMS.api.fertilizer.response.GetAllFertilizerResponse;
import com.ims.IMS.api.plant.GetAllPlantDataResponseID;
import com.ims.IMS.api.plant.GetPlantDataResponse;
import com.ims.IMS.model.fertilizerProduct.Fertilizer;
import com.ims.IMS.model.plantProduct.Plant;

import java.util.List;

public interface ProductAgriMapping {
    // Method to map Fertilizer entity to GetAllFertilizerResponse DTO
    GetAllFertilizerResponse toGetAllFertilizerResponse(Fertilizer fertilizer);
    // Method to map a list of Fertilizer entities to a list of GetAllFertilizerResponse DTOs
    List<GetAllFertilizerResponse> toGetAllFertilizerResponseList(List<Fertilizer> fertilizers);
    // Method to map Plant entity to GetPlantDataResponse DTO
    GetPlantDataResponse toGetPlantDataResponse(Plant plant);
    GetAllPlantDataResponseID toGetPlantDataResponseID(Plant plant);
    // Method to map a list of Plant entities to a list of GetPlantDataResponse DTOs
    List<GetPlantDataResponse> toGetPlantDataResponseList(List<Plant> plants);
    List<GetAllPlantDataResponseID> toGetPlantDataResponseListID(List<Plant> plants);
}
