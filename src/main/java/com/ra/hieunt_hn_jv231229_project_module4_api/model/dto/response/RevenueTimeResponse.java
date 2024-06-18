package com.ra.hieunt_hn_jv231229_project_module4_api.model.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;

import java.util.Date;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class RevenueTimeResponse
{
    @JsonFormat(pattern = "dd-MM-yyyy")
    private Date from;
    @JsonFormat(pattern = "dd-MM-yyyy")
    private Date to;
    private Double revenue;
}
