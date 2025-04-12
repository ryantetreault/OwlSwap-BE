package com.cboard.marketplace.marketplace_backend.model;

import jakarta.persistence.Entity;
import com.cboard.marketplace.marketplace_common.*;

import jakarta.persistence.*;


@Entity
@Table(name = "service")
//@DiscriminatorValue("service")
public class Service extends Item
{
    private int durationMinutes;

    public Service() {
    }

    public Service(int itemId, String name, String description, double price, Category category, String releaseDate, boolean available, Location location, String itemType, String image_name, String image_type, byte[] image_date, int durationMinutes)
    {
        super(itemId, name, description, price, category, releaseDate, available, location, itemType, image_name, image_type, image_date);
        this.durationMinutes = durationMinutes;
    }

    public int getDurationMinutes() {
        return durationMinutes;
    }

    public void setDurationMinutes(int durationMinutes) {
        this.durationMinutes = durationMinutes;
    }

}