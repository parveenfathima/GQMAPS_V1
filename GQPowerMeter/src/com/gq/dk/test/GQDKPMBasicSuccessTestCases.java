package com.gq.dk.test;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.net.UnknownHostException;

/* Testcases for Good packet receiving with pattern of SO01L */ 
public class GQDKPMBasicSuccessTestCases {

	Socket requestSocket;
	String servMsg;
	BufferedWriter bswriter;
	BufferedReader bsreader;
	InputStreamReader isr;    
	Thread thread=new Thread();
	
	//***************************************
	// Case - oneGoodFullPacketWithSO() 
	// send - pkt with so
	// recv - SEL
	//***************************************
	void oneGoodFullPacketWithSO() {
		try {
			// 1. creating a socket to connect to the server
			 requestSocket = new Socket("182.72.206.38", 1100);
			//requestSocket = new Socket("192.168.1.95", 1100);

			System.out.println("Connected to localhost in port 1100");
			// 2. get Input and Output reders
			bswriter = new BufferedWriter(new OutputStreamWriter(requestSocket.getOutputStream()));
			isr = new InputStreamReader( requestSocket.getInputStream() /*, SOCKET_DATA_CHARSET */);
			//bsreader = new BufferedReader(new InputStreamReader( dkpmClientDeviceSocket.getInputStream()));

			char [] dataFromDeviceAsCharBuffer = new char[2000];
			int deviceDataLength;
			
			// 3: Communicating with the server
			
			do {
					//one Good full packet record with SO pattern
					sendMessage("S000A80CBA6590C000A00FF010000000000000000000000000E000000E80302002001B80B0000F5AALSO01L"); //80B00003B1BLSO01L");
					deviceDataLength = isr.read(dataFromDeviceAsCharBuffer, 0, dataFromDeviceAsCharBuffer.length);
					servMsg=new String(dataFromDeviceAsCharBuffer,0,deviceDataLength);
					System.out.println("message From SERVER====>" + servMsg);
			} while (!servMsg.equals("SEL"));
		} 
		catch (UnknownHostException unknownHost) {
			System.err.println("You are trying to connect to an unknown host!");
		} 
		catch (IOException ioException) {
			ioException.printStackTrace();
		} 
		finally {
			// 4: Closing connection
			try {
				requestSocket.close();
			} catch (IOException ioException) {
				ioException.printStackTrace();
			}
		}
	}
	
	//***************************************
	// Case - oneBadFullPacketWithSO_MismatchRecords() 
	// send - pkt with so and number of records Mismatch
	// recv - SRETL
	//***************************************
	void oneBadFullPacketWithSO_MismatchRecords() {
		try {
			// 1. creating a socket to connect to the server
			 //requestSocket = new Socket("59.90.1.246", 1025);
			requestSocket = new Socket("192.168.1.95", 1100);

			System.out.println("Connected to localhost in port 1100");
			// 2. get Input and Output reders
			bswriter = new BufferedWriter(new OutputStreamWriter(requestSocket.getOutputStream()));
			isr = new InputStreamReader( requestSocket.getInputStream() /*, SOCKET_DATA_CHARSET */);
			//bsreader = new BufferedReader(new InputStreamReader( dkpmClientDeviceSocket.getInputStream()));

			char [] dataFromDeviceAsCharBuffer = new char[2000];
			int deviceDataLength;
			
			// 3: Communicating with the server
			
			do {
					//one bad full packet record with SO pattern
					sendMessage("S000A80CBA6590C000A00FF010000000000000000000000000E000000E80302002001B80B0000F5AALSO04L"); //80B00003B1BLSO01L");
					deviceDataLength = isr.read(dataFromDeviceAsCharBuffer, 0, dataFromDeviceAsCharBuffer.length);
					servMsg=new String(dataFromDeviceAsCharBuffer,0,deviceDataLength);
					System.out.println("message From SERVER====>" + servMsg);
			} while (!servMsg.equals("SRETL"));
		} 
		catch (UnknownHostException unknownHost) {
			System.err.println("You are trying to connect to an unknown host!");
		} 
		catch (IOException ioException) {
			ioException.printStackTrace();
		} 
		finally {
			// 4: Closing connection
			try {
				requestSocket.close();
			} catch (IOException ioException) {
				ioException.printStackTrace();
			}
		}
	}
	
