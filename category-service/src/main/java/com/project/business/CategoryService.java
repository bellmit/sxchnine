package com.project.business;

import com.project.exception.CategoryNotFoundException;
import com.project.model.Category;
import com.project.repository.CategoryRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryService {
    private static final Logger LOG = LoggerFactory.getLogger(CategoryService.class);

    @Autowired
    private CategoryRepository categoryRepository;

    public Category getCategoryById(String id){
        return categoryRepository.findById(id).orElseThrow(() -> new CategoryNotFoundException("Category not found !"));
    }

    public List<Category> getCategories(){
        return (List<Category>)categoryRepository.findAll();
    }

    public Category save(Category category){
        return categoryRepository.save(category);
    }
}
