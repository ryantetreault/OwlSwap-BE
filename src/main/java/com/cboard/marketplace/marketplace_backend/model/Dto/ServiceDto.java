package com.cboard.marketplace.marketplace_backend.model.Dto;

import jakarta.validation.constraints.NotNull;

import java.util.LinkedHashMap;
import java.util.Map;

public class ServiceDto extends ItemDto
{
    @NotNull(message = "Duration is required...")
    private Integer durationMinutes;

    public ServiceDto() {
    }

    public ServiceDto(int itemId, String name, String description, Double price, int userId, String category, String releaseDate, boolean available, String location, String itemType, String image_name, String image_type, byte[] image_date, Integer durationMinutes)
    {
        super(itemId, name, description, price, userId, category, releaseDate, available, location, itemType, image_name, image_type, image_date);
        this.durationMinutes = durationMinutes;
    }

    @Override
    public Map<String, String> getSpecificFields() {
        Map<String, String> fields = new LinkedHashMap<>();
        fields.put("Duration [minutes]", String.valueOf(durationMinutes));
        return fields;
    }

    @Override
    public void setSpecificFields(Map<String, String> fields)
    {
        if (fields.containsKey("Duration [minutes]")) {
            try {
                this.durationMinutes = Integer.parseInt(fields.get("Duration [minutes]"));
            } catch (NumberFormatException e) {
                this.durationMinutes = 0;
            }
        }
    }

    public int getDurationMinutes()
    {
        return durationMinutes;
    }

    public void setDurationMinutes(int durationMinutes)
    {
        this.durationMinutes = durationMinutes;
    }
}
