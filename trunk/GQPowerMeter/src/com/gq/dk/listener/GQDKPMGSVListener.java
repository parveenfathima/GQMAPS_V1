package com.gq.dk.listener;

public abstract class GQDKPMGSVListener implements GQDKPMListener {
 
	// handshake and parser specifics
	protected static final String SNXXL_PATTERN = "(.*)SN[0-F][0-F]L"; // at the end i didnt put (.*) because extra stuff will not come without us sending a SRL first
	protected static final String SOXXL_PATTERN = "(.*)SO[0-F][0-F]L";

	protected static final String RESPONSE_SRL = "SRL";
	protected static final String RESPONSE_SEL = "SEL";
	protected static final String RESPONSE_SRETL = "SRETL";
	
	protected static final int SOCKET_DATA_LENGTH = 2000;  // since max is only 15 records and each 82 bytes long
	//private static final String SOCKET_DATA_CHARSET = "UTF-8";


	// thread local variables
//	private static ThreadLocal<Long> clientInteractionStartDttmLong = new ThreadLocal<Long> (); 
//	private static ThreadLocal<Integer> clientNumberOfRetries = new ThreadLocal<Integer> (); 
//	private static ThreadLocal<StringBuilder> clientDeviceDataRecord = new ThreadLocal<StringBuilder> (); 
	
	abstract void initializeListener(int portNumber) ;
	
	// starts and runs server for time indefinite
	abstract void startDeviceHandler() ;
	
	public final void run(int portNumber) {
		initializeListener(portNumber) ;
		startDeviceHandler() ;
	}
	
} // main class GQDKPMListener ends

