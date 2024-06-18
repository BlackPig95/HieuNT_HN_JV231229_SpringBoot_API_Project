package com.ra.hieunt_hn_jv231229_project_module4_api.objectmapper;

import com.ra.hieunt_hn_jv231229_project_module4_api.model.dto.request.AddressRequest;
import com.ra.hieunt_hn_jv231229_project_module4_api.model.entity.Address;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
public class AddressMapper
{
    public AddressRequest getAddressRequest(Address address)
    {
        return AddressRequest.builder()
                .fullAddress(address.getFullAddress())
                .phone(address.getPhone())
                .receiveName(address.getReceiveName())
                .build();
    }
}
