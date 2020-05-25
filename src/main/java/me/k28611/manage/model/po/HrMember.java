package me.k28611.manage.model.po;

import java.io.Serializable;

public class HrMember implements Serializable {
    private Integer workno;

    private String username;

    private String password;

    private String authority;

    private String note;

    private Boolean isvalid;

    private String email;

    private Integer accountno;

    private String headimgurl;

    private static final long serialVersionUID = 1L;

    public Integer getWorkno() {
        return workno;
    }

    public void setWorkno(Integer workno) {
        this.workno = workno;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username == null ? null : username.trim();
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password == null ? null : password.trim();
    }

    public String getAuthority() {
        return authority;
    }

    public void setAuthority(String authority) {
        this.authority = authority == null ? null : authority.trim();
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note == null ? null : note.trim();
    }

    public Boolean getIsvalid() {
        return isvalid;
    }

    public void setIsvalid(Boolean isvalid) {
        this.isvalid = isvalid;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email == null ? null : email.trim();
    }

    public Integer getAccountno() {
        return accountno;
    }

    public void setAccountno(Integer accountno) {
        this.accountno = accountno;
    }

    public String getHeadimgurl() {
        return headimgurl;
    }

    public void setHeadimgurl(String headimgurl) {
        this.headimgurl = headimgurl == null ? null : headimgurl.trim();
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", workno=").append(workno);
        sb.append(", username=").append(username);
        sb.append(", password=").append(password);
        sb.append(", authority=").append(authority);
        sb.append(", note=").append(note);
        sb.append(", isvalid=").append(isvalid);
        sb.append(", email=").append(email);
        sb.append(", accountno=").append(accountno);
        sb.append(", headimgurl=").append(headimgurl);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }
}