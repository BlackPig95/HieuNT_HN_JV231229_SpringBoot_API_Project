package com.ra.hieunt_hn_jv231229_project_module4_api.controller;

import com.google.api.Http;
import com.ra.hieunt_hn_jv231229_project_module4_api.model.dto.request.CategoryRequest;
import com.ra.hieunt_hn_jv231229_project_module4_api.model.dto.request.ProductRequest;
import com.ra.hieunt_hn_jv231229_project_module4_api.model.dto.request.TimeRangeRequest;
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

import java.util.Map;

@RestController
@RequestMapping("/api.myservice.com/v1/admin")
@RequiredArgsConstructor
public class AdminController
{
    private final IUserService userService;
    private final IRoleService roleService;
    private final HttpStatus httpOk = HttpStatus.OK;
    private final ICategoryService categoryService;
    private final IProductService productService;
    private final IOrderService orderService;

    @GetMapping("/users")//Get list user with pagination and sorting
    public ResponseEntity<?> listUser(@PageableDefault(page = 0, size = 3,
            sort = "username", direction = Sort.Direction.ASC) Pageable pageable)
    {
        Page<UserPageableResponse> pageUser = userService.findAllUserPageable(pageable);
        return new ResponseEntity<>(CustomResponseEntity.builder()
                .statusCode(httpOk.value())
                .status(httpOk)
                .data(pageUser)
                .message("Page number " + (pageable.getPageNumber() + 1) + " / " + pageUser.getTotalPages())
                .build(), httpOk);
    }

    @GetMapping("/roles")//Get list of all roles available
    public ResponseEntity<?> listRoles()
    {
        return new ResponseEntity<>(CustomResponseEntity.builder()
                .statusCode(httpOk.value())
                .status(httpOk)
                .data(roleService.findAll())
                .message("List of roles")
                .build(), HttpStatus.OK);
    }

    @GetMapping("/users/search")//Search for user by name
    public ResponseEntity<?> findUserByName(@RequestParam(name = "fullname", defaultValue = "") String fullname)
    {
        return new ResponseEntity<>(CustomResponseEntity.builder()
                .statusCode(httpOk.value())
                .status(httpOk)
                .data(userService.findAllUsersByName(fullname))
                .message("List of user found with name similar to: " + fullname)
                .build(), httpOk);
    }

    @PutMapping("/users/{userId}")//Change user status based on Id
    public ResponseEntity<?> lockUser(@PathVariable(name = "userId") Long userId)
    {
        User blockUser = userService.lockUserById(userId);
        return new ResponseEntity<>(CustomResponseEntity.builder()
                .statusCode(httpOk.value())
                .status(httpOk)
                .data("Initial status of user " + blockUser.getUsername() + ": " + (!userService.findUserById(userId).getStatus() ? "Active" : "Blocked"))
                .message("Updated status of user: " + (blockUser.getStatus() ? "Active" : "Blocked"))
                .build(), httpOk);
    }

    @GetMapping("/categories")//List of all categories with pagination and sorting
    public CustomResponseEntity<Page<Category>> listCategoriesPageable
            (@PageableDefault(page = 0, size = 3,
                    sort = "categoryId", direction = Sort.Direction.ASC) Pageable pageable)
    {
        Page<Category> pageCat = categoryService.findAll(pageable);
        return CustomResponseEntity.<Page<Category>>builder()
                .statusCode(httpOk.value())
                .status(httpOk)
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
                .statusCode(HttpStatus.CREATED.value())
                .status(HttpStatus.CREATED)
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
                .statusCode(httpOk.value())
                .status(httpOk)
                .data(dataToShow)
                .message("Updated category")
                .build();
    }

    @DeleteMapping("/categories/{categoryId}")//Delete a category
    public CustomResponseEntity<?> deleteCategory(@PathVariable(name = "categoryId") Long categoryId)
    {
        Category deletedCategory = categoryService.deleteCategory(categoryId);
        return CustomResponseEntity.builder()
                .statusCode(HttpStatus.NO_CONTENT.value())
                .status(HttpStatus.NO_CONTENT)
                .data("Category " + deletedCategory.getCategoryName() + " is deleted")
                .message("Request to delete category")
                .build();
    }

    @GetMapping("/categories/{categoryId}")//Find a category
    public CustomResponseEntity<?> findCategory(@PathVariable(name = "categoryId") Long categoryId)
    {
        return CustomResponseEntity.builder()
                .statusCode(httpOk.value())
                .status(httpOk)
                .data(categoryService.findById(categoryId))
                .message("Category information")
                .build();
    }

    @PostMapping("/products")//Add a new product
    public CustomResponseEntity<?> addProduct(@Valid @ModelAttribute ProductRequest productRequest)
    {
        return CustomResponseEntity.builder()
                .statusCode(HttpStatus.CREATED.value())
                .status(HttpStatus.CREATED)
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
                .statusCode(httpOk.value())
                .status(httpOk)
                .data(productPage)
                .message("All products available. Page number " + (pageable.getPageNumber() + 1) + " / " + productPage.getTotalPages())
                .build();
    }

    @DeleteMapping("/products/{productId}")//Delete a product
    public CustomResponseEntity<?> deleteProduct(@PathVariable Long productId)
    {
        return CustomResponseEntity.builder()
                .statusCode(HttpStatus.NO_CONTENT.value())
                .status(HttpStatus.NO_CONTENT)
                .data(productService.deleteProduct(productId))
                .message("Deleted product information")
                .build();
    }

    @PutMapping("/products/{productId}")//Update/ Edit a product
    public CustomResponseEntity<?> updateProduct(@PathVariable Long productId,
                                                 @RequestBody ProductRequest productRequest)
    {
        return CustomResponseEntity.builder()
                .statusCode(httpOk.value())
                .status(httpOk)
                .data(productService.updateProduct(productId, productRequest))
                .message("Updated product information")
                .build();
    }

