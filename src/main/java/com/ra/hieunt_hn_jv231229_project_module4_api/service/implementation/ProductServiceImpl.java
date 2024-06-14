package com.ra.hieunt_hn_jv231229_project_module4_api.service.implementation;

import com.ra.hieunt_hn_jv231229_project_module4_api.model.entity.Product;
import com.ra.hieunt_hn_jv231229_project_module4_api.repository.IProductRepo;
import com.ra.hieunt_hn_jv231229_project_module4_api.service.design.IProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements IProductService
{
    private final IProductRepo productRepo;

    @Override
    public Page<Product> findProductsPageable(Pageable pageable)
    {
        Page<Product> pageProduct = productRepo.findAll(pageable);
        //Index start from 0 => Need to add 1 to balance with total page size
        if (pageable.getPageNumber() + 1 > pageProduct.getTotalPages())
        {
            throw new RuntimeException("Out of size of product list");
        }
        return pageProduct;
    }

    @Override
    public Product findProductById(Long id)
    {
        return productRepo.findById(id).orElseThrow(() -> new RuntimeException("Product not found"));
    }

    @Override
    public List<Product> findProductsByCategory(Long catId)
    {
        return productRepo.findProductByCategory(catId);
    }

    @Override
    public List<Product> findNewestProducts(Integer limit)
    {
        return productRepo.findNewestProducts(limit);
    }
}
