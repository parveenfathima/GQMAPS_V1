package com.gq.meter.object;

import java.io.Serializable;
import java.util.HashSet;

public class NSRG implements Serializable {

    private CPNId id;
    Asset assetObj;
    NSRGSnapshot nsrgSnapShot;
    HashSet<NSRGConnDevice> nsrgConnectedDevices;

    public NSRG() {

    }

    public CPNId getId() {
        return id;
    }

    public void setId(CPNId id) {
        this.id = id;
    }

    public Asset getAssetObj() {
        return assetObj;
    }

    public NSRGSnapshot getNsrgSnapShot() {
        return nsrgSnapShot;
    }

    public HashSet<NSRGConnDevice> getNsrgConnectedDevices() {
        return nsrgConnectedDevices;
    }

    public NSRG(CPNId id, Asset assetObj, NSRGSnapshot nsrgSnapShot, HashSet<NSRGConnDevice> nsrgConnectedDevices) {
        super();
        this.id = id;
        this.assetObj = assetObj;
        this.nsrgSnapShot = nsrgSnapShot;
        this.nsrgConnectedDevices = nsrgConnectedDevices;
    }

}
