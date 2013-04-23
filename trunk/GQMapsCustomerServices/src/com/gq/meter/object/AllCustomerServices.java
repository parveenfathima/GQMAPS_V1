package com.gq.meter.object;

import java.util.List;

public class AllCustomerServices {

    List<LocationMaster> locationMasterResult;
    List<OsType> ostypeResult;
    List<PwrSlab> pwrSlabResult;
    List<Protocol> protocolResult;
    List<Rcmndtn> rcmndtnResult;
    List<SrvrAppType> srvrAppTypeResult;
    List<DeviceCatalog> deviceCatalogResult;

    public List<LocationMaster> getLocationMasterResult() {
        return locationMasterResult;
    }

    public void setLocationMasterResult(List<LocationMaster> locationMasterResult) {
        this.locationMasterResult = locationMasterResult;
    }

    public List<OsType> getOstypeResult() {
        return ostypeResult;
    }

    public void setOstypeResult(List<OsType> ostypeResult) {
        this.ostypeResult = ostypeResult;
    }

    public List<PwrSlab> getPwrSlabResult() {
        return pwrSlabResult;
    }

    public void setPwrSlabResult(List<PwrSlab> pwrSlabResult) {
        this.pwrSlabResult = pwrSlabResult;
    }

    public List<Protocol> getProtocolResult() {
        return protocolResult;
    }

    public void setProtocolResult(List<Protocol> protocolResult) {
        this.protocolResult = protocolResult;
    }

    public List<Rcmndtn> getRcmndtnResult() {
        return rcmndtnResult;
    }

    public void setRcmndtnResult(List<Rcmndtn> rcmndtnResult) {
        this.rcmndtnResult = rcmndtnResult;
    }

    public List<SrvrAppType> getSrvrAppTypeResult() {
        return srvrAppTypeResult;
    }

    public void setSrvrAppTypeResult(List<SrvrAppType> srvrAppTypeResult) {
        this.srvrAppTypeResult = srvrAppTypeResult;
    }

    public List<DeviceCatalog> getDeviceCatalogResult() {
        return deviceCatalogResult;
    }

    public void setDeviceCatalogResult(List<DeviceCatalog> deviceCatalogResult) {
        this.deviceCatalogResult = deviceCatalogResult;
    }

    public AllCustomerServices(List<LocationMaster> locationMasterResult, List<OsType> ostypeResult,
            List<PwrSlab> pwrSlabResult, List<Protocol> protocolResult, List<Rcmndtn> rcmndtnResult,
            List<SrvrAppType> srvrAppTypeResult, List<DeviceCatalog> deviceCatalogResult) {
        super();
        this.locationMasterResult = locationMasterResult;
        this.ostypeResult = ostypeResult;
        this.pwrSlabResult = pwrSlabResult;
        this.protocolResult = protocolResult;
        this.rcmndtnResult = rcmndtnResult;
        this.srvrAppTypeResult = srvrAppTypeResult;
        this.deviceCatalogResult = deviceCatalogResult;
    }
}
