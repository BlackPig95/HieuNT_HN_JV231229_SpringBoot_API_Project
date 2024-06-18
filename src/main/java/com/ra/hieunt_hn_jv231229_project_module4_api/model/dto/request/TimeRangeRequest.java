package com.ra.hieunt_hn_jv231229_project_module4_api.model.dto.request;

import lombok.*;

import java.util.Date;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class TimeRangeRequest
{
    private Date from;
    private Date to;
}
