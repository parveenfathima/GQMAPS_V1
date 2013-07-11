package com.gq.meter;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import com.gq.meter.assist.ProtocolData;

public class GQMeterResponse {

    // meter id
    String gqmid;

    // run id
    int runid;

    // time when data is recorded at client site
    Date recDttm;

    // version of the meter - not used much in v1
    String version;

    // total number of assets scanned from the asset input file
    short assetScanned;

    // total number of assets scanned from the asset input file
    short assetDiscovered;

    // total number of assets scanned from the asset input file
    long runTimeMiliSeconds;;

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

    public int getRunid() {
        return runid;
    }

    public void setRunid(int runid) {
        this.runid = runid;
    }

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

    public short getAssetScanned() {
        return assetScanned;
    }

    public void setAssetScanned(short assetScanned) {
        this.assetScanned = assetScanned;
    }

    public short getAssetDiscovered() {
        return assetDiscovered;
    }

    public void setAssetDiscovered(short assetDiscovered) {
        this.assetDiscovered = assetDiscovered;
    }

    public long getRunTimeMiliSeconds() {
        return runTimeMiliSeconds;
    }

    public void setRunTimeMiliSeconds(long runTimeMiliSeconds) {
        this.runTimeMiliSeconds = runTimeMiliSeconds;
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
