package com.ra.hieunt_hn_jv231229_project_module4_api.controller;

import com.ra.hieunt_hn_jv231229_project_module4_api.model.dto.response.CustomResponseEntity;
import com.ra.hieunt_hn_jv231229_project_module4_api.model.dto.response.UserPageableResponse;
import com.ra.hieunt_hn_jv231229_project_module4_api.model.entity.Category;
import com.ra.hieunt_hn_jv231229_project_module4_api.model.entity.User;
import com.ra.hieunt_hn_jv231229_project_module4_api.service.design.ICategoryService;
import com.ra.hieunt_hn_jv231229_project_module4_api.service.design.IRoleService;
import com.ra.hieunt_hn_jv231229_project_module4_api.service.design.IUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api.myservice.com/v1/admin")
@RequiredArgsConstructor
public class AdminController
{
    private final IUserService userService;
    private final IRoleService roleService;
    private final HttpStatus http = HttpStatus.OK;
    private final ICategoryService categoryService;

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
}
