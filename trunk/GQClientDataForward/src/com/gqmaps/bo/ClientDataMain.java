package com.gqmaps.bo;

import com.gqmaps.db.ClientDataTrackerDBInterface;

public class ClientDataMain {

    public static void main(String[] args) {
        ClientDataTrackerDBInterface clientDataTrackerDBInterface = new ClientDataTrackerDBInterface();
        clientDataTrackerDBInterface.getDeviceDetails();

    }

}
