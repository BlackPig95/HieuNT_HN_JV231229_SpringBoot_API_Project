package com.ra.hieunt_hn_jv231229_project_module4_api.controller;

import com.ra.hieunt_hn_jv231229_project_module4_api.model.dto.request.CategoryRequest;
import com.ra.hieunt_hn_jv231229_project_module4_api.model.dto.request.ProductRequest;
import com.ra.hieunt_hn_jv231229_project_module4_api.model.dto.response.CustomResponseEntity;
import com.ra.hieunt_hn_jv231229_project_module4_api.model.dto.response.UserPageableResponse;
import com.ra.hieunt_hn_jv231229_project_module4_api.model.entity.Category;
import com.ra.hieunt_hn_jv231229_project_module4_api.model.entity.Product;
import com.ra.hieunt_hn_jv231229_project_module4_api.model.entity.User;
import com.ra.hieunt_hn_jv231229_project_module4_api.service.design.*;
import com.ra.hieunt_hn_jv231229_project_module4_api.util.FormatCategoryData;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api.myservice.com/v1/admin")
@RequiredArgsConstructor
public class AdminController
{
    private final IUserService userService;
    private final IRoleService roleService;
    private final HttpStatus http = HttpStatus.OK;
    private final ICategoryService categoryService;
    private final IProductService productService;
    private final IOrderService orderService;

    @GetMapping("/users")//Get list user with pagination and sorting
    public ResponseEntity<?> listUser(@PageableDefault(page = 0, size = 3,
            sort = "username", direction = Sort.Direction.ASC) Pageable pageable)
    {
        Page<UserPageableResponse> pageUser = userService.findAllUserPageable(pageable);
        return new ResponseEntity<>(CustomResponseEntity.builder()
                .statusCode(http.value())
                .status(http)
                .data(pageUser)
                .message("Page number " + (pageable.getPageNumber() + 1) + " / " + pageUser.getTotalPages())
                .build(), http);
    }

    @GetMapping("/roles")//Get list of all roles available
    public ResponseEntity<?> listRoles()
    {
        return new ResponseEntity<>(CustomResponseEntity.builder()
                .statusCode(http.value())
                .status(http)
                .data(roleService.findAll())
                .message("List of roles")
                .build(), HttpStatus.OK);
    }

    @GetMapping("/users/search")//Search for user by name
    public ResponseEntity<?> findUserByName(@RequestParam(name = "fullname", defaultValue = "") String fullname)
    {
        return new ResponseEntity<>(CustomResponseEntity.builder()
                .statusCode(http.value())
                .status(http)
                .data(userService.findAllUsersByName(fullname))
                .message("List of user found with name similar to: " + fullname)
                .build(), http);
    }

    @PutMapping("/users/{userId}")//Change user status based on Id
    public ResponseEntity<?> lockUser(@PathVariable(name = "userId") Long userId)
    {
        User blockUser = userService.lockUserById(userId);
        return new ResponseEntity<>(CustomResponseEntity.builder()
                .statusCode(http.value())
                .status(http)
                .data("Initial status of user " + blockUser.getUsername() + ": " + (!userService.findUserById(userId).getStatus() ? "Active" : "Blocked"))
                .message("Updated status of user: " + (blockUser.getStatus() ? "Active" : "Blocked"))
                .build(), http);
    }

    @GetMapping("/categories")//List of all categories with pagination and sorting
    public CustomResponseEntity<Page<Category>> listCategoriesPageable
            (@PageableDefault(page = 0, size = 3,
                    sort = "categoryId", direction = Sort.Direction.ASC) Pageable pageable)
    {
        Page<Category> pageCat = categoryService.findAll(pageable);
        return CustomResponseEntity.<Page<Category>>builder()
                .statusCode(http.value())
                .status(http)
                .data(pageCat)
                .message("Page number " + (pageable.getPageNumber() + 1) + " / " + pageCat.getTotalPages())
                .build();
    }

    @PostMapping("/categories")//Add a new category
    public CustomResponseEntity<Map<String, String>> addCategory(@RequestBody CategoryRequest categoryRequest)
    {
        Category addedCategory = categoryService.saveCategory(categoryRequest);
        Map<String, String> dataToShow = FormatCategoryData.formatDataToShow(addedCategory);
//        Map<String, String> dataToShow = new HashMap<>();
//        dataToShow.put("Category Id: ", addedCategory.getCategoryId().toString());
//        dataToShow.put("Category Name: ", addedCategory.getCategoryName());
//        dataToShow.put("Description: ", addedCategory.getDescription());
//        dataToShow.put("Category status: ", addedCategory.getStatus() ? "Active" : "Inactive");
        return CustomResponseEntity.<Map<String, String>>builder()
                .statusCode(http.value())
                .status(http)
                .data(dataToShow)
                .message("Added category")
                .build();
    }

