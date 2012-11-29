package com.gq.meter.util;

import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Vector;
import java.util.concurrent.TimeUnit;

import org.snmp4j.CommunityTarget;
import org.snmp4j.PDU;
import org.snmp4j.Snmp;
import org.snmp4j.Target;
import org.snmp4j.TransportMapping;
import org.snmp4j.event.ResponseEvent;
import org.snmp4j.mp.SnmpConstants;
import org.snmp4j.smi.Integer32;
import org.snmp4j.smi.Null;
import org.snmp4j.smi.OID;
import org.snmp4j.smi.OctetString;
import org.snmp4j.smi.UdpAddress;
import org.snmp4j.smi.VariableBinding;
import org.snmp4j.transport.DefaultUdpTransportMapping;

import com.gq.meter.ComputerMeter;
import com.gq.meter.PrinterMeter;
import com.gq.meter.SwitchMeter;

public class MeterUtils {

    /**
     * @param communityString
     * @param currIp
     * @return
     */
    public static HashMap<String, String> isSnmpConfigured(String communityString, String currIp) {
        String oidString = MeterConstants.SNMP_CHECK_OCTET;
        HashMap<String, String> assetType = null;
        String asset = null;
        String snmpVersion = MeterConstants.SNMP_VERSION_2;

        CommunityTarget target = MeterUtils.makeTarget(currIp, communityString, snmpVersion);
        OID rootOID = new OID(oidString);
        List<VariableBinding> result = walk(rootOID, target);
        if (result == null || result.isEmpty() || result.size() == 0) {
            // may be the device is serving snmp but not version 2 , so lets try version 1
            System.out.println("SNMP Version2 is failed for this device,trying for version1 now");
            snmpVersion = MeterConstants.SNMP_VERSION_1;
            target = makeTarget(currIp, communityString, snmpVersion); // needs to be done only once.
            result = walk(rootOID, target);
            if (result == null || result.size() == 0 || result.isEmpty()) {
                // may be the device is not serving snmp at all. so lets get out or throw io exception
                System.out.println("SNMP Version2 && version1 are failed ");
                return assetType; // if snmp is configured & the SNMP_CHECK_OCTET doesn't exist then return null;
            }
        }
        // call diff walks
        oidString = ".1.3.6.1.2.1.25.1.6"; // computer
        rootOID = new OID(oidString);
        result = walk(rootOID, target);
        asset = MeterConstants.SNMP_COMPUTER_ASSET;

        if (result == null || result.size() == 0 || result.isEmpty()) {
            oidString = ".1.3.6.1.2.1.25.3.5.1.1"; // Printer
            rootOID = new OID(oidString);
            result = walk(rootOID, target);
            asset = MeterConstants.SNMP_PRINTER_ASSET;

            if (result == null || result.size() == 0 || result.isEmpty()) {
                oidString = "1.3.6.1.2.1.17.2.6"; // Switch
                rootOID = new OID(oidString);
                result = walk(rootOID, target);
                asset = MeterConstants.SNMP_SWITCH_ASSET;

                if (result == null || result.size() == 0 || result.isEmpty()) {
                    oidString = ".1.3.6.1.2.1.83.1.1.7"; // router
                    rootOID = new OID(oidString);
                    result = walk(rootOID, target);
                    asset = MeterConstants.SNMP_ROUTER_ASSET;
                }
            }
        }
        if (asset != null) {
            assetType = new HashMap<String, String>();
            assetType.put("asset", asset);
            assetType.put("snmpVersion", snmpVersion);
        }

        return assetType;

    }

