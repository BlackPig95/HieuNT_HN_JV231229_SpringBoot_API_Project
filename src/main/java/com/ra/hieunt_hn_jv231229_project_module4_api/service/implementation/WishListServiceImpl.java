package com.ra.hieunt_hn_jv231229_project_module4_api.service.implementation;

import com.ra.hieunt_hn_jv231229_project_module4_api.exception.CustomException;
import com.ra.hieunt_hn_jv231229_project_module4_api.model.dto.response.WishListResponse;
import com.ra.hieunt_hn_jv231229_project_module4_api.model.entity.Product;
import com.ra.hieunt_hn_jv231229_project_module4_api.model.entity.User;
import com.ra.hieunt_hn_jv231229_project_module4_api.model.entity.WishList;
import com.ra.hieunt_hn_jv231229_project_module4_api.repository.IProductRepo;
import com.ra.hieunt_hn_jv231229_project_module4_api.repository.IWishListRepo;
import com.ra.hieunt_hn_jv231229_project_module4_api.service.design.IUserService;
import com.ra.hieunt_hn_jv231229_project_module4_api.service.design.IWishListService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class WishListServiceImpl implements IWishListService
{
    private final IProductRepo productRepo;
    private final IUserService userService;
    private final IWishListRepo wishlistRepo;

    @Override
    public WishListResponse addProductToWishlist(Long productId) throws CustomException
    {
        Product addedProduct = productRepo.findById(productId).orElseThrow(() -> new CustomException("This product does not exist", HttpStatus.CONFLICT));
        User currentUser = userService.getSignedInUser();
        List<WishList> userWishList = wishlistRepo.findAllByUser(currentUser);
        for (WishList wishList : userWishList)
        {
            if (wishList.getProduct().getProductId().equals(productId))
            {
                throw new CustomException("This product is already in your wishlist", HttpStatus.CONFLICT);
            }
        }
        WishList newFavorite = WishList.builder()
                .product(addedProduct)
                .user(currentUser)
                .build();
        wishlistRepo.save(newFavorite);
        return WishListResponse.builder()
                .productId(addedProduct.getProductId())
                .productName(addedProduct.getProductName())
                .productDescription(addedProduct.getDescription())
                .productPrice(addedProduct.getUnitPrice())
                .productCategory(addedProduct.getCategory().getCategoryName())
                .build();
    }

    @Override
    public List<WishListResponse> getWishList() throws CustomException
    {
        User currentUser = userService.getSignedInUser();
        List<WishList> userWishList = wishlistRepo.findAllByUser(currentUser);
        if (userWishList.isEmpty())
        {
            throw new CustomException("You don't have any items in wishlist", HttpStatus.NOT_FOUND);
        }
        List<WishListResponse> userWishListResponse = new ArrayList<>();
        for (WishList wishList : userWishList)
        {
            userWishListResponse.add(WishListResponse.builder()
                    .productId(wishList.getWishListId())
                    .productName(wishList.getProduct().getProductName())
                    .productDescription(wishList.getProduct().getDescription())
                    .productPrice(wishList.getProduct().getUnitPrice())
                    .productCategory(wishList.getProduct().getCategory().getCategoryName())
                    .build());
        }
        return userWishListResponse;
    }

    @Override
    public WishListResponse deleteProductFromWishlist(Long wishListId) throws CustomException
    {
        WishList deletedWishList = wishlistRepo.findById(wishListId).orElseThrow(() -> new CustomException("This wish list Id does not exist", HttpStatus.NOT_FOUND));
        if (userService.getSignedInUser().getUserId().equals(deletedWishList.getUser().getUserId()))
        {
            wishlistRepo.delete(deletedWishList);
            return WishListResponse.builder()
                    .productId(deletedWishList.getWishListId())
                    .productName(deletedWishList.getProduct().getProductName())
                    .productDescription(deletedWishList.getProduct().getDescription())
                    .productPrice(deletedWishList.getProduct().getUnitPrice())
                    .productCategory(deletedWishList.getProduct().getCategory().getCategoryName())
                    .build();
        }
        //If different user => Can't delete wish list from another user
        throw new CustomException("This wish list Id does not exist on your wish list", HttpStatus.NOT_FOUND);
    }
}
