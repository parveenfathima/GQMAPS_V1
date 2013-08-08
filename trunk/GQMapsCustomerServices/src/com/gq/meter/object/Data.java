/**
 * 
 */
package com.gq.meter.object;

import java.util.Date;

/**
 * @author GQ
 * 
 */
public class Data {
    private String name;
    private double value;
    private Date date;
    private CharSequence chartData;

    public Data() {

    }

    public Data(String name, long value, Date date, CharSequence chartData) {
        this.name = name;
        this.value = value;
        this.date = date;
        this.setChartData(chartData);

    }

    /**
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * @param name the name to set
     * @return
     */
    public String setName(String name) {
        return this.name = name;
    }

    /**
     * @return the value
     */
    public double getValue() {
        return value;
    }

    /**
     * @param d the value to set
     * @return
     */
    public double setValue(double d) {
        return this.value = d;
    }

    /**
     * @return the date
     */
    public Date getDate() {
        return date;
    }

    /**
     * @param date the date to set
     * @return
     */
    public Date setDate(Date date) {
        return this.date = date;
    }

    public CharSequence getChartData() {
        return chartData;
    }

    public void setChartData(CharSequence chartData) {
        this.chartData = chartData;
    }

}