	//***************************************
	// Case - oneBadFullPacketWithSO_checksumError() 
	// send - pkt with so with checksum Error
	// recv - SRETL
	//***************************************
	void oneBadFullPacketWithSO_checksumError() {
		try {
			// 1. creating a socket to connect to the server
			 //requestSocket = new Socket("59.90.1.246", 1025);
			requestSocket = new Socket("192.168.1.95", 1100);

			System.out.println("Connected to localhost in port 1100");
			// 2. get Input and Output reders
			bswriter = new BufferedWriter(new OutputStreamWriter(requestSocket.getOutputStream()));
			isr = new InputStreamReader( requestSocket.getInputStream() /*, SOCKET_DATA_CHARSET */);
			//bsreader = new BufferedReader(new InputStreamReader( dkpmClientDeviceSocket.getInputStream()));

			char [] dataFromDeviceAsCharBuffer = new char[2000];
			int deviceDataLength;
			
			// 3: Communicating with the server
			
			do {
					//one bad full packet record with SO pattern and checksum error
					sendMessage("S000A80CBA6590C000A00FF010000000000000000000000000E000000E80302002001B80B0000F5A1LSO01L"); 
					deviceDataLength = isr.read(dataFromDeviceAsCharBuffer, 0, dataFromDeviceAsCharBuffer.length);
					servMsg=new String(dataFromDeviceAsCharBuffer,0,deviceDataLength);
					System.out.println("message From SERVER====>" + servMsg);
			} while (!servMsg.equals("SRETL"));
		} 
		catch (UnknownHostException unknownHost) {
			System.err.println("You are trying to connect to an unknown host!");
		} 
		catch (IOException ioException) {
			ioException.printStackTrace();
		} 
		finally {
			// 4: Closing connection
			try {
				requestSocket.close();
			} catch (IOException ioException) {
				ioException.printStackTrace();
			}
		}
	}
	
	//***************************************
	// Case - oneBadFullPacketWithSO_lengthError() 
	// send - pkt with so with lengthError
	// recv - SRETL
	//***************************************
	void oneBadFullPacketWithSO_lengthError() {
		try {
			// 1. creating a socket to connect to the server
			 //requestSocket = new Socket("59.90.1.246", 1025);
			requestSocket = new Socket("192.168.1.95", 1100);

			System.out.println("Connected to localhost in port 1100");
			// 2. get Input and Output reders
			bswriter = new BufferedWriter(new OutputStreamWriter(requestSocket.getOutputStream()));
			isr = new InputStreamReader( requestSocket.getInputStream() /*, SOCKET_DATA_CHARSET */);
			//bsreader = new BufferedReader(new InputStreamReader( dkpmClientDeviceSocket.getInputStream()));

			char [] dataFromDeviceAsCharBuffer = new char[2000];
			int deviceDataLength;
			
			// 3: Communicating with the server
			
			do {
					//one bad full packet record with SO pattern and lengthError
					sendMessage("S000A80CBA6590C0001B0000F5A1LSO01L"); 
					deviceDataLength = isr.read(dataFromDeviceAsCharBuffer, 0, dataFromDeviceAsCharBuffer.length);
					servMsg=new String(dataFromDeviceAsCharBuffer,0,deviceDataLength);
					System.out.println("message From SERVER====>" + servMsg);
			} while (!servMsg.equals("SRETL"));
		} 
		catch (UnknownHostException unknownHost) {
			System.err.println("You are trying to connect to an unknown host!");
		} 
		catch (IOException ioException) {
			ioException.printStackTrace();
		} 
		finally {
			// 4: Closing connection
			try {
				requestSocket.close();
			} catch (IOException ioException) {
				ioException.printStackTrace();
			}
		}
	}
	
	//***************************************
	// Case - oneBadAndGoodFullPacketWithSO_MisMatchRecords() 
	// send - one pkt  with so and number of mismatch records.
	// recv - SRETL
	// send - another pkt with so and matching number of records
	// recv - SEL
	//***************************************
	void oneBadAndGoodFullPacketWithSO_MisMatchRecords() {
		try {
			// 1. creating a socket to connect to the server
			 //requestSocket = new Socket("59.90.1.246", 1025);
		String[] data=new String[]{"S000A80CBA6590C000A00FF010000000000000000000000000E000000E80302002001B80B0000F5AALSO02L",
				"S000A80CBA6590C000A00FF010000000000000000000000000E000000E80302002001B80B0000F5AALS000A80CBA6590C000A00FF010000000000000000000000000E000000E80302002001B80B0000F5AALSO02L","S000A80CBA6590C000A00FF010000000000000000000000000E000000E80302002001B80B0000F5AALSO02L"};
		int i=0;
		do {
			requestSocket = new Socket("192.168.1.95", 1100);

			System.out.println("Connected to localhost in port 1100");
			// 2. get Input and Output reders
			
			bswriter = new BufferedWriter(new OutputStreamWriter(requestSocket.getOutputStream()));
			isr = new InputStreamReader( requestSocket.getInputStream() /*, SOCKET_DATA_CHARSET */);
			//bsreader = new BufferedReader(new InputStreamReader( dkpmClientDeviceSocket.getInputStream()));

			char [] dataFromDeviceAsCharBuffer = new char[2000];
			int deviceDataLength;
			
			// 3: Communicating with the server
	
			
					//send - one pkt  with so and number of mismatch records.
					sendMessage(data[i]); 
					deviceDataLength = isr.read(dataFromDeviceAsCharBuffer, 0, dataFromDeviceAsCharBuffer.length);
					servMsg=new String(dataFromDeviceAsCharBuffer,0,deviceDataLength);
					
					System.out.println("message From SERVER====>" + servMsg);
					i++;
			} while (!servMsg.equals("SEL"));
		} 
		catch (UnknownHostException unknownHost) {
			System.err.println("You are trying to connect to an unknown host!");
		} 
		catch (IOException ioException) {
			ioException.printStackTrace();
		} 
		finally {
			// 4: Closing connection
			try {
				requestSocket.close();
			} catch (IOException ioException) {
				ioException.printStackTrace();
			}
		}
	}
	
