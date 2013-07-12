/**
 * 
 */
package com.gq.meter.object;

/**
 * @author Rathish
 * 
 */
public class AssetLoad {
    private String assetId;
    private String name;
    private double loadAvg;

    public AssetLoad() {

    }

    public AssetLoad(String assetId, String name, double loadAvg) {
        this.setAssetId(assetId);
        this.name = name;
        this.loadAvg = loadAvg;

    }

    /**
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * @param name the name to set
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return the loadAvg
     */
    public double getLoadAvg() {
        return loadAvg;
    }

    /**
     * @param loadAvg the loadAvg to set
     */
    public void setLoadAvg(double loadAvg) {
        this.loadAvg = loadAvg;
    }

    public String getAssetId() {
        return assetId;
    }

    public void setAssetId(String assetId) {
        this.assetId = assetId;
    }

}
