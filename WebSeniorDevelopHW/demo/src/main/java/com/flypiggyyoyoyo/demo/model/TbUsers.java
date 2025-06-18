package com.flypiggyyoyoyo.demo.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 
 * @TableName tb_users
 */
@TableName(value ="tb_users")
@Data
@Accessors(chain = true)
public class TbUsers {
    /**
     * 
     */
    @TableId(value = "user_id", type = IdType.AUTO)
    private Integer userId;

    /**
     * 
     */
    private String userLogname;

    /**
     * 
     */
    private String userPwd;

    /**
     * 
     */
    private String userRealname;

    /**
     * 
     */
    private String userEmail;

    /**
     * 
     */
    private Integer userRole;

    /**
     * 
     */
    private Integer userState;

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
        TbUsers other = (TbUsers) that;
        return (this.getUserId() == null ? other.getUserId() == null : this.getUserId().equals(other.getUserId()))
            && (this.getUserLogname() == null ? other.getUserLogname() == null : this.getUserLogname().equals(other.getUserLogname()))
            && (this.getUserPwd() == null ? other.getUserPwd() == null : this.getUserPwd().equals(other.getUserPwd()))
            && (this.getUserRealname() == null ? other.getUserRealname() == null : this.getUserRealname().equals(other.getUserRealname()))
            && (this.getUserEmail() == null ? other.getUserEmail() == null : this.getUserEmail().equals(other.getUserEmail()))
            && (this.getUserRole() == null ? other.getUserRole() == null : this.getUserRole().equals(other.getUserRole()))
            && (this.getUserState() == null ? other.getUserState() == null : this.getUserState().equals(other.getUserState()));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getUserId() == null) ? 0 : getUserId().hashCode());
        result = prime * result + ((getUserLogname() == null) ? 0 : getUserLogname().hashCode());
        result = prime * result + ((getUserPwd() == null) ? 0 : getUserPwd().hashCode());
        result = prime * result + ((getUserRealname() == null) ? 0 : getUserRealname().hashCode());
        result = prime * result + ((getUserEmail() == null) ? 0 : getUserEmail().hashCode());
        result = prime * result + ((getUserRole() == null) ? 0 : getUserRole().hashCode());
        result = prime * result + ((getUserState() == null) ? 0 : getUserState().hashCode());
        return result;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", userId=").append(userId);
        sb.append(", userLogname=").append(userLogname);
        sb.append(", userPwd=").append(userPwd);
        sb.append(", userRealname=").append(userRealname);
        sb.append(", userEmail=").append(userEmail);
        sb.append(", userRole=").append(userRole);
        sb.append(", userState=").append(userState);
        sb.append("]");
        return sb.toString();
    }
}