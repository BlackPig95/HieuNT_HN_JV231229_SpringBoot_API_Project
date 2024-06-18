package com.ra.hieunt_hn_jv231229_project_module4_api.service.implementation;

import com.ra.hieunt_hn_jv231229_project_module4_api.model.dto.request.AddressRequest;
import com.ra.hieunt_hn_jv231229_project_module4_api.model.dto.response.UserAddressResponse;
import com.ra.hieunt_hn_jv231229_project_module4_api.model.entity.Address;
import com.ra.hieunt_hn_jv231229_project_module4_api.model.entity.User;
import com.ra.hieunt_hn_jv231229_project_module4_api.objectmapper.AddressMapper;
import com.ra.hieunt_hn_jv231229_project_module4_api.repository.IAddressRepo;
import com.ra.hieunt_hn_jv231229_project_module4_api.repository.IUserRepo;
import com.ra.hieunt_hn_jv231229_project_module4_api.service.design.IAddressService;
import com.ra.hieunt_hn_jv231229_project_module4_api.service.design.IUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AddressServiceImpl implements IAddressService
{
    private final IUserService userService;
    private final IAddressRepo addressRepo;
    private final AddressMapper addressMapper;

    @Override
    public String addNewAddress(AddressRequest addressRequest)
    {
        User currentUser = userService.getSignedInUser();
//        List<String> phoneList = userRepo.findAllPhone();
//        if (phoneList.stream().anyMatch(phone -> phone.equals(addressRequest.getPhone())))
//        {
//            //If yes, check to see if the phone matches with the current user's phone or not
//            if (!addressRequest.getPhone().equals(currentUser.getPhone()))
//            {
//                //Do not allow to use this phone number if it matches another user's phone number
//                throw new RuntimeException("This phone number is used by another user");
//            }
//        }
        //Address Id will automatically increment when saved to database
        Address newlyAddedAddress = Address.builder()
                .fullAddress(addressRequest.getFullAddress())
                .phone(addressRequest.getPhone())
                .receiveName(addressRequest.getReceiveName())
                .user(currentUser)
                .build();
        addressRepo.save(newlyAddedAddress);
        return "Added new address";
    }

    @Override
    public String deleteAddress(Long addressId)
    {
        addressRepo.findById(addressId).orElseThrow(() -> new RuntimeException("Address with Id: " + addressId + " not found"));
        addressRepo.deleteById(addressId);
        return "Deleted address with Id: " + addressId;
    }

    @Override
    public UserAddressResponse getAllAddresses()
    {
        User currentUser = userService.getSignedInUser();
        //Get all address associated with user in database
        List<Address> allAddresses = addressRepo.findAll();
        //Create a list of these subsidiary addresses to return to the view of API call
        List<AddressRequest> listResponseAddress = new ArrayList<>();
        for (Address add : allAddresses)
        {
            AddressRequest addressRequest =
                    addressMapper.getAddressRequest(add);
            listResponseAddress.add(addressRequest);
        }
        //Main address = address on user entity
        //other addresses = extra addresses added in address table in database
        return UserAddressResponse.builder()
                .mainAddress(currentUser.getAddress())
                .mainPhone(currentUser.getPhone())
                .mainReceiveName(currentUser.getFullname())
                .otherAddresses(listResponseAddress)
                .build();
    }

    @Override
    public AddressRequest getAddressById(Long addressId)
    {
        Address address = addressRepo.findById(addressId).orElseThrow(() -> new RuntimeException("Address with Id: " + addressId + " not found"));
        return addressMapper.getAddressRequest(address);
    }
}
