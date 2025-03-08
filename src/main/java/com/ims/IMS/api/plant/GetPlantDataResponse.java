package com.ims.IMS.api.plant;

public record GetPlantDataResponse(
    String name,
    String type,
    double height,
    Double price, // Price of the plant in dollars
    String fertilizerRecommendations // Suggested fertilizers
) {}
