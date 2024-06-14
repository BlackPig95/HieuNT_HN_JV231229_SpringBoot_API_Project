package com.ra.hieunt_hn_jv231229_project_module4_api.security.principal;

import com.ra.hieunt_hn_jv231229_project_module4_api.model.entity.User;
import com.ra.hieunt_hn_jv231229_project_module4_api.repository.IUserRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserDetailServiceCustom implements UserDetailsService
{
    private final IUserRepo userRepo;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException
    {
        Optional<User> optionalUser = userRepo.findByUsername(username);
        if (optionalUser.isPresent())
        {
            User user = optionalUser.get();
            return UserDetailCustom.builder()
                    .username(user.getUsername())
                    .email(user.getEmail())
                    .password(user.getPassword())
                    .roles(user.getRoles())
                    .authorities(user.getRoles().stream().
                            map(role -> new SimpleGrantedAuthority(role.getRoleName().name())).toList())
                    .build();
        }
        throw new UsernameNotFoundException("No such username exist");
    }
}
