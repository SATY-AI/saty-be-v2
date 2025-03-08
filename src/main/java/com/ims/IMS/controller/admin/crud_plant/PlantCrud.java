package com.ims.IMS.controller.admin.crud_plant;


import com.ims.IMS.api.plant.AddPlantRequest;
import com.ims.IMS.api.plant.GetAllPlantDataResponseID;
import com.ims.IMS.api.plant.GetPlantDataResponse;
import com.ims.IMS.lib.api.ResponseApi;
import com.ims.IMS.mapper.ProductAgriMappingImpl;
import com.ims.IMS.model.groupuser.Admin;
import com.ims.IMS.model.plantProduct.Plant;
import com.ims.IMS.repository.PlantRepository;
import com.ims.IMS.service.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/ims/admin/plant")
public class PlantCrud {

    @Autowired
    private PlantRepository plantRepository;
    @Autowired
    private AdminService adminService;
    @Autowired
    private ProductAgriMappingImpl agriMapping;

    // GET request to retrieve all plants
    @GetMapping("/getAllPlant")
    public ResponseApi<List<GetAllPlantDataResponseID>> getAllPlants(@RequestHeader("Authorization") String token) {
        Admin adminData = adminService.validateTokenAndGetAdmin(token);
        List<Plant> plants = plantRepository.findAll();
        return ResponseApi.success(agriMapping.toGetPlantDataResponseListID(plants));
    }

    @GetMapping("/get-plant-id/{id}")
    public ResponseApi<GetPlantDataResponse> getPlantById(@RequestHeader("Authorization") String token, @PathVariable Integer id) {
        Admin adminData = adminService.validateTokenAndGetAdmin(token);  // Validate token and get admin
        Plant plant = plantRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Plant not found with ID: " + id));  // Handle case when plant is not found
        return ResponseApi.success(agriMapping.toGetPlantDataResponse(plant));  // Map and return the response
    }

    // POST request to add a new plant
    @PostMapping("/add-plant")
    public ResponseApi<GetPlantDataResponse> addPlant(@RequestHeader("Authorization") String token,
                                                      @RequestBody AddPlantRequest addPlantRequest) {
        Admin adminData = adminService.validateTokenAndGetAdmin(token);  // Validate the token
        Plant plant = Plant.builder()
                .name(addPlantRequest.name())
                .type(addPlantRequest.type())
                .height(addPlantRequest.height())
                .build();  // Create a new Plant entity from the request
        Plant savedPlant = plantRepository.save(plant);  // Save the plant in the database
        return ResponseApi.success(agriMapping.toGetPlantDataResponse(savedPlant));  // Return the saved plant data as a response
    }

    @PutMapping("/update-plant/{id}")
    public ResponseApi<GetPlantDataResponse> updatePlant(@RequestHeader("Authorization") String token,
                                                         @PathVariable Integer id,
                                                         @RequestBody AddPlantRequest updatePlantRequest) {
        Admin adminData = adminService.validateTokenAndGetAdmin(token);  // Validate the token
        Plant existingPlant = plantRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Plant not found with ID: " + id));  // Retrieve the plant or throw exception if not found
        // Update the fields of the existing plant
        existingPlant.setName(updatePlantRequest.name());
        existingPlant.setType(updatePlantRequest.type());
        existingPlant.setHeight(updatePlantRequest.height());
        existingPlant.setFertilizerRecommendations(updatePlantRequest.fertilizerRecommendations());
        existingPlant.setPrice(updatePlantRequest.price());
        Plant updatedPlant = plantRepository.save(existingPlant);  // Save the updated plant
        return ResponseApi.success(agriMapping.toGetPlantDataResponse(updatedPlant));  // Return the updated plant data as a response
    }

    // DELETE request to remove a plant by its ID
    @DeleteMapping("/delete-plant/{id}")
    public ResponseApi<String> deletePlant(@RequestHeader("Authorization") String token, @PathVariable Integer id) {
        Admin adminData = adminService.validateTokenAndGetAdmin(token);  // Validate the token
        Plant plant = plantRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Plant not found with ID: " + id));  // Retrieve the plant or throw exception if not found
        plantRepository.delete(plant);  // Delete the plant
        return ResponseApi.success("Plant deleted successfully with ID: " + id);  // Return success message
    }

}
