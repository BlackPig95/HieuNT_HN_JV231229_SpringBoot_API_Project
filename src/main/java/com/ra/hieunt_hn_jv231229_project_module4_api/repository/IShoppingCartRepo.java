package com.ra.hieunt_hn_jv231229_project_module4_api.repository;

import com.ra.hieunt_hn_jv231229_project_module4_api.model.entity.ShoppingCart;
import com.ra.hieunt_hn_jv231229_project_module4_api.model.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface IShoppingCartRepo extends JpaRepository<ShoppingCart, Long>
{
    Optional<List<ShoppingCart>> findShoppingCartsByUser(User user);
}
