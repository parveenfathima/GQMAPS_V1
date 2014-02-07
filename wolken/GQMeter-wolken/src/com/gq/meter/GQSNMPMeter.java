package com.gq.meter;

import java.util.LinkedList;

public interface GQSNMPMeter {

    /**
     * This is the base method for all the meter class implementations
     * 
     * @param communityString
     * @param ipAddress
     * @param snmpVersion
     * @param toggleSwitches
     * @return
     */
    public GQMeterData implement(String communityString, String ipAddress, String snmpVersion,
            LinkedList<String> toggleSwitches);

}
