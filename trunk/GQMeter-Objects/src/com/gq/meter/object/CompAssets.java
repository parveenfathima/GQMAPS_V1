package com.gq.meter.object;

// Generated Feb 18, 2013 3:22:08 PM by Hibernate Tools 3.4.0.CR1

/**
 * CompAssets generated by hbm2java
 */
public class CompAssets implements java.io.Serializable {

    private String assetId;
    private String typeId;

    public CompAssets() {
    }

    public CompAssets(String assetId, String typeId) {
        super();
        this.assetId = assetId;
        this.typeId = typeId;
    }

    public String getAssetId() {
        return this.assetId;
    }

    public void setAssetId(String assetId) {
        this.assetId = assetId;
    }

    public String getTypeId() {
        return this.typeId;
    }

    public void setTypeId(String typeId) {
        this.typeId = typeId;
    }

}
