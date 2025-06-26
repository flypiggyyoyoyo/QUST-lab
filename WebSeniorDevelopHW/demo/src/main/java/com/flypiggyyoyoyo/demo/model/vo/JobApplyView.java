package com.flypiggyyoyoyo.demo.model.vo;

import lombok.Data;

import java.util.Date;

@Data
public class JobApplyView {
    private String applicantEmail;
    private String applicantName;
    private String jobName;
    private String companyName;
    private Date applyDate;
    private Integer applyState;
}
