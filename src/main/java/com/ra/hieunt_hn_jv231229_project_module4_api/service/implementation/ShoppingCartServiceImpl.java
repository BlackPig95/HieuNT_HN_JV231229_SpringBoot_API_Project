package com.ra.hieunt_hn_jv231229_project_module4_api.service.implementation;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.ra.hieunt_hn_jv231229_project_module4_api.model.constants.OrderStatus;
import com.ra.hieunt_hn_jv231229_project_module4_api.model.dto.request.CartCheckoutRequest;
import com.ra.hieunt_hn_jv231229_project_module4_api.model.dto.request.ProductInCartRequest;
import com.ra.hieunt_hn_jv231229_project_module4_api.model.dto.response.CheckoutProductDetailResponse;
import com.ra.hieunt_hn_jv231229_project_module4_api.model.dto.response.CheckoutResponse;
import com.ra.hieunt_hn_jv231229_project_module4_api.model.dto.response.OrderDetailWithProductInfoResponse;
import com.ra.hieunt_hn_jv231229_project_module4_api.model.dto.response.ShoppingCartDetailsResponse;
import com.ra.hieunt_hn_jv231229_project_module4_api.model.entity.*;
import com.ra.hieunt_hn_jv231229_project_module4_api.repository.*;
import com.ra.hieunt_hn_jv231229_project_module4_api.service.design.IShoppingCartService;
import com.ra.hieunt_hn_jv231229_project_module4_api.service.design.IUserService;
import jakarta.persistence.*;
import lombok.RequiredArgsConstructor;
import org.hibernate.type.descriptor.DateTimeUtils;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ShoppingCartServiceImpl implements IShoppingCartService
{
    private final IShoppingCartRepo shoppingCartRepo;
    private final IUserService userService;
    private final IProductRepo productRepo;
    private final IOrderRepo orderRepo;
    private final IOrderDetailrepo orderDetailrepo;

    @Override
    public List<ShoppingCartDetailsResponse> getResponseShoppingCarts()
    {
        //Get the shopping cart entities that belongs to the current signed in user
        //Each user can have multiple cart items => Need to get a list
        List<ShoppingCart> shoppingCartList = getCurrentUserShoppingCartInfo();
        //Response entity to return to the view of the API call
        List<ShoppingCartDetailsResponse> detailCartList = new ArrayList<>();
        for (ShoppingCart cart : shoppingCartList)
        {
            //Set information for each product in the cart
            Product product = cart.getProduct();
            ShoppingCartDetailsResponse shoppingCartDetailsResponse
                    = ShoppingCartDetailsResponse.builder()
                    .orderQuantity(cart.getOrderQuantity())
                    .productName(product.getProductName())
                    .unitPrice(product.getUnitPrice())
                    .description(product.getDescription())
                    .image(product.getImage())
                    .build();
            detailCartList.add(shoppingCartDetailsResponse);
        }
        return detailCartList;
    }

    @Override
    public List<ShoppingCartDetailsResponse> addProductToCart(ProductInCartRequest productInCartRequest)
    {
        //Get the shopping cart entities that belongs to the current signed in user
        //Each user can have multiple cart items => Need to get a list
        List<ShoppingCart> shoppingCartList = getCurrentUserShoppingCartInfo();
        for (ShoppingCart cart : shoppingCartList)
        {   //Check if the product being chosen is already presented in the cart or not
            //Need to check both current user and productId to make sure it's the same shoppingCart
            //entity (2 foreign keys)
            if (cart.getProduct().getProductId().equals(productInCartRequest.getProductId())
                    && cart.getUser().getUserId().equals(userService.getSignedInUser().getUserId()))
            {   //If already exist => Update new quantity
                cart.setOrderQuantity(cart.getOrderQuantity() + productInCartRequest.getQuantity());
                if (cart.getOrderQuantity() <= 0)
                {
                    throw new RuntimeException("Total order quantity of product " + cart.getProduct().getProductName() + " must be greater than 0");
                }
                //There can be only 1 shopping cart entity with both productId and userId
                //that matches the newly added product => Once found, can immediately finish loop
                shoppingCartRepo.save(cart);
                return getResponseShoppingCarts();
            }
        }
        //If the loop finished without returning => ProductId did not exist
        //=> Need to create a new cart to hold this new product
        Product productAdded = productRepo.findById(productInCartRequest.getProductId()).orElseThrow(() -> new RuntimeException("This product does not exist"));
        // Still need to check to make sure the quantity added is not smaller than 0
        if (productInCartRequest.getQuantity() <= 0)
        {
            throw new RuntimeException("Total order quantity of product " + productAdded.getProductName() + " must be greater than 0");
        }
        //Create a new shopping cart entity to hold the value of the newly added product
        ShoppingCart newCart = new ShoppingCart();
        //Set new productId as the foreign key
        newCart.setProduct(productAdded);
        newCart.setOrderQuantity(productInCartRequest.getQuantity());
        //Set the currently logged-in user as the foreign key
        newCart.setUser(userService.getSignedInUser());
        shoppingCartRepo.save(newCart);
        return getResponseShoppingCarts();
    }

    @Override
    public List<ShoppingCartDetailsResponse> updateProductQuantity(ProductInCartRequest productInCartRequest)
    {
        List<ShoppingCart> shoppingCartList = getCurrentUserShoppingCartInfo();
        //Check to see if the product exist or not
        Product updatedProduct = productRepo.findById(productInCartRequest.getProductId()).orElseThrow(() -> new RuntimeException("This product does not exist"));
        for (ShoppingCart cart : shoppingCartList)
        {   //Check if the product being chosen is already presented in the cart or not
            //Need to check both current user and productId to make sure it's the same shoppingCart
            //entity (2 foreign keys)
            if (cart.getProduct().getProductId().equals(productInCartRequest.getProductId())
                    && cart.getUser().getUserId().equals(userService.getSignedInUser().getUserId()))
            {
                if (productInCartRequest.getQuantity() <= 0)
                {
                    throw new RuntimeException("Total order quantity of product " + cart.getProduct().getProductName() + " must be greater than 0");
                }
                //If already exist => Update new quantity with the quantity customer provided
                cart.setOrderQuantity(productInCartRequest.getQuantity());
                //There can be only 1 shopping cart entity with both productId and userId
                //that matches the newly added product => Once found, can immediately finish loop
                shoppingCartRepo.save(cart);
                return getResponseShoppingCarts();
            }
        }
        //If the product does not present in the shopping cart, cannot update quantity
        throw new RuntimeException(updatedProduct.getProductName() + " is not available in your shopping cart. Please add this product to shopping cart first");
    }

    @Override
    public List<ShoppingCartDetailsResponse> deleteProductInCart(Long productId)
    {
        List<ShoppingCart> shoppingCartList = getCurrentUserShoppingCartInfo();
        //Check to see if the product exist or not
        Product updatedProduct = productRepo.findById(productId).orElseThrow(() -> new RuntimeException("This product does not exist"));
        for (ShoppingCart cart : shoppingCartList)
        {   //Check if the product being chosen is already presented in the cart or not
            //Need to check both current user and productId to make sure it's the same shoppingCart
            //entity (2 foreign keys)
            if (cart.getProduct().getProductId().equals(productId)
                    && cart.getUser().getUserId().equals(userService.getSignedInUser().getUserId()))
            {
                //If the product exist => Delete the shopping cart entity that holds this product
                shoppingCartRepo.delete(cart);
                return getResponseShoppingCarts();
            }
        }
        throw new RuntimeException("Can't remove item. " + updatedProduct.getProductName() + " does not exist in your shopping cart");
    }

    @Override
    public String clearShoppingCart()
    {
        List<ShoppingCart> shoppingCartList = getCurrentUserShoppingCartInfo();
        shoppingCartRepo.deleteAll(shoppingCartList);
        return "Your shopping cart is cleared";
    }

    @Override
    public CheckoutResponse checkOutCart(CartCheckoutRequest checkoutRequest)
    {
        User currentUser = userService.getSignedInUser();
        List<ShoppingCart> shoppingCartList = getCurrentUserShoppingCartInfo();
        //If no items in cart, don't allow checkout
        if (shoppingCartList.isEmpty())
        {
            throw new RuntimeException("No items in cart. Cannot checkout");
        }
        Double totalOrderPrice = //Calculate the total price of the order based on quantity and product price
                shoppingCartList.stream().mapToDouble(cart -> cart.getOrderQuantity() * cart.getProduct().getUnitPrice()).sum();
        Order orderCheckedOut =
                Order.builder()
                        .totalPrice(totalOrderPrice)
                        .status(OrderStatus.WAITING)
                        .note(checkoutRequest.getNote())
                        //If customer didn't add new receive name => get the current user name as receive name
                        .receiveName(checkoutRequest.getReceiveName() != null ? checkoutRequest.getReceiveName() : currentUser.getFullname())
                        //If the customer didn't provide new shipping address => get current user address
                        .receiveAddress(checkoutRequest.getReceiveAddress() != null ? checkoutRequest.getReceiveAddress() : currentUser.getAddress())
                        //If the customer didn't provide new phone number => get current user phone number
                        .receivePhone(checkoutRequest.getReceivePhone() != null ? checkoutRequest.getReceivePhone() : currentUser.getPhone())
                        //The date the order is created is on the day the checkout request was made
                        .createdAt(new Date())
                        //Received date is estimated to be 4 days from the day order created
                        .receivedAt(new Date(new Date().getTime() + (4 * 1000 * 60 * 60 * 24)))
                        .user(currentUser)
                        .build();
        //Set up serial number for the order
        orderCheckedOut.setSerialNumber(UUID.randomUUID().toString());
        //Save the order into database
        orderRepo.save(orderCheckedOut);
        //Create a list of orderDetail to save to database when checkout is performed
        List<OrderDetail> orderDetailList = new ArrayList<>();
        //Also create response entity to return to the API call's body
        List<CheckoutProductDetailResponse> listOrderDetailResponse = new ArrayList<>();
        for (ShoppingCart cart : shoppingCartList)
        {
            //For each product in the cart, build a new orderDetail entity which links to
            //the newly created order and the product in the cart
            Product product = cart.getProduct();
            OrderDetail orderCheckoutDetail =
                    OrderDetail.builder()
                            .name(product.getProductName())
                            .unitPrice(product.getUnitPrice())
                            .orderQuantity(cart.getOrderQuantity())
                            .build();
            OrderDetailCompositeKey compositeKey = new OrderDetailCompositeKey();
            //Create composite key for this newly added orderDetail
            compositeKey.setProduct(product);
            compositeKey.setOrder(orderCheckedOut);
            //Set composite key => primary + foreign key
            orderCheckoutDetail.setCompositeKey(compositeKey);
            //Add these order detail entity to the list
            orderDetailList.add(orderCheckoutDetail);

            CheckoutProductDetailResponse orderDetailResponse
                    = CheckoutProductDetailResponse.builder()
                    .productName(product.getProductName())
                    .description(product.getDescription())
                    .pricePerProduct(product.getUnitPrice())
                    .image(product.getImage())
                    .productCategoryName(product.getCategory().getCategoryName())
                    .build();
            listOrderDetailResponse.add(orderDetailResponse);
        }
        //Save orderDetailList to database
        orderDetailrepo.saveAll(orderDetailList);
        //After done saving all info on order and order detail, remove the current cart item
        //to clear the cart and allow user to make new cart
        clearShoppingCart();
        //Create a response to return to the API call's body
        return CheckoutResponse.builder()
                .serialNumber(orderCheckedOut.getSerialNumber())
                .totalPrice(orderCheckedOut.getTotalPrice())
                .status(orderCheckedOut.getStatus().name())
                .note(orderCheckedOut.getNote())
                .receiveName(orderCheckedOut.getReceiveName())
                .receiveAddress(orderCheckedOut.getReceiveAddress())
                .receivePhone(orderCheckedOut.getReceivePhone())
                .checkoutDate(orderCheckedOut.getCreatedAt())
                .expectedReceiveDate(orderCheckedOut.getReceivedAt())
                //Set the list created above to this entity
                .listOrderDetail(listOrderDetailResponse)
                .build();
    }

    //Support method to get the list of shopping cart entities of current user
    private List<ShoppingCart> getCurrentUserShoppingCartInfo()
    {
        User currentUser = userService.getSignedInUser();
        return shoppingCartRepo.findShoppingCartsByUser(currentUser).orElseThrow(() -> new RuntimeException("User not found. Can't display shopping cart info"));
    }
}
