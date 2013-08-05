package com.gq.meter.object;

import java.util.List;

public class MapsDomainBean {
    List<DevCtlg> devCtlgResult;
    List<SrvrAppType> srvrAppType;
    List<CompType> compType;
    List<AssetImp> assetImp;

    public List<SrvrAppType> getSrvrAppType() {
        return srvrAppType;
    }

    public void setSrvrAppType(List<SrvrAppType> srvrAppType) {
        this.srvrAppType = srvrAppType;
    }

    public List<DevCtlg> getDevCtlgResult() {
        return devCtlgResult;
    }

    public void setDevCtlgResult(List<DevCtlg> devCtlgResult) {
        this.devCtlgResult = devCtlgResult;
    }

    public List<CompType> getCompType() {
        return compType;
    }

    public void setCompType(List<CompType> compType) {
        this.compType = compType;
    }

    public List<AssetImp> getAssetImp() {
        return assetImp;
    }

    public void setAssetImp(List<AssetImp> assetImp) {
        this.assetImp = assetImp;
    }

    public MapsDomainBean(List<DevCtlg> devCtlgResult, List<SrvrAppType> srvrAppType, List<CompType> compType,
            List<AssetImp> assetImp) {
        super();
        this.devCtlgResult = devCtlgResult;
        this.srvrAppType = srvrAppType;
        this.compType = compType;
        this.assetImp = assetImp;
    }

}
