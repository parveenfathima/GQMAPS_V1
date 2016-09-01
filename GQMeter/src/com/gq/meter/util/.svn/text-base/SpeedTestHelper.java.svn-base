package com.gq.meter.util;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.snmp4j.smi.OID;
import org.snmp4j.smi.VariableBinding;

public class SpeedTestHelper {
	public HashMap<String, String> linuxAssetIdCalc(List<VariableBinding> result, OID rootOid, String[] ethernet,
            HashMap<String, List<Long>> networkMap, String assetId, String sysDescription) {
        String macOid = null;
        String rootId = rootOid.toString();
        HashMap<String, HashMap<String, String>> macOidMap = new HashMap<String, HashMap<String, String>>();
        HashMap<String, String> networkValues = new HashMap<String, String>();

        for (int i = 0; i < ethernet.length; i++) { // 1st for loop starts
            for (VariableBinding vb : result) { // 2nd for loop starts
                if (vb.getVariable().toString().trim().equalsIgnoreCase(ethernet[i])) { // 1st if loop starts
                    String lastchar = String.valueOf(vb.getOid().last());

                    macOid = rootId + "." + "6" + "." + lastchar;
                    for (VariableBinding vbs : result) { // 3rd for loop starts

                        if (vbs.getOid().toString().trim().equals(macOid)) { // 2nd if loop starts
                            if (macOidMap.get(ethernet[i]) == null || macOidMap.get(ethernet[i]).size() == 0
                                    || macOidMap.get(ethernet[i]).isEmpty()) {
                                HashMap<String, String> macMap = new HashMap<String, String>();
                                macMap.put(vbs.getOid().toString(), vbs.getVariable().toString());
                                macOidMap.put(ethernet[i], macMap);
                            }
                            else {
                                macOidMap.get(ethernet[i]).put(vbs.getOid().toString(), vbs.getVariable().toString());
                            }
                        } // 2nd if loop ends
                    } // 3rd for loop ends
                } // 1st if loop ends
            } // 2nd for loop ends
        } // 1st for loop ends

        Set<String> uniqueValues = new HashSet<String>(macOidMap.get("eth0").values());
        if (macOidMap.get("eth0") != null && macOidMap.get("eth0").size() != 0) {
            String eth0MacAddress = uniqueValues.toString().substring(1, uniqueValues.toString().length() - 1).trim()
                    .replaceAll(":", "");
            assetId = eth0MacAddress;
            networkValues.put("assetId", assetId);
        }
        return networkValues;
    }

    /**
     * This method is used to Windows Asset ID
     * 
     * @param result
     * @param rootOid
     * @param winNetworkMap
     * @return
     */
    public HashMap<String, String> winAssetIdCalc(List<VariableBinding> result, OID rootOid,
            HashMap<String, String> winNetworkMap) {

        String networkOid = null;
        String macWinNetworkId = null;
        String rootId = rootOid.toString();

        for (VariableBinding vb : result) { // 1st for loop starts
            String lastchar = String.valueOf(vb.getOid().last());
            networkOid = rootId + "." + "10" + "." + lastchar;

            if (networkOid != null && vb.getOid().toString().contains(networkOid)) { // 1st if loop starts
                String winNetworkInVal = vb.getVariable().toString().trim();

                if (!winNetworkInVal.equalsIgnoreCase("0")) { // 2nd if loop starts
                    String winNetworkInValue = vb.getVariable().toString().trim();
                    winNetworkMap.put("InBytes", winNetworkInValue);

                    macWinNetworkId = rootId + "." + "6" + "." + lastchar;
                    for (VariableBinding vbs : result) { // 2nd for loop starts
                        if (macWinNetworkId != null && vbs.getOid().toString().contains(macWinNetworkId)) {
                            String macWinNetworkValue = vbs.getVariable().toString().trim().replaceAll(":", "");
                            winNetworkMap.put("macWinNetworkValue", macWinNetworkValue);
                        }
                    }// 2nd for loop ends
                } // 2nd if loop ends
            } // 1st if loop ends
        }// 1st for loop ends
        return winNetworkMap;
    }

}
