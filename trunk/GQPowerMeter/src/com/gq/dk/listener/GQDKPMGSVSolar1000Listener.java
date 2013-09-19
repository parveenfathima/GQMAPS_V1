package com.gq.dk.listener;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.InterruptedIOException;
import java.io.OutputStreamWriter;

import java.net.ServerSocket;
import java.net.Socket;
import java.util.UUID;

import com.gq.dk.exception.GQDKPMException;
import com.gq.dk.model.GQDKPMDBManager;
import com.gq.dk.model.SystemProfile;
import com.gq.dk.parser.GQDKPMParser;
import com.gq.dk.parser.GQDKPMParserFactory;
import com.gq.dk.parser.GQDKPMSolar1000Parser;
import com.gq.dk.util.GQDKPMConstants;
import com.gq.dk.util.SMSDispatcher;


public final class GQDKPMGSVSolar1000Listener extends GQDKPMGSVListener{

	private static ServerSocket providerSocket;

	// thread local variables
//	private static ThreadLocal<Long> clientInteractionStartDttmLong = new ThreadLocal<Long> (); 
//	private static ThreadLocal<Integer> clientNumberOfRetries = new ThreadLocal<Integer> (); 
//	private static ThreadLocal<StringBuilder> clientDeviceDataRecord = new ThreadLocal<StringBuilder> (); 

	@Override
	public void initializeListener(int portNumber) {
		
		// creating main listener server socket
		
		try {
			GQDKPMConstants.logger.info("************ Server started at port "+ portNumber + ".... ************" );
			providerSocket = new ServerSocket(portNumber);
			
		} catch (IOException e) {
			e.printStackTrace();
			GQDKPMConstants.logger.fatal("************ Unable to start server at port "+ portNumber + 
					" to listen for incoming connections. TERMINATING server .... ************" );
			System.exit(1);
		}
		
	}


	@Override
	final void startDeviceHandler() {
		
		try {
			// infinite method to keep server alive for all incoming connections
			while (true) {
				Socket socketConnection = providerSocket.accept();
				GQDKPMConstants.logger.debug("Connection received from "
						+ socketConnection.getInetAddress() );

				// spawn a socket listener for the new device request
				Thread t = new Thread(new GQDKPMClientDeviceHandler(socketConnection));
				t.start();
			}
		} 
		catch (IOException e) {
			e.printStackTrace();
			GQDKPMConstants.logger.fatal("************ Unable to start new thread connection to receive client requests .. TERMINATING server .... ************" );
			// todo - take corrective action... this must trigger some kind of a notify , graceful winding up of thread already running happens
			System.exit(1);
		}

		GQDKPMConstants.logger.info("************ Server listener terminated ***********" );		

	} // start method ends

		
	// write an inner class to handle client requests
	class GQDKPMClientDeviceHandler implements Runnable {

		BufferedWriter bswriter;
		BufferedReader bsreader;
		Socket dkpmClientDeviceSocket;
		InputStreamReader isr;    
		
