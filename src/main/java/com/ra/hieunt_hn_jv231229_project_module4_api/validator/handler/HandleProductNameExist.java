package com.ra.hieunt_hn_jv231229_project_module4_api.validator.handler;

import com.ra.hieunt_hn_jv231229_project_module4_api.model.dto.request.ProductRequest;
import com.ra.hieunt_hn_jv231229_project_module4_api.model.entity.Product;
import com.ra.hieunt_hn_jv231229_project_module4_api.repository.IProductRepo;
import com.ra.hieunt_hn_jv231229_project_module4_api.validator.annotation.ProductNameExist;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanWrapperImpl;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class HandleProductNameExist implements ConstraintValidator<ProductNameExist, String>
{
    private final IProductRepo productRepo;

    @Override
    public boolean isValid(String productName, ConstraintValidatorContext context)
    {
//        String nameOfProduct = (String) new BeanWrapperImpl(productRequest).getPropertyValue(this.productName);
//        //Get list of all availabe products
//        List<Product> products = productRepo.findAll();
//        //Get the product being updated
//        Product checkNameProduct = productRepo.findById(productRequest.getProductId()).orElseThrow(() -> new RuntimeException("Can't find product. Can't update"));
//        //Check if any product name matches the name being used for update
//        if (products.stream().anyMatch(p -> p.getProductName().equals(checkNameProduct.getProductName())))
//        {   //If the product name being used for update matches the product being updated itself=> Allow use
//            if (checkNameProduct.getProductName().equals(nameOfProduct))
//            {
//                return true;
//            }
//        }
//        //Check for action add product and refuse update if product name matches another product
        return !productRepo.existsByProductName(productName);
    }
}
