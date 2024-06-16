package com.ra.hieunt_hn_jv231229_project_module4_api.util;

import com.ra.hieunt_hn_jv231229_project_module4_api.model.entity.Category;

import java.util.HashMap;
import java.util.Map;

public class FormatCategoryData
{
    private FormatCategoryData()
    {
    }

    ;

    public static Map<String, String> formatDataToShow(Category category)
    {
        Map<String, String> dataToShow = new HashMap<>();
        dataToShow.put("Category Id: ", category.getCategoryId().toString());
        dataToShow.put("Category Name: ", category.getCategoryName());
        dataToShow.put("Description: ", category.getDescription());
        dataToShow.put("Category status: ", category.getStatus() ? "Active" : "Inactive");
        return dataToShow;
    }
}
