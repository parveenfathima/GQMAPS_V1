package com.gq.meter;

import java.io.IOException;
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

import com.gq.meter.object.GQErrorInformation;
import com.gq.meter.object.Printer;
import com.gq.meter.util.MeterUtils;

public class PrinterMeter implements GQMeter {

	Snmp snmp = null;
	String protocolAddress = null;
	String communityString = null;

	private static String assetId; // uniq identifier abt the asset

	private static long uptime; // seconds
	private static double tonerPercentage;
	private static long outOfPaperIndicator; // 0 means no paper , v2
	private static long networkBytesIn; // bytes , v2
	private static long networkBytesOut; // bytes , v2
	private static long printsTakenCount; // v2

	private static String sysName;
	private static String sysIP; // string
	private static String sysDescr;
	private static String sysContact;
	private static String sysLocation; // string
	private static String errorCondition;
	private static String operationalState; // for this and the next value , we
											// maintain a table ; need to work
											// more on it.
	private static String currentState;
	private static String mfgModel; // v2
	private static String tonerColor;

	private static String extras; // anything device specific but to be
									// discussed , v2

	public static final String SNMP_VERSION_1 = "v1";
	public static final String SNMP_VERSION_2 = "v2c";
	public static final String SNMP_VERSION_3 = "v3";

	HashMap<String, String> printerStatus;

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

			// The below oid's is used to get the toner percentage

			oidString = ".1.3.6.1.2.1.43.11.1.1";
			rootOID = new OID(oidString);
			result = MeterUtils.walk(rootOID, target);

			tonerPercentage = tonerPercentageCalc(result, rootOID);
			System.out.println("Toner Percentage : " + tonerPercentage);

			oidString = "1.3.6.1.2.1.43.11.1.1.6";
			rootOID = new OID(oidString);
			result = MeterUtils.walk(rootOID, target);

			if (result.toString().trim()
					.contains("1.3.6.1.2.1.43.11.1.1.6.1.1")) {
				temp = oidString + ".1.1";
				tonerColor = getSNMPValue(temp, result);
				System.out.println("Toner Color : " + tonerColor);
			} else {
				System.out.println("Toner Color : Color toner printer");
			}

			// The following oid's is used to get the errorCondition,
			// operationalState , currentState , mfgMakeAndModel

			oidString = "1.3.6.1.2.1.25.3";
			rootOID = new OID(oidString);
			result = MeterUtils.walk(rootOID, target);

			temp = oidString + ".5.1.2.1";
			errorCondition = getSNMPValue(temp, result);
			System.out.println("Error Condition : " + errorCondition);

			oidString = "1.3.6.1.2.1.25.3";
			rootOID = new OID(oidString);
			result = MeterUtils.walk(rootOID, target);

			printerStatus = printerStatusCalc(result, rootOID);
			currentState = printerStatus.get("currentState");
			System.out.println("Current State : " + currentState);
			operationalState = printerStatus.get("operationalState");
			System.out.println("Operational State : " + operationalState);

			temp = oidString + ".2.1.3.1";
			mfgModel = getSNMPValue(temp, result);
			System.out.println("Manufacture Make And Model : " + mfgModel);

			// The below oid's is used to get the asset id

			oidString = "1.3.6.1.2.1.43.5.1.1";
			rootOID = new OID(oidString);
			result = MeterUtils.walk(rootOID, target);

			temp = oidString + ".17.1";
			assetId = getSNMPValue(temp, result);
			System.out.println("Asset Id : " + "Printer" + "-" + assetId);

			Printer printerObject = new Printer(assetId, uptime,
					tonerPercentage, outOfPaperIndicator, networkBytesIn,
					networkBytesOut, printsTakenCount, sysName, sysIP,
					sysDescr, sysContact, sysLocation, errorCondition,
					operationalState, currentState, mfgModel, tonerColor,
					extras);

			return printerObject;
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

	public double tonerPercentageCalc(List<VariableBinding> result, OID rootOid) {

		String totalValue = null;
		String remainingUsage = null;
		double totalValueFloat = 0L;
		double remainingUsagefloat = 0L;
		double finalTonerPercentage = 0L;
		String rootId = rootOid.toString();

		for (VariableBinding vb : result) {

			totalValue = rootId + ".8.1.1";
			remainingUsage = rootId + ".9.1.1";

			if (totalValue != null && vb.getOid().toString().equals(totalValue)) {

				String totalValuestr = vb.getVariable().toString().trim();
				totalValueFloat = Double.parseDouble(totalValuestr);

			} else if (remainingUsage != null
					&& vb.getOid().toString().equals(remainingUsage)) {

				String remainingUsageStr = vb.getVariable().toString().trim();
				remainingUsagefloat = Double.parseDouble(remainingUsageStr);

			}

		} // for loop ends
		finalTonerPercentage = (remainingUsagefloat / totalValueFloat) * 100;
		return finalTonerPercentage;

	}

	public HashMap<String, String> printerStatusCalc(
			List<VariableBinding> result, OID rootOid) {

		String operationalStateKey = "operationalState";
		String currentStateKey = "currentState";
		String currentStatus = null;
		String operationalStatus = null;
		String rootId = rootOid.toString();
		String currentStateOid = rootId + ".5.1.1.1";
		String operationalStateOid = rootId + ".2.1.5.1";
		String N_A = "Not Avaiable";
		// Predefined printer status map
		HashMap<String, String> printerOperationalStateMap = new HashMap<String, String>();
		printerOperationalStateMap.put("2", "Running");
		printerOperationalStateMap.put("3", "Warning");

		HashMap<String, String> printerCurrentStateMap = new HashMap<String, String>();
		printerCurrentStateMap.put("1", "Other");
		printerCurrentStateMap.put("3", "Idle");
		for (VariableBinding vb : result) {
			if (currentStateOid != null
					&& vb.getOid().toString().equals(currentStateOid)) {
				String currentStateValue = vb.getVariable().toString().trim(); // has
																				// the
																				// value
																				// 1
																				// or
																				// 3
																				// or
																				// x
				if (printerCurrentStateMap.containsKey(currentStateValue)) { // check
																				// in
																				// the
																				// predefined
																				// map
																				// whether
																				// the
																				// map
																				// has
																				// the
																				// value
					currentStatus = printerCurrentStateMap
							.get(currentStateValue); // returns other or idle
				} else {
					currentStatus = N_A;
				}

			} else if (operationalStateOid != null
					&& vb.getOid().toString().equals(operationalStateOid)) {
				String operationalStateValue = vb.getVariable().toString()
						.trim(); // has the value 2 or 3 or x
				if (printerOperationalStateMap
						.containsKey(operationalStateValue)) { // check in the
																// predefined
																// map whether
																// the map has
																// the value
					operationalStatus = printerOperationalStateMap
							.get(operationalStateValue); // returns running or
															// warning
				} else {
					operationalStatus = N_A;
				}
			}
		}
		HashMap<String, String> printerStatus = new HashMap<String, String>(); // return
																				// the
																				// status
																				// of
																				// the
																				// printer
																				// with
																				// currentstate
																				// and
																				// operationalState
		printerStatus.put(currentStateKey, currentStatus); // current
		printerStatus.put(operationalStateKey, operationalStatus); // operational
		return printerStatus;
	}
}