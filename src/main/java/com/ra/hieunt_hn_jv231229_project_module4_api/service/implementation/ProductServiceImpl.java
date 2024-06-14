package com.ra.hieunt_hn_jv231229_project_module4_api.service.implementation;

import com.ra.hieunt_hn_jv231229_project_module4_api.model.dto.response.ProductResponse;
import com.ra.hieunt_hn_jv231229_project_module4_api.model.entity.Product;
import com.ra.hieunt_hn_jv231229_project_module4_api.repository.IOrderDetailrepo;
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
    private final IOrderDetailrepo orderDetailrepo;

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

    @Override
    public List<Product> findByNameOrDesc(String productName, String productDescription)
    {
        if (productName.isEmpty())
        {
            productName = null;
        }
        if (productDescription.isEmpty())
        {
            productDescription = null;
        }
        return productRepo.findProductsByProductNameContainingOrDescriptionContaining(productName, productDescription);
    }

    @Override
    public List<ProductResponse> findBestSellerProducts(Integer limit)
    {
        List<Product> products = productRepo.findBestSellerProducts(limit);
        return products.stream().map(p -> ProductResponse.builder()
                .productId(p.getProductId())
                .productName(p.getProductName())
                .description(p.getDescription())
                .unitPrice(p.getUnitPrice())
                .stockQuantity(p.getStockQuantity())
                .soldQuantity(orderDetailrepo.findQuantityPerProduct(p.getProductId()))
                .build()).toList();
    }
}
