package com.gq.meter.util;

import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;
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
import com.gq.meter.GQMeterData;
import com.gq.meter.PrinterMeter;
import com.gq.meter.NSRGMeter;

public class MeterUtils {

    public static long snmpKnownTime = 0;
    public static long snmpUnknownTime = 0;
    public static long compMeterTime = 0;
    public static long printMeterTime = 0;
    public static long isrMeterTime = 0;
    public static LinkedList<String> snmpKnownIPList = new LinkedList<String>();;
    public static LinkedList<String> snmpUnknownIPList = new LinkedList<String>();
    public static String restURL = "http://localhost:8080/GQGatekeeper/";

    /**
     * @param communityString
     * @param currIp
     * @return
     */
    public static HashMap<String, String> isSnmpConfigured(String communityString, String currIp) {
        long snmpStartTime = System.currentTimeMillis();

        String oidString = MeterConstants.SNMP_CHECK_OCTET;
        String snmpVersion = MeterConstants.SNMP_VERSION_2;

        HashMap<String, String> assetDetails = new HashMap<String, String>();
        CommunityTarget target = MeterUtils.makeTarget(currIp, communityString, snmpVersion);
        OID rootOID = new OID(oidString);
        List<VariableBinding> result = walk(rootOID, target);

        if (result == null || result.isEmpty()) {
            // may be the device is serving snmp but not version 2 , so lets try version 1
            System.out.println("SNMP Version2 is failed for this device,trying for version1 now");
            snmpVersion = MeterConstants.SNMP_VERSION_1;

            target = makeTarget(currIp, communityString, snmpVersion); // needs to be done only once.
            result = walk(rootOID, target);

            if (result == null || result.isEmpty()) {
                // may be the device is not serving snmp at all. so lets get out or throw io exception
                System.out.println("SNMP Version2 && version1 are failed, The asset is not configure with SNMP ");
                long snmpEndTime = System.currentTimeMillis();

                snmpUnknownTime = snmpUnknownTime + (snmpEndTime - snmpStartTime);// Time taken to find snmp is not
                                                                                  // configured
                snmpUnknownIPList.add(currIp);

                System.out.println("### SNMP is not configured in this device ### : " + (snmpEndTime - snmpStartTime));
                return assetDetails; // if snmp is configured & the SNMP_CHECK_OCTET doesn't exist then return null;
            }// 2nd if ends
        }// 1st if ends

        assetDetails.put("snmpVersion", snmpVersion);
        assetDetails.put("assetDesc", result.get(0).getVariable().toString());

        long snmpEndTime = System.currentTimeMillis();

        snmpKnownTime = snmpKnownTime + (snmpEndTime - snmpStartTime); // Time taken to find snmp is configured
        snmpKnownIPList.add(currIp);

        System.out.println("*** Time taken to find isSnmpConfigured or not : " + (snmpEndTime - snmpStartTime));
        return assetDetails;
    }

    /**
     * @param communityString
     * @param currIp
     * @param snmpVersion
     * @return
     */
    public static MeterProtocols getAssetType(String communityString, String currIp, String snmpVersion) {
        CommunityTarget target = MeterUtils.makeTarget(currIp, communityString, snmpVersion);
        String oidString = MeterConstants.SNMP_CHECK_PRINTER_OCTET; // Printer
        MeterProtocols mProtocol = null;
        // call diff walks

        OID rootOID = new OID(oidString);
        List<VariableBinding> result = walk(rootOID, target);
        mProtocol = MeterProtocols.PRINTER;

        if (result == null || result.size() == 0 || result.isEmpty()) {
            oidString = MeterConstants.SNMP_CHECK_NSRG_OCTET; // ISR
            rootOID = new OID(oidString);
            result = walk(rootOID, target);
            mProtocol = MeterProtocols.NSRG;

            if (result == null || result.size() == 0 || result.isEmpty()) {
                oidString = MeterConstants.SNMP_CHECK_COMPUTER_OCTET; // Computer
                rootOID = new OID(oidString);
                result = walk(rootOID, target);
                mProtocol = MeterProtocols.COMPUTER;

                if (result == null || result.size() == 0 || result.isEmpty()) {
                    mProtocol = MeterProtocols.UNKNOWN;
                    return mProtocol;
                }
            }
        }
        return mProtocol;
    }

    /**
     * This method used to returns the asset object with all the informations
     * 
     * @param protocol
     * @param communityString
     * @param currIp
     * @param snmpVersion
     * @param switchList
     * @return
     */
    public static GQMeterData getAssetObject(MeterProtocols protocol, String communityString, String currIp,
            String snmpVersion, LinkedList<String> switchList) {

        GQMeterData assetObject = null;
        if (protocol.equals(MeterProtocols.PRINTER)) {
            assetObject = new PrinterMeter().implement(communityString, currIp, snmpVersion, switchList);
        }
        else if (protocol.equals(MeterProtocols.NSRG)) {
            assetObject = new NSRGMeter().implement(communityString, currIp, snmpVersion, switchList);
        }
        else if (protocol.equals(MeterProtocols.COMPUTER)) {
            assetObject = new ComputerMeter().implement(communityString, currIp, snmpVersion, switchList);
        }
        /*
         * switch (protocol) {
         * 
         * case PRINTER: assetObject = new PrinterMeter().implement(communityString, currIp, snmpVersion); break; case
         * ISR: assetObject = new NSRGMeter().implement(communityString, currIp, snmpVersion); break; case COMPUTER:
         * assetObject = new ComputerMeter().implement(communityString, currIp, snmpVersion); break; }
         */

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
        }// for loop ends

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
     * @param protocolAddress
     * @param communityString
     * @param version
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

