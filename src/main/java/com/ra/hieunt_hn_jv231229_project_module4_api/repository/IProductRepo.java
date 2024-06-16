package com.ra.hieunt_hn_jv231229_project_module4_api.repository;

import com.ra.hieunt_hn_jv231229_project_module4_api.model.entity.Product;
import jakarta.annotation.Nonnull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IProductRepo extends JpaRepository<Product, Long>, PagingAndSortingRepository<Product, Long>
{   //Check whether the productId is present in OrderDetail or not, if present => Product sold
    //Utilize this method, can find out products that were sold => Can use to find out categories attached with them
    @Query("select p from Product p where p.productId in (select distinct o.compositeKey.product.productId from OrderDetail o)")
    Page<Product> findAllProductsSold(Pageable pageable);

    @Query("select p from Product p where p.category.categoryId = :catId")
    List<Product> findProductByCategory(Long catId);

    @Query("select p from Product p order by p.createdAt desc limit :limit")
    List<Product> findNewestProducts(Integer limit);

    //Using auto query function of JpaRepo
    List<Product> findProductsByProductNameContainingOrDescriptionContaining(String productName, String description);

    //Based on total quantity sold in all OrderDetails with the same productId
    @Query("select p from Product p left join OrderDetail od on p.productId=od.compositeKey.product.productId " +
            "group by p.productId order by sum(od.orderQuantity) desc limit :limit")
    List<Product> findBestSellerProducts(Integer limit);

    Product findProductByProductName(String productName);

    boolean existsByProductName(String productName);

    @Override
    Page<Product> findAll(Pageable pageable);
}
