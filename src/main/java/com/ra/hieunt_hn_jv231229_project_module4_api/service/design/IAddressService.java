package com.ra.hieunt_hn_jv231229_project_module4_api.service.design;

import com.ra.hieunt_hn_jv231229_project_module4_api.model.dto.request.AddressRequest;
import com.ra.hieunt_hn_jv231229_project_module4_api.model.dto.response.UserAddressResponse;
import com.ra.hieunt_hn_jv231229_project_module4_api.model.entity.Address;

import java.util.List;

public interface IAddressService
{
    String addNewAddress(AddressRequest addressRequest);

    String deleteAddress(Long addressId);

    UserAddressResponse getAllAddresses();

    AddressRequest getAddressById(Long addressId);
}
