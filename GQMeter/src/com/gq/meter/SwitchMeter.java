package com.gq.meter;

import java.io.IOException;
import java.util.ArrayList;
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

import com.gq.meter.object.GQErrorInformation;
import com.gq.meter.object.Switch;
import com.gq.meter.util.MeterUtils;

public class SwitchMeter implements GQMeter {

	Snmp snmp = null;
	String protocolAddress = null;
	String communityString = null;

	String assetId; // uniq identifier abt the asset

	public static long uptime; // seconds
	public static long numberOfPorts;
	public static long numberOfPortsUp;
	public static long networkBytesIn; // bytes , v2
	public static long networkBytesOut; // bytes , v2
	public static long costToRoot; // v2

	public static String sysName;
	public static String sysIP; // string
	public static String sysDescr;
	public static String sysContact;
	public static String sysLocation; // string

	public static String extras; // anything device specific but to be discussed
									// , v2

	public static final String SNMP_VERSION_1 = "v1";
	public static final String SNMP_VERSION_2 = "v2c";
	public static final String SNMP_VERSION_3 = "v3";

	@Override
	public Object implement(String communityString, String ipAddress,
			CommunityTarget target) {
		if (communityString != null && ipAddress != null) {

			try {
				makeSnmpListen(ipAddress, communityString);
			} catch (IOException e) {
				e.printStackTrace();
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
			System.out.println("result : " + result);

			temp = oidString + ".1.0";
			sysDescr = getSNMPValue(temp, result);
			System.out.println("System Description : " + sysDescr);

			temp = oidString + ".3.0";
			tempStr = getSNMPValue(temp, result);
			// System.out.println("Uptime : " + tempStr);
			uptime = upTimeCalc(tempStr);
			System.out.println("Uptime : " + uptime);

			temp = oidString + ".4.0";
			sysContact = getSNMPValue(temp, result);
			System.out.println("System Contact : " + sysContact);

			temp = oidString + ".5.0";
			sysName = getSNMPValue(temp, result);
			System.out.println("System Name : " + sysName);

			temp = oidString + ".6.0";
			sysLocation = getSNMPValue(temp, result);
			System.out.println("System Location : " + sysLocation);

			// The following oid's is used to get number of parameters
			oidString = "1.3.6.1.2.1.2.1";
			rootOID = new OID(oidString);
			result = MeterUtils.walk(rootOID, target);

			temp = oidString + ".0";
			tempStr = getSNMPValue(temp, result);
			numberOfPorts = Integer.parseInt(tempStr);
			System.out.println("Number Of Ports : " + numberOfPorts);

			// The following oid's is used to get number of active ports
			oidString = "1.3.6.1.2.1.2.2.1.7";
			rootOID = new OID(oidString);
			result = MeterUtils.walk(rootOID, target);

			numberOfPortsUp = activePortsCalc(result, rootOID);
			System.out.println("Total Active Ports : " + numberOfPortsUp);

			String switchAssetId = sysDescr + sysName;
			int assetIdVal = switchAssetId.hashCode();
			String assetIdStr = Integer.toString(assetIdVal);
			assetId = "Switch" + "-" + assetIdStr + "-" + numberOfPorts;
			System.out.println("assetId : " + assetId);

			Switch switchObject = new Switch(assetId, uptime, numberOfPorts,
					numberOfPortsUp, networkBytesIn, networkBytesOut,
					costToRoot, sysName, sysIP, sysDescr, sysContact,
					sysLocation, extras);

			return switchObject;
		} else {
			String assetDescr = ""; // put the system ip and if u want system
									// description
			List errorList = new LinkedList<String>(); // add the null values
														// and empty details to
														// this list and return
			GQErrorInformation gqerr = new GQErrorInformation(assetDescr,
					errorList);
		}
		return null;
	}

	public void makeSnmpListen(String ip, String community) throws IOException {
		/**
		 * Port 161 is used for Read and Other operations Port 162 is used for
		 * the trap generation
		 */
		protocolAddress = ip; // "UDP:"+IP+"//"+"161";
		communityString = community;
		/**
		 * Start the SNMP session. If you forget the listen() method you will
		 * not get any answers because the communication is asynchronous and the
		 * listen() method listens for answers.
		 * 
		 * @throws IOException
		 */
		snmp = new Snmp(new DefaultUdpTransportMapping());
		snmp.listen();
	}

	private String getSNMPValue(String octetString, List<VariableBinding> result) {

		for (VariableBinding vb : result) {
			if (octetString.equals(vb.getOid().toString())) {
				return vb.getVariable().toString();
			}
		} // for loop ends
		return null;
	}

	public long upTimeCalc(String time) {
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
		} else {
			timeString = time.trim();
		}

		if (dayString != null) {
			System.out.println("dayString :" + dayString);
			if (dayString.split(" ")[1].toString().trim().equals("day")) {
				long day = Long.parseLong(dayString.replace("day", "").trim());
				dayseconds = TimeUnit.SECONDS.convert(day, TimeUnit.DAYS);
			} else {
				long days = Long
						.parseLong(dayString.replace("days", "").trim());
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
			secondsConc = timeArray[2].split("\\.")[1].trim();
		}
		long secs = dayseconds + hourSec + minSec + seconds;
		String secsString = String.valueOf(secs) + secondsConc;
		long sec = Long.parseLong(secsString);

		return sec;
	}

	public long activePortsCalc(List<VariableBinding> result, OID rootOid) {

		String rootId = rootOid.toString();
		String totalPorts = null;
		long totalActivePorts = 0;
		for (VariableBinding vb : result) {

			String lastchar = String.valueOf(vb.getOid().last());

			totalPorts = rootId + "." + lastchar;

			if (totalPorts != null && vb.getOid().toString().equals(totalPorts)) {

				String activeAndInactivePorts = vb.getVariable().toString()
						.trim();
				if (!activeAndInactivePorts.equalsIgnoreCase("2")) {
					totalActivePorts++;
					String activePorts = vb.getVariable().toString().trim();

				}
			}
		}
		return totalActivePorts;

	}
}
