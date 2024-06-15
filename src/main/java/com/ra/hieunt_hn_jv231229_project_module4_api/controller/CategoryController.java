package com.ra.hieunt_hn_jv231229_project_module4_api.controller;

import com.ra.hieunt_hn_jv231229_project_module4_api.model.dto.response.CustomResponseEntity;
import com.ra.hieunt_hn_jv231229_project_module4_api.model.entity.Category;
import com.ra.hieunt_hn_jv231229_project_module4_api.model.entity.Product;
import com.ra.hieunt_hn_jv231229_project_module4_api.service.design.ICategoryService;
import com.ra.hieunt_hn_jv231229_project_module4_api.service.design.IProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api.myservice.com/v1/categories")
@RequiredArgsConstructor
public class CategoryController
{
    private final ICategoryService categoryService;
    private final IProductService productService;
    private final HttpStatus http = HttpStatus.OK;

    @GetMapping//Find out which categories having products that were sold
    public CustomResponseEntity<List<Category>> findSoldCategories
            (@PageableDefault(page = 0, size = 1000)
             Pageable pageable)
    {
        //Get list of products that were sold, using pre-built method in productService
        Page<Product> soldProducts = productService.findProductsPageable(pageable);
        //Use stream.map to turn them into a list of product Id
        List<Long> listSoldProductId = soldProducts.map(Product::getProductId).toList();
        return CustomResponseEntity.<List<Category>>builder()
                .statusCode(http.value())
                .status(http) //Find out categories that have products that were sold
                .data(categoryService.findSoldCategories(listSoldProductId))
                .message("List of categories having products that were sold")
                .build();
    }
}