	//***************************************
	// Case - oneBadAndGoodFullPacketWithSO_ChksumError() 
	// send - one pkt  with so and ChksumError.
	// recv - SRETL
	// send - another pkt with so and without ChksumError
	// recv - SEL
	//***************************************
	void oneBadAndGoodFullPacketWithSO_ChksumError() {
		try {
			// 1. creating a socket to connect to the server
			 //requestSocket = new Socket("59.90.1.246", 1025);
		String[] data=new String[]{"S000A80CBA6590C000A00FF010000000000000000000000000E000000E80302002001B80B0000F5A1LSO01L","S000A80CBA6590C000A00FF010000000000000000000000000E000000E80302002001B80B0000F5AALSO01L"};
		int i=0;
		do {
			requestSocket = new Socket("192.168.1.95", 1100);

			System.out.println("Connected to localhost in port 1100");
			// 2. get Input and Output reders
			
			bswriter = new BufferedWriter(new OutputStreamWriter(requestSocket.getOutputStream()));
			isr = new InputStreamReader( requestSocket.getInputStream() /*, SOCKET_DATA_CHARSET */);
			//bsreader = new BufferedReader(new InputStreamReader( dkpmClientDeviceSocket.getInputStream()));

			char [] dataFromDeviceAsCharBuffer = new char[2000];
			int deviceDataLength;
			
			// 3: Communicating with the server
	
			
					//send - one pkt  with so and number of mismatch records.
					sendMessage(data[i]); 
					deviceDataLength = isr.read(dataFromDeviceAsCharBuffer, 0, dataFromDeviceAsCharBuffer.length);
					servMsg=new String(dataFromDeviceAsCharBuffer,0,deviceDataLength);
					
					System.out.println("message From SERVER====>" + servMsg);
					i++;
			} while (!servMsg.equals("SEL"));
		} 
		catch (UnknownHostException unknownHost) {
			System.err.println("You are trying to connect to an unknown host!");
		} 
		catch (IOException ioException) {
			ioException.printStackTrace();
		} 
		finally {
			// 4: Closing connection
			try {
				requestSocket.close();
			} catch (IOException ioException) {
				ioException.printStackTrace();
			}
		}
	}
	
	//***************************************
	// Case - oneBadAndGoodFullPacketWithSO_LengthError() 
	// send - one pkt  with so and LengthError.
	// recv - SRETL
	// send - another pkt with so and without LengthError
	// recv - SEL
	//***************************************
	void oneBadAndGoodFullPacketWithSO_LengthError() {
		try {
			// 1. creating a socket to connect to the server
			 //requestSocket = new Socket("59.90.1.246", 1025);
		String[] data=new String[]{"S000A80CBA6590C000A00FF010000000000000000000000000E000000LSO01L","S000A80CBA6590C000A00FF010000000000000000000000000E000000E80302002001B80B0000F5AALSO01L"};
		int i=0;
		do {
			requestSocket = new Socket("192.168.1.95", 1100);

			System.out.println("Connected to localhost in port 1100");
			// 2. get Input and Output reders
			
			bswriter = new BufferedWriter(new OutputStreamWriter(requestSocket.getOutputStream()));
			isr = new InputStreamReader( requestSocket.getInputStream() /*, SOCKET_DATA_CHARSET */);
			//bsreader = new BufferedReader(new InputStreamReader( dkpmClientDeviceSocket.getInputStream()));

			char [] dataFromDeviceAsCharBuffer = new char[2000];
			int deviceDataLength;
			
			// 3: Communicating with the server
	
			
					sendMessage(data[i]); 
					deviceDataLength = isr.read(dataFromDeviceAsCharBuffer, 0, dataFromDeviceAsCharBuffer.length);
					servMsg=new String(dataFromDeviceAsCharBuffer,0,deviceDataLength);
					
					System.out.println("message From SERVER====>" + servMsg);
					i++;
			} while (!servMsg.equals("SEL"));
		} 
		catch (UnknownHostException unknownHost) {
			System.err.println("You are trying to connect to an unknown host!");
		} 
		catch (IOException ioException) {
			ioException.printStackTrace();
		} 
		finally {
			// 4: Closing connection
			try {
				requestSocket.close();
			} catch (IOException ioException) {
				ioException.printStackTrace();
			}
		}
	}
	
