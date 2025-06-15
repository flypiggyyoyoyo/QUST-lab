package com.flypiggyyoyoyo.demo.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.util.Date;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 
 * @TableName tb_applicant
 */
@TableName(value ="tb_applicant")
@Data
@Accessors(chain = true)
public class TbApplicant {
    /**
     * 
     */
    @TableId(type = IdType.AUTO)
    private Integer applicantId;

    /**
     * 
     */
    private String applicantEmail;

    /**
     * 
     */
    private String applicantPwd;

    /**
     * 
     */
    private Date applicantRegistdate;

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
        TbApplicant other = (TbApplicant) that;
        return (this.getApplicantId() == null ? other.getApplicantId() == null : this.getApplicantId().equals(other.getApplicantId()))
            && (this.getApplicantEmail() == null ? other.getApplicantEmail() == null : this.getApplicantEmail().equals(other.getApplicantEmail()))
            && (this.getApplicantPwd() == null ? other.getApplicantPwd() == null : this.getApplicantPwd().equals(other.getApplicantPwd()))
            && (this.getApplicantRegistdate() == null ? other.getApplicantRegistdate() == null : this.getApplicantRegistdate().equals(other.getApplicantRegistdate()));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getApplicantId() == null) ? 0 : getApplicantId().hashCode());
        result = prime * result + ((getApplicantEmail() == null) ? 0 : getApplicantEmail().hashCode());
        result = prime * result + ((getApplicantPwd() == null) ? 0 : getApplicantPwd().hashCode());
        result = prime * result + ((getApplicantRegistdate() == null) ? 0 : getApplicantRegistdate().hashCode());
        return result;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", applicantId=").append(applicantId);
        sb.append(", applicantEmail=").append(applicantEmail);
        sb.append(", applicantPwd=").append(applicantPwd);
        sb.append(", applicantRegistdate=").append(applicantRegistdate);
        sb.append("]");
        return sb.toString();
    }
}