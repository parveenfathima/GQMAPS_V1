package com.gq.meter.util;

public class MeterConstants {

    public static final String STATUS_PASS = "pass";
    public static final String STATUS_FAIL = "fail";

    public static final String SNMP_VERSION_1 = "v1";

    public static final String SNMP_VERSION_2 = "v2c";

    public static final String SNMP_VERSION_3 = "v3";

    // first level snmp check octet

    public static final String SNMP_CHECK_OCTET = "1.3.6.1.2.1.1"; // Find a minimal value to do walk

    public static final String SNMP_CHECK_PRINTER_OCTET = "1.3.6.1.2.1.25.3.5.1.1.1"; // Printer serial number

    public static final String SNMP_CHECK_COMPUTER_OCTET = "1.3.6.1.2.1.25.1.6.0"; // Number of process

    public static final String SNMP_CHECK_SWITCH_OCTET = "1.3.6.1.2.1.17.2.6.0"; // Number of ports

    public static final String SNMP_CHECK_ROUTER_OCTET = "1.3.6.1.2.1.83.1.1.7.0";// find

    public static final String SNMP_COMPUTER_ASSET = "COMPUTER";

    public static final String SNMP_PRINTER_ASSET = "PRINTER";

    public static final String SNMP_ROUTER_ASSET = "ROUTER";

    public static final String SNMP_SWITCH_ASSET = "SWITCH";

} // class ends
