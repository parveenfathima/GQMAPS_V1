package com.gq.meter.object;

import java.util.List;

public class AllCustomerServices {

    List<OsType> ostypeResult;

    List<Protocol> protocolResult;

    List<SrvrAppType> srvrAppTypeResult;
    List<DevCtlg> DevCtlgResult;

    public List<OsType> getOstypeResult() {
        return ostypeResult;
    }

    public void setOstypeResult(List<OsType> ostypeResult) {
        this.ostypeResult = ostypeResult;
    }

    public List<Protocol> getProtocolResult() {
        return protocolResult;
    }

    public void setProtocolResult(List<Protocol> protocolResult) {
        this.protocolResult = protocolResult;
    }

    public List<SrvrAppType> getSrvrAppTypeResult() {
        return srvrAppTypeResult;
    }

    public void setSrvrAppTypeResult(List<SrvrAppType> srvrAppTypeResult) {
        this.srvrAppTypeResult = srvrAppTypeResult;
    }

    public List<DevCtlg> getDevCtlgResult() {
        return DevCtlgResult;
    }

    public void setDevCtlgResult(List<DevCtlg> DevCtlgResult) {
        this.DevCtlgResult = DevCtlgResult;
    }

    public AllCustomerServices(List<Protocol> protocolResult, List<SrvrAppType> srvrAppTypeResult,
            List<DevCtlg> devCtlgResult) {
        super();
        this.protocolResult = protocolResult;
        this.srvrAppTypeResult = srvrAppTypeResult;
        DevCtlgResult = devCtlgResult;
    }

}
