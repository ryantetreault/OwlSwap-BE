package com.cboard.marketplace.marketplace_backend.service;

import com.cboard.marketplace.marketplace_backend.dao.ItemDao;

import com.cboard.marketplace.marketplace_backend.model.DtoMapping.fromDto.DtoToItemFactory;
import com.cboard.marketplace.marketplace_backend.model.DtoMapping.toDto.ItemToDtoFactory;
import com.cboard.marketplace.marketplace_backend.model.Item;
import com.cboard.marketplace.marketplace_common.ItemDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ItemService
{
    @Autowired
    ItemDao dao;
    private final ItemToDtoFactory toDtoFactory;
    private final DtoToItemFactory fromDtoFactory;

    //this automatically adds all the different dto mappers through injection
    public ItemService(ItemDao dao, ItemToDtoFactory toDtoFactory, DtoToItemFactory fromDtoFactory)
    {
        this.dao = dao;
        this.toDtoFactory = toDtoFactory;
        this.fromDtoFactory = fromDtoFactory;
    }

    //ResponseEntity<List<Item>>
    public List<Item> getAllItems()
    {
        return dao.findAll();

        /*try
        {
            return new ResponseEntity<>(dao.findAll(), HttpStatus.OK);
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
        return new ResponseEntity<>(new ArrayList<>(), HttpStatus.BAD_REQUEST);*/
    }

    public List<ItemDto> getAllDtoItems()
    {
        return dao.findAll().stream()
                .map(item -> {
                    try
                    {
                        return toDtoFactory.toDto(item);
                    }
                    catch(IllegalAccessException e)
                    {
                        e.printStackTrace();
                        throw new RuntimeException("Error converting item to DTO: " + item, e);
                    }
                        }
                )
                .toList();

    }

    public ResponseEntity<String> addItem(ItemDto itemDto)
    {
        try
        {
            Item item = fromDtoFactory.fromDto(itemDto);
            dao.save(item);
            return new ResponseEntity<>("Success", HttpStatus.CREATED);
        }
        catch(IllegalAccessException e)
        {
            e.printStackTrace();
            throw new RuntimeException("Error converting DTO to item: " + itemDto, e);
        }

    }
}
