package com.gq.meter;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

import org.snmp4j.CommunityTarget;
import org.snmp4j.Snmp;
import org.snmp4j.smi.OID;
import org.snmp4j.smi.VariableBinding;
import org.snmp4j.transport.DefaultUdpTransportMapping;

import com.gq.meter.object.Asset;
import com.gq.meter.object.CPNId;
import com.gq.meter.object.Storage;
import com.gq.meter.object.StorageSnpsht;
import com.gq.meter.util.MeterConstants;
import com.gq.meter.util.MeterUtils;

/**
 * @author yogalakshmi.s
 * 
 */
public class StorageMeter implements GQSNMPMeter {

    List<String> errorList = new LinkedList<String>();

    @Override
    public GQMeterData implement(String communityString, String ipAddress, String snmpVersion,
            LinkedList<String> toggleSwitches) {

        long storageStartTime = System.currentTimeMillis();

        CPNId id = null;
        int runId = 0;

        // variables that are used to get the NSRG snapshot
        String assetId = null;
        long upTime = 0; // seconds
        long totalDiskSpace = 0; // bytes
        long usedDiskSpace = 0; // bytes
        short numberOfController = 0;
        short numberOfDisks = 0;
        short numberOfSAS = 0;
        long IOPS = 0;
        String ConnectorProtocol = null;
        String extras = null; // anything device specific but to be discussed v2

        Snmp snmp = null;
        CommunityTarget target = null;
        Asset assetObj = null;

        try {
            snmp = new Snmp(new DefaultUdpTransportMapping());
            snmp.listen();
            target = MeterUtils.makeTarget(ipAddress, communityString, snmpVersion);
        }
        catch (IOException e) {
            e.printStackTrace();
            return null;
        }

        try {
            // the below line is used to get the system basic info.
            assetObj = MeterUtils.sysBasicInfo(communityString, ipAddress, snmpVersion, toggleSwitches);
            assetObj.setProtocolId(MeterConstants.STORAGE_PROTOCOL);

            String oidString = null;
            String temp;
            String tempStr;

            OID rootOID = null;
            List<VariableBinding> result = null;

            // The following oid's is used to get the asset ID.
            oidString = "1.3.6.1.2.1.1";
            rootOID = new OID(oidString);
            result = MeterUtils.walk(rootOID, target);

            if (result != null && !result.isEmpty()) {
                temp = oidString + ".5.0";
                String assetIdVal = MeterUtils.getSNMPValue(temp, result);
                assetId = assetIdVal;
                assetObj.setAssetId(assetId);

            }
            else {
                errorList.add(assetId + " Root OID : 1.3.6.1.2.1.1.5.0" + " " + MeterConstants.ASSET_ID_ERROR);
            }

            // ASSET ID , RUN ID STARTS HERE.
            id = new CPNId(runId, assetId);

            oidString = "1.3.6.1.2.1.1";
            rootOID = new OID(oidString);

            result = MeterUtils.walk(rootOID, target);
            if (result != null && !result.isEmpty()) {
                temp = oidString + ".3.0";
                tempStr = MeterUtils.getSNMPValue(temp, result);
                upTime = MeterUtils.upTimeCalc(tempStr);
            }

            oidString = "1.3.6.1.4.1.";
            rootOID = new OID(oidString);
            result = MeterUtils.walk(rootOID, target);
            totalDiskSpace = totalHardDiskCalc(result, rootOID);
            usedDiskSpace = usedHardDiskCalc(result, rootOID);
            numberOfController = controllerCalc(result, rootOID);
            numberOfDisks = individualHardDiskCalc(result, rootOID);

        }
        catch (Exception e) {
            errorList.add(ipAddress + " " + e.getMessage());
        }

        StorageSnpsht storageSnapshot = new StorageSnpsht(id, upTime, totalDiskSpace, usedDiskSpace,
                numberOfController, numberOfDisks, IOPS, ConnectorProtocol, extras);

        Storage storageObj = new Storage(id, assetObj, storageSnapshot);

        GQErrorInformation gqErrorInfo = null;
        if (errorList != null && !errorList.isEmpty()) {
            gqErrorInfo = new GQErrorInformation(assetObj.getDescr(), errorList);
        }
        GQMeterData gqMeterObject = new GQMeterData(gqErrorInfo, storageObj);
        long storageEndTime = System.currentTimeMillis();
        MeterUtils.storageMeterTime = MeterUtils.storageMeterTime + (storageEndTime - storageStartTime);
        System.out.println(" [GQMETER] Time taken by the storage meter is : " + (storageEndTime - storageStartTime));
        return gqMeterObject;
    }

    private long totalHardDiskCalc(List<VariableBinding> result, OID rootOID) {
        String oidString = "1.3.6.1.4.1.";
        String checkOid = ".4.1.1.5.0";
        long totalHardDiskSpace = 0;
        for (VariableBinding vb : result) { // for loop starts
            String actualOid = vb.getOid().toString();
            actualOid = actualOid.replace(oidString, "");
            if (actualOid.contains(checkOid)) { // 1st if loop starts
                actualOid = actualOid.replace(checkOid, "");
                if (!actualOid.contains(".")) {
                    String expectedOid = vb.getVariable().toString().trim();
                    totalHardDiskSpace = Long.parseLong(expectedOid);
                }
            } // 1st if loop ends
        } // for loop ends

        return totalHardDiskSpace;
    }

    private long usedHardDiskCalc(List<VariableBinding> result, OID rootOID) {
        String oidString = "1.3.6.1.4.1.";
        String checkOid = ".4.1.1.4.0";
        long UsedHardDiskSpace = 0;
        for (VariableBinding vb : result) { // for loop starts
            String actualOid = vb.getOid().toString();
            actualOid = actualOid.replace(oidString, "");
            if (actualOid.contains(checkOid)) { // 1st if loop starts
                actualOid = actualOid.replace(checkOid, "");
                if (!actualOid.contains(".")) {
                    String expectedOid = vb.getVariable().toString().trim();
                    UsedHardDiskSpace = Long.parseLong(expectedOid);
                }
            } // 1st if loop ends
        } // for loop ends

        return UsedHardDiskSpace;
    }

    private short controllerCalc(List<VariableBinding> result, OID rootOID) {
        String oidString = "1.3.6.1.4.1.";
        String checkOid = ".4.1.1.3.0";
        short numOfController = 0;
        for (VariableBinding vb : result) { // for loop starts
            String actualOid = vb.getOid().toString();
            actualOid = actualOid.replace(oidString, "");
            if (actualOid.contains(checkOid)) { // 1st if loop starts
                actualOid = actualOid.replace(checkOid, "");
                if (!actualOid.contains(".")) {
                    String expectedOid = vb.getVariable().toString().trim();
                    numOfController = Short.parseShort(expectedOid);
                }
            }// 1st if loop ends
        } // for loop ends
        return numOfController;
    }

    private short individualHardDiskCalc(List<VariableBinding> result, OID rootOID) {
        String oidString = "1.3.6.1.4.1.";
        String checkOid = ".4.1.1.2.0";
        short numOfDisk = 0;
        for (VariableBinding vb : result) { // for loop starts
            String actualOid = vb.getOid().toString();
            actualOid = actualOid.replace(oidString, "");
            if (actualOid.contains(checkOid)) { // 1st if loop starts
                actualOid = actualOid.replace(checkOid, "");
                if (!actualOid.contains(".")) {
                    String expectedOid = vb.getVariable().toString().trim();
                    numOfDisk = Short.parseShort(expectedOid);
                }
            } // 1st if loop ends
        } // for loop ends
        return numOfDisk;
    }
}
