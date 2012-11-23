package com.gq.meter.object;

public class ProtocolData {

    // name of the protocol implemented.. can only be from a list of implemented protocols such as
    // air , water , router , computer , power , switch , printer etc
    String protocol;

    // contains the actual protocol data , like power , computer , water etc
    String data;

    public ProtocolData(String protocol, String data) {
        super();
        this.protocol = protocol;
        this.data = data;
    }

    public String getProtocol() {
        return protocol;
    }

    public String getData() {
        return data;
    }

}