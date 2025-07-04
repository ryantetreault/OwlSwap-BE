package com.cboard.marketplace.marketplace_backend.model.Dto;

import jakarta.validation.constraints.NotNull;

import java.util.LinkedHashMap;
import java.util.Map;

public class RequestDto extends ItemDto
{
    @NotNull(message = "Deadline is required...")
    private String deadline;

    public RequestDto() {
    }



    public RequestDto(int itemId, String name, String description, Double price, int userId, String category, String releaseDate, boolean available, String location, String itemType, String image_name, String image_type, byte[] image_date, String deadline)
    {
        super(itemId, name, description, price, userId, category, releaseDate, available, location, itemType, image_name, image_type, image_date);
        this.deadline = deadline;
    }

    @Override
    public Map<String, String> getSpecificFields() {
        Map<String, String> fields = new LinkedHashMap<>();
        fields.put("Deadline", deadline);
        return fields;
    }

    @Override
    public void setSpecificFields(Map<String, String> fields)
    {
        if (fields.containsKey("Deadline"))
        {
            this.deadline = fields.get("Deadline");
        }
    }


    public String getDeadline() {
        return deadline;
    }

    public void setDeadline(String deadline) {
        this.deadline = deadline;
    }
}
