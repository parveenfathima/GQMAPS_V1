package com.gq.meter;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

import org.snmp4j.CommunityTarget;
import org.snmp4j.Snmp;
import org.snmp4j.smi.OID;
import org.snmp4j.smi.VariableBinding;
import org.snmp4j.transport.DefaultUdpTransportMapping;

import com.gq.meter.object.Switch;
import com.gq.meter.util.MeterConstants;
import com.gq.meter.util.MeterUtils;

public class SwitchMeter implements GQSNMPMeter {

    @Override
    public GQMeterData implement(String communityString, String ipAddress, String snmpVersion) {

        Snmp snmp = null;

        String assetId = null; // unique identifier about the asset
        String sysName = null;
        String sysIP = null; // string
        String sysDescr = null;
        String sysContact = null;
        String sysLocation = null; // string
        String extras = null; // anything device specific but to be discussed v2

        long uptime = 0; // seconds
        long numberOfPorts = 0;
        long numberOfPortsUp = 0;
        long networkBytesIn = 0; // bytes , v2
        long networkBytesOut = 0; // bytes , v2
        long costToRoot = 0; // v2

        sysIP = ipAddress;

        CommunityTarget target = null;

        List<String> errorList = new LinkedList<String>();

        try {
            snmp = new Snmp(new DefaultUdpTransportMapping());
            snmp.listen();
            target = MeterUtils.makeTarget(ipAddress, communityString, snmpVersion);
        }
        catch (IOException e) {
            e.printStackTrace();
            return null;
        }

        // The following oid's is used to get system parameters

        String oidString = "1.3.6.1.2.1.1";
        String temp;
        String tempStr;

        OID rootOID = new OID(oidString);
        List<VariableBinding> result = null;

        result = MeterUtils.walk(rootOID, target); // walk done with the
                                                   // initial assumption
                                                   // that device is v2
        if (result != null && !result.isEmpty()) {
            System.out.println("result : " + result);

            temp = oidString + ".1.0";
            sysDescr = MeterUtils.getSNMPValue(temp, result);
            System.out.println("System Description : " + sysDescr);

            temp = oidString + ".3.0";
            tempStr = MeterUtils.getSNMPValue(temp, result);
            // System.out.println("Uptime : " + tempStr);
            uptime = MeterUtils.upTimeCalc(tempStr);
            System.out.println("Uptime : " + uptime);

            temp = oidString + ".4.0";
            sysContact = MeterUtils.getSNMPValue(temp, result);
            System.out.println("System Contact : " + sysContact);

            temp = oidString + ".5.0";
            sysName = MeterUtils.getSNMPValue(temp, result);
            System.out.println("System Name : " + sysName);

            temp = oidString + ".6.0";
            sysLocation = MeterUtils.getSNMPValue(temp, result);
            System.out.println("System Location : " + sysLocation);
        }
        else {
            errorList.add("Root OID : 1.3.6.1.2.1.1" + " " + "Basic info of a asset gets failed");
        }

        // The following oid's is used to get number of parameters
        oidString = "1.3.6.1.2.1.2.1";
        rootOID = new OID(oidString);
        result = MeterUtils.walk(rootOID, target);

        if (result != null && !result.isEmpty()) {
            temp = oidString + ".0";
            tempStr = MeterUtils.getSNMPValue(temp, result);
            numberOfPorts = Integer.parseInt(tempStr);
            System.out.println("Number Of Ports : " + numberOfPorts);
        }
        else {
            errorList.add("Root OID : 1.3.6.1.2.1.1" + " " + "Number of ports of switch gets failed");
        }

        // The following oid's is used to get number of active ports
        oidString = "1.3.6.1.2.1.2.2.1.7";
        rootOID = new OID(oidString);
        result = MeterUtils.walk(rootOID, target);

        if (result != null && !result.isEmpty()) {
            numberOfPortsUp = activePortsCalc(result, rootOID);
            System.out.println("Total Active Ports : " + numberOfPortsUp);

            String switchAssetId = sysDescr + sysName;
            int assetIdVal = switchAssetId.hashCode();
            String assetIdStr = Integer.toString(assetIdVal);
            assetId = MeterConstants.SNMP_SWITCH_ASSET + "-" + assetIdStr + "-" + numberOfPorts;
            System.out.println("assetId : " + assetId);
        }
        else {
            errorList.add("Root OID : 1.3.6.1.2.1.2.2.1.7" + " "
                    + "Total active ports and asset id of switch gets failed");
        }
        Switch switchObject = new Switch(assetId, uptime, numberOfPorts, numberOfPortsUp, networkBytesIn,
                networkBytesOut, costToRoot, sysName, sysIP, sysDescr, sysContact, sysLocation, extras);

        GQErrorInformation GqError = null;
        List<GQErrorInformation> gqerrorInformationList = new LinkedList<GQErrorInformation>();
        if (errorList == null || errorList.size() == 0 || errorList.isEmpty()) {
            gqerrorInformationList.add(GqError);
        }
        else {
            GqError = new GQErrorInformation(sysDescr, errorList);
            gqerrorInformationList.add(GqError);
        }
        GQMeterData gqMeterObject = new GQMeterData(gqerrorInformationList, switchObject);
        return gqMeterObject;
    }

    private long activePortsCalc(List<VariableBinding> result, OID rootOid) {

        String rootId = rootOid.toString();
        String totalPorts = null;
        long totalActivePorts = 0;
        for (VariableBinding vb : result) {

            String lastchar = String.valueOf(vb.getOid().last());

            totalPorts = rootId + "." + lastchar;

            if (totalPorts != null && vb.getOid().toString().equals(totalPorts)) {

                String activeAndInactivePorts = vb.getVariable().toString().trim();
                if (!activeAndInactivePorts.equalsIgnoreCase("2")) {
                    totalActivePorts++;
                    /*
                     * String activePorts = vb.getVariable().toString().trim(); System.out.println("activePorts : "
                     * +activePorts);
                     */
                }
            }
        }
        return totalActivePorts;

    }
}
