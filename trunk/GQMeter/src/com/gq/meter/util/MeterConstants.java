package com.gq.meter.util;

import java.util.HashMap;

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

    public static final String SNMP_CHECK_NSRG_OCTET = "1.3.6.1.2.1.47.1.1.1.1.2"; // NSRG octet

    public static final String COLOR_PRINTER = "Y"; // is_color_printer = Yes

    public static final String BLACKWHITE_PRINTER = "N";// is_color_printer = No

    public static final String STANDARD_SYSTEM_ATTRIBUTES_ERROR = "Unable to get standard system attributes";

    public static final String ASSET_ID_ERROR = "Unable to collate asset ID";

    public static final String INSTALLED_SOFTWARE = "inst_sw";

    public static final String CONNECTED_DEVICES = "conn_devices";

    public static final String PROCESS = "process";

    public static final String SNAPSHOT = "snap_shot";

    public static final String COMPUTER_SWITCHS = "@computer";

    public static final String PRINTER_SWITCHS = "@printer";

    public static final String NSRG_SWITCHS = "@nsrg";

    public static final String STORAGE_SWITCHS = "@storage";

    public static final String METER_ID = "$meterid";

    public static final String FULL_DETAILS = "full";

    public static final String NO_VALUE = "No value is found";

    public static final String NO_OID = "No OID is found";

    public static final String NSRG = "GqMaps NSRG Meter";

    public static final String COMPUTER = "GqMaps Computer Meter";

    public static final String PRINTER = "GqMaps Printer Meter";

    public static final String COMPUTER_PROTOCOL = "computer";

    public static final String PRINTER_PROTOCOL = "printer";

    public static final String NSRG_PROTOCOL = "nsrg";

    public static final String STORAGE_PROTOCOL = "storage";

    public static String PROTOCOL_ID = "it";
    public static HashMap<String, String> linuxOSFreshMap = new HashMap<String, String>();

} // class ends
