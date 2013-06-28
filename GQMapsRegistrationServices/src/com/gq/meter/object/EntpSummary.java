/**
 * 
 */
package com.gq.meter.object;

import java.util.Date;

/**
 * @author Rathish
 * 
 */
public class EntpSummary implements java.io.Serializable {
    
    //enterprise object members
    
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
    private Integer dcUsePctg;
    private Integer dcTemp;
    private char regCmplt;
    private char active;
    private String comments;
    private Date creDttm;
    
    //enterprise meter object members
    
    private int mCount;
    
    //gate keeper object members
    
    private Date expDttm;    

    public EntpSummary() {

    }
    
    public Short getSid()
	{
		return sid;
	}

	public void setSid(Short sid)
	{
		this.sid = sid;
	}

	public String getEnterpriseId()
	{
		return enterpriseId;
	}

	public void setEnterpriseId(String enterpriseId)
	{
		this.enterpriseId = enterpriseId;
	}

	public String getBlCd()
	{
		return blCd;
	}

	public void setBlCd(String blCd)
	{
		this.blCd = blCd;
	}

	public String geteName()
	{
		return eName;
	}

	public void seteName(String eName)
	{
		this.eName = eName;
	}

	public String getPhone()
	{
		return phone;
	}

	public void setPhone(String phone)
	{
		this.phone = phone;
	}

	public String getEmail()
	{
		return email;
	}

	public void setEmail(String email)
	{
		this.email = email;
	}

	public String getUserId()
	{
		return userId;
	}

	public void setUserId(String userId)
	{
		this.userId = userId;
	}

	public String getPasswd()
	{
		return passwd;
	}

	public void setPasswd(String passwd)
	{
		this.passwd = passwd;
	}

	public Short getSecQtn1()
	{
		return secQtn1;
	}

	public void setSecQtn1(Short secQtn1)
	{
		this.secQtn1 = secQtn1;
	}

	public String getAns1()
	{
		return ans1;
	}

	public void setAns1(String ans1)
	{
		this.ans1 = ans1;
	}

	public Short getSecQtn2()
	{
		return secQtn2;
	}

	public void setSecQtn2(Short secQtn2)
	{
		this.secQtn2 = secQtn2;
	}

	public String getAns2()
	{
		return ans2;
	}

	public void setAns2(String ans2)
	{
		this.ans2 = ans2;
	}

	public char getStoreFwd()
	{
		return storeFwd;
	}

	public void setStoreFwd(char storeFwd)
	{
		this.storeFwd = storeFwd;
	}

	public String getFwdUrl()
	{
		return fwdUrl;
	}

	public void setFwdUrl(String fwdUrl)
	{
		this.fwdUrl = fwdUrl;
	}

	public Integer getNoOfEmpl()
	{
		return noOfEmpl;
	}

	public void setNoOfEmpl(Integer noOfEmpl)
	{
		this.noOfEmpl = noOfEmpl;
	}

	public Integer getEntSqft()
	{
		return entSqft;
	}

	public void setEntSqft(Integer entSqft)
	{
		this.entSqft = entSqft;
	}

	public Integer getEntAssetCount()
	{
		return entAssetCount;
	}

	public void setEntAssetCount(Integer entAssetCount)
	{
		this.entAssetCount = entAssetCount;
	}

	public Integer getDcSqft()
	{
		return dcSqft;
	}

	public void setDcSqft(Integer dcSqft)
	{
		this.dcSqft = dcSqft;
	}

	public Integer getDcAssetCount()
	{
		return dcAssetCount;
	}

	public void setDcAssetCount(Integer dcAssetCount)
	{
		this.dcAssetCount = dcAssetCount;
	}

	public Integer getDcUsePctg()
	{
		return dcUsePctg;
	}

	public void setDcUsePctg(Integer dcUsePctg)
	{
		this.dcUsePctg = dcUsePctg;
	}

	public Integer getDcTemp()
	{
		return dcTemp;
	}

	public void setDcTemp(Integer dcTemp)
	{
		this.dcTemp = dcTemp;
	}

	public char getRegCmplt()
	{
		return regCmplt;
	}

	public void setRegCmplt(char regCmplt)
	{
		this.regCmplt = regCmplt;
	}

	public char getActive()
	{
		return active;
	}

	public void setActive(char active)
	{
		this.active = active;
	}

	public String getComments()
	{
		return comments;
	}

	public void setComments(String comments)
	{
		this.comments = comments;
	}

	public Date getCreDttm()
	{
		return creDttm;
	}

	public void setCreDttm(Date creDttm)
	{
		this.creDttm = creDttm;
	}

	public Date getExpDttm()
	{
		return expDttm;
	}

	public void setExpDttm(Date expDttm)
	{
		this.expDttm = expDttm;
	}

	public int getmCount()
	{
		return mCount;
	}

	public void setmCount(int mCount)
	{
		this.mCount = mCount;
	}


}
