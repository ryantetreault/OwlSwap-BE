package com.cboard.marketplace.marketplace_backend.service;

import com.cboard.marketplace.marketplace_backend.dao.CategoryDao;
import com.cboard.marketplace.marketplace_backend.model.Category;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CategoryService
{
    @Autowired
    CategoryDao dao;
    public Category findByName(String name)
    {
        return dao.findByName(name);
    }
}
