package com.project.controller;

import com.project.business.CategoryService;
import com.project.model.Category;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RefreshScope
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    @GetMapping("/category/id/{id}")
    public Category getCategory(@PathVariable String id){
        return categoryService.getCategoryById(id);
    }

    @GetMapping("/category/all")
    public List<Category> getCategories(){
        return categoryService.getCategories();
    }

    @PostMapping("/category/save")
    public Category createOrUpdate(@RequestBody Category category){
        return categoryService.save(category);
    }
}
