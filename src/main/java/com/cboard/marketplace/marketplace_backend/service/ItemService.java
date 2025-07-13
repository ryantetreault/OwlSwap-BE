package com.cboard.marketplace.marketplace_backend.service;

import com.cboard.marketplace.marketplace_backend.dao.ItemDao;

import com.cboard.marketplace.marketplace_backend.model.Dto.ItemDto;
import com.cboard.marketplace.marketplace_backend.model.DtoMapping.fromDto.DtoToItemFactory;
import com.cboard.marketplace.marketplace_backend.model.DtoMapping.toDto.ItemToDtoFactory;
import com.cboard.marketplace.marketplace_backend.model.Item;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
public class ItemService
{
    @Autowired
    ItemDao dao;
    @Autowired
    UserService userService;
    private final ItemToDtoFactory toDtoFactory;
    private final DtoToItemFactory fromDtoFactory;

    //this automatically adds all the different dto mappers through injection
    public ItemService(ItemDao dao, ItemToDtoFactory toDtoFactory, DtoToItemFactory fromDtoFactory)
    {
        this.dao = dao;
        this.toDtoFactory = toDtoFactory;
        this.fromDtoFactory = fromDtoFactory;
    }

    public ResponseEntity<Page<ItemDto>> getAllItems(Pageable pageable)
    {
        Integer userId = userService.getProfile().getBody().getUserId();
        Page<Item> items = dao.findByAvailableTrueAndUserUserIdNot(userId, pageable);

        Page<ItemDto> dtoPage = items
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
                );

        /*List<ItemDto> items = dao.findAll().stream()
                .filter(Item::isAvailable)
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
                .toList();*/

        return new ResponseEntity<>(dtoPage, HttpStatus.OK);

    }

    //returns an item from its id if it exists and it is available
    public ResponseEntity<?> getItem(int itemId)
    {
        try
        {
            Item item = dao.findByItemId(itemId);

            if (!item.isAvailable())
                return new ResponseEntity<>("Item not available...", HttpStatus.NOT_FOUND);

            ItemDto dto = toDtoFactory.toDto(item);
            return new ResponseEntity<>(dto, HttpStatus.OK);
        }
        catch(Exception e)
        {
            e.printStackTrace();
            return new ResponseEntity<>("Error occurred", HttpStatus.BAD_REQUEST);
        }
    }


    public ResponseEntity<List<ItemDto>> getItemByOwner(int userId)
    {
        try
        {
             List<ItemDto> items = dao.findAllByUser_UserId(userId).stream().filter(Item::isAvailable)
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
            return new ResponseEntity<>(items, HttpStatus.OK);
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
        return new ResponseEntity<>(new ArrayList<>(), HttpStatus.BAD_REQUEST);

    }

    public ResponseEntity<String> addItem(ItemDto itemDto)
    {
        try
        {
            Item item = fromDtoFactory.fromDto(itemDto);
            if(item.getReleaseDate() == null)
                item.setReleaseDate(String.valueOf(LocalDate.now()));
            dao.save(item);
            return new ResponseEntity<>("Success", HttpStatus.CREATED);
        }
        catch(IllegalAccessException e)
        {
            e.printStackTrace();
            throw new RuntimeException("Error converting DTO to item: " + itemDto, e);
        }

    }


    public ResponseEntity<String> updateItem(ItemDto dto)
    {
        if (!dao.existsById(dto.getItemId()))
            return new ResponseEntity<>("Item not found", HttpStatus.NOT_FOUND);

        try
        {
            Item item = fromDtoFactory.fromDto(dto);
            item.setItemId(dto.getItemId());

            dao.save(item);
            return new ResponseEntity<>("Item updated", HttpStatus.OK);
        }
        catch(IllegalAccessException e)
        {
            e.printStackTrace();
            return new ResponseEntity<>("Illegal access error", HttpStatus.BAD_REQUEST);
        }

    }

    public ResponseEntity<String> softDeleteItem(int itemId)
    {
        if(!dao.existsById(itemId))
            return new ResponseEntity<>("Item not found", HttpStatus.NOT_FOUND);

        dao.softDeleteItem(itemId);
        return new ResponseEntity<>("Item deleted", HttpStatus.OK);
    }

    public ResponseEntity<String> deleteItem(int itemId)
    {
        if(!dao.existsById(itemId))
            return new ResponseEntity<>("Item not found", HttpStatus.NOT_FOUND);

        dao.delete(dao.findByItemId(itemId));
        return new ResponseEntity<>("Item deleted", HttpStatus.OK);
    }

    public ResponseEntity<String> uploadImage(int itemId, MultipartFile file) throws IOException
    {
        Item item = dao.findById(itemId)
                .orElseThrow(() -> new RuntimeException("Item not found"));

        item.setImage_name(file.getOriginalFilename());
        item.setImage_type(file.getContentType());
        item.setImage_date(file.getBytes());

        dao.save(item);
        return ResponseEntity.ok("Image uploaded");
    }

    public ResponseEntity<String> addItemWithImage(ItemDto dto, MultipartFile image) throws IOException
    {
        try
        {
            Item item = fromDtoFactory.fromDto(dto);
            if(item.getReleaseDate() == null)
                item.setReleaseDate(String.valueOf(LocalDate.now()));

            item.setImage_name(image.getOriginalFilename());
            item.setImage_type(image.getContentType());
            item.setImage_date(image.getBytes());
            dao.save(item);

            return new ResponseEntity<>("Item created with image!", HttpStatus.CREATED);
        }
        catch(IllegalAccessException e)
        {
            e.printStackTrace();
            throw new RuntimeException("Error converting DTO to item: " + dto, e);
        }
    }

    public ResponseEntity<String> updateItemWithImage(ItemDto dto, MultipartFile image) throws IOException
    {
        if (!dao.existsById(dto.getItemId()))
            return new ResponseEntity<>("Item not found", HttpStatus.NOT_FOUND);

        try
        {
            Item item = fromDtoFactory.fromDto(dto);
            item.setItemId(dto.getItemId());

            item.setImage_name(image.getOriginalFilename());
            item.setImage_type(image.getContentType());
            item.setImage_date(image.getBytes());

            dao.save(item);
            return new ResponseEntity<>("Item updated with image", HttpStatus.OK);
        }
        catch(IllegalAccessException e)
        {
            e.printStackTrace();
            return new ResponseEntity<>("Illegal access error", HttpStatus.BAD_REQUEST);
        }
    }

    public ResponseEntity<Page<ItemDto>> searchItems(String keyword, Pageable pageable)
    {
        Integer userId = userService.getProfile().getBody().getUserId();

        Page<Item> items = dao.findByUserUserIdNotAndNameContainingIgnoreCaseAndAvailableTrueOrUserUserIdNotAndDescriptionContainingIgnoreCaseAndAvailableTrue(userId, keyword, userId, keyword, pageable);

        if(items.isEmpty())
            return new ResponseEntity<>(Page.empty(pageable), HttpStatus.OK);

        Page<ItemDto> dtoPage = items
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
                ;

        return new ResponseEntity<>(dtoPage, HttpStatus.OK);
    }
}