	//***************************************
	// Case - oneBadAndGoodFullPacketWithSO_exceed3retries() 
	// send - first pkt  with so and LengthError.
	// recv - SRETL
	// send - second pkt with so and without LengthError
	// recv - SRETL
	// send - third pkt with so and without LengthError
	// recv - SRETL
	//***************************************
	void oneBadAndGoodFullPacketWithSO_exceedRetries() {
		try {
			// 1. creating a socket to connect to the server
			 //requestSocket = new Socket("59.90.1.246", 1025);
		String[] data=new String[]{"S000A80CBA6590C000A00FF010000000000LSO01L","S000A80CBA6590C02002001B80B0000F5AALSO01L","S000A80CBA6590C02002001B80B0000F5AALSO01L"};
		int i=0;
		do {
			requestSocket = new Socket("192.168.1.95", 1100);

			System.out.println("Connected to localhost in port 1100");
			// 2. get Input and Output reders
			
			bswriter = new BufferedWriter(new OutputStreamWriter(requestSocket.getOutputStream()));
			isr = new InputStreamReader( requestSocket.getInputStream() /*, SOCKET_DATA_CHARSET */);
			//bsreader = new BufferedReader(new InputStreamReader( dkpmClientDeviceSocket.getInputStream()));

			char [] dataFromDeviceAsCharBuffer = new char[2000];
			int deviceDataLength;
			
			// 3: Communicating with the server
	
			
					sendMessage(data[i]); 
					deviceDataLength = isr.read(dataFromDeviceAsCharBuffer, 0, dataFromDeviceAsCharBuffer.length);
					servMsg=new String(dataFromDeviceAsCharBuffer,0,deviceDataLength);
					
					System.out.println("message From SERVER====>" + servMsg);
					i++;
			} while (!servMsg.equals("SEL"));
		} 
		catch (UnknownHostException unknownHost) {
			System.err.println("You are trying to connect to an unknown host!");
		} 
		catch (IOException ioException) {
			ioException.printStackTrace();
		} 
		finally {
			// 4: Closing connection
			try {
				requestSocket.close();
			} catch (IOException ioException) {
				ioException.printStackTrace();
			}
		}
	}

	//***************************************
	// Case - oneBadAndGoodFullPacketWithSO_twise() 
	// send - first pkt  with so and LengthError.
	// recv - SRETL
	// send - second pkt with so and chksumError
	// recv - SRETL
	// send - third pkt with so and endwith correct Record
	// recv - SEL
	//***************************************
	void oneBadAndGoodFullPacketWithSO_twise() {
		try {
			// 1. creating a socket to connect to the server
			 //requestSocket = new Socket("59.90.1.246", 1025);
		String[] data=new String[]{"S000A80CBA6590C000A00FF010000000000L","S000A80CBA6590C000A00FF010000000000000000000000000E000000E80302002001B80B0000F5A2LSO01L",
				"S000A80CBA6590C000A00FF010000000000000000000000000E000000E80302002001B80B0000F5AALSO01L"};
		int i=0;
		do {
			requestSocket = new Socket("192.168.1.95", 1100);

			System.out.println("Connected to localhost in port 1100");
			// 2. get Input and Output reders
			
			bswriter = new BufferedWriter(new OutputStreamWriter(requestSocket.getOutputStream()));
			isr = new InputStreamReader( requestSocket.getInputStream() /*, SOCKET_DATA_CHARSET */);
			//bsreader = new BufferedReader(new InputStreamReader( dkpmClientDeviceSocket.getInputStream()));

			char [] dataFromDeviceAsCharBuffer = new char[2000];
			int deviceDataLength;
			
			// 3: Communicating with the server
	
			
					sendMessage(data[i]); 
					deviceDataLength = isr.read(dataFromDeviceAsCharBuffer, 0, dataFromDeviceAsCharBuffer.length);
					servMsg=new String(dataFromDeviceAsCharBuffer,0,deviceDataLength);
					
					System.out.println("message From SERVER====>" + servMsg);
					i++;
			} while (!servMsg.equals("SEL"));
		} 
		catch (UnknownHostException unknownHost) {
			System.err.println("You are trying to connect to an unknown host!");
		} 
		catch (IOException ioException) {
			ioException.printStackTrace();
		} 
		finally {
			// 4: Closing connection
			try {
				requestSocket.close();
			} catch (IOException ioException) {
				ioException.printStackTrace();
			}
		}
	}
	
