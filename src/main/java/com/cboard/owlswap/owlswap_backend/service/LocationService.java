package com.cboard.owlswap.owlswap_backend.service;

import com.cboard.owlswap.owlswap_backend.dao.LocationDao;
import com.cboard.owlswap.owlswap_backend.exception.DtoMappingException;
import com.cboard.owlswap.owlswap_backend.exception.NotFoundException;
import com.cboard.owlswap.owlswap_backend.model.Dto.LocationDto;
import com.cboard.owlswap.owlswap_backend.model.DtoMapping.LocationMapper;
import com.cboard.owlswap.owlswap_backend.model.Location;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LocationService
{
    @Autowired
    LocationDao dao;
    @Autowired
    LocationMapper locationMapper;


    public List<LocationDto> getAllLocations()
    {
        return dao.findAll()
                .stream()
                .map(loc -> {
                try {
                    return locationMapper.locationToDto(loc);
                } catch (Exception e) {
                    throw new DtoMappingException("Failed to map Location to DTO. locationId=" + loc.getLocationId(), e);
                }
            })
                .toList();

    }
    public LocationDto getLocationById(Integer locationId)
    {
        Location loc = dao.findById(locationId)
                .orElseThrow(() -> new NotFoundException("Location not found. locationId=" + locationId));
        try {
            return locationMapper.locationToDto(loc);
        } catch (Exception e) {
            throw new DtoMappingException("Failed to map Location to DTO. locationId=" + loc.getLocationId(), e);
        }
    }
}
