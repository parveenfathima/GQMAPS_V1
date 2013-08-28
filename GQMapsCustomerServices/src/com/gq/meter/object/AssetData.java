package com.gq.meter.object;

public class AssetData {
    private String asset_Id;
    private String ipAddr;
    private double serverCost;
    private int monthlyRent;

    public AssetData() {

    }

    public AssetData(String asset_Id, String ipAddr, double serverCost, int monthlyRent) {
        this.asset_Id = asset_Id;
        this.ipAddr = ipAddr;
        this.serverCost = serverCost;
        this.monthlyRent = monthlyRent;
    }

    /**
     * @return the asset_Id
     */
    public String getAsset_Id() {
        return asset_Id;
    }

    /**
     * @param asset_Id the asset_Id to set
     */
    public void setAsset_Id(String asset_Id) {
        this.asset_Id = asset_Id;
    }

    /**
     * @return the ipAddr
     */
    public String getIpAddr() {
        return ipAddr;
    }

    /**
     * @param ipAddr the ipAddr to set
     */
    public void setIpAddr(String ipAddr) {
        this.ipAddr = ipAddr;
    }

    /**
     * @return the serverCost
     */
    public double getServerCost() {
        return serverCost;
    }

    /**
     * @param serverCost the serverCost to set
     */
    public void setServerCost(double serverCost) {
        this.serverCost = serverCost;
    }

    /**
     * @return the monthlyRent
     */
    public int getMonthlyRent() {
        return monthlyRent;
    }

    /**
     * @param monthlyRent the monthlyRent to set
     */
    public void setMonthlyRent(int monthlyRent) {
        this.monthlyRent = monthlyRent;
    }

}
