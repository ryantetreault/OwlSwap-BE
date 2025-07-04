package com.cboard.marketplace.marketplace_backend.model.DtoMapping.fromDto;

import com.cboard.marketplace.marketplace_backend.model.*;
import com.cboard.marketplace.marketplace_backend.model.Dto.ItemDto;


public interface DtoToItemMapper<D extends ItemDto>
{
    Item fromDto(D dto);
    Class<D> getDtoClass();
}