    /**
     * This method used to returns the asset object with all the informations
     * 
     * @param assetType
     * @param communityString
     * @param currIp
     * @param target
     * @return
     */
    public static Object getAssetObject(String assetType, String communityString, String currIp, String snmpVersion) {

        Object assetObject = null;
        switch (assetType.trim()) {

        case MeterConstants.SNMP_COMPUTER_ASSET:
            assetObject = new ComputerMeter().implement(communityString, currIp, snmpVersion);
            break;
        case MeterConstants.SNMP_PRINTER_ASSET:
            assetObject = new PrinterMeter().implement(communityString, currIp, snmpVersion);
            break;
        case MeterConstants.SNMP_SWITCH_ASSET:
            assetObject = new SwitchMeter().implement(communityString, currIp, snmpVersion);
            break;
        case MeterConstants.SNMP_ROUTER_ASSET:

            break;
        }

        return assetObject;
    }

    /**
     * This method used to find an asset is reachable or not from the host where our GQMeter is running.
     * 
     * @param ipAddress
     * @return
     */
    public static boolean isAssetAlive(String ipAddress) {
        boolean hostIsReachable = false;

        try {
            InetAddress inet = InetAddress.getByName(ipAddress);
            // System.out.println("Sending Ping Request to " + ipAddress);

            boolean status = inet.isReachable(5000); // Timeout = 5000 milli seconds

            if (status) {
                hostIsReachable = true;
                System.out.println("Status : Host is reachable");
            }
            else {
                System.out.println("Status : Host is not reachable : " + ipAddress);
            }
        }
        catch (UnknownHostException e) {
            System.err.println("Host does not exists : " + ipAddress);
        }
        catch (IOException e) {
            System.err.println("Error in reaching the Host : " + ipAddress);
        }
        return hostIsReachable;
    }// isAssetAlive ends

    /**
     * @param input
     * @return
     */
    public static final String nextIpAddress(final String input) {
        final String[] tokens = input.split("\\.");
        if (tokens.length != 4) throw new IllegalArgumentException();

        for (int i = tokens.length - 1; i >= 0; i--) {
            final int item = Integer.parseInt(tokens[i]);
            if (item < 255) {
                tokens[i] = String.valueOf(item + 1);
                for (int j = i + 1; j < 4; j++) {
                    tokens[j] = "0";
                }
                break;
            }
        }

        return new StringBuilder().append(tokens[0]).append('.').append(tokens[1]).append('.').append(tokens[2])
                .append('.').append(tokens[3]).toString();
    }// nextIpAddress ends

    /**
     * @param ipAddrLowerBound
     * @param ipAddrUpperBound
     * @return
     */
    public LinkedList<String> getIpAddressesInInclusiveRange(String ipAddrLowerBound, String ipAddrUpperBound) {

        LinkedList<String> ipList = new LinkedList<String>();

        ipList.add(ipAddrLowerBound);

        String currIp = ipAddrLowerBound;

        while (!(currIp = nextIpAddress(currIp)).equals(ipAddrUpperBound)) {
            ipList.add(currIp);
        }

        ipList.add(ipAddrUpperBound);

        return ipList;

    } // getIpAddressesInInclusiveRange ends

    /**
     * @param is
     * @return
     * @throws IOException
     */
    public static String read(InputStream is) throws IOException {
        StringWriter sessions = new StringWriter();
        int c;
        while ((c = is.read()) != -1) {
            sessions.write(c);
        }
        return sessions.toString();
    }// read ends

    /**
     * This method returns a Target, which contains information about where the data should be fetched and how.
     * 
     * @return
     */
    public static CommunityTarget makeTarget(String protocolAddress, String communityString, String version) {
        UdpAddress targetAddress = new UdpAddress(protocolAddress + '/' + 161);
        CommunityTarget target = new CommunityTarget();
        target.setCommunity(new OctetString(communityString));
        target.setAddress(targetAddress);
        target.setRetries(1);
        target.setTimeout(1500);
        if (version.equalsIgnoreCase("v2c")) {
            target.setVersion(SnmpConstants.version2c);
        }
        else if (version.equalsIgnoreCase("v1")) {
            target.setVersion(SnmpConstants.version1);
        }
        else {
            target.setVersion(SnmpConstants.version3);
        }
        return target;
    }

