package com.gq.meter.assist;

import com.gq.meter.util.MeterProtocols;

public class ProtocolData {

    // name of the protocol implemented.. can only be from a list of implemented protocols such as
    // air , water , router , computer , power , switch , printer etc
    MeterProtocols protocol;

    // contains the actual protocol data , like power , computer , water etc
    String data;

    public ProtocolData(MeterProtocols protocol, String data) {
        super();
        this.protocol = protocol;
        this.data = data;
    }

    public MeterProtocols getProtocol() {
        return protocol;
    }

    public String getData() {
        return data;
    }

}