/**
 * 
 */
package com.gq.meter.object;

/**
 * @author GQ
 * 
 */
public class Data {
    private String name;
    private long value;

    public Data() {

    }

    public Data(String name, long value) {
        this.name = name;
        this.value = value;

    }

    /**
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * @param name the name to set
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return the value
     */
    public long getValue() {
        return value;
    }

    /**
     * @param value the value to set
     */
    public void setValue(long value) {
        this.value = value;
    }

}
