package com.ra.hieunt_hn_jv231229_project_module4_api.repository;

import com.ra.hieunt_hn_jv231229_project_module4_api.model.entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IProductRepo extends JpaRepository<Product, Long>, PagingAndSortingRepository<Product, Long>
{
    Page<Product> findAll(Pageable pageable);

    @Query("select p from Product p where p.category.categoryId = :catId")
    List<Product> findProductByCategory(Long catId);

    @Query("select p from Product p order by p.createdAt desc limit :limit")
    List<Product> findNewestProducts(Integer limit);
}
