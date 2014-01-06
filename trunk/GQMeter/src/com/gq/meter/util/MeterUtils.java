package com.gq.meter.util;

import java.io.IOException;

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

import com.gq.meter.object.Asset;

public final class MeterUtils {

    public long compMeterTime = 0;
    public long printMeterTime = 0;
    public long nsrgMeterTime = 0;
    public long storageMeterTime = 0;

    // public static final String restURL = "http://localhost:8080/GQGatekeeper/";
    public static Asset sysBasicInfo(String communityString, String ipAddress, String snmpVersion,
            List<String> errorList) {
        Asset assetObj = null;
        try {

            // ASSET
            String sysName = null; // string
            String sysDescr = null; // string
            String sysContact = null; // string
            String sysLocation = null; // string
            String assetId = null; // unique identifier about the asset
            String protocolId = null;
            String ipAddr = null;
            String oidString = "1.3.6.1.2.1.1";
            String temp;
            int sysLength;

            OID rootOID = new OID(oidString);
            List<VariableBinding> result = null;

            CommunityTarget target = null;
            target = MeterUtils.makeTarget(ipAddress, communityString, snmpVersion);

            result = MeterUtils.walk(rootOID, target); // walk done with the initial assumption that device is v2

            if (result != null && !result.isEmpty()) {
                temp = oidString + ".1.0";
                sysDescr = MeterUtils.getSNMPValue(temp, result).toLowerCase();
                sysLength = sysDescr.length();
                if (sysLength >= 200) {
                    sysDescr = sysDescr.substring(0, 199);
                }
                temp = oidString + ".4.0";
                sysContact = MeterUtils.getSNMPValue(temp, result);
                sysLength = sysContact.length();
                if (sysLength >= 45) {
                    sysContact = sysContact.substring(0, 44);
                }

                temp = oidString + ".5.0";
                sysName = MeterUtils.getSNMPValue(temp, result);
                sysLength = sysName.length();
                if (sysLength >= 45) {
                    sysName = sysName.substring(0, 44);
                }

                temp = oidString + ".6.0";
                sysLocation = MeterUtils.getSNMPValue(temp, result);
                sysLength = sysLocation.length();
                if (sysLength >= 45) {
                    sysLocation = sysLocation.substring(0, 44);
                }
            }
            else {
                errorList.add("Root OID : 1.3.6.1.2.1.1" + " " + MeterConstants.STANDARD_SYSTEM_ATTRIBUTES_ERROR);
            }

            ipAddr = ipAddress;
            assetObj = new Asset();
            assetObj.setAssetId(assetId);
            assetObj.setProtocolId(protocolId);
            assetObj.setName(sysName);
            assetObj.setDescr(sysDescr);
            assetObj.setIpAddr(ipAddr);
            assetObj.setContact(sysContact);
            assetObj.setLocation(sysLocation);
        }
        catch (Exception e) {
            errorList.add(ipAddress + " " + e.getMessage());
        }
        return assetObj;
    }

