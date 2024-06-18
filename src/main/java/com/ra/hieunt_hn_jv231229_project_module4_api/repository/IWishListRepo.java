package com.ra.hieunt_hn_jv231229_project_module4_api.repository;

import com.ra.hieunt_hn_jv231229_project_module4_api.model.dto.response.WishListResponse;
import com.ra.hieunt_hn_jv231229_project_module4_api.model.entity.User;
import com.ra.hieunt_hn_jv231229_project_module4_api.model.entity.WishList;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IWishListRepo extends JpaRepository<WishList, Long>
{
    List<WishList> findAllByUser(User user);

    @Query("select w.product.productId from WishList w group by w.product.productId order by count(w.user.userId) desc limit 10")
    List<Long> listMostLikeProductId();

    Integer countByProductProductId(Long productId);
}
