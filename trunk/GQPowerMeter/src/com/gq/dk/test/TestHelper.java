package com.gq.dk.test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import com.gq.dk.exception.GQDKPMException;
import com.gq.dk.util.GQDKPMConstants;

public class TestHelper implements Runnable{

	/*
	protected static final int getDecFromHex(String inputHex) {
		
		String msb = inputHex.substring(0, 2);
		String lsb = inputHex.substring(2, 4);

		int val =  ( Integer.parseInt(msb, 16) * 256 ) + ( Integer.parseInt(lsb, 16) * 1 );
		System.out.println("incoming lsb = " + lsb +" lsb hex-dec = "+Integer.parseInt(lsb, 16) * 1 +
				" , msb = "+ msb + " msb hex-dec = "+Integer.parseInt(msb, 16) * 256 + " , val = " + val );
		return val;
	}
	
	protected static final int calcCRC( String s ) {
		
		int crcValue = 0xffff;
		int crcValueLSB = 1;
		int Tmp8H;
		int Tmp8L;
		
		for ( int i=0 ; i < s.length() ; i++ ) {

			Tmp8H = s.charAt(i);
			i++;
			Tmp8L = s.charAt(i);

			if((Tmp8H > 0x2f) && (Tmp8H < 0x3a)){
				Tmp8H -= 0x30;
			}
			else if((Tmp8H > 0x40) && (Tmp8H < 0x47)){
				Tmp8H -= 0x37;
			}
			Tmp8H <<= 4;
			
			if((Tmp8L > 0x2f) && (Tmp8L < 0x3a)){
				Tmp8L -= 0x30;
			}
			else if((Tmp8L > 0x40) && (Tmp8L < 0x47)){
				Tmp8L -= 0x37;
			}
			Tmp8H |= Tmp8L;
			
			crcValue = crcValue ^  Tmp8H;
						
			for ( int j =0 ; j < 8 ; j++ ) {
				if ( ( crcValue & 0x01 ) == 1 ) {
					crcValueLSB = 1;		
				}
				else {
					crcValueLSB = 0;
				}
				
				crcValue >>= 1;				// Shift one bit to the right
				
				if(crcValueLSB == 1 ) {
					crcValue = crcValue ^ 0xa001;
				}
				
			} // for j ends 
			
		} // for i ends	
		
		return crcValue;
	}
	private static final String SNXXL_PATTERN = "(.*)SN[0-9][0-9]L(.*)";
	private static final String SOXXL_PATTERN = "(.*)SO[0-9][0-9]L";
	
	public static void main(String[] args) throws GQDKPMException{

		//SMC0001EB067D590800060000005800580000000200EA05000000000000000000003CB0LSMC000100077D590700050000005800580000000200EA050000000000000000000099BELSMC000140077D590800050000005800580000000200EA0500000000000000000000CDBELSMC000180077D590700050000005800580000000200EA050000000000000000000079BELSMC0001C0077D590700050000005800580000000200EA050000000000000000000009BELSMC000100087D590700050000005800580000000200EA0500000000000000000000D9B5LSMC000140087D590700050000005800580000000200EA0500000000000000000000A9B5LSMC000180087D5907000500000058004C0000000200EA05000000000000000000007C41LSMC0001C0087D5907000500000058004C0000000200EA05000000000000000000000C41LSMC000100097D5907000500000058004C0000000200EA05000000000000000000005C40LSMC000140097D5907000500000058004C0000000200EA05000000000000000000002C40LSMC000180097D5907000500000058004C0000000200EA0500000000000000000000BC40LSMC0001C1097D5907000500000058004C0000000200EA0500000000000000000000CD10LSMC0001000A7D5907000500000058004C0000000200EA05000000000000000000001C42LSMC0001400A7D5907000500000058004C0000000200EA05000000000000000000006C42LSN0FLSCL
	
		String deviceRecord = "SMC0001EB067D590800060000005800580000000200EA05000000000000000000003CB0LSO09L";
		
//		if ( s.matches(SOXXL_PATTERN) ) {
//			System.out.println("pattern was found");
//		}
//		else {
//			System.out.println("nope...... pattern was not found");
//
//		}
		
		System.out.println("num recs - <"+ deviceRecord.substring(deviceRecord.length()-3, deviceRecord.length()-1)+">");

		// check 1- number of records vs records in snxxl string 
		int numberOfRecords = Integer.parseInt(( deviceRecord.substring(deviceRecord.length()-3, deviceRecord.length()-1)), 16) * 256 ;
		
		String [] recordsArray = deviceRecord.substring(0,deviceRecord.length()-4).split("L");
		
		if ( recordsArray.length !=  numberOfRecords ) {
			throw new GQDKPMException(GQDKPMConstants.GQ_DK_PM_EXCEPTION_NUM_RECS_MISMATCH);
		}
		
		// check 2 - individual record length

		for ( int i = 0 ; i < recordsArray.length ; i++ ) {
			System.out.println("record " + i + " = <" + recordsArray[i] + ">");
			
			// since split cut the last L from the records , we add it back
			recordsArray[i] = recordsArray[i]+"L";
			
			if ( recordsArray[i].length() != GQDKPMConstants.GQ_DK_PM_SOLAR_1000_RECORD_LENGTH ) {
				throw new GQDKPMException(GQDKPMConstants.GQ_DK_PM_EXCEPTION_REC_LENGTH_MISMATCH);
			}
		}
		
		// check 3- checksum of individual records

		for ( int i = 0 ; i < recordsArray.length ; i++ ) {			
			String crcInput = recordsArray[i].substring(recordsArray[i].length()-5, recordsArray[i].length()-1);
			String crcCheckStr = recordsArray[i].substring(1, recordsArray[i].length()-5);
			
			if ( getDecFromHex(crcInput) != calcCRC(crcCheckStr) ) {
				throw new GQDKPMException(GQDKPMConstants.GQ_DK_PM_EXCEPTION_CHECKSUM_ERROR);
			}
		}
		return;
		//return new ArrayList<String>(Arrays.asList(recordsArray));
		
	}
*/

	     int num = 0;  
	      
	    public void run(){  
	        Thread t = Thread.currentThread();  
	        String name = t.getName();  
	        if(name.equals("Thread1")){  
	            num=10;  
	        }  
	        else{  
	            System.out.println("value of num is :"+num);  
	        }  
	          
	    }  
	    public static void main(String args[]) throws InterruptedException{  
	        Runnable r = new TestHelper();  
	        Thread t1 = new Thread(r);  
	        t1.setName("Thread1");  
	        t1.start();  
	          
	       // Thread.sleep(1000);  
	      
	        Thread t2 = new Thread(r);  
	        t2.setName("Thread2");  
	        t2.start();  
	    }  
	  
}
