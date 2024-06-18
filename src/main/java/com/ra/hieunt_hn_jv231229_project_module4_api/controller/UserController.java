package com.ra.hieunt_hn_jv231229_project_module4_api.controller;

import com.ra.hieunt_hn_jv231229_project_module4_api.exception.CustomException;
import com.ra.hieunt_hn_jv231229_project_module4_api.model.dto.request.*;
import com.ra.hieunt_hn_jv231229_project_module4_api.model.dto.response.CustomResponseEntity;
import com.ra.hieunt_hn_jv231229_project_module4_api.service.design.IAddressService;
import com.ra.hieunt_hn_jv231229_project_module4_api.service.design.IShoppingCartService;
import com.ra.hieunt_hn_jv231229_project_module4_api.service.design.IUserService;
import com.ra.hieunt_hn_jv231229_project_module4_api.service.design.IWishListService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api.myservice.com/v1/user")
@RequiredArgsConstructor
public class UserController
{
    private final HttpStatus http = HttpStatus.OK;
    private final IUserService userService;
    private final IShoppingCartService shoppingCartService;
    private final IAddressService addressService;
    private final IWishListService wishlistService;

    @GetMapping("/account")//Information about the user currently signed in
    public CustomResponseEntity<?> userDetails()
    {
        return CustomResponseEntity.builder()
                .statusCode(http.value())
                .status(http)
                .data(userService.informationOfTheCurrentUser())
                .message("Your detailed information")
                .build();
    }

    @PutMapping("/account/change-password")//Allow current user to change password
    public CustomResponseEntity<?> changePassword(@RequestBody ChangePasswordRequest passwordRequest)
    {
        return CustomResponseEntity.builder()
                .statusCode(http.value())
                .status(http)
                .data(userService.changePassword(passwordRequest) ? "Updated password successfully" : "Update failed. Please check your new password and confirm new password fields match")
                .message("Change password request")
                .build();
    }

    @PutMapping("/account")//Update user information
    public CustomResponseEntity<?> updateUserDetails(@Valid @RequestBody ChangeUserDetailRequest changeRequest)
    {
        return CustomResponseEntity.builder()
                .statusCode(http.value())
                .status(http)
                .data(userService.changeUserDetails(changeRequest))
                .message("Updated information")
                .build();
    }

    @GetMapping("/cart/list")//Get list of products currently availabe in user's shopping cart
    public CustomResponseEntity<?> productsInShoppingCart()
    {
        return CustomResponseEntity.builder()
                .statusCode(http.value())
                .status(http)
                .data(shoppingCartService.getResponseShoppingCarts())
                .message("Your current shopping cart info")
                .build();
    }

    @PostMapping("/cart/add")//Add new product to shopping cart
    public CustomResponseEntity<?> addProductToShoppingCart(@RequestBody ProductInCartRequest productInCartRequest)
    {
        return CustomResponseEntity.builder()
                .statusCode(http.value())
                .status(http)
                .data(shoppingCartService.addProductToCart(productInCartRequest))
                .message("Added product. Here is your latest shopping cart info")
                .build();
    }

    @PutMapping("/cart/items/{cartItemId}")//Update product quantity in shopping cart
    public CustomResponseEntity<?> updateProductQuantity(@PathVariable(name = "cartItemId") Long productId,
                                                         @RequestBody ProductInCartRequest productInCartRequest)
    {
        productInCartRequest.setProductId(productId);
        return CustomResponseEntity.builder()
                .statusCode(http.value())
                .status(http)
                .data(shoppingCartService.updateProductQuantity(productInCartRequest))
                .message("Updated product quantity. Here is your latest shopping cart info")
                .build();
    }

    @DeleteMapping("/cart/items/{cartItemId}")//Remove 1 item from shopping cart
    public CustomResponseEntity<?> deleteProductInCart(@PathVariable(name = "cartItemId") Long productId)
    {
        return CustomResponseEntity.builder()
                .statusCode(http.value())
                .status(http)
                .data(shoppingCartService.deleteProductInCart(productId))
                .message("Removed product from your shopping cart. Here is your latest shopping cart info")
                .build();
    }

    @DeleteMapping("/cart/clear")//Remove the whole shopping cart
    public CustomResponseEntity<?> clearShoppingCart()
    {
        return CustomResponseEntity.builder()
                .statusCode(http.value())
                .status(http)
                .data(shoppingCartService.clearShoppingCart())
                .message("No items in shopping cart")
                .build();
    }

