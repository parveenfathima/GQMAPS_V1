package com.gq.bo;

import java.util.List;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.gq.meter.GQMeterResponse;
import com.gq.meter.assist.ProtocolData;
import com.gq.meter.object.Computer;
import com.gq.meter.object.NSRG;
import com.gq.meter.object.Printer;

// this class is takes care of validating and distributing incoming requests

public class GateKeeperFilter {

    public void process(GQMeterResponse gqmResponse) {

        // parse the object and get the protocols and save them for now..... ss , feb 19 , 2013
        System.out.println("ready to parse and save....");
        Gson gson = new GsonBuilder().setPrettyPrinting().create();

        System.out.println(" MeterID : " + gqmResponse.getGqmid());
        System.out.println(" Total Asset Scanned : " + gqmResponse.getAssetScanned());
        System.out.println(" Meter Version : " + gqmResponse.getVersion());

        List<ProtocolData> pdList = gqmResponse.getAssetInformationList();

        for (ProtocolData pdData : pdList) {

            switch (pdData.getProtocol()) {

            case COMPUTER:
                Computer computerObj = gson.fromJson(pdData.getData(), Computer.class);
                System.out.println(computerObj.getId().getAssetId());
                GqMeterComputer.insertData(computerObj, gqmResponse);
                break;

            case PRINTER:
                Printer printerData = gson.fromJson(pdData.getData(), Printer.class);
                System.out.println(printerData.getId().getAssetId());
                GqMeterPrinter.insertData(printerData, gqmResponse);
                break;

            case NSRG:
                NSRG isrData = gson.fromJson(pdData.getData(), NSRG.class);
                System.out.println(isrData.getId().getAssetId());
                GqMeterNSRG.insertData(isrData, gqmResponse);
                break;

            case AIR:
                break;

            case POWER:
                break;

            case UNKNOWN:
                break;

            case WATER:
                break;

            default:
                break;
            }

        }

    }

}