	//***************************************
	// Case - oneBadSoFullPacket_lessbytes() 
	// send - first pkt  with 40 characters and wait for 12 seconds.
	// send - second pkt  with remaining packets which is bad pkt
	// recv - SRETL
	
	//***************************************
	void oneBadSoFullPacket_lessbytes() {
		   try {
		    String[] data=new String[]{"S000A80CBA6590C000A00FF01000000000000200",
		      "20020010F5AALSN02L",
		      };
		        
		    // 1. creating a socket to connect to the server 
		    int i=0;
		    do {
		    requestSocket = new Socket("192.168.1.95", 1100);
		    System.out.println("Connected to localhost in port 1100");
		    
		    // 2. get Input and Output reders
		    bswriter = new BufferedWriter(new OutputStreamWriter(requestSocket.getOutputStream()));
		    isr = new InputStreamReader( requestSocket.getInputStream());
		    char [] dataFromDeviceAsCharBuffer = new char[2000];
		    int deviceDataLength;
		    
		    // 3: Communicating with the server
			    	sendMessage(data[i]);
		    		try {
		    			thread.sleep(12000);
		    		} 
		    		catch (InterruptedException e1) {
		      		System.out.println("Thread interrupted!");
		      		e1.printStackTrace();
		    		}
		    		
		    		i++;
		    		sendMessage(data[i]);  
		      
		      deviceDataLength = isr.read(dataFromDeviceAsCharBuffer, 0, dataFromDeviceAsCharBuffer.length);
		      servMsg=new String(dataFromDeviceAsCharBuffer,0,deviceDataLength);
		      System.out.println("message From SERVER====>" + servMsg);
		    
		    } while (!servMsg.equals("SRETL"));
		   } 
		   catch (UnknownHostException unknownHost) {
		    System.err.println("You are trying to connect to an unknown host!");
		   } 
		   catch (IOException ioException) {
		    ioException.printStackTrace();
		   } 
		   finally {
		    // 4: Closing connection
		    try {
		     requestSocket.close();
		    } catch (IOException ioException) {
		     ioException.printStackTrace();
		    }
		   }
		  }
	//***************************************
	// Case - oneGoodSoFullPacket_lessbytes() 
	// send - first pkt  with 40 characters and wait for 12 seconds.
	// send - second pkt  with remaining packets which is Good pkt
	// recv - SEL
	
	//***************************************
	void oneGoodSoFullPacket_lessbytes() {
		   try {
		    String[] data=new String[]{"S000A80CBA6590C000A00FF010000000000000000000000000E000000E80302002001B80",
		      "B0000F5AALSO01L",
		      };
		        
		    // 1. creating a socket to connect to the server 
		    int i=0;
		    do {
		    requestSocket = new Socket("192.168.1.95", 1100);
		    System.out.println("Connected to localhost in port 1100");
		    
		    // 2. get Input and Output reders
		    bswriter = new BufferedWriter(new OutputStreamWriter(requestSocket.getOutputStream()));
		    isr = new InputStreamReader( requestSocket.getInputStream());
		    char [] dataFromDeviceAsCharBuffer = new char[2000];
		    int deviceDataLength;
		    
		    // 3: Communicating with the server
			    	sendMessage(data[i]);
		    		try {
		    			thread.sleep(12000);
		    		} 
		    		catch (InterruptedException e1) {
		      		System.out.println("Thread interrupted!");
		      		e1.printStackTrace();
		    		}
		    		
		    		i++;
		    		sendMessage(data[i]);  
		      
		      deviceDataLength = isr.read(dataFromDeviceAsCharBuffer, 0, dataFromDeviceAsCharBuffer.length);
		      servMsg=new String(dataFromDeviceAsCharBuffer,0,deviceDataLength);
		      System.out.println("message From SERVER====>" + servMsg);
		    
		    } while (!servMsg.equals("SEL"));
		   } 
		   catch (UnknownHostException unknownHost) {
		    System.err.println("You are trying to connect to an unknown host!");
		   } 
		   catch (IOException ioException) {
		    ioException.printStackTrace();
		   } 
		   finally {
		    // 4: Closing connection
		    try {
		     requestSocket.close();
		    } catch (IOException ioException) {
		     ioException.printStackTrace();
		    }
		   }
		  }
	
	//***************************************
	// Case - oneGoodSoFullPacket 
	// send - first pkt  with all characters and wait for 15 seconds.
	// recv - SEL
	