    /**
     * @param rootOID
     * @param target
     * @return
     */
    public static List<VariableBinding> walk(OID rootOID, Target target) {
        List<VariableBinding> ret = new ArrayList<VariableBinding>();
        PDU requestPDU = new PDU();
        requestPDU.add(new VariableBinding(rootOID));
        requestPDU.setType(PDU.GETNEXT);
        requestPDU.setMaxRepetitions(5);

        try {
            TransportMapping transport = new DefaultUdpTransportMapping();
            Snmp snmp = new Snmp(transport);
            transport.listen();

            boolean finished = false;
            int iter = 0;

            while (!finished) {

                VariableBinding vb = null;

                ResponseEvent respEvt = snmp.send(requestPDU, target);
                PDU responsePDU = respEvt.getResponse();

                if (responsePDU != null) {
                    Vector vbs = responsePDU.getVariableBindings();
                    if (vbs != null && vbs.size() > 0) {
                        for (int i = 0; i < vbs.size(); i++) {
                            // vb sanity check
                            vb = (VariableBinding) vbs.get(i);
                            if (vb.getOid() == null) {
                                finished = true;
                                break;
                            }
                            else if (vb.getOid().size() < rootOID.size()) {
                                finished = true;
                                break;
                            }
                            else if (rootOID.leftMostCompare(rootOID.size(), vb.getOid()) != 0) {
                                finished = true;
                                break;
                            }
                            else if (Null.isExceptionSyntax(vb.getVariable().getSyntax())) {
                                finished = true;
                                break;
                            }
                            else if (vb.getOid().compareTo(rootOID) <= 0) {
                                finished = true;
                                break;
                            }
                            ret.add(vb);
                        }
                    }
                }

                if (!finished) {
                    if (responsePDU == null) {
                        finished = true;
                    }
                    else if (responsePDU.getErrorStatus() != 0) {
                        finished = true;
                    }
                    else {
                        // Set up the variable binding for the next entry.
                        requestPDU.setRequestID(new Integer32(0));
                        requestPDU.set(0, vb);
                    }
                }
            }
            snmp.close();
        }
        catch (IOException e) {
            System.out.println("Exception occured while doing the snmp walk : " + e);
        }
        return ret;
    }

    public static String getSNMPValue(String octetString, List<VariableBinding> result) {

        for (VariableBinding vb : result) {
            if (octetString.equals(vb.getOid().toString())) {
                return vb.getVariable().toString();
            }
        } // for loop ends
        return null;
    }
    
    public static long upTimeCalc(String time) {
        String dayString = null;
        String[] upTimeArray = null;
        String timeString = null;
        long dayseconds = 0L;
        long hourSec = 0L;
        long minSec = 0L;
        long seconds = 0L;
        String secondsConc = null;

        if (time.contains(",")) {
            upTimeArray = time.split(",");
        }
        if (upTimeArray != null) {
            dayString = upTimeArray[0].trim();
            timeString = upTimeArray[1].trim();
        }
        else {
            timeString = time.trim();
        }

        if (dayString != null) {
            System.out.println("dayString :" + dayString);
            if (dayString.split(" ")[1].toString().trim().equals("day")) {
                long day = Long.parseLong(dayString.replace("day", "").trim());
                dayseconds = TimeUnit.SECONDS.convert(day, TimeUnit.DAYS);
            }
            else {
                long days = Long.parseLong(dayString.replace("days", "").trim());
                dayseconds = TimeUnit.SECONDS.convert(days, TimeUnit.DAYS);
            }
        }
        if (timeString != null) {
            String[] timeArray = timeString.split(":");

            long hour = Long.parseLong(timeArray[0].trim());
            hourSec = TimeUnit.SECONDS.convert(hour, TimeUnit.HOURS);

            long minutes = Long.parseLong(timeArray[1].trim());
            minSec = TimeUnit.SECONDS.convert(minutes, TimeUnit.MINUTES);

            seconds = Long.parseLong(timeArray[2].split("\\.")[0].trim());
        }
        long secs = dayseconds + hourSec + minSec + seconds;
        return secs;
    }

} // class ends
