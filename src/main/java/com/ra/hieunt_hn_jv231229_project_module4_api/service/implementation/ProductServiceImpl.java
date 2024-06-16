package com.ra.hieunt_hn_jv231229_project_module4_api.service.implementation;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.ra.hieunt_hn_jv231229_project_module4_api.model.dto.request.ProductRequest;
import com.ra.hieunt_hn_jv231229_project_module4_api.model.dto.response.ProductResponse;
import com.ra.hieunt_hn_jv231229_project_module4_api.model.entity.Category;
import com.ra.hieunt_hn_jv231229_project_module4_api.model.entity.Product;
import com.ra.hieunt_hn_jv231229_project_module4_api.objectmapper.ProductMapper;
import com.ra.hieunt_hn_jv231229_project_module4_api.repository.IOrderDetailrepo;
import com.ra.hieunt_hn_jv231229_project_module4_api.repository.IProductRepo;
import com.ra.hieunt_hn_jv231229_project_module4_api.service.design.IProductService;
import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements IProductService
{
    private final IProductRepo productRepo;
    private final IOrderDetailrepo orderDetailrepo;
    private final ProductMapper productMapper;

    //Find all product that were sold (appear in orderDetail). Called by ProductController
    @Override
    public Page<Product> findProductsPageable(Pageable pageable)
    {
        Page<Product> pageProduct = productRepo.findAllProductsSold(pageable);
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
    {   //Avoid search method return all products if one parameter is empty string
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

    @Override
    public Product addProduct(ProductRequest productRequest)
    {
        Product addedProduct = productMapper.requestToProduct(productRequest);
        //Adding new product => Should let the Id auto increment and auto create UUID
        addedProduct.setProductId(null);
        addedProduct.setSku(UUID.randomUUID().toString());
        //Same for created and updated date
        addedProduct.setCreatedAt(new Date());
        addedProduct.setUpdatedAt(new Date());
        return productRepo.save(addedProduct);
    }

    @Override//Find all products regardless of sold or not. Called by AdminController
    public Page<Product> findAllProductsAvailable(Pageable pageable)
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
    public Product deleteProduct(Long id)
    {
        Product productDeleted = productRepo.findById(id).orElseThrow(() -> new RuntimeException("Can't delete product. Id not found"));
        productRepo.delete(productDeleted);
        return productDeleted;
    }

    @Override
    public Product updateProduct(Long id, ProductRequest productRequest)
    {   //Check to see if this product exist or not
        Product checkProduct = productRepo.findById(id).orElseThrow(() -> new RuntimeException("Can't update. Product not found"));
        //Update product info with info from the productRequest
        Product updatedProduct = productMapper.updateProductInfo(id, productRequest);
        updatedProduct.setSku(checkProduct.getSku());//Reset the sku
        //Get all products availabe
        List<Product> products = productRepo.findAll();
        //Check if any product name matches the name being used for update
        if (products.stream().anyMatch(p -> p.getProductName().equals(productRequest.getProductName())))
        {   //If the product name being used for update matches the product being updated itself=> Allow use
            //If not throw exception
            if (!checkProduct.getProductName().equals(productRequest.getProductName()))
            {
                throw new RuntimeException("The product name is duplicated with another product. Please choose a different name");
            }
        }
        return productRepo.save(updatedProduct);
    }

    @Override
    public Product findById(Long productId)
    {
        return productRepo.findById(productId).orElseThrow(() -> new RuntimeException("Can't find any product with id: " + productId));
    }
}