    /**
     * @param octetString
     * @param result
     * @return
     */
    public static String getSNMPValue(String octetString, List<VariableBinding> result) {

        for (VariableBinding vb : result) {
            if (octetString.equals(vb.getOid().toString())) {
                return vb.getVariable().toString();
            }
        } // for loop ends
        return null;
    }

    /**
     * @param time
     * @return
     */
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

    /**
     * This mmethod used to read the meterid and meter switches from the asset file
     * 
     * @param line
     * @param assetSwitches
     * @return
     */
    public static HashMap<MeterProtocols, LinkedList<String>> manageSwitches(String line,
            HashMap<MeterProtocols, LinkedList<String>> assetSwitches) {
        if (line.toLowerCase().startsWith(MeterConstants.METER_ID)) {
            line = line.replace(MeterConstants.METER_ID, "").trim();
            System.out.println("##############################################");
            System.out.println("Meter id : " + line);
            System.out.println("##############################################");
        }
        else if (line.toLowerCase().startsWith(MeterConstants.COMPUTER_SWITCHS)) {
            line = line.replace(MeterConstants.COMPUTER_SWITCHS, "").trim();
            LinkedList<String> compSwitchList = null;

            if (line.contains(MeterConstants.FULL_DETAILS)) {
                compSwitchList = new LinkedList<String>();
                compSwitchList.add(MeterConstants.FULL_DETAILS);
                assetSwitches.put(MeterProtocols.COMPUTER, compSwitchList);
            }
            else {
                compSwitchList = new LinkedList<String>();
                for (String switches : line.split("\\|")) {
                    compSwitchList.add(switches);
                }
                assetSwitches.put(MeterProtocols.COMPUTER, compSwitchList);
            }

        }
        else if (line.toLowerCase().startsWith(MeterConstants.PRINTER_SWITCHS)) {
            line = line.replace(MeterConstants.PRINTER_SWITCHS, "").trim();
            LinkedList<String> printerSwitchList = null;

            if (line.contains(MeterConstants.FULL_DETAILS)) {
                printerSwitchList = new LinkedList<String>();
                printerSwitchList.add(MeterConstants.FULL_DETAILS);
                assetSwitches.put(MeterProtocols.PRINTER, printerSwitchList);
            }
            else {
                printerSwitchList = new LinkedList<String>();
                for (String switches : line.split("\\|")) {
                    printerSwitchList.add(switches);
                }
                assetSwitches.put(MeterProtocols.PRINTER, printerSwitchList);
            }

        }
        else if (line.toLowerCase().startsWith(MeterConstants.NSRG_SWITCHS)) {
            line = line.replace(MeterConstants.NSRG_SWITCHS, "").trim();
            LinkedList<String> isrSwitchList = null;

            if (line.contains(MeterConstants.FULL_DETAILS)) {
                isrSwitchList = new LinkedList<String>();
                isrSwitchList.add(MeterConstants.FULL_DETAILS);
                assetSwitches.put(MeterProtocols.NSRG, isrSwitchList);
            }
            else {
                isrSwitchList = new LinkedList<String>();
                for (String switches : line.split("\\|")) {
                    isrSwitchList.add(switches);
                }
                assetSwitches.put(MeterProtocols.NSRG, isrSwitchList);
            }
        }
        return assetSwitches;
    }

    /**
     * This method used to convert the given ip address to a numeric value
     * 
     * @param ip
     * @return
     */
    private static Long toNumeric(String ip) {
        /*
         * long numericIp = 0l; try (Scanner sc = new Scanner(ip)) { Scanner scIp = sc.useDelimiter("\\."); numericIp =
         * (scIp.nextLong() << 24) + (scIp.nextLong() << 16) + (scIp.nextLong() << 8) + (scIp.nextLong()); } catch
         * (Exception e) { System.out.println("Exception occured while parsing the ip" + e); }
         */
        Scanner sc = new Scanner(ip).useDelimiter("\\.");
        long numericIp = (sc.nextLong() << 24) + (sc.nextLong() << 16) + (sc.nextLong() << 8) + (sc.nextLong());
        return numericIp;
    }

    /**
     * Overrding the Comaprator class comapare method to comapre two ip's numeric values
     */
    public static Comparator<String> ipComparator = new Comparator<String>() {
        @Override
        public int compare(String ip1, String ip2) {
            return toNumeric(ip1).compareTo(toNumeric(ip2));
        }
    };

} // class ends
