package com.ra.hieunt_hn_jv231229_project_module4_api.model.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.ra.hieunt_hn_jv231229_project_module4_api.validator.annotation.PhoneExist;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;
import java.util.Set;

@Entity
@Table(name = "user")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class User
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id", unique = true)
    private Long userId;
    @Column(name = "username", length = 100)
    private String username;
    @Column(name = "email", length = 255)
    private String email;
    @Column(name = "fullname", length = 100, nullable = false)
    private String fullname;
    @Column(name = "status")
    private Boolean status = true;
    @Column(name = "password")
    private String password;
    @Column(name = "avatar", length = 255)
    private String avatar;
    @Column(name = "phone", length = 15, unique = true)
    private String phone;
    @Column(name = "address", length = 255, nullable = false)
    private String address;
    @Column(name = "created_at")
    @Temporal(TemporalType.DATE)
    @JsonFormat(pattern = "dd-MM-yyyy")
    private Date createdAt = new Date();
    @Column(name = "updated_at")
    @Temporal(TemporalType.DATE)
    @JsonFormat(pattern = "dd-MM-yyyy")
    private Date updatedAt;
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "user_role",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id"))
    private Set<Role> roles;
}
