package com.ims.IMS.api.plant;

public record GetAllPlantDataResponseID(
        Integer id,
        String name,
        String type,
        double height,
        Double price, // Price of the plant in dollars
        String fertilizerRecommendations // Suggested fertilizers
) {}