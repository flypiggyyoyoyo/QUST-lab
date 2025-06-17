package com.flypiggyyoyoyo.demo.data.company.register;

import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Data
public class CompanyRegisterRequest {

    @NotEmpty
    private String companyName;

    @NotEmpty
    private String companyArea;

    @NotEmpty
    private String companySize;

    @NotEmpty
    private String companyType;

    @NotEmpty
    private String companyBrief;

    @NotNull
    private int companyState;

    @NotNull
    private int companySort;

    @NotEmpty
    private String companyPic;
}
