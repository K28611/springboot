package me.k28611.manage.model.po;

import java.io.Serializable;


public class TUser implements Serializable {
    private Integer kId;

    private String kName;

    private String kPassword;

    private Boolean kAuthority;
    private String kNote;

    private static final long serialVersionUID = 1L;

    public Integer getkId() {
        return kId;
    }

    public void setkId(Integer kId) {
        this.kId = kId;
    }

    public String getkName() {
        return kName;
    }

    public void setkName(String kName) {
        this.kName = kName == null ? null : kName.trim();
    }

    public String getkPassword() {
        return kPassword;
    }

    public void setkPassword(String kPassword) {
        this.kPassword = kPassword == null ? null : kPassword.trim();
    }

    public Boolean getkAuthority() {
        return kAuthority;
    }

    public void setkAuthority(Boolean kAuthority) {
        this.kAuthority = kAuthority;
    }

    public String getkNote() {
        return kNote;
    }

    public void setkNote(String kNote) {
        this.kNote = kNote == null ? null : kNote.trim();
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", kId=").append(kId);
        sb.append(", kName=").append(kName);
        sb.append(", kPassword=").append(kPassword);
        sb.append(", kAuthority=").append(kAuthority);
        sb.append(", kNote=").append(kNote);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }
}