	//***************************************
	void oneGoodSoFullPacket() {
		   try {
		    String[] data=new String[]{"S000A80CBA6590C000A00FF010000000000000000000000000E000000E80302002001B80B0000F5AALSO01L",
		    		};
		        
		    // 1. creating a socket to connect to the server 
		    int i=0;
		    do {
		    requestSocket = new Socket("192.168.1.95", 1100);
		    System.out.println("Connected to localhost in port 1100");
		    
		    // 2. get Input and Output reders
		    bswriter = new BufferedWriter(new OutputStreamWriter(requestSocket.getOutputStream()));
		    isr = new InputStreamReader( requestSocket.getInputStream());
		    char [] dataFromDeviceAsCharBuffer = new char[2000];
		    int deviceDataLength;
		    
		    // 3: Communicating with the server
		    try {
				thread.sleep(15000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			    	sendMessage(data[i]);
		    		
		      deviceDataLength = isr.read(dataFromDeviceAsCharBuffer, 0, dataFromDeviceAsCharBuffer.length);
		      servMsg=new String(dataFromDeviceAsCharBuffer,0,deviceDataLength);
		      System.out.println("message From SERVER====>" + servMsg);
		    
		    } while (!servMsg.equals("SEL"));
		   } 
		   catch (UnknownHostException unknownHost) {
		    System.err.println("You are trying to connect to an unknown host!");
		   } 
		   catch (IOException ioException) {
		    ioException.printStackTrace();
		   } 
		   finally {
		    // 4: Closing connection
		    try {
		     requestSocket.close();
		    } catch (IOException ioException) {
		     ioException.printStackTrace();
		    }
		   }
		  }
	
	//***************************************
	// Case - oneBadSoFullPacket_timeout 
	// send - first pkt  with less characters and wait for 15 seconds to send remaining packet.
	// recv - SEL
	
	//***************************************
	void oneBadSoFullPacket_timeout() {
		   try {
		    String[] data=new String[]{"S000A80CBA6590C000A00FF010000000000000000000000000",
		    		"E000000E80302002001B80B0000F5AALSO01L"
		    		};
		        
		    // 1. creating a socket to connect to the server 
		    int i=0;
		    do {
		    requestSocket = new Socket("192.168.1.95", 1100);
		    System.out.println("Connected to localhost in port 1100");
		    
		    // 2. get Input and Output reders
		    bswriter = new BufferedWriter(new OutputStreamWriter(requestSocket.getOutputStream()));
		    isr = new InputStreamReader( requestSocket.getInputStream());
		    char [] dataFromDeviceAsCharBuffer = new char[2000];
		    int deviceDataLength;
		    	sendMessage(data[0]);
		    	// 3: Communicating with the server
		    try {
				thread.sleep(15000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			    	sendMessage(data[1]);
		    		
		      deviceDataLength = isr.read(dataFromDeviceAsCharBuffer, 0, dataFromDeviceAsCharBuffer.length);
		      servMsg=new String(dataFromDeviceAsCharBuffer,0,deviceDataLength);
		      System.out.println("message From SERVER====>" + servMsg);
		    
		    } while (!servMsg.equals("SEL"));
		   } 
		   catch (UnknownHostException unknownHost) {
		    System.err.println("You are trying to connect to an unknown host!");
		   } 
		   catch (IOException ioException) {
		    ioException.printStackTrace();
		   } 
		   finally {
		    // 4: Closing connection
		    try {
		     requestSocket.close();
		    } catch (IOException ioException) {
		     ioException.printStackTrace();
		    }
		   }
		  }
	
	//***************************************
	// Case - oneGoodFullPacket_Multipletimeout 
	// send - first pkt  with less characters and wait for 15 seconds 
	// send - second pkt  with remaining characters and wait for 55 seconds 
	// send - third pkt  with remaining characters and wait for 15 seconds 
	// recv - SEL
	
	//***************************************
	void oneGoodFullPacket_Multipletimeout() {
		   try {
		    String[] data=new String[]{"S000A80CBA6590C000A00FF010000000000000000000000000",
		    		"E000000E80302002001B80",
		    		"B0000F5AALSO01L"
		    		};
		        
		    // 1. creating a socket to connect to the server 
		    int i=0;
		    do {
		    requestSocket = new Socket("192.168.1.95", 1100);
		    System.out.println("Connected to localhost in port 1100");
		    
		    // 2. get Input and Output reders
		    bswriter = new BufferedWriter(new OutputStreamWriter(requestSocket.getOutputStream()));
		    isr = new InputStreamReader( requestSocket.getInputStream());
		    char [] dataFromDeviceAsCharBuffer = new char[2000];
		    int deviceDataLength;
		    	sendMessage(data[0]);
		    	// 3: Communicating with the server
		    try {
				thread.sleep(15000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			    	sendMessage(data[1]);
			    	try {
						thread.sleep(55000);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					sendMessage(data[2]);
					try {
						thread.sleep(15000);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
		    		
		      deviceDataLength = isr.read(dataFromDeviceAsCharBuffer, 0, dataFromDeviceAsCharBuffer.length);
		      servMsg=new String(dataFromDeviceAsCharBuffer,0,deviceDataLength);
		      System.out.println("message From SERVER====>" + servMsg);
		    
		    } while (!servMsg.equals("SEL"));
		   } 
		   catch (UnknownHostException unknownHost) {
		    System.err.println("You are trying to connect to an unknown host!");
		   } 
		   catch (IOException ioException) {
		    ioException.printStackTrace();
		   } 
		   finally {
		    // 4: Closing connection
		    try {
		     requestSocket.close();
		    } catch (IOException ioException) {
		     ioException.printStackTrace();
		    }
		   }
		  }
	
	//***************************************
	// Case - oneBadSoFullPacket_Multipletimeout 
	// send - first pkt  with less characters and wait for 20 seconds 
	// send - second pkt  with remaining characters and wait for 25 seconds 
	// send - third pkt  with remaining characters and wait for 15 seconds 
	// recv - SRETL
	
	//***************************************
	
	void oneBadFullPacket_Multipletimeout() {
		   try {
		    String[] data=new String[]{"S000A80CBA6590C0010000000000000000000000000",
		    		"E000000E80302001B80",
		    		"B0000F5A1LSO01L"
		    		};
		        
		    // 1. creating a socket to connect to the server 
		    int i=0;
		    do {
		    requestSocket = new Socket("192.168.1.95", 1100);
		    System.out.println("Connected to localhost in port 1100");
		    
		    // 2. get Input and Output reders
		    bswriter = new BufferedWriter(new OutputStreamWriter(requestSocket.getOutputStream()));
		    isr = new InputStreamReader( requestSocket.getInputStream());
		    char [] dataFromDeviceAsCharBuffer = new char[2000];
		    int deviceDataLength;
		    	sendMessage(data[0]);
		    	// 3: Communicating with the server
		    try {
				thread.sleep(20000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			    	sendMessage(data[1]);
			    	try {
						thread.sleep(35000);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					sendMessage(data[2]);
					try {
						thread.sleep(15000);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
		    		
		      deviceDataLength = isr.read(dataFromDeviceAsCharBuffer, 0, dataFromDeviceAsCharBuffer.length);
		      servMsg=new String(dataFromDeviceAsCharBuffer,0,deviceDataLength);
		      System.out.println("message From SERVER====>" + servMsg);
		    
		    } while (!servMsg.equals("SRETL"));
		   } 
		   catch (UnknownHostException unknownHost) {
		    System.err.println("You are trying to connect to an unknown host!");
		   } 
		   catch (IOException ioException) {
		    ioException.printStackTrace();
		   } 
		   finally {
		    // 4: Closing connection
		    try {
		     requestSocket.close();
		    } catch (IOException ioException) {
		     ioException.printStackTrace();
		    }
		   }
		  }
	
	/* For Anish */
void SendIncompleteBadPkt_SendSNPatternWithinTimeout() {
		   try {
		    
		    String[] data=new String[]{"S000A80CBA6590C000A00FF010000000000000000000000000E000000E8030",
		      "2002001B80B0000F5AALSN02L",
		      "S000A80CBA6590C000A00FF010000000000000000000000000E000000E80302002001B80B0000F5AALSO01L"};
		         int i=0,j = 2;
		         boolean k=false;
		    // 1. creating a socket to connect to the server    
		    do {
		    requestSocket = new Socket("192.168.1.95", 1100);
		    System.out.println("Connected to localhost in port 1100");
		    
		    // 2. get Input and Output reders
		    bswriter = new BufferedWriter(new OutputStreamWriter(requestSocket.getOutputStream()));
		    isr = new InputStreamReader( requestSocket.getInputStream());
		    char [] dataFromDeviceAsCharBuffer = new char[2000];
		    int deviceDataLength;
		    
		    // 3: Communicating with the server
		    if (!k) { 
		    	sendMessage(data[i]);
		    		try {
		    			thread.sleep(8000);
		    		} 
		    		catch (InterruptedException e1) {
		      		System.out.println("Thread interrupted!");
		      		e1.printStackTrace();
		    		}
		    		
		    		i++;
		    		sendMessage(data[i]);  
		      
		      deviceDataLength = isr.read(dataFromDeviceAsCharBuffer, 0, dataFromDeviceAsCharBuffer.length);
		      servMsg=new String(dataFromDeviceAsCharBuffer,0,deviceDataLength);
		      System.out.println("message From SERVER====>" + servMsg);
		      k=true;
		    }
		    else
		    {
		    	sendMessage(data[j]);
		    	  deviceDataLength = isr.read(dataFromDeviceAsCharBuffer, 0, dataFromDeviceAsCharBuffer.length);
			      servMsg=new String(dataFromDeviceAsCharBuffer,0,deviceDataLength);
			      System.out.println("message From SERVER====>" + servMsg);
		    }
		    
		    } while (!servMsg.equals("SEL"));
		   } 
		   catch (UnknownHostException unknownHost) {
		    System.err.println("You are trying to connect to an unknown host!");
		   } 
		   catch (IOException ioException) {
		    ioException.printStackTrace();
		   } 
		   finally {
		    // 4: Closing connection
		    try {
		     requestSocket.close();
		    } catch (IOException ioException) {
		     ioException.printStackTrace();
		    }
		   }
		  }

//***************************************
// Case - SNxxl and SOxxL Pattern check () 
// send - pkt with so
// recv - SEL
//***************************************
void oneMatchingSOOBL() {
	try {
		// 1. creating a socket to connect to the server
		 requestSocket = new Socket("192.168.8.19", 1100);
		//requestSocket = new Socket("192.168.1.95", 1100);

		System.out.println("Connected to localhost in port 1100");
		// 2. get Input and Output reders
		bswriter = new BufferedWriter(new OutputStreamWriter(requestSocket.getOutputStream()));
		isr = new InputStreamReader( requestSocket.getInputStream() /*, SOCKET_DATA_CHARSET */);
		//bsreader = new BufferedReader(new InputStreamReader( dkpmClientDeviceSocket.getInputStream()));

		char [] dataFromDeviceAsCharBuffer = new char[2000];
		int deviceDataLength;
		
		// 3: Communicating with the server
		
		do {
				//one Good full packet record with SO pattern
				sendMessage("S001940B0D45908000100000000000000000000000000000000000000000000001701000000003851L"+
				"S001980B0D45908000100000000000000000000000000000000000000000000001701000000002501L"+	
				"S0019C0B0D45908000100000000000000000000000000000000000000000000001701000000002E31LS001900B1D45908000100000000000000000000000000000000000000000000001701000000001FA1LS001940B1D45908000100000000000000000000000000000000000000000000001701000000001491L"+
				"S001980B1D459080001000000000000000000000000000000000000000000000017010000000009C1LS0019C0B1D459080001000000000000000000000000000000000000000000000017010000000002F1L"+
				"S001900B2D459080001000000000000000000000000000000000000000000000017010000000068A1L"+
				"S001940B2D45908000100000000000000000000000000000000000000000000001701000000006391LS0019EAB2D4590000000000000000000000000000000000000000000000000000000000000000CA4BLS001900B3D45908000100000000000000000000000000000000000000000000001701000000004461LSN0BL"
				
				); //80B00003B1BLSO01L");
				deviceDataLength = isr.read(dataFromDeviceAsCharBuffer, 0, dataFromDeviceAsCharBuffer.length);
				servMsg=new String(dataFromDeviceAsCharBuffer,0,deviceDataLength);
				System.out.println("message From SERVERSOBL test====>" + servMsg);
		} while (!servMsg.equals("SEL"));
	} 
	catch (UnknownHostException unknownHost) {
		System.err.println("You are trying to connect to an unknown host!");
	} 
	catch (IOException ioException) {
		ioException.printStackTrace();
	} 
	finally {
		// 4: Closing connection
		try {
			requestSocket.close();
		} catch (IOException ioException) {
			ioException.printStackTrace();
		}
	}
}
	
	void sendMessage(String msg) {
		try {
			bswriter.write (msg);
			bswriter.flush();
			System.out.println("client==>" + msg);
		} catch (IOException ioException) {
			ioException.printStackTrace();
		}
	}

	public static void main(String args[]) {
		GQDKPMBasicSuccessTestCases client = new GQDKPMBasicSuccessTestCases();
		client.oneGoodFullPacketWithSO();
		//client.oneBadFullPacketWithSO_MismatchRecords();
		//client.oneBadFullPacketWithSO_checksumError();
		//client.oneBadFullPacketWithSO_lengthError();
		//client.oneBadAndGoodFullPacketWithSO_MisMatchRecords();
		//client.oneBadAndGoodFullPacketWithSO_ChksumError();
		//client.oneBadAndGoodFullPacketWithSO_LengthError();
		//client.oneBadAndGoodFullPacketWithSO_exceedRetries();
		//client.oneBadAndGoodFullPacketWithSO_twise();
		//client.oneBadAndGoodFullPacketWithSO_lessbytes();
		//client.SendIncompleteBadPkt_SendSNPatternWithinTimeout();
		//client.oneBadSoFullPacket_lessbytes();
		//client.oneGoodSoFullPacket_lessbytes();
		//client.oneGoodSoFullPacket();
		//client.oneBadSoFullPacket_timeout();
//		client.oneGoodFullPacket_Multipletimeout();
		//client.oneBadFullPacket_Multipletimeout();
		
		//client.oneMatchingSOOBL();
		
	}
}
