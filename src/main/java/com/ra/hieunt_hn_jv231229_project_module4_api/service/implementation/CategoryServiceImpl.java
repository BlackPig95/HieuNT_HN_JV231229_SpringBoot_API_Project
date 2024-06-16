package com.ra.hieunt_hn_jv231229_project_module4_api.service.implementation;

import com.ra.hieunt_hn_jv231229_project_module4_api.model.dto.request.CategoryRequest;
import com.ra.hieunt_hn_jv231229_project_module4_api.model.entity.Category;
import com.ra.hieunt_hn_jv231229_project_module4_api.model.entity.Product;
import com.ra.hieunt_hn_jv231229_project_module4_api.model.entity.User;
import com.ra.hieunt_hn_jv231229_project_module4_api.repository.ICategoryRepo;
import com.ra.hieunt_hn_jv231229_project_module4_api.service.design.ICategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements ICategoryService
{
    private final ICategoryRepo categoryRepo;

    @Override
    public List<Category> findSoldCategories(List<Long> soldProductId)
    {

        return categoryRepo.findSoldCategories(soldProductId);
    }

    @Override
    public Page<Category> findAll(Pageable pageable)
    {
        Page<Category> categoryPage = categoryRepo.findAll(pageable);
        //Index start from 0 => Need to add 1 to balance with total page size
        if (pageable.getPageNumber() + 1 > categoryPage.getTotalPages())
        {
            throw new RuntimeException("No more category to show");
        }
        return categoryRepo.findAll(pageable);
    }

    @Override
    public Category saveCategory(CategoryRequest categoryRequest)
    {
        Category category = Category.builder()
                .categoryName(categoryRequest.getCategoryName())
                .description(categoryRequest.getDescription())
                .status(categoryRequest.getStatus())
                .build();
        return categoryRepo.save(category);
    }

    @Override
    public Category updateCategory(Long categoryId, CategoryRequest categoryRequest)
    {
        Category updatedCategory = categoryRepo.findById(categoryId).orElseThrow(() -> new RuntimeException("Category not found. Can't update"));
        updatedCategory.setCategoryName(categoryRequest.getCategoryName());
        updatedCategory.setDescription(categoryRequest.getDescription());
        updatedCategory.setStatus(categoryRequest.getStatus());
        return categoryRepo.save(updatedCategory);
    }

    @Override
    public Category findById(Long categoryId)
    {
        return categoryRepo.findById(categoryId).orElseThrow(() -> new RuntimeException("Category not found"));
    }

    @Override
    public Category deleteCategory(Long categoryId)
    {
        //Check to see if the Id exist before delete
        Category deletedCategory = categoryRepo.findById(categoryId).orElseThrow(() -> new RuntimeException("Category not found. Can't delete"));
        categoryRepo.deleteById(categoryId);
        return deletedCategory;
    }
}
