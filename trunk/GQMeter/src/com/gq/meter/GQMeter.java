package com.gq.meter;

import org.snmp4j.CommunityTarget;

public interface GQMeter {

    public Object implement(String communityString, String ipAddress, CommunityTarget target);

}
