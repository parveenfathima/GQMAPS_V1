/**
 * 
 */
package com.gq.meter.xchange.util;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

/**
 * @author Rathish
 * 
 */
public class GQRegistrationConstants {
    public static final Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create();

}
