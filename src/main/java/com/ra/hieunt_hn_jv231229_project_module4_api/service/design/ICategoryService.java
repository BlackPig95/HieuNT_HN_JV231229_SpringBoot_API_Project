package com.ra.hieunt_hn_jv231229_project_module4_api.service.design;

import com.ra.hieunt_hn_jv231229_project_module4_api.model.entity.Category;
import com.ra.hieunt_hn_jv231229_project_module4_api.model.entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ICategoryService
{
    List<Category> findSoldCategories(List<Long> soldProductId);

    Page<Category> findAll(Pageable pageable);
}
