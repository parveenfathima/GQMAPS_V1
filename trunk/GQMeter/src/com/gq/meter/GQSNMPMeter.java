package com.gq.meter;

public interface GQSNMPMeter {

    public GQMeterData implement(String communityString, String ipAddress, String snmpVersion);

}