    @GetMapping("/products/{productId}")
    public CustomResponseEntity<?> getProductById(@PathVariable Long productId)
    {
        return CustomResponseEntity.builder()
                .statusCode(httpOk.value())
                .status(httpOk)
                .data(productService.findById(productId))
                .message("Information on product with Id: " + productId)
                .build();
    }

    @GetMapping("/orders")//Get list of all orders currently available
    public CustomResponseEntity<?> allOrders()
    {
        return CustomResponseEntity.builder()
                .statusCode(httpOk.value())
                .status(httpOk)
                .data(orderService.findAllOrders())
                .message("All orders currently available")
                .build();
    }

    @PutMapping("/orders/{orderId}/status")//Update/Edit order status
    public CustomResponseEntity<?> updateOrderStatus(@PathVariable(name = "orderId") Long orderId, @RequestParam(name = "status") String newOrderStatus)
    {
        return CustomResponseEntity.builder()
                .statusCode(httpOk.value())
                .status(httpOk)
                .data("New order status: " + orderService.updateOrderStatus(orderId, newOrderStatus).getStatus())
                .message("Updated status of the order")
                .build();
    }

    @GetMapping("/orders/{orderId}")//Get detailed information on the order
    public CustomResponseEntity<?> orderDetailsInfo(@PathVariable(name = "orderId") Long orderId)
    {
        return CustomResponseEntity.builder()
                .statusCode(httpOk.value())
                .status(httpOk)
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
                .statusCode(httpOk.value())
                .status(httpOk)
                .data(orderService.findOrdersByStatus(orderStatus))
                .message("List of Order having status of: " + orderStatus.toUpperCase())
                .build();
    }

    @PostMapping("/users/{userId}/role/{roleId}")
    public CustomResponseEntity<?> addRoleForUser(@PathVariable(name = "userId") Long userId,
                                                  @PathVariable(name = "roleId") Long roleId)
    {
        return CustomResponseEntity.builder()
                .statusCode(HttpStatus.CREATED.value())
                .status(HttpStatus.CREATED)
                .data(userService.addRoleForUser(userId, roleId))
                .message("Added role for this user")
                .build();
    }

    @DeleteMapping("/users/{userId}/role/{roleId}")
    public CustomResponseEntity<?> deleteUserRole(@PathVariable(name = "userId") Long userId,
                                                  @PathVariable(name = "roleId") Long roleId)
    {
        return CustomResponseEntity.builder()
                .statusCode(HttpStatus.NO_CONTENT.value())
                .status(HttpStatus.NO_CONTENT)
                .data(userService.deleteUserRole(userId, roleId))
                .message("Deleted role for this user")
                .build();
    }

    @GetMapping("/sales-revenue-over-time")//Get the sales revenue based on the time range passed in
    public CustomResponseEntity<?> getRevenue(@RequestBody TimeRangeRequest timeRangeRequest)
    {
        return CustomResponseEntity.builder()
                .statusCode(httpOk.value())
                .status(httpOk)
                .data(orderService.findRevenueInTime(timeRangeRequest.getFrom(), timeRangeRequest.getTo()))
                .message("Total revenue during this time")
                .build();
    }

    @GetMapping("/reports/best-seller-products")//Get list of best seller products in time period
    public CustomResponseEntity<?> bestSellerInTime(@RequestBody TimeRangeRequest timeRangeRequest)
    {
        return CustomResponseEntity.builder()
                .statusCode(httpOk.value())
                .status(httpOk)
                .data(productService.findBestSellerInTime(timeRangeRequest.getFrom(), timeRangeRequest.getTo()))
                .message("Products sold the most during this time range")
                .build();
    }

    @GetMapping("/reports/most-liked-products")//List of products that were added to wish list the most
    public CustomResponseEntity<?> mostLikedProducts()
    {
        return CustomResponseEntity.builder()
                .statusCode(httpOk.value())
                .status(httpOk)
                .data(productService.findMostLikedProducts())
                .message("10 products added to wish list the most")
                .build();
    }

    @GetMapping("/reports/revenue-by-category")
    public CustomResponseEntity<?> revenueByCategory()
    {
        return CustomResponseEntity.builder()
                .statusCode(httpOk.value())
                .status(httpOk)
                .data(categoryService.revenueByCategory())
                .message("Revenue per category")
                .build();
    }

    @GetMapping("/reports/top-spending-customers")
    public CustomResponseEntity<?> topSpendingCustomer(@RequestBody TimeRangeRequest timeRangeRequest)
    {
        return CustomResponseEntity.builder()
                .statusCode(httpOk.value())
                .status(httpOk)
                .data(userService.topSpendingCustomer(timeRangeRequest.getFrom(), timeRangeRequest.getTo()))
                .message("Top 10 spending customer during this time period")
                .build();
    }

    @GetMapping("/reports/new-accounts-this-month")
    public CustomResponseEntity<?> newAccountsStatistics()
    {
        return CustomResponseEntity.builder()
                .statusCode(httpOk.value())
                .status(httpOk)
                .data(userService.findNewAccountCurrentMonth())
                .message("List of new user accounts created in this month")
                .build();
    }

    @GetMapping("/reports/invoices-over-time")
    public CustomResponseEntity<?> invoicesInTimePeriod(@RequestBody TimeRangeRequest timeRangeRequest)
    {
        return CustomResponseEntity.builder()
                .statusCode(httpOk.value())
                .status(httpOk)
                .data(orderService.findNumberOfInvoicesInTimePeriod(timeRangeRequest.getFrom(), timeRangeRequest.getTo()))
                .message("Number of invoices exported during this time period")
                .build();
    }
}
