package com.flypiggyyoyoyo.demo.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * 
 * @TableName tb_company
 */
@TableName(value ="tb_company")
@Data
public class TbCompany {
    /**
     * 
     */
    @TableId(type = IdType.AUTO)
    private Integer companyId;

    /**
     * 
     */
    private String companyName;

    /**
     * 
     */
    private String companyArea;

    /**
     * 
     */
    private String companySize;

    /**
     * 
     */
    private String companyType;

    /**
     * 
     */
    private String companyBrief;

    /**
     * 
     */
    private Integer companyState;

    /**
     * 
     */
    private Integer companySort;

    /**
     * 
     */
    private Long companyViewnum;

    /**
     * 
     */
    private String companyPic;

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
        TbCompany other = (TbCompany) that;
        return (this.getCompanyId() == null ? other.getCompanyId() == null : this.getCompanyId().equals(other.getCompanyId()))
            && (this.getCompanyName() == null ? other.getCompanyName() == null : this.getCompanyName().equals(other.getCompanyName()))
            && (this.getCompanyArea() == null ? other.getCompanyArea() == null : this.getCompanyArea().equals(other.getCompanyArea()))
            && (this.getCompanySize() == null ? other.getCompanySize() == null : this.getCompanySize().equals(other.getCompanySize()))
            && (this.getCompanyType() == null ? other.getCompanyType() == null : this.getCompanyType().equals(other.getCompanyType()))
            && (this.getCompanyBrief() == null ? other.getCompanyBrief() == null : this.getCompanyBrief().equals(other.getCompanyBrief()))
            && (this.getCompanyState() == null ? other.getCompanyState() == null : this.getCompanyState().equals(other.getCompanyState()))
            && (this.getCompanySort() == null ? other.getCompanySort() == null : this.getCompanySort().equals(other.getCompanySort()))
            && (this.getCompanyViewnum() == null ? other.getCompanyViewnum() == null : this.getCompanyViewnum().equals(other.getCompanyViewnum()))
            && (this.getCompanyPic() == null ? other.getCompanyPic() == null : this.getCompanyPic().equals(other.getCompanyPic()));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getCompanyId() == null) ? 0 : getCompanyId().hashCode());
        result = prime * result + ((getCompanyName() == null) ? 0 : getCompanyName().hashCode());
        result = prime * result + ((getCompanyArea() == null) ? 0 : getCompanyArea().hashCode());
        result = prime * result + ((getCompanySize() == null) ? 0 : getCompanySize().hashCode());
        result = prime * result + ((getCompanyType() == null) ? 0 : getCompanyType().hashCode());
        result = prime * result + ((getCompanyBrief() == null) ? 0 : getCompanyBrief().hashCode());
        result = prime * result + ((getCompanyState() == null) ? 0 : getCompanyState().hashCode());
        result = prime * result + ((getCompanySort() == null) ? 0 : getCompanySort().hashCode());
        result = prime * result + ((getCompanyViewnum() == null) ? 0 : getCompanyViewnum().hashCode());
        result = prime * result + ((getCompanyPic() == null) ? 0 : getCompanyPic().hashCode());
        return result;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", companyId=").append(companyId);
        sb.append(", companyName=").append(companyName);
        sb.append(", companyArea=").append(companyArea);
        sb.append(", companySize=").append(companySize);
        sb.append(", companyType=").append(companyType);
        sb.append(", companyBrief=").append(companyBrief);
        sb.append(", companyState=").append(companyState);
        sb.append(", companySort=").append(companySort);
        sb.append(", companyViewnum=").append(companyViewnum);
        sb.append(", companyPic=").append(companyPic);
        sb.append("]");
        return sb.toString();
    }
}