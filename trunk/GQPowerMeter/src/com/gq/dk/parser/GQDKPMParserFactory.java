package com.gq.dk.parser;

import com.gq.dk.util.GQDKPMConstants;


public class GQDKPMParserFactory {


	private static final int determineParser( String s ) {
		// do the necessary check to find what the protocol is 
		
		// for eg , in our case , if it starts with an S and ends with an L , it is a Solar1000
		// todo - we are giving the full device string and trying to deduce the protocol , 
		// is all of that required or not is still an open question due to lack of availability of device protocols.
		return GQDKPMConstants.GQ_DK_PM_PROTOCOL_SOLAR_1000;
	}
	
    public static final GQDKPMParser getParser(String s) {
        
        switch ( determineParser(s) ) {
            case GQDKPMConstants.GQ_DK_PM_PROTOCOL_SOLAR_1000:
                return new GQDKPMSolar1000Parser(s);
            case GQDKPMConstants.GQ_DK_PM_PROTOCOL_SOLAR_1000_NEW:
                return new GQDKPMSolar1000Parser(s); //todo
            default:
            	return new GQDKPMSolar1000Parser(s); //todo
        }
    }

}


