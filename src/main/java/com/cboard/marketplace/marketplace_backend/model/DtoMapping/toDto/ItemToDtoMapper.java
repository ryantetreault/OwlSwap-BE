package com.cboard.marketplace.marketplace_backend.model.DtoMapping.toDto;

import com.cboard.marketplace.marketplace_backend.model.Item;
import com.cboard.marketplace.marketplace_common.ItemDto;

public interface ItemToDtoMapper<T extends Item>
{
    ItemDto mapToDto(T item);
    Class<T> getMappedClass();

}
