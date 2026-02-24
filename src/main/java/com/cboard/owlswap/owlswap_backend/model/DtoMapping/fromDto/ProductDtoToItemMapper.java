package com.cboard.owlswap.owlswap_backend.model.DtoMapping.fromDto;

import com.cboard.owlswap.owlswap_backend.dao.LocationDao;
import com.cboard.owlswap.owlswap_backend.dao.UserDao;
import com.cboard.owlswap.owlswap_backend.exception.NotFoundException;
import com.cboard.owlswap.owlswap_backend.model.*;
import com.cboard.owlswap.owlswap_backend.model.Dto.ItemImageDto;
import com.cboard.owlswap.owlswap_backend.security.CurrentUser;
import com.cboard.owlswap.owlswap_backend.service.CategoryService;
import com.cboard.owlswap.owlswap_backend.service.LocationService;
import com.cboard.owlswap.owlswap_backend.service.UserService;
import com.cboard.owlswap.owlswap_backend.model.Dto.ProductDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class ProductDtoToItemMapper implements DtoToItemMapper<ProductDto>
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
    public Item fromDto(ProductDto dto)
    {
        Product p = new Product(
                dto.getItemId(),
                dto.getName(),
                dto.getDescription(),
                dto.getPrice(),
                userDao.findById(dto.getUserId())
                        .orElseThrow(() -> new NotFoundException("User not found.")),
                catService.findByName(dto.getCategory()),
                dto.getReleaseDate(),
                dto.isAvailable(),
                locDao.findById(dto.getLocationId())
                        .orElseThrow(() -> new NotFoundException("Location not found.")),
                dto.getItemType(),
                new ArrayList<>(),
                /*dto.getImage_name(),
                dto.getImage_type(),
                dto.getImage_date(),*/
                dto.getQuantity(),
                dto.getBrand()
        );
        List<ItemImage> images = new ArrayList<>();
        for(ItemImageDto img : dto.getImages())
            images.add(imageMapper.fromDto(img));
        p.setImages(images);
        return p;
    }

    @Override
    public Class<ProductDto> getDtoClass()
    {
        return ProductDto.class;
    }
}
