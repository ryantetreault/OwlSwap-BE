package com.cboard.marketplace.marketplace_backend.model.DtoMapping.toDto;

import com.cboard.marketplace.marketplace_backend.model.Dto.ItemDto;
import com.cboard.marketplace.marketplace_backend.model.Item;


public interface ItemToDtoMapper<T extends Item>
{
    ItemDto mapToDto(T item);
    Class<T> getMappedClass();

}
