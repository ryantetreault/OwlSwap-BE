package com.cboard.owlswap.owlswap_backend.controller;

import com.cboard.owlswap.owlswap_backend.model.Dto.ItemDto;
import com.cboard.owlswap.owlswap_backend.model.ItemFavorite;
import com.cboard.owlswap.owlswap_backend.service.ItemFavoritesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.data.domain.Pageable;


import java.util.List;

@RestController
@RequestMapping("item-favorites")
public class ItemFavoritesController
{
    @Autowired
    ItemFavoritesService service;

    @GetMapping("item/{id}")
    public ResponseEntity<List<ItemFavorite>> getItemFavoritesByItem(@PathVariable("id") int itemId)
    {
        return ResponseEntity.ok(service.getItemFavoritesByItem(itemId));
    }
    @GetMapping("user/me")
    public ResponseEntity<Page<ItemDto>> getMyItemFavorites(@PageableDefault(size = 6) Pageable pageable)
    {
        return ResponseEntity.ok(service.getMyItemFavorites(pageable));
    }

    @GetMapping("is-favorite/item/{itemId}")
    public ResponseEntity<Boolean> getIsFavorite(@PathVariable("itemId") int itemId)
    {
        return ResponseEntity.ok(service.getIsFavorite(itemId));
    }

    @PostMapping("add-favorite/item/{itemId}")
    public ResponseEntity<String> addFavorite(@PathVariable("itemId") int itemId)
    {
        service.addFavorite(itemId);
        return ResponseEntity.ok("Item favorited!");
    }

    @DeleteMapping("remove-favorite/item/{itemId}")
    public ResponseEntity<String> removeFavorite(@PathVariable("itemId") int itemId)
    {
        service.removeFavorite(itemId);
        return ResponseEntity.noContent().build();
    }
}
