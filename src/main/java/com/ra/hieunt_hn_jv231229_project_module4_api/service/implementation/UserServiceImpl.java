package com.ra.hieunt_hn_jv231229_project_module4_api.service.implementation;

import com.ra.hieunt_hn_jv231229_project_module4_api.exception.CustomException;
import com.ra.hieunt_hn_jv231229_project_module4_api.model.constants.OrderStatus;
import com.ra.hieunt_hn_jv231229_project_module4_api.model.constants.RoleName;
import com.ra.hieunt_hn_jv231229_project_module4_api.model.dto.request.ChangePasswordRequest;
import com.ra.hieunt_hn_jv231229_project_module4_api.model.dto.request.ChangeUserDetailRequest;
import com.ra.hieunt_hn_jv231229_project_module4_api.model.dto.response.OrderWithDetailResponse;
import com.ra.hieunt_hn_jv231229_project_module4_api.model.dto.response.UserPageableResponse;
import com.ra.hieunt_hn_jv231229_project_module4_api.model.dto.response.UserSideResponse;
import com.ra.hieunt_hn_jv231229_project_module4_api.model.dto.response.UserSpendingResponse;
import com.ra.hieunt_hn_jv231229_project_module4_api.model.entity.*;
import com.ra.hieunt_hn_jv231229_project_module4_api.objectmapper.UserMapper;
import com.ra.hieunt_hn_jv231229_project_module4_api.repository.*;
import com.ra.hieunt_hn_jv231229_project_module4_api.security.jwt.JwtProvider;
import com.ra.hieunt_hn_jv231229_project_module4_api.security.principal.UserDetailCustom;
import com.ra.hieunt_hn_jv231229_project_module4_api.service.design.IOrderService;
import com.ra.hieunt_hn_jv231229_project_module4_api.service.design.IUserService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements IUserService
{
    private static final Logger log = LoggerFactory.getLogger(UserServiceImpl.class);
    private final IUserRepo userRepo;
    private final JwtProvider jwtProvider;
    private final PasswordEncoder passwordEncoder;
    private final IOrderService orderService;
    private final IOrderRepo orderRepo;
    private final IOrderDetailrepo orderDetailrepo;
    private final IProductRepo productRepo;
    private final IRoleRepo roleRepo;

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

    @Override
    public User getSignedInUser()
    {
        //Support method to get the User entity of the currently logged in user
        //Extract from authentication object
        UserDetailCustom currentUSerDetailCustom = getCurrentUSerDetailCustom();
        return userRepo.findById(currentUSerDetailCustom.getUserId()).orElseThrow(() -> new RuntimeException("User with username " + currentUSerDetailCustom.getUsername() + " not found"));
    }

    @Override
    public List<OrderWithDetailResponse> getUserPurchaseHistory()
    {
        User currentUser = getSignedInUser();
        List<Long> orderIdList = orderService.findOrdersIdByUserId(currentUser.getUserId());
        List<OrderWithDetailResponse> userPurchaseHistory = new ArrayList<>();
        for (Long orderId : orderIdList)
        {
            OrderWithDetailResponse orderHistory = orderService.findOrderAndDetails(orderId);
            userPurchaseHistory.add(orderHistory);
        }
        return userPurchaseHistory;
    }

    @Override
    public OrderWithDetailResponse getPurchaseHistoryBySerial(String serialNumber) throws CustomException
    {
        List<OrderWithDetailResponse> userPurchaseHistory = getUserPurchaseHistory();
        for (OrderWithDetailResponse orderWithDetailResponse : userPurchaseHistory)
        {
            if (orderWithDetailResponse.getSerialNumber().equals(serialNumber))
            {
                return orderWithDetailResponse;
            }
        }
        throw new CustomException("Serial number did not match any order history", HttpStatus.NOT_FOUND);
    }

    @Override
    public OrderWithDetailResponse getPurchaseHistoryByStatus(String orderStatus) throws CustomException
    {
        boolean legitStatus = false;
        for (OrderStatus status : OrderStatus.values())
        {
            if (status.name().equalsIgnoreCase(orderStatus))
            {
                legitStatus = true;
                break;
            }
        }
        //If the spelling of the status word is wrong => Can't check
        if (!legitStatus)
        {
            throw new CustomException("Order status is not valid, please choose one of these status: WAITING, CONFIRM, DELIVERY, SUCCESS, CANCEL, DENIED", HttpStatus.CONFLICT);
        }
        List<OrderWithDetailResponse> userPurchaseHistory = getUserPurchaseHistory();
        for (OrderWithDetailResponse orderWithDetailResponse : userPurchaseHistory)
        {
            if (orderWithDetailResponse.getStatus().equalsIgnoreCase(orderStatus))
            {
                return orderWithDetailResponse;
            }
        }
        throw new CustomException("There is no order history with status of: " + orderStatus.toUpperCase(), HttpStatus.NOT_FOUND);
    }

    @Override
    public String cancelWaitingOrder(Long orderId)
    {
        Order order = orderRepo.findById(orderId).orElseThrow(() -> new RuntimeException("Order with id " + orderId + " not found"));
        //Get the orderDetail entities that matches this orderId
        List<OrderDetail> orderDetailList = orderDetailrepo.findAllByCompositeKeyOrderOrderId(orderId);
        //Get list of products attached to these order details
        List<Product> productList = new ArrayList<>();
        for (OrderDetail orderDetail : orderDetailList)
        {
            Product product = productRepo.findById(orderDetail.getCompositeKey().getProduct().getProductId()).orElseThrow(() -> new RuntimeException("Product not found"));
            productList.add(product);
        }
        if (order.getStatus().name().equals("WAITING"))
        {
            order.setStatus(OrderStatus.CANCEL);
            orderRepo.save(order);
            //After successfully cancel order, add the number of order quantity back to each product in database
            for (Product product : productList)
            {
                for (OrderDetail orderDetail : orderDetailList)
                {    //Check to make sure the productId matches the orderDetail's productId
                    if (product.getProductId().equals(orderDetail.getCompositeKey().getProduct().getProductId()))
                    {   //Return the amount of orderQuantity back to product's stockQuantity
                        product.setStockQuantity(product.getStockQuantity() + orderDetail.getOrderQuantity());
                    }
                }
            }
            //Update product stock quantity in database
            productRepo.saveAll(productList);
            return "Order with id " + orderId + " was canceled successfully";
        }
        throw new RuntimeException("Order with id " + orderId + " has a status of " + order.getStatus().name() +
                " .Can't cancel");
    }

    @Override
    public User addRoleForUser(Long userId, Long roleId)
    {
        User currentUser = userRepo.findById(userId).orElseThrow(() -> new RuntimeException("User with id " + userId + " not found"));
        Role addedRole = roleRepo.findById(roleId).orElseThrow(() -> new RuntimeException("No such role exist"));
        if (addedRole.getRoleName().name().equals("ROLE_ADMIN"))
        {
            throw new RuntimeException("It is forbidden to add administrator role for normal user");
        }
        //User roles is a set => Can't duplicate => No need to check duplication
        currentUser.getRoles().add(addedRole);
        return userRepo.save(currentUser);
    }

    @Override
    public User deleteUserRole(Long userId, Long roleId)
    {
        User currentUser = userRepo.findById(userId).orElseThrow(() -> new RuntimeException("User with id " + userId + " not found"));
        Role deletedRole = roleRepo.findById(roleId).orElseThrow(() -> new RuntimeException("No such role exist"));
        if (currentUser.getRoles().stream().anyMatch(role -> role.getRoleName().name().equals("ROLE_ADMIN"))
                && deletedRole.getRoleName().name().equals("ROLE_ADMIN"))
        {//Do not allow admin to delete his own admin role
            throw new RuntimeException("This is admin account, can't delete admin role privilege");
        }
        if (currentUser.getRoles().stream().noneMatch(role -> role.getRoleName().name().equals(deletedRole.getRoleName().name())))
        {
            throw new RuntimeException("Delete not needed. This user does not have the role " + deletedRole.getRoleName().name());
        }
        currentUser.getRoles().remove(deletedRole);
        return userRepo.save(currentUser);
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
    public List<UserSpendingResponse> topSpendingCustomer(Date from, Date to)
    {
        Date tempDate = new Date();
        //Make sure the date can be passed in regardless of order in calendar and still
        //provide the same result
        if (from.after(to))
        {
            tempDate = from;
            from = to;
            to = tempDate;
        }
        List<User> topSpendingUsers = userRepo.topSpendingCustomer(from, to);
        return topSpendingUsers.stream()
                .map(user -> UserSpendingResponse.builder()
                        .userId(user.getUserId())
                        .username(user.getUsername())
                        .email(user.getEmail())
                        .fullname(user.getFullname())
                        .status(user.getStatus())
                        .avatar(user.getAvatar())
                        .phone(user.getPhone())
                        .address(user.getAddress())
                        .roles(user.getRoles())
                        .totalSpending(orderRepo.findTotalPricePerUser(user.getUserId()))
                        .build()).toList();
    }

    @Override
    public List<User> findNewAccountCurrentMonth()
    {
        return userRepo.findNewAccountsCurrentMonth();
    }
}
