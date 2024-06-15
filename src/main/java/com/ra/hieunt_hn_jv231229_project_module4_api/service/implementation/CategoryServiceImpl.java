package com.ra.hieunt_hn_jv231229_project_module4_api.service.implementation;

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
}
