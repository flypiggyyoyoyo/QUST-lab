package com.flypiggyyoyoyo.demo.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.util.Date;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 
 * @TableName tb_resume_basicinfo
 */
@TableName(value ="tb_resume_basicinfo")
@Data
@Accessors(chain = true)
public class TbResumeBasicinfo {
    /**
     * 
     */
    @TableId(type = IdType.AUTO)
    private Integer basicinfoId;

    /**
     * 
     */
    private Integer applicantId;

    /**
     * 
     */
    private String realname;

    /**
     * 
     */
    private String gender;

    /**
     * 
     */
    private Date birthday;

    /**
     * 
     */
    private String currentLoc;

    /**
     * 
     */
    private String residentLoc;

    /**
     * 
     */
    private String telephone;

    /**
     * 
     */
    private String email;

    /**
     * 
     */
    private String jobIntension;

    /**
     * 
     */
    private String jobExperience;

    /**
     * 
     */
    private String headShot;

    @Override
    public boolean equals(Object that) {
        if (this == that) {
            return true;
        }
        if (that == null) {
            return false;
        }
        if (getClass() != that.getClass()) {
            return false;
        }
        TbResumeBasicinfo other = (TbResumeBasicinfo) that;
        return (this.getBasicinfoId() == null ? other.getBasicinfoId() == null : this.getBasicinfoId().equals(other.getBasicinfoId()))
            && (this.getApplicantId() == null ? other.getApplicantId() == null : this.getApplicantId().equals(other.getApplicantId()))
            && (this.getRealname() == null ? other.getRealname() == null : this.getRealname().equals(other.getRealname()))
            && (this.getGender() == null ? other.getGender() == null : this.getGender().equals(other.getGender()))
            && (this.getBirthday() == null ? other.getBirthday() == null : this.getBirthday().equals(other.getBirthday()))
            && (this.getCurrentLoc() == null ? other.getCurrentLoc() == null : this.getCurrentLoc().equals(other.getCurrentLoc()))
            && (this.getResidentLoc() == null ? other.getResidentLoc() == null : this.getResidentLoc().equals(other.getResidentLoc()))
            && (this.getTelephone() == null ? other.getTelephone() == null : this.getTelephone().equals(other.getTelephone()))
            && (this.getEmail() == null ? other.getEmail() == null : this.getEmail().equals(other.getEmail()))
            && (this.getJobIntension() == null ? other.getJobIntension() == null : this.getJobIntension().equals(other.getJobIntension()))
            && (this.getJobExperience() == null ? other.getJobExperience() == null : this.getJobExperience().equals(other.getJobExperience()))
            && (this.getHeadShot() == null ? other.getHeadShot() == null : this.getHeadShot().equals(other.getHeadShot()));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getBasicinfoId() == null) ? 0 : getBasicinfoId().hashCode());
        result = prime * result + ((getApplicantId() == null) ? 0 : getApplicantId().hashCode());
        result = prime * result + ((getRealname() == null) ? 0 : getRealname().hashCode());
        result = prime * result + ((getGender() == null) ? 0 : getGender().hashCode());
        result = prime * result + ((getBirthday() == null) ? 0 : getBirthday().hashCode());
        result = prime * result + ((getCurrentLoc() == null) ? 0 : getCurrentLoc().hashCode());
        result = prime * result + ((getResidentLoc() == null) ? 0 : getResidentLoc().hashCode());
        result = prime * result + ((getTelephone() == null) ? 0 : getTelephone().hashCode());
        result = prime * result + ((getEmail() == null) ? 0 : getEmail().hashCode());
        result = prime * result + ((getJobIntension() == null) ? 0 : getJobIntension().hashCode());
        result = prime * result + ((getJobExperience() == null) ? 0 : getJobExperience().hashCode());
        result = prime * result + ((getHeadShot() == null) ? 0 : getHeadShot().hashCode());
        return result;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", basicinfoId=").append(basicinfoId);
        sb.append(", applicantId=").append(applicantId);
        sb.append(", realname=").append(realname);
        sb.append(", gender=").append(gender);
        sb.append(", birthday=").append(birthday);
        sb.append(", currentLoc=").append(currentLoc);
        sb.append(", residentLoc=").append(residentLoc);
        sb.append(", telephone=").append(telephone);
        sb.append(", email=").append(email);
        sb.append(", jobIntension=").append(jobIntension);
        sb.append(", jobExperience=").append(jobExperience);
        sb.append(", headShot=").append(headShot);
        sb.append("]");
        return sb.toString();
    }
}