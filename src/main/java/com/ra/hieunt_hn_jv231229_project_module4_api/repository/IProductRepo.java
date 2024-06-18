package com.ra.hieunt_hn_jv231229_project_module4_api.repository;

import com.ra.hieunt_hn_jv231229_project_module4_api.model.dto.response.ProductSoldResponse;
import com.ra.hieunt_hn_jv231229_project_module4_api.model.entity.Product;
import jakarta.annotation.Nonnull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.Date;
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
    @Query("select p from Product p join OrderDetail od on p.productId=od.compositeKey.product.productId " +
            "join Order o on o.orderId=od.compositeKey.order.orderId where o.status='SUCCESS' " +
            "group by p.productId order by sum(od.orderQuantity) desc limit :limit")
    List<Product> findBestSellerProducts(Integer limit);

    Product findProductByProductName(String productName);

    boolean existsByProductName(String productName);

    @Override
    Page<Product> findAll(Pageable pageable);

    //Find products that sold the most in a time range
    @Query("select p from Product p join OrderDetail od on p.productId = od.compositeKey.product.productId " +
            "join Order o on o.orderId = od.compositeKey.order.orderId " +
            "where o.status='SUCCESS' and o.receivedAt between :from and :to " +
            "group by p.productId order by sum(od.orderQuantity) desc limit 10")
    List<Product> findBestSellerInTime(Date from, Date to);

    //Find products that were added to wishlist the most
    //Count number of user for each product id => number of likes for this product
    //Order desc group by productId to select out the most liked products descending
    //From this wishlist table, get the list of product
    //Could not use where in clause?? MYSQL version doesn't support??
//    @Query("select distinct p from Product p join (select w.product.productId from WishList w group by w.product.productId " +
//            "order by count (w.user.userId) desc limit 2) wishtable on p.productId=wishtable.productId")
//    @Query("    select p" +
//            "    from Product p" +
//            "    where p.productId in (select distinct p.productId" +
//            "        from (select w.product.productId" +
//            "        from WishList w" +
//            "        group by w.product.productId" +
//            "        order by count(w.user.userId) desc" +
//            "    limit 2) as wishtable)")
//    List<Product> findMostLikedProduct();

//    select p.*
//    from product p
//    where p.product_id in (select distinct product_id
//        from (select w1.product_id
//        from wish_list w1
//        group by w1.product_id
//        order by count(w1.user_id) desc
//    limit 2) as wishtable);
}
