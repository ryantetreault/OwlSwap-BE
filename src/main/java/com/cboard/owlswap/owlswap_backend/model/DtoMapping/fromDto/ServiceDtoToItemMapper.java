package com.cboard.owlswap.owlswap_backend.model.DtoMapping.fromDto;

import com.cboard.owlswap.owlswap_backend.dao.LocationDao;
import com.cboard.owlswap.owlswap_backend.dao.UserDao;
import com.cboard.owlswap.owlswap_backend.exception.NotFoundException;
import com.cboard.owlswap.owlswap_backend.model.*;
import com.cboard.owlswap.owlswap_backend.model.Dto.ItemImageDto;
import com.cboard.owlswap.owlswap_backend.model.Dto.ServiceDto;
import com.cboard.owlswap.owlswap_backend.service.CategoryService;
import com.cboard.owlswap.owlswap_backend.service.LocationService;
import com.cboard.owlswap.owlswap_backend.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class ServiceDtoToItemMapper implements DtoToItemMapper<ServiceDto>
{
    @Autowired
    CategoryService catService;
    @Autowired
    LocationService locService;
    @Autowired
    LocationDao locDao;
    @Autowired
    UserService userService;
    @Autowired
    UserDao userDao;
    @Autowired
    DtoToImageMapper imageMapper;

    @Override
    public Item fromDto(ServiceDto dto)
    {
        Service s = new Service(
                dto.getItemId(),
                dto.getName(),
                dto.getDescription(),
                dto.getPrice(),
                userDao.findById(dto.getUserId())
                        .orElseThrow(() -> new NotFoundException("User not found.")),                catService.findByName(dto.getCategory()),
                dto.getReleaseDate(),
                dto.isAvailable(),
                locDao.findById(dto.getLocationId())
                        .orElseThrow(() -> new NotFoundException("Location not found.")),                dto.getItemType(),
                new ArrayList<>(),
                /*dto.getImage_name(),
                dto.getImage_type(),
                dto.getImage_date(),*/
                dto.getDurationMinutes()
        );
        List<ItemImage> images = new ArrayList<>();
        for(ItemImageDto img : dto.getImages())
            images.add(imageMapper.fromDto(img));
        s.setImages(images);
        return s;
    }

    @Override
    public Class<ServiceDto> getDtoClass()
    {
        return ServiceDto.class;
    }
}
