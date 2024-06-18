package com.ra.hieunt_hn_jv231229_project_module4_api.model.dto.response;

import com.ra.hieunt_hn_jv231229_project_module4_api.model.dto.request.AddressRequest;
import lombok.*;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class UserAddressResponse
{
    private String mainAddress;
    private String mainPhone;
    private String mainReceiveName;
    private List<AddressRequest> otherAddresses;
}
