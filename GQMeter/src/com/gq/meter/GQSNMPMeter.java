package com.gq.meter;

import java.util.LinkedList;

public interface GQSNMPMeter {

    public GQMeterData implement(String communityString, String ipAddress, String snmpVersion,
            LinkedList<String> toggleSwitches);

}
