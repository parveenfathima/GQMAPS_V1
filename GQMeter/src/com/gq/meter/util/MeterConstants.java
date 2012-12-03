package com.gq.meter.util;

public class MeterConstants {

    public static final String STATUS_PASS = "pass";
    public static final String STATUS_FAIL = "fail";

    public static final String SNMP_VERSION_1 = "v1";

    public static final String SNMP_VERSION_2 = "v2c";

    public static final String SNMP_VERSION_3 = "v3";

    // first level snmp check octet

    public static final String SNMP_CHECK_OCTET = "1.3.6.1.2.1.1.1"; // Find a minimal value to do walk

    public static final String SNMP_CHECK_PRINTER_OCTET = "1.3.6.1.2.1.43.11.1.1"; // Printer tonner oid

    public static final String SNMP_CHECK_COMPUTER_OCTET = "1.3.6.1.2.1.25.3.3.1.2"; // Cpu load of computer

    public static final String SNMP_CHECK_ISR_OCTET = "1.3.6.1.2.1.47.1.1.1.1.2"; // ISR octet

} // class ends
