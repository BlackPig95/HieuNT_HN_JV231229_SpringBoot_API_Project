package com.ra.hieunt_hn_jv231229_project_module4_api.repository;

import com.ra.hieunt_hn_jv231229_project_module4_api.model.entity.User;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface IUserRepo extends JpaRepository<User, Long>, PagingAndSortingRepository<User, Long>
{
    Optional<User> findByUsername(String username);

    boolean existsByPhone(String phone);

    boolean existsByUsername(String username);

    Page<User> findAll(Pageable pageable);

    List<User> findUsersByFullnameContaining(String fullname);

//    @Modifying
//    @Transactional
//    @Query("update User u set u.status = not (u.status) where u.userId = :id")
//    void lockUserById(Long id);
}