    /**
     * @param communityString
     * @param currIp
     * @return
     */
    public static HashMap<String, String> isSnmpConfigured(String communityString, String currIp) {

        String oidString = MeterConstants.SNMP_CHECK_OCTET;
        String snmpVersion = MeterConstants.SNMP_VERSION_2;

        HashMap<String, String> assetDetails = new HashMap<String, String>();
        CommunityTarget target = MeterUtils.makeTarget(currIp, communityString, snmpVersion);
        OID rootOID = new OID(oidString);
        List<VariableBinding> result = walk(rootOID, target);

        if (result == null || result.isEmpty()) {
            // may be the device is serving snmp but not version 2 , so lets try version 1
            snmpVersion = MeterConstants.SNMP_VERSION_1;

            target = makeTarget(currIp, communityString, snmpVersion); // needs to be done only once.
            result = walk(rootOID, target);

            if (result == null || result.isEmpty()) {
                // may be the device is not serving snmp at all. so lets get out or throw io exception

                assetDetails.put("snmpUnKnownIp", currIp);
                return assetDetails; // if snmp is configured & the SNMP_CHECK_OCTET doesn't exist then return null;
            }// 2nd if ends
        }// 1st if ends

        assetDetails.put("snmpVersion", snmpVersion);
        assetDetails.put("assetDesc", result.get(0).getVariable().toString());

        assetDetails.put("snmpKnownIp", currIp);
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

        OID rootOID = new OID(oidString);

        List<VariableBinding> result = walk(rootOID, target);
        mProtocol = MeterProtocols.PRINTER;

        if (result == null || result.size() == 0 || result.isEmpty()) {
            oidString = MeterConstants.SNMP_CHECK_NSRG_OCTET; // ISR
            rootOID = new OID(oidString);
            result = walk(rootOID, target);
            mProtocol = MeterProtocols.NSRG;

            if (result == null || result.size() == 0 || result.isEmpty()) {
                oidString = MeterConstants.SNMP_CHECK_COMPUTER_OCTET;// computer
                rootOID = new OID(oidString);
                result = walk(rootOID, target);
                mProtocol = MeterProtocols.COMPUTER;

                if (result == null || result.size() == 0 || result.isEmpty()) {
                    // We are here either due to the fact that this asset is not one of the above three.
                    // or the network speeds were very low and hence check octet call timed out.
                    // or lastly the device may be a storage device
                    // for now we will return null;
                    // mProtocol = MeterProtocols.STORAGE;
                    mProtocol = null;

                }
            }
        }
        return mProtocol;
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
            boolean status = inet.isReachable(5000); // Timeout = 5000 milli seconds

            if (status) {
                hostIsReachable = true;
                System.out.println(" [GQMETER] Status : Host is reachable");
            }
            else {
                System.out.println(" [GQMETER] Status : Host is not reachable : " + ipAddress);
            }
        }
        catch (UnknownHostException e) {
            System.err.println(" [GQMETER] Host does not exists : " + ipAddress);
        }
        catch (IOException e) {
            System.err.println(" [GQMETER] Error in reaching the Host : " + ipAddress);
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
    public static LinkedList<String> getIpAddressesInInclusiveRange(String ipAddrLowerBound, String ipAddrUpperBound) {

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
            TransportMapping<?> transport = new DefaultUdpTransportMapping();
            Snmp snmp = new Snmp(transport);
            transport.listen();
            boolean finished = false;
            while (!finished) {
                VariableBinding vb = null;
                ResponseEvent respEvt = snmp.send(requestPDU, target);
                PDU responsePDU = respEvt.getResponse();
                if (responsePDU != null) {
                    Vector<?> vbs = responsePDU.getVariableBindings();
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
            System.out.println(" [GQMETER] Exception occured while doing the snmp walk : " + e);
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
     * Overriding the Comparator class compare method to compare two ip's numeric values
     */
    public static Comparator<String> ipComparator = new Comparator<String>() {
        @Override
        public int compare(String ip1, String ip2) {
            return toNumeric(ip1).compareTo(toNumeric(ip2));
        }
    };

	// this method returns
	// 1.ip address , if cant resolve it to hostname
	// 2.trimmed to last domain name in case of valid host resolution
	// 3.localhost for 127.0.0.1 related strings
	// 4.null for null
	public static String getTrimmedHostName(String ipAddress) {
		System.out.println("incoming ip address  = "+ ipAddress );
		if ( ( ipAddress == null ) || ( ipAddress.trim().equals("") ) ) {
			return null;
		}
		
		ipAddress = ipAddress.trim();
		
		if ( ipAddress.contains("127.0.0.1")) {
			return "localhost";
		}
		else { // probably a good ip
	        InetAddress addr;
			try {
				addr = InetAddress.getByName(ipAddress);
			} catch (UnknownHostException e) {
				return "unknown-host";
			}
			
	        String cnName = addr.getCanonicalHostName();

	        if ( cnName.equals(ipAddress)) {
	        	return ipAddress;
	        }
	        else {
		   	    String[] hostNameParts = cnName.split("\\.");
		   	       
		   	    //to trim the unwanted characters and to display a valid domain names like google.co.in..
		   	    if(hostNameParts.length >= 3) {
		   	    	return hostNameParts[hostNameParts.length-3]+"."+hostNameParts[hostNameParts.length-2]+"."+hostNameParts[hostNameParts.length-1];
		   	    } 
		   	    else {// to display a completely qualified domain names
		   	    	return hostNameParts[hostNameParts.length-2]+"."+hostNameParts[hostNameParts.length-1];
		   	    }
	        }
		}
	} // getTrimmedHostName ends
	

} // class ends
