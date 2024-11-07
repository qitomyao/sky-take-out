package com.sky.service;

import com.sky.dto.CategoryDTO;
import com.sky.dto.CategoryPageQueryDTO;
import com.sky.entity.Category;
import com.sky.result.PageResult;

import java.util.List;

public interface CategoryService {
    PageResult pageQuery(CategoryPageQueryDTO categoryPageQueryDTO);

    void startOrStop(Integer status, Long id);

    void deleteById(Integer id);

    void updateCategory(CategoryDTO categoryDTO);

    void addCategory(CategoryDTO categoryDTO);

    List<Category> list(Integer type);
}
