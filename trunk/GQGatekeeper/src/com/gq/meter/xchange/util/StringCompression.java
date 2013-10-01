package com.gq.meter.xchange.util;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
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
        GQGateKeeperConstants.logger.info("JSON length before decompression : " + str.length());
        GZIPInputStream gis = new GZIPInputStream(new ByteArrayInputStream(str.getBytes("ISO-8859-1")));
        BufferedReader bf = new BufferedReader(new InputStreamReader(gis, "ISO-8859-1"));
        String outStr = "";
        String line;
        while ((line = bf.readLine()) != null) {
            outStr += line;
        }
        GQGateKeeperConstants.logger.info("JSON length after decompression : " + outStr.length());
        return outStr;
    }
}
