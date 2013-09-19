package com.gq.dk.test;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;




public class GQDKPMBasicTimeoutTestCases{
	Socket requestSocket;
	 String servMsg;
	 BufferedWriter bswriter;
	 BufferedReader bsreader;
	 InputStreamReader isr;  
	 Thread th = new Thread();
	
	 
	 //***************************************
	 // Case 1 - SendIncompletePkt_DontSendPatternWithinTimeout() 
	 // send - pkt without SO or SN
	 // recv - No communication
	 //***************************************
	 
	 void SendIncompletePkt_DontSendPatternWithinTimeout() {
	  try {
	   // 1. creating a socket to connect to the server
	   
	   requestSocket = new Socket("192.168.1.95", 1100);

	   System.out.println("Connected to localhost in port 1100");
	   // 2. get Input and Output reders
	   bswriter = new BufferedWriter(new OutputStreamWriter(requestSocket.getOutputStream()));
	   isr = new InputStreamReader( requestSocket.getInputStream());
	  

	   char [] dataFromDeviceAsCharBuffer = new char[2000];
	   int deviceDataLength;
	   
	   // 3: Communicating with the server
	   
	   do {
	     //one Good full packet record with SO pattern
	     sendMessage("S000A80CBA6590C000A00FF010000000000000000000000000E000000E8030200"); //80B00003B1BLSO01L");
	     try {
			th.sleep(120000);
		} catch (InterruptedException e) {
			 System.out.println("Thread interrupted!");
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
	    System.out.println("isr close before====>" );
	    requestSocket.close();
	    System.out.println("isr close after====>" );
	   } catch (IOException ioException) {
	    ioException.printStackTrace();
	   }
	  }
	 }

	 
	 //***************************************
	 // Case 2- SendIncompleteGoodPkt_SendSNPatternWithinTimeout() 
	 // send - Broken Packet with 8 seconds delay with Correct records 
	 // recv - SRL 
	 // send - correct record with SOxxL
	 // recv - SEL 
	 //***************************************
	 void SendIncompleteGoodPkt_SendSNPatternWithinTimeout() {
		
		     try {
		       
		      String[] data=new String[]{"S000A80CBA6590C000A00FF010000000000000000000000000E000000E80",
		        "302002001B80B0000F5AALSN01L",
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
		         th.sleep(6000);
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
	 // Case 3- SendIncompleteBadPkt_SendSNPatternWithinTimeout() 
	 // send - Broken Packet with 8 seconds delay with mismatch records 
	 // recv - SRETL 
	 // send - correct record with SOxxL
	 // recv - SEL 
	 //***************************************
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
	         th.sleep(8000);
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
	 // Case 4- SendIncompleteGoodPkt_SendSOPatternWithinTimeout() 
	 // send - Broken Packet with 3 mins delay
	// send - Broken Packet with 2 mins delay with SOxxL
	 // recv - SEL 
	
	 //***************************************
	 void SendIncompleteGoodPkt_SendSOPatternWithinTimeout() {
	     try {
	      
	      String[] data=new String[]{"S000A80CBA6590C000A00FF",
	        "010000000000000000000000000E000000E8030200",
	        "2001B80B0000F5AALSO01L"
	        };
	          
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
	      
	       sendMessage(data[0]);
	        try {
	         th.sleep(180000);
	        } 
	        catch (InterruptedException e1) {
	          System.out.println("Thread interrupted!");
	          e1.printStackTrace();
	        }
	        
	        sendMessage(data[1]); 
	        try {
		         th.sleep(120000);
		        } 
		        catch (InterruptedException e1) {
		          System.out.println("Thread interrupted!");
		          e1.printStackTrace();
		        }
	        sendMessage(data[2]);
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
	 // Case 5- SendTenGoodPacketsusingforLoop() 
	 // send - 4Good Packets with SOxxL and 6 bad packets - FOR LOOP
	 // recv - SEL 
	 
	 //***************************************
	 void SendTenGoodPacketsusingforLoop() {
	     try {
	      
	      String[] data=new String[]{
	    		  "S000A80CBA6590C000A00FF010000000000000000000000000E000000E80302002001B80B0000F5AALSO01L",
	    		  "S000A80CBA6590C000A00FF010000000000000000000000000E000000E80302002001B80B0000F5AALSO01L",
	    		  "S000A80CBA6590C000A20FF010000000000000000000000000E000000E80302002001B80B0000F5AALSO01L",
	    		  "S000A80CBA6590C000A30FF010000000000000000000000000E000000E80302002001B80B0000F5AALSN01L",
	    		  "S000A80CBA6590C000A40FF010000000000000000000000000E000000E80302002001B80B0000F5AALSO01L",
	    		  "S000A80CBA6590C000A50FF010000000000000000000000000E000000E80302002001B80B0000F5AALSO01L",
	    		  "S000A80CBA6590C000A60FF010000000000000000000000000E000000E80302002001B80B0000F5AALSO01L",
	    		  "S000A80CBA6590C000A00FF010000000000000000000000000E000000E80302002001B80B0000F5AALSO01L",
	    		  "S000A80CBA6599C000A00FF010000000000000000000000000E000000E80302002001B80B0000F5AALSO01L",
	    		  "S000A80CBA6590C000A00FF010000000000000000000000000E000000E80302002001B80B0000F5AALSO01L"
	    		  };
	           
	          
	      // 1. creating a socket to connect to the server    
	     
	     
	      
	      // 2. get Input and Output reders
	     
	      
	      // 3: Communicating with the server
	      for(int i = 0; i < data.length; i++){
	    	  requestSocket = new Socket("192.168.1.95", 1100);
		      System.out.println("Connected to localhost in port 1100");
		      bswriter = new BufferedWriter(new OutputStreamWriter(requestSocket.getOutputStream()));
		      isr = new InputStreamReader( requestSocket.getInputStream());
		      char [] dataFromDeviceAsCharBuffer = new char[2000];
		      int deviceDataLength;
				sendMessage(data[i]);
				   deviceDataLength = isr.read(dataFromDeviceAsCharBuffer, 0, dataFromDeviceAsCharBuffer.length);
			        servMsg=new String(dataFromDeviceAsCharBuffer,0,deviceDataLength);
			        System.out.println("message From SERVER====>" + servMsg);
			}	 
	     
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
	        String[] data=new String[]{"S000A80CBA6590C000A00FF010000000000LSO01L","S000A80CBA6590C000A00FF010000000000000000000000000E000000E80302002001B80B0000F5A2LSO01L","S000A80CBA6590C000A00FF010000000000000000000000000E000000E80302002001B80B0000F5AALSO01L"};
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
		 // Case 6- Sendbadpacket_RecordsMistachwithSN01L() 
		 // send - Record Mismatch Packet
		 // recv - SRETL 
		
		 //***************************************
		 void Sendbadpacket_RecordsMistachwithSN01L() {
		     try {
		      
              String[] data=new String[]{
            		  "S000A80CBA6590C000A00FF010000000000000000000000000E000000E80302002001B80B0000F5AALSN02L"
		        };
		          
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
		      
		       sendMessage(data[0]);
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
		 // Case 7- Sendbadpacket_ChecksomeErrorwithSN01L() 
		 // send - Check Some Packet
		 // recv - SRETL 
		
		 //***************************************
		 void Sendbadpacket_ChecksomeErrorwithSN01L() {
		     try {
		      
              String[] data=new String[]{
            		  "S000A80CBA6590C000A00FF010000000000000000000000000E000000E80302002001B80B1000F5AALSN01L"
		        };
		          
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
		      
		       sendMessage(data[0]);
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
		 // Case 8- Sendbadpacket_LengthErrorwithSN01L() 
		 // send - Length Error Packet
		 // recv - SRETL 
		
		 //***************************************
		 void Sendbadpacket_LengthErrorwithSN01L() {
		     try {
		      
              String[] data=new String[]{
            		  "S000A80CBA6590C000A00FF010000000000000000000000000E000000E000F5AALSN01L"
		        };
		          
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
		      
		       sendMessage(data[0]);
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
		    // Case 9 - Sendbadpacket_RecordMismatchandGoodPacketwithSN01L() 
		    // send - first pkt  with RecordMismatch.
		    // recv - SRETL
		    // send - second pkt with SN 
		    // recv - SRL
		   //***************************************
		    void Sendbadpacket_RecordMismatchandGoodPacketwithSN01L() {
		        try {
		            // 1. creating a socket to connect to the server
		             //requestSocket = new Socket("59.90.1.246", 1025);
		        String[] data=new String[]{
		        		"S000A80CBA6590C000A00FF010000000000000000000000000E000000E80302002001B80B0000F5AALSN03L",		        		
		        		"S000A80CBA6590C000A00FF010000000000000000000000000E000000E80302002001B80B0000F5AALSN01L"};
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
		            } while (!servMsg.equals("SRL"));
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
		    // Case 10 - Sendbadpacket_ChecksomeandGoodPacketwithSN01L() 
		    // send - first pkt  with Checksome.
		    // recv - SRETL
		    // send - second pkt with SN 
		    // recv - SRL
		    //***************************************
		    void Sendbadpacket_ChecksomeandGoodPacketwithSN01L() {
		        try {
		            // 1. creating a socket to connect to the server
		             //requestSocket = new Socket("59.90.1.246", 1025);
		        String[] data=new String[]{
		        		"S000A80CBA6590C000A00FF010000000000000000000000000E000000E80302002001B80B1000F5AALSN01L",
		        		"S000A80CBA6590C000A00FF010000000000000000000000000E000000E80302002001B80B0000F5AALSN01L"};
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
		            } while (!servMsg.equals("SRL"));
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
		    // Case 11 - Sendbadpacket_LengthErrorandGoodPacketwithSN01L() 
		    // send - first pkt  with LengthError.
		    // recv - SRETL
		    // send - second pkt with SN 
		    // recv - SRL
		    //***************************************
		    void Sendbadpacket_LengthandGoodPacketwithSN01L() {
		        try {
		            // 1. creating a socket to connect to the server
		             //requestSocket = new Socket("59.90.1.246", 1025);
		        	 String[] data=new String[]{
				        		"S000A80CBA6590C000A00FF01000000000000001B80B0000F5AALSN01L",
				        		"S000A80CBA6590C000A00FF010000000000000000000000000E000000E80302002001B80B0000F5AALSN01L"};
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
		            } while (!servMsg.equals("SRL"));
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
		    // Case 12 - SixCorrectRecordsendingwithSO01L() 
		    // send - 5 Good packets with SNxxL.
		    // recv - SRL
		    // send - Last Packet with SOxxL 
		    // recv - SEL
		    //***************************************
		    void SixCorrectRecordsendingwithSO01L() {
		        try {
		            // 1. creating a socket to connect to the server
		             //requestSocket = new Socket("59.90.1.246", 1025);
		        	 String[] data=new String[]{
				        		"S000A80CBA6590C000A00FF010000000000000000000000000E000000E80302002001B80B0000F5AALSN01L",
				        		"S000A80CBA6590C000A00FF010000000000000000000000000E000000E80302002001B80B0000F5AALSN01L",
				        		"S000A80CBA6590C000A00FF010000000000000000000000000E000000E80302002001B80B0000F5AALSN01L",
				        		"S000A80CBA6590C000A00FF010000000000000000000000000E000000E80302002001B80B0000F5AALSN01L",
				        		"S000A80CBA6590C000A00FF010000000000000000000000000E000000E80302002001B80B0000F5AALSN01L",
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
		    // Case 13 - Fourgoodpacketsandtwobadpackets() 
		    // send - 4 Good packets --- inserted in database.
		    // send - 2 Bad packets --- not inserted in database.
		    //***************************************
		    
		    void Fourgoodpacketsandtwobadpackets() {
		        try {
		            // 1. creating a socket to connect to the server
		             //requestSocket = new Socket("59.90.1.246", 1025);
		        	 String[] data=new String[]{
				        		"S000A80CBA6590C000A00FF010000000000000000000000000E000000E80302002001B80B0000F5AALSN01L",
				        		"S000A80CBA6590C000A00FF010000000000000000000000000E000000E80302002001B80B0000F5AALSN01L",
				        		"S000A80CBA6590C000A00FF010000000000000000000000000E000000E80302002001B80B0000F5AALSN05L",
				        		"S000A80CBA6590C000A00FF010000000000000000000000000E000000E80302002001B80F5AALSN01L",
				        		"S000A80CBA6590C000A00FF010000000000000000000000000E000000E80302002001B80B0000F5AALSN01L",
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
		    // Case 14 - FivegoodpacketsandonebadSOxxL() 
		    // send - 5 Good packets with SNxxL.
		    // recv - SRL
		    // send - Last Bad Packet with SOxxL 
		    // recv - SRETL and Error
		    //***************************************
		    void FivegoodpacketsandonebadSOxxL() {
		        try {
		            // 1. creating a socket to connect to the server
		             //requestSocket = new Socket("59.90.1.246", 1025);
		        	 String[] data=new String[]{
				        		"S000A80CBA6590C000A00FF010000000000000000000000000E000000E80302002001B80B0000F5AALSN01L",
				        		"S000A80CBA6590C000A00FF010000000000000000000000000E000000E80302002001B80B0000F5AALSN01L",
				        		"S000A80CBA6590C000A00FF010000000000000000000000000E000000E80302002001B80B0000F5AALSN01L",
				        		"S000A80CBA6590C000A00FF010000000000000000000000000E000000E80302002001B80B0000F5AALSN01L",
				        		"S000A80CBA6590C000A00FF010000000000000000000000000E000000E80302002001B80B0000F5AALSN01L",
				        		"S000A80CBA6590C000A00FF010000000000000000000000000E000000E80302002001B80B0000F5AALSO05L"};
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
		    // Case 15 - Onegoodpacketsfiveminssleep5jointrecords() 
		    // send - 5 Good Joint packets with SNxxL.
		    // recv - SRL -- inserted seperately in database
		    // send - Last Packet with SOxxL 
		    // recv - SEL
		    //***************************************
		    void Onegoodpacketsfiveminssleep5jointrecords() {
		    	  try {
			            // 1. creating a socket to connect to the server
			             //requestSocket = new Socket("59.90.1.246", 1025);
			        String[] data=new String[]{"S000A80CBA6590C000A00FF010000000000000000000000000E000000E80302002001B80B0000F5AALSN01L",
			        		"S000A80CBA6590C000A00FF010000000000000000000000000E000000E80302002001B80B0000F5AALS000A80CBA6590C000A"+
			                 "00FF010000000000000000000000000E000000E80302002001B80B0000F5AALS000A80CBA6590C000A00FF0100000000000000"+
			                 "00000000000E000000E80302002001B80B0000F5AALS000A80CBA6590C000A00FF010000000000000000000000000E000000E803"+
			                 "02002001B80B0000F5AALS000A80CBA6590C000A00FF010000000000000000000000000E000000E80302002001B80B0000F5AALSN05L",
			                 "S000A80CBA6590C000A00FF010000000000000000000000000E000000E80302002001B80B0000F5AALSO01L"
			        		};
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
			                    try {
									th.sleep(5000);
								} catch (InterruptedException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
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
		    // Case 16 - JoinedPacketsendingwithSO05L() 
		    // send - SNO5L and SO01L in same packet.
		    // recv - SRETL
		    
		    //***************************************
		    void JoinedPacketsendingwithSO05L() {
		    	  try {
			            // 1. creating a socket to connect to the server
			             //requestSocket = new Socket("59.90.1.246", 1025);
			        String[] data=new String[]{"S000A80CBA6590C000A00FF010000000000000000000000000E000000E80302002001B80B0000F5AALSN01L",
			        		"S000A80CBA6590C000A00FF010000000000000000000000000E000000E80302002001B80B0000F5AALS000A80CBA6590C000A"+
			                 "00FF010000000000000000000000000E000000E80302002001B80B0000F5AALS000A80CBA6590C000A00FF0100000000000000"+
			                 "00000000000E000000E80302002001B80B0000F5AALS000A80CBA6590C000A00FF010000000000000000000000000E000000E803"+
			                 "02002001B80B0000F5AALS000A80CBA6590C000A00FF010000000000000000000000000E000000E80302002001B80B0000F5AALSN05L"+
			                 "S000A80CBA6590C000A00FF010000000000000000000000000E000000E80302002001B80B0000F5AALSO01L"
			        		};
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
			                    try {
									th.sleep(5000);
								} catch (InterruptedException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
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
		    // Case 17 - JoinedPacketswithSNandSO() 
		    // send - SN and SN in joint packet
		    // recv - SRETL
		    
		    //***************************************
		    
		    void JoinedPacketswithSNandSO() {
		    	  try {
			            // 1. creating a socket to connect to the server
			             //requestSocket = new Socket("59.90.1.246", 1025);
			        String[] data=new String[]{"S000A80CBA6590C000A00FF010000000000000000000000000E000000E80302002001B80B0000F5AALSN01L",
			        		"S000A80CBA6590C000A00FF010000000000000000000000000E000000E80302002001B80B0000F5AALS000A80CBA6590C000A"+
			                 "00FF010000000000000000000000000E000000E80302002001B80B0000F5AALS000A80CBA6590C000A00FF0100000000000000"+
			                 "00000000000E000000E80302002001B80B0000F5AALS000A80CBA6590C000A00FF010000000000000000000000000E000000E803"+
			                 "02002001B80B0000F5AALS000A80CBA6590C000A00FF010000000000000000000000000E000000E80302002001B80B0000F5AALSN05L",
			                 "S000A80CBA6590C000A00FF010000000000000000000000000E000000E80302002001B80B0000F5AALSO01L"
			        		};
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
			                    try {
									th.sleep(5000);
								} catch (InterruptedException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
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
	 void sendMessage(String msg) {
	  try {
	   bswriter.write (msg);
	   bswriter.flush();
	   System.out.println("client>" + msg);
	  } catch (IOException ioException) {
	   ioException.printStackTrace();
	  }
	 }

	 public static void main(String args[]) {
		 GQDKPMBasicTimeoutTestCases client = new GQDKPMBasicTimeoutTestCases(); 
		 
//		 client.JoinedPacketswithSNandSO();                          /*CASE TEST 17*/
//		 client.JoinedPacketsendingwithSO05L();                      /*CASE TEST 16*/		
//		 client.Onegoodpacketsfiveminssleep5jointrecords();          /*CASE TEST 15*/
//		 client.FivegoodpacketsandonebadSOxxL();                     /*CASE TEST 14*/
//		 client.Fourgoodpacketsandtwobadpackets();                   /*CASE TEST 13*/
//		 client.SixCorrectRecordsendingwithSO01L();                  /*CASE TEST 12*/
//		 client.Sendbadpacket_LengthandGoodPacketwithSN01L();        /*CASE TEST 11*/
//		 client.Sendbadpacket_ChecksomeandGoodPacketwithSN01L();     /*CASE TEST 10*/
//		 client.Sendbadpacket_RecordMismatchandGoodPacketwithSN01L();/*CASE TEST 9*/
//		 client.Sendbadpacket_LengthErrorwithSN01L();                /*CASE TEST 8*/
//		 client.Sendbadpacket_ChecksomeErrorwithSN01L();             /*CASE TEST 7*/
//		 client.Sendbadpacket_RecordsMistachwithSN01L();             /*CASE TEST 6*/
//		 client.SendTenGoodPacketsusingforLoop();                    /*CASE TEST 5*/
//		 client.SendIncompleteGoodPkt_SendSOPatternWithinTimeout();  /*CASE TEST 4*/
//		 client.SendIncompleteBadPkt_SendSNPatternWithinTimeout();   /*CASE TEST 3*/
	     client.SendIncompleteGoodPkt_SendSNPatternWithinTimeout();  /*CASE TEST 2*/
//	     client.SendIncompletePkt_DontSendPatternWithinTimeout();    /*CASE TEST 1*/
//       client.oneBadAndGoodFullPacketWithSO_twise();               /*CASE Vinoth Sir*/
	 }
	}

	 
	 
