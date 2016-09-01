package com.gq.meter.util;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

public final class StringCompression {

    public static String compress(String str) throws IOException {
        if (str == null || str.length() == 0) {
            return str;
        }
        System.out.println(" [GQMETER] JSON length prior to compression : " + str.length());
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        GZIPOutputStream gzip = new GZIPOutputStream(out);
        gzip.write(str.getBytes());
        gzip.close();
        String outStr = out.toString("ISO-8859-1");
        System.out.println(" [GQMETER] JSON length after compression : " + outStr.length());
        return outStr;
     }
    
    public static String decompress(String str) throws IOException {
        if (str == null || str.length() == 0) {
            return str;
        }
        System.out.println("Input String length : " + str.length());
        GZIPInputStream gis = new GZIPInputStream(new ByteArrayInputStream(str.getBytes("ISO-8859-1")));
        BufferedReader bf = new BufferedReader(new InputStreamReader(gis, "ISO-8859-1"));
        String outStr = "";
        String line;
        while ((line=bf.readLine())!=null) {
          outStr += line;
        }
        System.out.println("Output String lenght : " + outStr.length());
        return outStr;
     }
  
     public static void main(String[] args) throws IOException {
        String filePath = "/koil/pazhanam_bak/pazhanam_bak/companies/gquotient/avaya-rcom.txt";
        
        String string = getFileData(filePath);
        System.out.println("after compress:");
        String compressed = compress(string);
        System.out.println(compressed);
        System.out.println("after decompress:");
        String decomp = decompress(compressed);
        System.out.println(decomp);
 
      }
     
     public static String getFileData(String filepath) throws FileNotFoundException,
                                                           IOException {
       BufferedReader bf = new BufferedReader(new FileReader(filepath));
       String data="";
       String line;
       while ((line=bf.readLine())!=null) {
         data += line;
       }
       return data;
     }
}
