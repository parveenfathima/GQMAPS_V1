package com.gq.dk.listener;

import com.gq.dk.util.GQDKPMConstants;


public class GQDKPMListenerFactory {

	private static final int determineListener( String s ) {
		// do the necessary check to find what the listener is 
		// todo , lets just return something to identify a solar 1000 listener...
		
		return GQDKPMConstants.GQ_DK_PM_PROTOCOL_SOLAR_1000;
	}
	
    public static final GQDKPMListener getListener(String s) {
        
        switch ( determineListener(s) ) {
            case GQDKPMConstants.GQ_DK_PM_PROTOCOL_SOLAR_1000:
                return new GQDKPMGSVSolar1000Listener();
            case GQDKPMConstants.GQ_DK_PM_PROTOCOL_SOLAR_1000_NEW:
                return new GQDKPMGSVSolar1000Listener(); //todo here to prove the point...
            default:
            	return new GQDKPMGSVSolar1000Listener(); //todo
        }
    }

}


