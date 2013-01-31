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

    public static final String COLOR_PRINTER = "color";

    public static final String BLACKWHITE_PRINTER = "black";

    public static final String STANDARD_SYSTEM_ATTRIBUTES_ERROR = "Unable to get standard system attributes";

    public static final String ASSET_ID_ERROR = "Unable to collate asset ID";

    public static final String INSTALLED_SOFTWARE = "inst_sw";

    public static final String CONNECTED_DEVICES = "conn_devices";

    public static final String PROCESS = "process";

    public static final String SNAPSHOT = "snapshot";

    public static final String COMPUTER_SWITCHS = "@computer";

    public static final String PRINTER_SWITCHS = "@printer";

    public static final String ISR_SWITCHS = "@isr";

    public static final String FULL_DETAILS = "full";

} // class ends
