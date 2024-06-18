package com.ra.hieunt_hn_jv231229_project_module4_api.service.design;

import com.ra.hieunt_hn_jv231229_project_module4_api.exception.CustomException;
import com.ra.hieunt_hn_jv231229_project_module4_api.model.dto.response.WishListResponse;

import java.util.List;

public interface IWishListService
{
    WishListResponse addProductToWishlist(Long productId) throws CustomException;

    List<WishListResponse> getWishList() throws CustomException;

    WishListResponse deleteProductFromWishlist(Long wishListId) throws CustomException;
}