		public GQDKPMClientDeviceHandler(Socket clientSocket) {

			dkpmClientDeviceSocket = clientSocket;
			
			try {
				bswriter = new BufferedWriter(new OutputStreamWriter(dkpmClientDeviceSocket.getOutputStream()));
				isr = new InputStreamReader( dkpmClientDeviceSocket.getInputStream() /*, SOCKET_DATA_CHARSET */);				
			} 
			catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		
		@Override
		public void run() {

			int badDataPacketRetries = 0;
			char [] dataFromDeviceAsCharBuffer = new char[SOCKET_DATA_LENGTH];
			int deviceDataLength;
			StringBuilder deviceData = new StringBuilder();
			boolean firstIncompleteCommunicationForThisTx = true;
			String uuid = UUID.randomUUID().toString().replace("-","").substring(22) ; // all log statements within this run will have this for tracking purposes.
			final String commId = "<comm-id:"+uuid+">  ";
			
			try {
				// 2. Wait for connection
				
				// this loop and terminated when a SOxxL pattern is encountered or timeout is reached or retry count is reached..				
				for(deviceDataLength = isr.read(dataFromDeviceAsCharBuffer, 0, dataFromDeviceAsCharBuffer.length);
						deviceDataLength != -1;
						deviceDataLength = isr.read(dataFromDeviceAsCharBuffer, 0, dataFromDeviceAsCharBuffer.length)) {

					String  dataInCurrentTx = new String(dataFromDeviceAsCharBuffer,0,deviceDataLength);
					GQDKPMConstants.logger.debug(commId + "data from device in this tx : " + dataInCurrentTx + " , length = " + deviceDataLength );		

					// append the data to the string builder we are keeping for device data. 
					deviceData.append(dataInCurrentTx);
						
					// the handshake works as follows....
					// 1. a data packet arrives with an ending SNxxL or SOxxL sequence. 
					// 2. a data packet arrives which doesnt have either and hence is incomplete. we will keep a timer to monitor this..
					
					if ( ( ! deviceData.toString().matches(SNXXL_PATTERN) ) && ( ! deviceData.toString().matches(SOXXL_PATTERN) ) ) { 
						// neither one found , meaning this is an incomplete packet
						GQDKPMConstants.logger.warn(commId + ".. got incomplete packet ..sending no response ; if this is the first time , will start timer");		
						if ( firstIncompleteCommunicationForThisTx ) {
							// we will start a timer thread and in that timer's run method , will close the socket.
							// it will make the thread.run to kill itself immediately if the packet is not fully received..
							// todo - to be investigated
							new Thread(new GQDKPMClientDeviceTimeoutHandler(dkpmClientDeviceSocket,commId)).start();
							
							GQDKPMConstants.logger.debug(commId + ".. started time keeper thread with "+ GQDKPMConstants.GQ_DK_PM_INCOMPLETE_PACKET_TIMEOUT_SECONDS +" seconds");	
						}
						
						firstIncompleteCommunicationForThisTx = false;
						
						continue;
					}
					
					// we are here because we got an SNxxL or SOxxL
					GQDKPMParser deviceDataParser =  GQDKPMParserFactory.getParser(deviceData.toString());
					
					try {
						
						deviceDataParser.process();
						
					} 
					catch (GQDKPMException e) {						
						// read the code and message. it could be one of record mismatch , rec length error , or checksum error
						GQDKPMConstants.logger.warn(commId + "error while parsing data : "+ e.getMessage()); 
					    
						// if the gqdkexception is not one of the 3 we expect , like db exception et al , we should
						// handle it differently. - todo
						if ( e.getMessage().equals(GQDKPMConstants.GQ_DK_PM_EXCEPTION_REC_LENGTH_MISMATCH) ||
								e.getMessage().equals(GQDKPMConstants.GQ_DK_PM_EXCEPTION_CHECKSUM_ERROR) ||
								e.getMessage().equals(GQDKPMConstants.GQ_DK_PM_EXCEPTION_NUM_RECS_MISMATCH) ) {
						
							// exception is one of the 3 known causes ; we are handling it with a retry
							badDataPacketRetries++; // if this count is >= GQ_DK_PM_NUM_RETRIES ,its a failure
						    
							if ( badDataPacketRetries >= GQDKPMConstants.GQ_DK_PM_NUM_RETRIES ) { 
								//the communication is over and we still dont have a valid data packet ; next communication is only at the next interval.
								GQDKPMConstants.logger.warn(commId + ".. still after " + GQDKPMConstants.GQ_DK_PM_NUM_RETRIES + " retries , no good packet , closing connection : ");	
								
								// here and in other places where we are going to just close the connection and not send a response , we 
								// need to put that record in the other table where we say no data received. - todo
								break; 
							}
							else {
								// send sretl for retrying.....
								GQDKPMConstants.logger.debug(commId + "..sending " + RESPONSE_SRETL +" on retry "+ badDataPacketRetries);		

								bswriter.write(RESPONSE_SRETL);
							    bswriter.flush();
							    
								deviceData.setLength(0);  // parsed and stored , it should be new for next iteration
							    continue;  // lets wait for the redo packet
							}
						}
						else {
							// exception source not known ; could be db related or else....
							// alert the field staff or gq admin , so that the problem is addressed asap...
							// throw a message to the gq contact so he can check
							
							long phNum = 0;
							try {
								phNum = Long.parseLong(GQDKPMConstants.getProfileEntry("GQ_SUPPORT_NUM")) ;
							}
							catch (Exception e1 ) {
								e1.printStackTrace();
								phNum = 7598223055L; // if none found , receiver must know this -todo- need a more generic gq support number
							}
							
							int unitId = 0;
							try {
								unitId = ( (GQDKPMSolar1000Parser)deviceDataParser ).getS1000Record().getUnitId();
							}
							catch (Exception e1 ) {
								e1.printStackTrace();
								unitId = 0; // if none found , receiver must know this
							}
							 
							SMSDispatcher.sendSystemAlert_Template2( phNum, unitId );
							
							deviceData.setLength(0);  // parsed and stored , it should be new for next iteration

							break; // lets break and close socket for now...
						}
						
					} // try catch ends
					
					if ( deviceData.toString().matches(SNXXL_PATTERN) ) {  				// SNXXL_PATTERN = "(.*)SN[0-9][0-9]L";  
						// got one or more packets and also more data in device
						GQDKPMConstants.logger.debug(commId + ".. got SN ..sending "+ RESPONSE_SRL);	
						// dont send any response yet as we still dont know how parsing will do ; lets do it after the parsing logic is done
						bswriter.write(RESPONSE_SRL);
					    bswriter.flush();
					    
						deviceData.setLength(0);  // parsed and stored , it should be new for next iteration

					    continue;  // redundant here but still kept for clarity
					}
					else if ( deviceData.toString().matches(SOXXL_PATTERN) ) { 			// SOXXL_PATTERN = "(.*)SO[0-9][0-9]L" ending SOxxL
						// got one or more packets and no more data in device
						//the communication is over and we got a valid data packet ; next communication is only at the next interval.
						GQDKPMConstants.logger.debug(commId + ".. got SOXXL_PATTERN  ; response " + RESPONSE_SEL +" sent ; closing connection ");		
						bswriter.write(RESPONSE_SEL);
					    bswriter.flush();
						break;
					}

				} // for isr.read ends
			} 
			catch(InterruptedIOException ie) {
				GQDKPMConstants.logger.warn(commId + ".. got InterruptedIOException ");	
				Thread.currentThread().interrupt();
			}
			catch (IOException ioException) {
				GQDKPMConstants.logger.warn(commId + ".. got IOException , socket might have been closed on a timeout");				
				// close the socket and associated streams by going to finally. that means dont do nothing here...
			}
			catch (Exception exception) {
				exception.printStackTrace();
				GQDKPMConstants.logger.warn(commId + ".. got Exception ; closing connection ");	
				// close the socket and associated streams by going to finally. that means dont do nothing here...
			}
			finally {
				// 4: Closing connection
				try {
					bswriter.close();
					isr.close();
					dkpmClientDeviceSocket.close();
					
					GQDKPMConstants.logger.info(commId + "Closing current device session ");
				} 
				catch (IOException e) {
					GQDKPMConstants.logger.info(commId + "will be here if socket timedout ; Closing current device session ");

					e.printStackTrace();
				}
			}
			
		}  // run ends
		
		
		// write an inner class to handle client request timeouts - inner-inner
		class GQDKPMClientDeviceTimeoutHandler implements Runnable {

			String commId;
			Socket timeoutSocket;
			
			public GQDKPMClientDeviceTimeoutHandler(Socket s , String cid) {
				commId = cid;
				timeoutSocket = s;
			}
			
			@Override
			public void run() {
				try {
					// no guarantee however but fine in our scenario  
					Thread.sleep(GQDKPMConstants.GQ_DK_PM_INCOMPLETE_PACKET_TIMEOUT_SECONDS * 1000);
					GQDKPMConstants.logger.info( commId + "..interrupting spawning thread ");		
					timeoutSocket.close();
				} 
				catch (InterruptedException e) {
					GQDKPMConstants.logger.info( commId + "inner thread InterruptedException..interrupting spawning thread ");
				} 
				catch (IOException e) {
					GQDKPMConstants.logger.info( commId + "inner thread IOException..interrupting spawning thread ");
				} 
			}
			
		} // class GQDKPMClientDeviceTimeoutHandler ends

	} // class GQDKPMGSVClientDeviceHandler ends

	
} // main class GQDKPMListener ends

