package com.gq.meter.object.assist;

// this class provides a container for all connected devices (is). one per is
/**
 * @author yogalakshmi.s
 * 
 */
public class ConnectedDevices {

    public String getIpAddress() {
        return ipAddress;
    }

    public String getConnectedPort() {
        return connectedPort;
    }

    public ConnectedDevices(String ipAddress, String connectedPort) {
        super();
        this.ipAddress = ipAddress;
        this.connectedPort = connectedPort;
    }

    String ipAddress;
    String connectedPort;

}
