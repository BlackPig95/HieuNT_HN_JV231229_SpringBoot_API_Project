package com.ra.hieunt_hn_jv231229_project_module4_api.service.implementation;

import com.ra.hieunt_hn_jv231229_project_module4_api.model.constants.RoleName;
import com.ra.hieunt_hn_jv231229_project_module4_api.model.dto.request.ChangePasswordRequest;
import com.ra.hieunt_hn_jv231229_project_module4_api.model.dto.request.ChangeUserDetailRequest;
import com.ra.hieunt_hn_jv231229_project_module4_api.model.dto.response.UserPageableResponse;
import com.ra.hieunt_hn_jv231229_project_module4_api.model.dto.response.UserSideResponse;
import com.ra.hieunt_hn_jv231229_project_module4_api.model.entity.User;
import com.ra.hieunt_hn_jv231229_project_module4_api.objectmapper.UserMapper;
import com.ra.hieunt_hn_jv231229_project_module4_api.repository.IUserRepo;
import com.ra.hieunt_hn_jv231229_project_module4_api.security.jwt.JwtProvider;
import com.ra.hieunt_hn_jv231229_project_module4_api.security.principal.UserDetailCustom;
import com.ra.hieunt_hn_jv231229_project_module4_api.service.design.IShoppingCartService;
import com.ra.hieunt_hn_jv231229_project_module4_api.service.design.IUserService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements IUserService
{
    private static final Logger log = LoggerFactory.getLogger(UserServiceImpl.class);
    private final IUserRepo userRepo;
    private final JwtProvider jwtProvider;
    private final PasswordEncoder passwordEncoder;

    @Override
    public Page<UserPageableResponse> findAllUserPageable(Pageable pageable)
    {
        Page<User> userPage = userRepo.findAll(pageable);
        //Index start from 0 => Need to add 1 to balance with total page size
        if (pageable.getPageNumber() + 1 > userPage.getTotalPages())
        {
            throw new RuntimeException("Out of size of user list");
        }
        return userPage.map(UserMapper::toUserPageableResponse);
    }

    @Override
    public List<UserPageableResponse> findAllUsersByName(String fullname)
    {
        List<User> userList = userRepo.findUsersByFullnameContaining(fullname);
        return userList.stream().map(UserMapper::toUserPageableResponse).toList();
    }

    @Override
    public User findUserById(Long id)
    {
        return userRepo.findById(id).orElse(null);
    }

    @Override
    public User lockUserById(Long id)
    {
        User userRoleCheck = findUserById(id);
        if (userRoleCheck == null)
        {
            log.error("User not found");
            throw new NoSuchElementException("Lock not successful. No user found with Id number: " + id);
        }
        if (userRoleCheck.getRoles().stream().anyMatch(role -> role.getRoleName() == RoleName.ROLE_ADMIN))
        {
            throw new RuntimeException("Can't block admin");
        }
        userRoleCheck.setStatus(!userRoleCheck.getStatus());
        userRepo.save(userRoleCheck);
        return userRoleCheck;
    }

    @Override
    public UserSideResponse informationOfTheCurrentUser()
    {
        return getCurrentUserResponseInfo(getCurrentUSerDetailCustom());
    }

    @Override
    public boolean changePassword(ChangePasswordRequest passwordRequest)
    {   //Get the current user signed in detail info
        UserDetailCustom currentUSerDetailCustom = getCurrentUSerDetailCustom();
        //Get the user entity that will be later saved to database
        User currentSignedInUser = getSignedInUser();
        //Check if the oldPassword entered matches with the password encrypted in database
        if (!passwordEncoder.matches(passwordRequest.getOldPassword(), currentUSerDetailCustom.getPassword()))
        {
            throw new RuntimeException("Old password does not match");
        }
        //Check if the newPassword and confirmNewPassword matches
        if (passwordRequest.getNewPassword().equals(passwordRequest.getConfirmPassword()))
        {
            //If matched, allow to save new password to database
            currentSignedInUser.setPassword(passwordEncoder.encode(passwordRequest.getNewPassword()));
            userRepo.save(currentSignedInUser);
            return true;
        }
        return false;
    }

    @Override
    public UserSideResponse changeUserDetails(ChangeUserDetailRequest changeRequest)
    {
        UserDetailCustom currentUSerDetailCustom = getCurrentUSerDetailCustom();
        if (!passwordEncoder.matches(changeRequest.getEnterPassword(), currentUSerDetailCustom.getPassword()))
        {
            throw new RuntimeException("Cannot update. Wrong password");
        }
        List<String> phoneList = userRepo.findAllPhone();
        //Check if the newly updated phone number matches any of the phone numbers saved in database
        if (phoneList.stream().anyMatch(phone -> phone.equals(changeRequest.getPhone())))
        {
            //If yes, check to see if the phone matches with the current user's phone or not
            if (!changeRequest.getPhone().equals(currentUSerDetailCustom.getPhone()))
            {
                //Do not allow to use this phone number if it matches another user's phone number
                throw new RuntimeException("This phone number is already registered");
            }
        }
        User currentSignedInUser = getSignedInUser();
        //Set new info to the current user
        currentSignedInUser.setEmail(changeRequest.getEmail());
        currentSignedInUser.setFullname(changeRequest.getFullname());
        currentSignedInUser.setAvatar(changeRequest.getAvatar());
        currentSignedInUser.setPhone(changeRequest.getPhone());
        currentSignedInUser.setAddress(changeRequest.getAddress());
        //Set updatedAt to indicate the time this user performed changing information
        currentSignedInUser.setUpdatedAt(new Date());
        //Save info to database
        userRepo.save(currentSignedInUser);
        //Get the latest user info saved in database and return to the API call
        return getCurrentUserResponseInfo(getCurrentUSerDetailCustom());
    }

    private UserDetailCustom getCurrentUSerDetailCustom()
    {   //Support method to get the UserDetailCustom from principal
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return (UserDetailCustom) auth.getPrincipal();
    }

    private UserSideResponse getCurrentUserResponseInfo(UserDetailCustom userDetailCustom)
    {   //Support method to help format the user data to return to the calling API url
        String userToken = jwtProvider.createToken(userDetailCustom);
        //Always update info with the latest user info that was saved to database
        User currentSignedInUser = userRepo.findById(userDetailCustom.getUserId()).orElseThrow(() -> new RuntimeException("User with username " + userDetailCustom.getUsername() + " not found"));
        return UserSideResponse.builder()
                .username(currentSignedInUser.getUsername())
                .email(currentSignedInUser.getEmail())
                .fullname(currentSignedInUser.getFullname())
                .password(currentSignedInUser.getPassword())
                .avatar(currentSignedInUser.getAvatar())
                .phone(currentSignedInUser.getPhone())
                .address(currentSignedInUser.getAddress())
                .accessToken(userToken)
                .build();
    }

    @Override
    public User getSignedInUser()
    {
        //Support method to get the User entity of the currently logged in user
        //Extract from authentication object
        UserDetailCustom currentUSerDetailCustom = getCurrentUSerDetailCustom();
        return userRepo.findById(currentUSerDetailCustom.getUserId()).orElseThrow(() -> new RuntimeException("User with username " + currentUSerDetailCustom.getUsername() + " not found"));
    }
}
