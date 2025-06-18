package com.flypiggyyoyoyo.demo.data.company.update;

import lombok.Data;

@Data
public class CompanyUpdateRequest {
    private Long companyId;
    private String companyName;
    private String companyArea;
    private String companySize;
    private String companyType;
    private String companyBrief;
    private Integer companyState;
    private Integer companySort;
    /** 前端已上传到 MinIO 的最终 URL */
    private String companyPic;
}
