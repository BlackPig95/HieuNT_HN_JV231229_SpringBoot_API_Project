package com.ra.hieunt_hn_jv231229_project_module4_api.service.implementation;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.ra.hieunt_hn_jv231229_project_module4_api.model.dto.request.CategoryRequest;
import com.ra.hieunt_hn_jv231229_project_module4_api.model.dto.response.CategoryRevenueResponse;
import com.ra.hieunt_hn_jv231229_project_module4_api.model.dto.response.ProductResponse;
import com.ra.hieunt_hn_jv231229_project_module4_api.model.entity.*;
import com.ra.hieunt_hn_jv231229_project_module4_api.repository.ICategoryRepo;
import com.ra.hieunt_hn_jv231229_project_module4_api.repository.IOrderDetailrepo;
import com.ra.hieunt_hn_jv231229_project_module4_api.repository.IOrderRepo;
import com.ra.hieunt_hn_jv231229_project_module4_api.repository.IProductRepo;
import com.ra.hieunt_hn_jv231229_project_module4_api.service.design.ICategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements ICategoryService
{
    private final ICategoryRepo categoryRepo;
    private final IOrderRepo orderRepo;
    private final IOrderDetailrepo orderDetailrepo;
    private final IProductRepo productRepo;

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

    @Override
    public List<CategoryRevenueResponse> revenueByCategory()
    {
        //Create a list to hold response body to API
        List<CategoryRevenueResponse> revenuePerCategoryList = new ArrayList<>();
        //First get a list of all successful orders
        List<Order> successfulOrderList = orderRepo.findOrdersByStatusSuccess();
        //Get a list of orderDetails that are matched with these Ordes
        List<OrderDetail> orderDetailList =
                successfulOrderList.stream().map(od -> orderDetailrepo.findOrderDetailByCompositeKeyOrderOrderId(od.getOrderId())).toList();
        //Get list of products sold in these orderDetails
        List<Product> productSoldList =
                orderDetailList.stream()
                        .map(od -> productRepo.findById(od.getCompositeKey().getProduct().getProductId()).
                                orElseThrow(() -> new RuntimeException("Product Id not exist"))).toList();
        //Get list of all currently available category
        List<Category> categoryList = categoryRepo.findAll();
        //Iterate over all category
        for (Category c : categoryList)
        {//If the product's category Id appear in category list => This category had sold item
            //Filter out which sold products belong to this category
            List<Product> soldProductInCat =
                    productSoldList.stream()
                            .filter(p -> p.getCategory().getCategoryId()
                                    .equals(c.getCategoryId())).toList();
            for (Product sold : soldProductInCat)
            {
                //Continue to filter out which order details hold information about these sold products
                List<OrderDetail> orderDetailOfSoldProductsInCat
                        = orderDetailList.stream()
                        .filter(od -> od.getCompositeKey().getProduct()
                                .getProductId().equals(sold.getProductId())).toList();
                //Continue to filter out which order has reference to these order details
                //=> These are the successful orders of each category that had
                //products sold (Orders => order details => products => category)
                for (OrderDetail success : orderDetailOfSoldProductsInCat)
                {
                    List<Order> successOrderPerCat =
                            successfulOrderList.stream()
                                    .filter(order -> order.getOrderId()
                                            .equals(success.getCompositeKey().getOrder()
                                                    .getOrderId())).toList();
                    //Sum up the price of each orders to get the total revenue of each category
                    Double totalRevenuePerCat = successOrderPerCat.stream().mapToDouble(Order::getTotalPrice).sum();
                    //Add each of these category to the list to return to API view
                    CategoryRevenueResponse catResponse =
                            CategoryRevenueResponse.builder()
                                    .categoryId(c.getCategoryId())
                                    .categoryName(c.getCategoryName())
                                    .description(c.getDescription())
                                    .totalRevenue(totalRevenuePerCat)
                                    .build();
                    revenuePerCategoryList.add(catResponse);
                }
            }
        }
        return revenuePerCategoryList;
    }
}
