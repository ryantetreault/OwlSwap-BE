package com.cboard.marketplace.marketplace_backend.controller;


import com.cboard.marketplace.marketplace_backend.model.Dto.CategoryDto;
import com.cboard.marketplace.marketplace_backend.model.Dto.ItemDto;
import com.cboard.marketplace.marketplace_backend.service.CategoryService;
import com.cboard.marketplace.marketplace_backend.service.ItemService;
import jakarta.validation.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("item")
public class ItemController
{
    @Autowired
    ItemService service;

    @Autowired
    private CategoryService categoryService;

    //returns all available items
    @GetMapping("all")
    public ResponseEntity<Page<ItemDto>> getAllItems(@PageableDefault(size=5) Pageable pageable)
    {
        //could potentially throw a runtime exception if cant map an item, consider handling this later?
        return service.getAllItems(pageable);
    }

    @GetMapping("search")
    public ResponseEntity<Page<ItemDto>> searchItems(@RequestParam String keyword, @PageableDefault(size=20) Pageable pageable)
    {
        return service.searchItems(keyword, pageable);
    }

    @GetMapping("{id}")
    public ResponseEntity<?> getItem(@PathVariable("id") int itemId)
    {
        return service.getItem(itemId);
    }

    @PostMapping("add")
    public ResponseEntity<String> addItem(@Valid @RequestBody ItemDto itemDto)
    {
        //could potentially throw a runtime exception if cant map an item, consider handling this later?
        return service.addItem(itemDto);

    }

    @PutMapping("update")
    public ResponseEntity<?> updateItem(@Valid @RequestBody ItemDto dto)
    {
        return service.updateItem(dto);
    }

    @PutMapping("update/with-image")
    public ResponseEntity<String> updateItemWithImage(@Valid @RequestPart("item") ItemDto dto, @RequestPart("image") MultipartFile image)
    {
        try
        {
            return service.updateItemWithImage(dto, image);
        }
        catch(Exception e)
        {
            e.printStackTrace();
            return new ResponseEntity<>("Error", HttpStatus.BAD_REQUEST);
        }
    }



    @GetMapping("{id}/owner")
    public ResponseEntity<List<ItemDto>> getItemByOwner(@PathVariable("id") int userId)
    {
        return service.getItemByOwner(userId);

    }

    //upload an image
    @PostMapping("{id}/upload-image")
    public ResponseEntity<String> uploadImage(@PathVariable("id") int itemId, @RequestParam("file")MultipartFile file)
    {
        try
        {
            return service.uploadImage(itemId, file);
        }
        catch(Exception e)
        {
            e.printStackTrace();
            return new ResponseEntity<>("Error", HttpStatus.BAD_REQUEST);
        }
    }


    //upload image and item at same time
    @PostMapping("add/with-image")
    public ResponseEntity<String> addItemWithImage(@RequestPart("item") ItemDto dto, @RequestPart("image") MultipartFile image)
    {
        try
        {
            return service.addItemWithImage(dto, image);
        }
        catch(Exception e)
        {
            e.printStackTrace();
            return new ResponseEntity<>("Error", HttpStatus.BAD_REQUEST);
        }
    }



    //soft deletes an item -- instead of actually deleting an item, turns its available attribute to false
    @DeleteMapping("{id}/delete")
    public ResponseEntity<String> deleteItem(@PathVariable("id") int itemId)
    {
        return service.deleteItem(itemId);
    }

    @GetMapping("types")
    public ResponseEntity<List<String>> getItemTypes() {
        List<String> itemTypes = List.of("Product", "Service", "Request");
        return ResponseEntity.ok(itemTypes);
    }

    @GetMapping("/categories")
    public List<CategoryDto> getAllCategories() {
        return categoryService.getAll();
    }
}