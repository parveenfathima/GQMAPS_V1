package com.gq.dk.main;

import com.gq.dk.exception.GQDKPMException;
import com.gq.dk.listener.GQDKPMListener;
import com.gq.dk.listener.GQDKPMListenerFactory;
import com.gq.dk.model.Enterprise;
import com.gq.dk.model.GQDKPMDBManager;
import com.gq.dk.model.SystemProfile;
import com.gq.dk.util.GQDKPMConstants;

public final class GQDKCommand {

	public static void main(String[] args) {

		String msg = null;
		
		// argument 1 is the enterprise id. if none specified , throw an error
		if ( args.length != 1) {
			msg = "************ Usage GQDKCommand ENTERPRISE_ID. "+ "TERMINATING server .... ************" ;
			System.out.println(msg);
			System.exit(1);
		}
		
		int enterpriseId = -1;
		try {
			enterpriseId = Integer.parseInt(args[0]);
		}
		catch ( NumberFormatException ne ) {
			msg = "************ Usage GQDKCommand ENTERPRISE_ID (number). "+ "TERMINATING server .... ************" ;
			System.out.println(msg);
			System.exit(1);
		}
		
		// make sure that enterprise exists in the database and enterprise parameters can be obtained from it.
		Enterprise eprise = null;
		
		try{
			eprise = GQDKPMDBManager.getEnterprise(enterpriseId);
			GQDKPMConstants.setEnterprise( eprise );
		}
		catch(GQDKPMException e) {
			System.out.println(e.getMessage());
			System.exit(1);
		}
		catch (Exception e){
			e.printStackTrace();
			msg = "No record available for enterprise with id " + enterpriseId + ". TERMINATING server .... ************";
			System.out.println(msg);
			System.exit(1);
		}
		
		// get a logger first
		GQDKPMConstants.createLogger(enterpriseId);
		
		try {
			SystemProfile sp = GQDKPMDBManager.getProfileValue("GQ_SUPPORT_NUM");
			GQDKPMConstants.addToProfileMap(sp.getKeycol(), sp.getValue());
		}
		catch (Exception e1 ) {
			e1.printStackTrace();
		}

		// the string doesnt matter here a solar 1000 listener is always going to be given irrespective. as of jul 31 , 2012
		GQDKPMListener listener = GQDKPMListenerFactory.getListener( "SOLAR_1000") ;  
		listener.run(eprise.getPort());
		
	}

}
