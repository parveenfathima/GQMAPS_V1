package com.gq.meter.object;

// default package
// Generated Apr 10, 2013 12:25:53 PM by Hibernate Tools 3.4.0.CR1

import java.util.Date;

/**
 * RcmndtnSet generated by hbm2java
 */
public class RcmndtnSet implements java.io.Serializable {

    private byte snpshtId;
    private byte rcmndtnId;
    private Date applyDate;
    private String usrNotes;

    public RcmndtnSet() {
    }

    public RcmndtnSet(byte snpshtId, byte rcmndtnId) {
        this.snpshtId = snpshtId;
        this.rcmndtnId = rcmndtnId;
    }

    public RcmndtnSet(byte snpshtId, byte rcmndtnId, Date applyDate, String usrNotes) {
        this.snpshtId = snpshtId;
        this.rcmndtnId = rcmndtnId;
        this.applyDate = applyDate;
        this.usrNotes = usrNotes;
    }

    public byte getSnpshtId() {
        return this.snpshtId;
    }

    public void setSnpshtId(byte snpshtId) {
        this.snpshtId = snpshtId;
    }

    public byte getRcmndtnId() {
        return this.rcmndtnId;
    }

    public void setRcmndtnId(byte rcmndtnId) {
        this.rcmndtnId = rcmndtnId;
    }

    public Date getApplyDate() {
        return this.applyDate;
    }

    public void setApplyDate(Date applyDate) {
        this.applyDate = applyDate;
    }

    public String getUsrNotes() {
        return this.usrNotes;
    }

    public void setUsrNotes(String usrNotes) {
        this.usrNotes = usrNotes;
    }

}
