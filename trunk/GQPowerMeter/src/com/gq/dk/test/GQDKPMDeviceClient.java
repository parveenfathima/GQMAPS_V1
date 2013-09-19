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


public class GQDKPMDeviceClient {

	Socket requestSocket;
//	ObjectOutputStream out;
	//ObjectInputStream in;
	String message;
	BufferedWriter bswriter;
	BufferedReader bsreader;
	InputStreamReader isr;    

	//SMC0001EB067D590800060000005800580000000200EA05000000000000000000003CB0LSMC000100077D590700050000005800580000000200EA050000000000000000000099BELSMC000140077D590800050000005800580000000200EA0500000000000000000000CDBELSMC000180077D590700050000005800580000000200EA050000000000000000000079BELSMC0001C0077D590700050000005800580000000200EA050000000000000000000009BELSMC000100087D590700050000005800580000000200EA0500000000000000000000D9B5LSMC000140087D590700050000005800580000000200EA0500000000000000000000A9B5LSMC000180087D5907000500000058004C0000000200EA05000000000000000000007C41LSMC0001C0087D5907000500000058004C0000000200EA05000000000000000000000C41LSMC000100097D5907000500000058004C0000000200EA05000000000000000000005C40LSMC000140097D5907000500000058004C0000000200EA05000000000000000000002C40LSMC000180097D5907000500000058004C0000000200EA0500000000000000000000BC40LSMC0001C1097D5907000500000058004C0000000200EA0500000000000000000000CD10LSMC0001000A7D5907000500000058004C0000000200EA05000000000000000000001C42LSMC0001400A7D5907000500000058004C0000000200EA05000000000000000000006C42LSN0FLSCL 
	
	void run() {
		try {
			// 1. creating a socket to connect to the server
			 //requestSocket = new Socket("59.90.1.246", 1025);
			requestSocket = new Socket("localhost", 1100);

			System.out.println("Connected to localhost in port 1100");
			// 2. get Input and Output reders
			bswriter = new BufferedWriter(new OutputStreamWriter(requestSocket.getOutputStream()));
			isr = new InputStreamReader( requestSocket.getInputStream() /*, SOCKET_DATA_CHARSET */);
			//bsreader = new BufferedReader(new InputStreamReader( dkpmClientDeviceSocket.getInputStream()));

			char [] dataFromDeviceAsCharBuffer = new char[2000];
			int deviceDataLength;
			
			// 3: Communicating with the server
			do {
				try {
					sendMessage("S0009C0DAAC590F000B00250400000000000000000000000000000000E8030300FF00B"); //80B00003B1BLSO01L");
				//	int msglen =isr.read(dataFromDeviceAsCharBuffer, 0, dataFromDeviceAsCharBuffer.length);
				//	System.out.println("server>" + dataFromDeviceAsCharBuffer.toString());
					Thread.sleep(4000);
					sendMessage("80B00003B1BLSO01L");
					int msglen =isr.read(dataFromDeviceAsCharBuffer, 0, dataFromDeviceAsCharBuffer.length);
					System.out.println("server>" + dataFromDeviceAsCharBuffer.toString());
					
				} catch (Exception classNot) {
					System.err.println("data received in unknown format");
				}
			} while (!message.equals("SEL"));
			System.out.println("server>" + dataFromDeviceAsCharBuffer.toString() +" ; i am out");
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
				isr.close();
				bswriter.close();
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
		GQDKPMDeviceClient client = new GQDKPMDeviceClient();
		client.run();
	}
}
