package com.mycompany.myapp.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.sql.Timestamp;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;

@Entity
@Table(name = "sys_user", schema = "voffice", catalog = "")
public class SysUser {

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "id")
    private Long id;

    @Basic
    @Column(name = "user_name")
    private String userName;

    @Basic
    @Column(name = "full_name")
    private String fullName;

    @Basic
    @Column(name = "password")
    private String password;

    @Basic
    @Column(name = "gender")
    private Integer gender;

    @Basic
    @Column(name = "date_of_birth")
    private Timestamp dateOfBirth;

    @Basic
    @Column(name = "birth_place")
    private String birthPlace;

    @Basic
    @Column(name = "identity_card")
    private String identityCard;

    @Basic
    @Column(name = "position_id")
    private Integer positionId;

    @Basic
    @Column(name = "position_name")
    private String positionName;

    @Basic
    @Column(name = "dept_id")
    private Integer deptId;

    @Basic
    @Column(name = "dept_name")
    private String deptName;

    @Basic
    @Column(name = "image_url")
    private String imageUrl;

    @Basic
    @Column(name = "status")
    private Integer status;

    @Basic
    @Column(name = "activation_key")
    private String activationKey;

    @Basic
    @Column(name = "reset_key")
    private String resetKey;

    @Basic
    @Column(name = "created_by")
    private String createdBy;

    @Basic
    @Column(name = "created_date")
    private Timestamp createdDate;

    @Basic
    @Column(name = "reset_date")
    private Timestamp resetDate;

    @Basic
    @Column(name = "last_modified_by")
    private String lastModifiedBy;

    @Basic
    @Column(name = "last_modified_date")
    private Timestamp lastModifiedDate;

    @Basic
    @Column(name = "email")
    private String email;

    @Basic
    @Column(name = "tenant_id")
    private Integer tenantId;

    @Basic
    @Column(name = "org_id")
    private Long orgId;

    @Basic
    @Column(name = "password_md5")
    private String passwordMd5;

    @Basic
    @Column(name = "description")
    private String description;

    @Basic
    @Column(name = "cellphone")
    private String cellphone;

    @Basic
    @Column(name = "is_active")
    private Integer isActive;

    @Basic
    @Column(name = "update_date")
    private Timestamp updateDate;

    @Basic
    @Column(name = "update_by")
    private String updateBy;

    @Basic
    @Column(name = "last_change_password")
    private Timestamp lastChangePassword;

    @Basic
    @Column(name = "last_reset_password")
    private Timestamp lastResetPassword;

    @Basic
    @Column(name = "login_failure_count")
    private Integer loginFailureCount;

    @Transient
    private Set<Authority> authorities;

    @ManyToMany
    @JoinTable(
        name = "sys_user_role",
        joinColumns = @JoinColumn(name = "user_id", referencedColumnName = "id"),
        inverseJoinColumns = @JoinColumn(name = "role_id", referencedColumnName = "id")
    )
    @JsonIgnoreProperties(value = { "app", "users" }, allowSetters = true)
    private Set<SysRole> roles = new HashSet<>();

    public Set<SysRole> getRoles() {
        return roles;
    }

    public void setRoles(Set<SysRole> roles) {
        this.roles = roles;
    }

    public Set<Authority> getAuthorities() {
        return authorities;
    }

    public void setAuthorities(Set<Authority> authorities) {
        this.authorities = authorities;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Integer getGender() {
        return gender;
    }

    public void setGender(Integer gender) {
        this.gender = gender;
    }

    public Timestamp getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(Timestamp dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public String getBirthPlace() {
        return birthPlace;
    }

    public void setBirthPlace(String birthPlace) {
        this.birthPlace = birthPlace;
    }

    public String getIdentityCard() {
        return identityCard;
    }

    public void setIdentityCard(String identityCard) {
        this.identityCard = identityCard;
    }

    public Integer getPositionId() {
        return positionId;
    }

    public void setPositionId(Integer positionId) {
        this.positionId = positionId;
    }

    public String getPositionName() {
        return positionName;
    }

    public void setPositionName(String positionName) {
        this.positionName = positionName;
    }

    public Integer getDeptId() {
        return deptId;
    }

    public void setDeptId(Integer deptId) {
        this.deptId = deptId;
    }

    public String getDeptName() {
        return deptName;
    }

    public void setDeptName(String deptName) {
        this.deptName = deptName;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getActivationKey() {
        return activationKey;
    }

    public void setActivationKey(String activationKey) {
        this.activationKey = activationKey;
    }

    public String getResetKey() {
        return resetKey;
    }

    public void setResetKey(String resetKey) {
        this.resetKey = resetKey;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public Timestamp getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Timestamp createdDate) {
        this.createdDate = createdDate;
    }

    public Timestamp getResetDate() {
        return resetDate;
    }

    public void setResetDate(Timestamp resetDate) {
        this.resetDate = resetDate;
    }

    public String getLastModifiedBy() {
        return lastModifiedBy;
    }

    public void setLastModifiedBy(String lastModifiedBy) {
        this.lastModifiedBy = lastModifiedBy;
    }

    public Timestamp getLastModifiedDate() {
        return lastModifiedDate;
    }

    public void setLastModifiedDate(Timestamp lastModifiedDate) {
        this.lastModifiedDate = lastModifiedDate;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Integer getTenantId() {
        return tenantId;
    }

    public void setTenantId(Integer tenantId) {
        this.tenantId = tenantId;
    }

    public Long getOrgId() {
        return orgId;
    }

    public void setOrgId(Long orgId) {
        this.orgId = orgId;
    }

    public String getPasswordMd5() {
        return passwordMd5;
    }

    public void setPasswordMd5(String passwordMd5) {
        this.passwordMd5 = passwordMd5;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCellphone() {
        return cellphone;
    }

    public void setCellphone(String cellphone) {
        this.cellphone = cellphone;
    }

    public Integer getIsActive() {
        return isActive;
    }

    public void setIsActive(Integer isActive) {
        this.isActive = isActive;
    }

    public Timestamp getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(Timestamp updateDate) {
        this.updateDate = updateDate;
    }

    public String getUpdateBy() {
        return updateBy;
    }

    public void setUpdateBy(String updateBy) {
        this.updateBy = updateBy;
    }

    public Timestamp getLastChangePassword() {
        return lastChangePassword;
    }

    public void setLastChangePassword(Timestamp lastChangePassword) {
        this.lastChangePassword = lastChangePassword;
    }

    public Timestamp getLastResetPassword() {
        return lastResetPassword;
    }

    public void setLastResetPassword(Timestamp lastResetPassword) {
        this.lastResetPassword = lastResetPassword;
    }

    public Integer getLoginFailureCount() {
        return loginFailureCount;
    }

    public void setLoginFailureCount(Integer loginFailureCount) {
        this.loginFailureCount = loginFailureCount;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        SysUser sysUser = (SysUser) o;

        if (id != null ? !id.equals(sysUser.id) : sysUser.id != null) return false;
        if (userName != null ? !userName.equals(sysUser.userName) : sysUser.userName != null) return false;
        if (fullName != null ? !fullName.equals(sysUser.fullName) : sysUser.fullName != null) return false;
        if (password != null ? !password.equals(sysUser.password) : sysUser.password != null) return false;
        if (gender != null ? !gender.equals(sysUser.gender) : sysUser.gender != null) return false;
        if (dateOfBirth != null ? !dateOfBirth.equals(sysUser.dateOfBirth) : sysUser.dateOfBirth != null) return false;
        if (birthPlace != null ? !birthPlace.equals(sysUser.birthPlace) : sysUser.birthPlace != null) return false;
        if (identityCard != null ? !identityCard.equals(sysUser.identityCard) : sysUser.identityCard != null) return false;
        if (positionId != null ? !positionId.equals(sysUser.positionId) : sysUser.positionId != null) return false;
        if (positionName != null ? !positionName.equals(sysUser.positionName) : sysUser.positionName != null) return false;
        if (deptId != null ? !deptId.equals(sysUser.deptId) : sysUser.deptId != null) return false;
        if (deptName != null ? !deptName.equals(sysUser.deptName) : sysUser.deptName != null) return false;
        if (imageUrl != null ? !imageUrl.equals(sysUser.imageUrl) : sysUser.imageUrl != null) return false;
        if (status != null ? !status.equals(sysUser.status) : sysUser.status != null) return false;
        if (activationKey != null ? !activationKey.equals(sysUser.activationKey) : sysUser.activationKey != null) return false;
        if (resetKey != null ? !resetKey.equals(sysUser.resetKey) : sysUser.resetKey != null) return false;
        if (createdBy != null ? !createdBy.equals(sysUser.createdBy) : sysUser.createdBy != null) return false;
        if (createdDate != null ? !createdDate.equals(sysUser.createdDate) : sysUser.createdDate != null) return false;
        if (resetDate != null ? !resetDate.equals(sysUser.resetDate) : sysUser.resetDate != null) return false;
        if (lastModifiedBy != null ? !lastModifiedBy.equals(sysUser.lastModifiedBy) : sysUser.lastModifiedBy != null) return false;
        if (lastModifiedDate != null ? !lastModifiedDate.equals(sysUser.lastModifiedDate) : sysUser.lastModifiedDate != null) return false;
        if (email != null ? !email.equals(sysUser.email) : sysUser.email != null) return false;
        if (tenantId != null ? !tenantId.equals(sysUser.tenantId) : sysUser.tenantId != null) return false;
        if (orgId != null ? !orgId.equals(sysUser.orgId) : sysUser.orgId != null) return false;
        if (passwordMd5 != null ? !passwordMd5.equals(sysUser.passwordMd5) : sysUser.passwordMd5 != null) return false;
        if (description != null ? !description.equals(sysUser.description) : sysUser.description != null) return false;
        if (cellphone != null ? !cellphone.equals(sysUser.cellphone) : sysUser.cellphone != null) return false;
        if (isActive != null ? !isActive.equals(sysUser.isActive) : sysUser.isActive != null) return false;
        if (updateDate != null ? !updateDate.equals(sysUser.updateDate) : sysUser.updateDate != null) return false;
        if (updateBy != null ? !updateBy.equals(sysUser.updateBy) : sysUser.updateBy != null) return false;
        if (
            lastChangePassword != null ? !lastChangePassword.equals(sysUser.lastChangePassword) : sysUser.lastChangePassword != null
        ) return false;
        if (
            lastResetPassword != null ? !lastResetPassword.equals(sysUser.lastResetPassword) : sysUser.lastResetPassword != null
        ) return false;
        if (
            loginFailureCount != null ? !loginFailureCount.equals(sysUser.loginFailureCount) : sysUser.loginFailureCount != null
        ) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (userName != null ? userName.hashCode() : 0);
        result = 31 * result + (fullName != null ? fullName.hashCode() : 0);
        result = 31 * result + (password != null ? password.hashCode() : 0);
        result = 31 * result + (gender != null ? gender.hashCode() : 0);
        result = 31 * result + (dateOfBirth != null ? dateOfBirth.hashCode() : 0);
        result = 31 * result + (birthPlace != null ? birthPlace.hashCode() : 0);
        result = 31 * result + (identityCard != null ? identityCard.hashCode() : 0);
        result = 31 * result + (positionId != null ? positionId.hashCode() : 0);
        result = 31 * result + (positionName != null ? positionName.hashCode() : 0);
        result = 31 * result + (deptId != null ? deptId.hashCode() : 0);
        result = 31 * result + (deptName != null ? deptName.hashCode() : 0);
        result = 31 * result + (imageUrl != null ? imageUrl.hashCode() : 0);
        result = 31 * result + (status != null ? status.hashCode() : 0);
        result = 31 * result + (activationKey != null ? activationKey.hashCode() : 0);
        result = 31 * result + (resetKey != null ? resetKey.hashCode() : 0);
        result = 31 * result + (createdBy != null ? createdBy.hashCode() : 0);
        result = 31 * result + (createdDate != null ? createdDate.hashCode() : 0);
        result = 31 * result + (resetDate != null ? resetDate.hashCode() : 0);
        result = 31 * result + (lastModifiedBy != null ? lastModifiedBy.hashCode() : 0);
        result = 31 * result + (lastModifiedDate != null ? lastModifiedDate.hashCode() : 0);
        result = 31 * result + (email != null ? email.hashCode() : 0);
        result = 31 * result + (tenantId != null ? tenantId.hashCode() : 0);
        result = 31 * result + (orgId != null ? orgId.hashCode() : 0);
        result = 31 * result + (passwordMd5 != null ? passwordMd5.hashCode() : 0);
        result = 31 * result + (description != null ? description.hashCode() : 0);
        result = 31 * result + (cellphone != null ? cellphone.hashCode() : 0);
        result = 31 * result + (isActive != null ? isActive.hashCode() : 0);
        result = 31 * result + (updateDate != null ? updateDate.hashCode() : 0);
        result = 31 * result + (updateBy != null ? updateBy.hashCode() : 0);
        result = 31 * result + (lastChangePassword != null ? lastChangePassword.hashCode() : 0);
        result = 31 * result + (lastResetPassword != null ? lastResetPassword.hashCode() : 0);
        result = 31 * result + (loginFailureCount != null ? loginFailureCount.hashCode() : 0);
        return result;
    }
}
