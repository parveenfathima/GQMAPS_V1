/**
 * 
 */
package com.gq.meter.object;

/**
 * @author Rathish
 * 
 */
public class EntpSummary implements java.io.Serializable {
    private Enterprise entp;
    private EnterpriseMeter entpMeter;
    private GateKeeper gkeeper;

    public EntpSummary() {

    }

    /**
     * @return the entp
     */
    public Enterprise getEntp() {
        return entp;
    }

    /**
     * @param entp the entp to set
     */
    public void setEntp(Enterprise entp) {
        this.entp = entp;
    }

    /**
     * @return the entpMeter
     */
    public EnterpriseMeter getEntpMeter() {
        return entpMeter;
    }

    /**
     * @param entpMeter the entpMeter to set
     */
    public void setEntpMeter(EnterpriseMeter entpMeter) {
        this.entpMeter = entpMeter;
    }

    /**
     * @return the gkeeper
     */
    public GateKeeper getGkeeper() {
        return gkeeper;
    }

    /**
     * @param gkeeper the gkeeper to set
     */
    public void setGkeeper(GateKeeper gkeeper) {
        this.gkeeper = gkeeper;
    }

}
