/**
 * 
 */
package com.gq.meter.object;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;

/**
 * @author GQ
 * 
 */
public class Computer implements Serializable {

    private CPNId id;

    public void setId(CPNId id) {
        this.id = id;
    }

    Asset assetObj;
    OsType osTypeObj;
    CompSnapshot snapShot;
    ArrayList<CompInstSoftware> compInstSwList;
    ArrayList<CompProcess> compProcList;
    HashSet<CompConnDevice> CompConnDeviceSet;

    public Computer() {

    }

    public CPNId getId() {
        return id;
    }

    public Asset getAssetObj() {
        return assetObj;
    }

    public OsType getOsTypeObj() {
        return osTypeObj;
    }

    public CompSnapshot getSnapShot() {
        return snapShot;
    }

    public ArrayList<CompInstSoftware> getCompInstSwList() {
        return compInstSwList;
    }

    public ArrayList<CompProcess> getCompProcList() {
        return compProcList;
    }

    public HashSet<CompConnDevice> getCompConnDeviceSet() {
        return CompConnDeviceSet;
    }

    public Computer(CPNId id, Asset assetObj, OsType osTypeObj, CompSnapshot snapShot,
            ArrayList<CompInstSoftware> compInstSwList, ArrayList<CompProcess> compProcList,
            HashSet<CompConnDevice> compConnDeviceSet) {
        super();
        this.id = id;
        this.assetObj = assetObj;
        this.osTypeObj = osTypeObj;
        this.snapShot = snapShot;
        this.compInstSwList = compInstSwList;
        this.compProcList = compProcList;
        CompConnDeviceSet = compConnDeviceSet;
    }

}
