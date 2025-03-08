package com.ims.IMS.mapper;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Converter
public class MapConverter implements AttributeConverter<Map<String, Object>, String> {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public String convertToDatabaseColumn(Map<String, Object> attribute) {
        try {
            // Convert Map to JSON String
            return objectMapper.writeValueAsString(attribute);
        } catch (JsonProcessingException e) {
            // Handle error
            throw new RuntimeException("Failed to convert Map to JSON string.", e);
        }
    }

    @Override
    public Map<String, Object> convertToEntityAttribute(String dbData) {
        try {
            // Convert JSON String back to Map
            return objectMapper.readValue(dbData, HashMap.class);
        } catch (IOException e) {
            // Handle error
            throw new RuntimeException("Failed to convert JSON string to Map.", e);
        }
    }
}
