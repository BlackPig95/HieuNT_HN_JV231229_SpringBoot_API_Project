package com.ra.hieunt_hn_jv231229_project_module4_api.service.design;

import com.ra.hieunt_hn_jv231229_project_module4_api.model.dto.request.CartCheckoutRequest;
import com.ra.hieunt_hn_jv231229_project_module4_api.model.dto.request.ProductInCartRequest;
import com.ra.hieunt_hn_jv231229_project_module4_api.model.dto.response.CheckoutResponse;
import com.ra.hieunt_hn_jv231229_project_module4_api.model.dto.response.ShoppingCartDetailsResponse;

import java.util.List;

public interface IShoppingCartService
{
    List<ShoppingCartDetailsResponse> getResponseShoppingCarts();

    List<ShoppingCartDetailsResponse> addProductToCart(ProductInCartRequest productInCartRequest);

    List<ShoppingCartDetailsResponse> updateProductQuantity(ProductInCartRequest productInCartRequest);

    List<ShoppingCartDetailsResponse> deleteProductInCart(Long productId);

    String clearShoppingCart();

    CheckoutResponse checkOutCart(CartCheckoutRequest checkoutRequest);
}
