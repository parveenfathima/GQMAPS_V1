package com.gq.dk.parser;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import com.gq.dk.util.GQDKPMConstants;


abstract class GQDKPMGSVParser extends GQDKPMParser{
	
	// keep methods specific to gsv devices only here.
	// getunitid , crc and dectohex are candidates

	protected static final int getUnitId(String inputHex) {
		
		String msb = inputHex.substring(0, 2);
		String lsb = inputHex.substring(2, 4);

		int val =  ( Integer.parseInt(msb, 16) * 256 ) + ( Integer.parseInt(lsb, 16) * 1 );
//		GQDKPMConstants.logger.debug("incoming lsb = " + lsb +" lsb hex-dec = "+Integer.parseInt(lsb, 16) * 1 +
//				" , msb = "+ msb + " msb hex-dec = "+Integer.parseInt(msb, 16) * 256 + " , val = " + val );
		return val;
	}

    // provides hex to decimal conversion 
	protected static final int getDecFromHex(String inputHex) {
		// msb should be leftmost but for parsing , we use it like lsb per gsv's protocol specification
		String lsb = inputHex.substring(0, 2);
		String msb = inputHex.substring(2, 4);

		int val =  ( Integer.parseInt(msb, 16) * 256 ) + ( Integer.parseInt(lsb, 16) * 1 );
//		GQDKPMConstants.logger.debug("incoming lsb = " + lsb +" lsb hex-dec = "+Integer.parseInt(lsb, 16) * 1 +
//				" , msb = "+ msb + " msb hex-dec = "+Integer.parseInt(msb, 16) * 256 + " , val = " + val );
		return val;
	}
	
	// crc checksum validator
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

	
	// date calculator
	protected static final java.util.Date calcDate(String dateTime) {
		
//		String dateTime = "C01D9159";
		long temp;
		long seconds , minutes , hours , day , month , year;

		String data1 = dateTime.substring(6, 8);
		String data2 = dateTime.substring(4, 6);
		String data3 = dateTime.substring(2, 4);
		String data4 = dateTime.substring(0, 2);		
		
		String reverseDateTimeString = data1 + data2 + data3 + data4;
		long longdate = Long.parseLong(reverseDateTimeString,16);

		GQDKPMConstants.logger.debug("incoming dttm string = " + dateTime +" after lsb msb switch = "+reverseDateTimeString + " reversed dttm in long = "+ longdate);
				
		temp = longdate % 256;
		temp &= 0x3f;
		seconds = temp;
		
		longdate >>= 6;
		temp = longdate % 256;
		temp &= 0x3f;
		minutes = temp;
		
		longdate >>= 6;
		temp = longdate % 256;
		temp &= 0x1f;
		hours = temp;

		longdate >>= 5;
		temp = longdate % 256;
		temp &= 0x1f;
		day = temp;

		longdate >>= 5;
		temp = longdate % 256;
		temp &= 0x0f;
		month = temp;
		
		longdate >>= 4;
		temp = longdate % 256;
		temp &= 0x3f;
		temp -= 10;
		year = temp;
		
		Calendar calendar = Calendar.getInstance();
		calendar.set((int)year, (int)month, (int)day, (int)hours, (int)minutes, (int)seconds);
		
		GQDKPMConstants.logger.debug("dd/mm/yy hh:mm:ss = " + day +"/"+month+"/"+year +" " +hours +":"+minutes+":"+seconds);
		//Converting string dateformat to Date
		String dateDisplay=day +"/"+month+"/"+year +" " +hours +":"+minutes+":"+seconds;
		Date date = null;
			try{
			 	DateFormat formatter ; 
			 	formatter = new SimpleDateFormat("dd/MM/yy HH:mm:ss");
			 	date = (Date)formatter.parse(dateDisplay);  
			  }
			   catch (ParseException e)
			  {
				GQDKPMConstants.logger.debug("Date parse exception::");
			  }  
		return date;
	}
	
} // class ends