    @PostMapping("/cart/checkout")
    public CustomResponseEntity<?> checkOutCart(@RequestBody CartCheckoutRequest checkoutRequest)
    {
        return CustomResponseEntity.builder()
                .statusCode(http.value())
                .status(http)
                .data(shoppingCartService.checkOutCart(checkoutRequest))
                .message("Checkout done. Here is your order info")
                .build();
    }

    @PostMapping("/account/addresses")
    public CustomResponseEntity<?> addNewAddress(@Valid @RequestBody AddressRequest addressRequest)
    {
        return CustomResponseEntity.builder()
                .statusCode(http.value())
                .status(http)
                .data(addressService.addNewAddress(addressRequest))
                .message("Request to add address completed")
                .build();
    }

    @DeleteMapping("/account/addresses/{addressId}")
    public CustomResponseEntity<?> deleteAddress(@PathVariable Long addressId)
    {
        return CustomResponseEntity.builder()
                .statusCode(http.value())
                .status(http)
                .data(addressService.deleteAddress(addressId))
                .message("Request to delete address completed")
                .build();
    }

    @GetMapping("/account/addresses")//Get list of current user's addresses
    public CustomResponseEntity<?> getAllAddress()
    {
        return CustomResponseEntity.builder()
                .statusCode(http.value())
                .status(http)
                .data(addressService.getAllAddresses())
                .message("Your addresses info")
                .build();
    }

    @GetMapping("/account/addresses/{addressId}")//Get info on only 1 address
    public CustomResponseEntity<?> getAddressById(@PathVariable Long addressId)
    {
        return CustomResponseEntity.builder()
                .statusCode(http.value())
                .status(http)
                .data(addressService.getAddressById(addressId))
                .message("Address info of the address with Id: " + addressId)
                .build();
    }

    @GetMapping("/history")//Get user's purchase history
    public CustomResponseEntity<?> getHistory()
    {
        return CustomResponseEntity.builder()
                .statusCode(http.value())
                .status(http)
                .data(userService.getUserPurchaseHistory())
                .message("Purchase history")
                .build();
    }

    @GetMapping("/history/{serialNumber}")//Retrieve order history by serial number
    public CustomResponseEntity<?> getHistoryBySerial(@PathVariable String serialNumber) throws CustomException
    {
        return CustomResponseEntity.builder()
                .statusCode(http.value())
                .status(http)
                .data(userService.getPurchaseHistoryBySerial(serialNumber))
                .message("Purchase history")
                .build();
    }

    //Have to change URL to avoid ambiguous call with the above API
    @GetMapping("/history/{orderStatus}/status")//Retrieve all order history with status
    public CustomResponseEntity<?> getHistoryByStatus(@PathVariable String orderStatus) throws CustomException
    {
        return CustomResponseEntity.builder()
                .statusCode(http.value())
                .status(http)
                .data(userService.getPurchaseHistoryByStatus(orderStatus))
                .message("Purchase history")
                .build();
    }

    @PutMapping("/history/{orderId}/cancel")
    public CustomResponseEntity<?> cancelWaitingOrder(@PathVariable Long orderId)
    {
        return CustomResponseEntity.builder()
                .statusCode(http.value())
                .status(http)
                .data(userService.cancelWaitingOrder(orderId))
                .message("Request to cancel waiting order completed")
                .build();
    }

    @PostMapping("/wish-list")
    public CustomResponseEntity<?> addProductToWishlist(@RequestParam Long productId) throws CustomException
    {
        return CustomResponseEntity.builder()
                .statusCode(http.value())
                .status(http)
                .data(wishlistService.addProductToWishlist(productId))
                .message("Information of the product added to your wishlist")
                .build();
    }

    @GetMapping("/wish-list")
    public CustomResponseEntity<?> getWishList() throws CustomException
    {
        return CustomResponseEntity.builder()
                .statusCode(http.value())
                .status(http)
                .data(wishlistService.getWishList())
                .message("Here is your wishlist items")
                .build();
    }

    @DeleteMapping("/wish-list/{wishListId}")
    public CustomResponseEntity<?> deleteFromWishList(@PathVariable Long wishListId) throws CustomException
    {
        return CustomResponseEntity.builder()
                .statusCode(http.value())
                .status(http)
                .data(wishlistService.deleteProductFromWishlist(wishListId))
                .message("This product is deleted from your wishlist")
                .build();
    }
}
