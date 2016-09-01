package com.gq.meter.object;

import java.io.Serializable;
import java.util.HashSet;

public class Printer implements Serializable {

    private CPNId id;
    Asset assetObj;
    PrinterSnapshot printerSnapShot;
    HashSet<PrinterConnDevice> printerConnectedDevice;

    public CPNId getId() {
        return id;
    }

    public void setId(CPNId id) {
        this.id = id;
    }

    public Asset getAssetObj() {
        return assetObj;
    }

    public PrinterSnapshot getPrinterSnapShot() {
        return printerSnapShot;
    }

    public HashSet<PrinterConnDevice> getPrinterConnectedDevice() {
        return printerConnectedDevice;
    }

    public Printer(CPNId id, Asset assetObj, PrinterSnapshot printerSnapShot,
            HashSet<PrinterConnDevice> printerConnectedDevice) {
        super();
        this.id = id;
        this.assetObj = assetObj;
        this.printerSnapShot = printerSnapShot;
        this.printerConnectedDevice = printerConnectedDevice;
    }
}
