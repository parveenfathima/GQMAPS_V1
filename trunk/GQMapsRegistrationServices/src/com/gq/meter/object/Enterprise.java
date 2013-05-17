package com.gq.meter.object;

//Generated May 2, 2013 12:10:05 PM by Hibernate Tools 3.4.0.CR1

import java.util.Date;

/**
 * Enterprise generated by hbm2java
 */
public class Enterprise implements java.io.Serializable {

    private Short sid;
    private String enterpriseId;
    private String blCd;
    private String eName;
    private String phone;
    private String email;
    private String userId;
    private String passwd;
    private Short secQtn1;
    private String ans1;
    private Short secQtn2;
    private String ans2;
    private char storeFwd;
    private String fwdUrl;
    private Integer noOfEmpl;
    private Integer entSqft;
    private Integer entAssetCount;
    private Integer dcSqft;
    private Integer dcAssetCount;
    private Double dcUsePctg;
    private Integer dcTemp;
    private Character regCmplt;
    private char active;
    private String comments;
    private Date creDttm;

    public Enterprise() {
    }

    public Enterprise(Short sid, String enterpriseId, String blCd, String eName, String phone, String email,
            String userId, String passwd, Short secQtn1, String ans1, Short secQtn2, String ans2, char storeFwd,
            String fwdUrl, Integer noOfEmpl, Integer entSqft, Integer entAssetCount, Integer dcSqft,
            Integer dcAssetCount, Double dcUsePctg, Integer dcTemp, Character regCmplt, char active, String comments,
            Date creDttm) {
        this.sid = sid;
        this.enterpriseId = enterpriseId;
        this.blCd = blCd;
        this.seteName(eName);
        this.phone = phone;
        this.email = email;
        this.userId = userId;
        this.passwd = passwd;
        this.secQtn1 = secQtn1;
        this.ans1 = ans1;
        this.secQtn2 = secQtn2;
        this.ans2 = ans2;
        this.storeFwd = storeFwd;
        this.fwdUrl = fwdUrl;
        this.noOfEmpl = noOfEmpl;
        this.entSqft = entSqft;
        this.entAssetCount = entAssetCount;
        this.dcSqft = dcSqft;
        this.dcAssetCount = dcAssetCount;
        this.dcUsePctg = dcUsePctg;
        this.dcTemp = dcTemp;
        this.regCmplt = regCmplt;
        this.active = active;
        this.comments = comments;
        this.creDttm = creDttm;
    }

    public Short getSid() {
        return this.sid;
    }

    public void setSid(Short sid) {
        this.sid = sid;
    }

    public String getEnterpriseId() {
        return this.enterpriseId;
    }

    public void setEnterpriseId(String enterpriseId) {
        this.enterpriseId = enterpriseId;
    }

    public String getBlCd() {
        return this.blCd;
    }

    public void setBlCd(String blCd) {
        this.blCd = blCd;
    }

    public String getPhone() {
        return this.phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return this.email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUserId() {
        return this.userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getPasswd() {
        return this.passwd;
    }

    public void setPasswd(String passwd) {
        this.passwd = passwd;
    }

    public Short getSecQtn1() {
        return this.secQtn1;
    }

    public void setSecQtn1(Short secQtn1) {
        this.secQtn1 = secQtn1;
    }

    public String getAns1() {
        return this.ans1;
    }

    public void setAns1(String ans1) {
        this.ans1 = ans1;
    }

    public Short getSecQtn2() {
        return this.secQtn2;
    }

    public void setSecQtn2(Short secQtn2) {
        this.secQtn2 = secQtn2;
    }

    public String getAns2() {
        return this.ans2;
    }

    public void setAns2(String ans2) {
        this.ans2 = ans2;
    }

    public Date getCreDttm() {
        return this.creDttm;
    }

    public void setCreDttm(Date creDttm) {
        this.creDttm = creDttm;
    }

    public char getActive() {
        return active;
    }

    public void setActive(char active) {
        this.active = active;
    }

    public char getStoreFwd() {
        return this.storeFwd;
    }

    public void setStoreFwd(char storeFwd) {
        this.storeFwd = storeFwd;
    }

    public String getFwdUrl() {
        return this.fwdUrl;
    }

    public void setFwdUrl(String fwdUrl) {
        this.fwdUrl = fwdUrl;
    }

    public Integer getNoOfEmpl() {
        return this.noOfEmpl;
    }

    public void setNoOfEmpl(Integer noOfEmpl) {
        this.noOfEmpl = noOfEmpl;
    }

    public Integer getDcSqft() {
        return this.dcSqft;
    }

    public void setDcSqft(Integer dcSqft) {
        this.dcSqft = dcSqft;
    }

    public Character getRegCmplt() {
        return this.regCmplt;
    }

    public void setRegCmplt(Character regCmplt) {
        this.regCmplt = regCmplt;
    }

    public Double getDcUsePctg() {
        return this.dcUsePctg;
    }

    public void setDcUsePctg(Double dcUsePctg) {
        this.dcUsePctg = dcUsePctg;
    }

    public Integer getDcTemp() {
        return this.dcTemp;
    }

    public void setDcTemp(Integer dcTemp) {
        this.dcTemp = dcTemp;
    }

    public String getComments() {
        return this.comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    public Integer getEntSqft() {
        return entSqft;
    }

    public void setEntSqft(Integer entSqft) {
        this.entSqft = entSqft;
    }

    public Integer getEntAssetCount() {
        return entAssetCount;
    }

    public void setEntAssetCount(Integer entAssetCount) {
        this.entAssetCount = entAssetCount;
    }

    public Integer getDcAssetCount() {
        return dcAssetCount;
    }

    public void setDcAssetCount(Integer dcAssetCount) {
        this.dcAssetCount = dcAssetCount;
    }

    public String geteName() {
        return eName;
    }

    public void seteName(String eName) {
        this.eName = eName;
    }

}
