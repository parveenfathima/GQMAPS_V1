package com.gq.meter.object;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;

public class GQMeterData {

    // meter id
    String gqmid;

    // time when data is recorded at client site
    Date recDttm;

    // version of the meter - not used much in v1
    String version;

    // can only be 'pass' or 'fail'
    String status;

    // all informational details , errors or warnings as reported by the meter
    String comment;

    // class holds each asset discovery data along with the asset type

    // this list contains all the auto discovered assets and their details
    List<ProtocolData> assetInformationList = new LinkedList<ProtocolData>();

    // this list contains error or other notifications as reported by the individual asset data gathering modules.
    // there is no one to one correspondence between this list and the asset info list
    List<GQErrorInformation> errorInformationList = new LinkedList<GQErrorInformation>();

    public String getGqmid() {
        return gqmid;
    }

    public void setGqmid(String gqmid) {
        this.gqmid = gqmid;
    }

    public Date getRecDttm() {
        return recDttm;
    }

    public void setRecDttm(Date recDttm) {
        this.recDttm = recDttm;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public List<ProtocolData> getAssetInformationList() {
        return assetInformationList;
    }

    public void setAssetInformationList(List<ProtocolData> assetInformationList) {
        this.assetInformationList = assetInformationList;
    }

    public void addToAssetInformationList(List<ProtocolData> assetInformationList) {
        this.assetInformationList.addAll(assetInformationList);
    }

    public List<GQErrorInformation> getErrorInformationList() {
        return errorInformationList;
    }

    public void setErrorInformationList(List<GQErrorInformation> errorInformationList) {
        this.errorInformationList = errorInformationList;
    }
}
