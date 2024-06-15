package com.ra.hieunt_hn_jv231229_project_module4_api.repository;

import com.ra.hieunt_hn_jv231229_project_module4_api.model.entity.Category;
import com.ra.hieunt_hn_jv231229_project_module4_api.model.entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ICategoryRepo extends JpaRepository<Category, Long>, PagingAndSortingRepository<Category, Long>
{
    //    @Query("select c from Category c where c.categoryId in " +
//            "(select productId from Product where productId in (select distinct o.compositeKey.product.productId from OrderDetail o))")
    @Query("select c from Category c where c.categoryId in :soldProductId")
    List<Category> findSoldCategories(List<Long> soldProductId);

    @Override
    Page<Category> findAll(Pageable pageable);
}