    @PutMapping("/categories/{categoryId}")//Update category info based on Id
    public CustomResponseEntity<?> updateCategory(@PathVariable(name = "categoryId") Long categoryId,
                                                  @Valid @RequestBody CategoryRequest categoryRequest)
    {
        Category updatedCategory = categoryService.updateCategory(categoryId, categoryRequest);
        Map<String, String> dataToShow = FormatCategoryData.formatDataToShow(updatedCategory);
        return CustomResponseEntity.<Map<String, String>>builder()
                .statusCode(http.value())
                .status(http)
                .data(dataToShow)
                .message("Updated category")
                .build();
    }

    @DeleteMapping("/categories/{categoryId}")//Delete a category
    public CustomResponseEntity<?> deleteCategory(@PathVariable(name = "categoryId") Long categoryId)
    {
        Category deletedCategory = categoryService.deleteCategory(categoryId);
        return CustomResponseEntity.builder()
                .statusCode(http.value())
                .status(http)
                .data("Category " + deletedCategory.getCategoryName() + " is deleted")
                .message("Request to delete category")
                .build();
    }

    @GetMapping("/categories/{categoryId}")//Find a category
    public CustomResponseEntity<?> findCategory(@PathVariable(name = "categoryId") Long categoryId)
    {
        return CustomResponseEntity.builder()
                .statusCode(http.value())
                .status(http)
                .data(categoryService.findById(categoryId))
                .message("Category information")
                .build();
    }

    @PostMapping("/products")//Add a new product
    public CustomResponseEntity<?> addProduct(@Valid @RequestBody ProductRequest productRequest)
    {
        return CustomResponseEntity.builder()
                .statusCode(http.value())
                .status(http)
                .data(productService.addProduct(productRequest))
                .message("Category information")
                .build();
    }

    @GetMapping("/products")//Get all products with pagination
    public CustomResponseEntity<?> productsPageable
            (@PageableDefault(page = 0, size = 3,
                    sort = "productId", direction = Sort.Direction.ASC) Pageable pageable)
    {
        Page<Product> productPage = productService.findAllProductsAvailable(pageable);
        return CustomResponseEntity.builder()
                .statusCode(http.value())
                .status(http)
                .data(productPage)
                .message("All products available. Page number " + (pageable.getPageNumber() + 1) + " / " + productPage.getTotalPages())
                .build();
    }

    @DeleteMapping("/products/{productId}")//Delete a product
    public CustomResponseEntity<?> deleteProduct(@PathVariable Long productId)
    {
        return CustomResponseEntity.builder()
                .statusCode(http.value())
                .status(http)
                .data(productService.deleteProduct(productId))
                .message("Deleted product information")
                .build();
    }

    @PutMapping("/products/{productId}")//Update/ Edit a product
    public CustomResponseEntity<?> updateProduct(@PathVariable Long productId,
                                                 @RequestBody ProductRequest productRequest)
    {
        return CustomResponseEntity.builder()
                .statusCode(http.value())
                .status(http)
                .data(productService.updateProduct(productId, productRequest))
                .message("Updated product information")
                .build();
    }

    @GetMapping("/products/{productId}")
    public CustomResponseEntity<?> getProductById(@PathVariable Long productId)
    {
        return CustomResponseEntity.builder()
                .statusCode(http.value())
                .status(http)
                .data(productService.findById(productId))
                .message("Information on product with Id: " + productId)
                .build();
    }

    @GetMapping("/orders")//Get list of all orders currently available
    public CustomResponseEntity<?> allOrders()
    {
        return CustomResponseEntity.builder()
                .statusCode(http.value())
                .status(http)
                .data(orderService.findAllOrders())
                .message("All orders currently available")
                .build();
    }

    @PutMapping("/orders/{orderId}/status")//Update/Edit order status
    public CustomResponseEntity<?> updateOrderStatus(@PathVariable(name = "orderId") Long orderId, @RequestParam(name = "status") String newOrderStatus)
    {
        return CustomResponseEntity.builder()
                .statusCode(http.value())
                .status(http)
                .data("New order status: " + orderService.updateOrderStatus(orderId, newOrderStatus).getStatus())
                .message("Updated status of the order")
                .build();
    }

    @GetMapping("/orders/{orderId}")//Get detailed information on the order
    public CustomResponseEntity<?> orderDetailsInfo(@PathVariable(name = "orderId") Long orderId)
    {
        return CustomResponseEntity.builder()
                .statusCode(http.value())
                .status(http)
                .data(orderService.findOrderAndDetails(orderId))
                .message("Detailed information of the order")
                .build();
    }

    //Have to change API URL due to ambiguous mapping with the above URL
    //to retrieve information on order by passing in Id
    //(both GET method with the same url pattern)
    @GetMapping("/orders/status/{orderStatus}")//List of orders based on status
    public CustomResponseEntity<?> ordersHavingStatus(@PathVariable String orderStatus)
    {
        return CustomResponseEntity.builder()
                .statusCode(http.value())
                .status(http)
                .data(orderService.findOrdersByStatus(orderStatus))
                .message("List of Order having status of: " + orderStatus.toUpperCase())
                .build();
    }
}
