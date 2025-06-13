package com.flypiggyyoyoyo.demo.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.util.Date;
import lombok.Data;

/**
 * 
 * @TableName tb_job
 */
@TableName(value ="tb_job")
@Data
public class TbJob {
    /**
     * 
     */
    @TableId(type = IdType.AUTO)
    private Integer jobId;

    /**
     * 
     */
    private Integer companyId;

    /**
     * 
     */
    private String jobName;

    /**
     * 
     */
    private Integer jobHiringnum;

    /**
     * 
     */
    private String jobSalary;

    /**
     * 
     */
    private String jobArea;

    /**
     * 
     */
    private String jobDesc;

    /**
     * 
     */
    private Date jobEndtime;

    /**
     * 
     */
    private Integer jobState;

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
        TbJob other = (TbJob) that;
        return (this.getJobId() == null ? other.getJobId() == null : this.getJobId().equals(other.getJobId()))
            && (this.getCompanyId() == null ? other.getCompanyId() == null : this.getCompanyId().equals(other.getCompanyId()))
            && (this.getJobName() == null ? other.getJobName() == null : this.getJobName().equals(other.getJobName()))
            && (this.getJobHiringnum() == null ? other.getJobHiringnum() == null : this.getJobHiringnum().equals(other.getJobHiringnum()))
            && (this.getJobSalary() == null ? other.getJobSalary() == null : this.getJobSalary().equals(other.getJobSalary()))
            && (this.getJobArea() == null ? other.getJobArea() == null : this.getJobArea().equals(other.getJobArea()))
            && (this.getJobDesc() == null ? other.getJobDesc() == null : this.getJobDesc().equals(other.getJobDesc()))
            && (this.getJobEndtime() == null ? other.getJobEndtime() == null : this.getJobEndtime().equals(other.getJobEndtime()))
            && (this.getJobState() == null ? other.getJobState() == null : this.getJobState().equals(other.getJobState()));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getJobId() == null) ? 0 : getJobId().hashCode());
        result = prime * result + ((getCompanyId() == null) ? 0 : getCompanyId().hashCode());
        result = prime * result + ((getJobName() == null) ? 0 : getJobName().hashCode());
        result = prime * result + ((getJobHiringnum() == null) ? 0 : getJobHiringnum().hashCode());
        result = prime * result + ((getJobSalary() == null) ? 0 : getJobSalary().hashCode());
        result = prime * result + ((getJobArea() == null) ? 0 : getJobArea().hashCode());
        result = prime * result + ((getJobDesc() == null) ? 0 : getJobDesc().hashCode());
        result = prime * result + ((getJobEndtime() == null) ? 0 : getJobEndtime().hashCode());
        result = prime * result + ((getJobState() == null) ? 0 : getJobState().hashCode());
        return result;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", jobId=").append(jobId);
        sb.append(", companyId=").append(companyId);
        sb.append(", jobName=").append(jobName);
        sb.append(", jobHiringnum=").append(jobHiringnum);
        sb.append(", jobSalary=").append(jobSalary);
        sb.append(", jobArea=").append(jobArea);
        sb.append(", jobDesc=").append(jobDesc);
        sb.append(", jobEndtime=").append(jobEndtime);
        sb.append(", jobState=").append(jobState);
        sb.append("]");
        return sb.toString();
    }
}