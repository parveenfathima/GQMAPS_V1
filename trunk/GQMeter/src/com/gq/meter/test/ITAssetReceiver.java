/**
 * 
 */
package com.gq.meter.test;

import java.util.List;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.gq.meter.GQMeterData;
import com.gq.meter.GQMeterResponse;
import com.gq.meter.assist.ProtocolData;
import com.gq.meter.object.Computer;
import com.gq.meter.object.NSRG;
import com.gq.meter.object.Printer;

/**
 * @author chandru.p
 * 
 */
public class ITAssetReceiver {

    public static void readJson(String jsonObject) {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        GQMeterData gqmData = null;
        GQMeterResponse gqmresponse = gson.fromJson(jsonObject, GQMeterResponse.class);
        System.out.println(" ---- " + gqmresponse.getGqmid());

        List<ProtocolData> pdList = gqmresponse.getAssetInformationList();

        for (ProtocolData pdData : pdList) {
            gqmData = gson.fromJson(pdData.getData(), GQMeterData.class);
            String meterData = gson.toJson(gqmData.getMeterData());

            switch (pdData.getProtocol()) {

            case COMPUTER:
                Computer computerObj = gson.fromJson(meterData, Computer.class);
                System.out.println(computerObj.getId().getAssetId());
                break;

            case PRINTER:
                Printer printerData = gson.fromJson(meterData, Printer.class);
                System.out.println(printerData.getId().getAssetId());
                break;

            case NSRG:
                NSRG isrData = gson.fromJson(meterData, NSRG.class);
                System.out.println(isrData.getId().getAssetId());
                break;
            }

        }
    }

}
