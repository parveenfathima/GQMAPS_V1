/**
 * 
 */
package com.gq.meter.object;

import java.util.Date;

/**
 * @author GQ
 * 
 */
public class ChartRowData {
    private String name;
    private double value;
    private CharSequence chartData;
    private double value1;

    public ChartRowData() {

    }

    public ChartRowData(String name, long value, Date date, CharSequence chartData, double value1) {
        this.name = name;
        this.value = value;
        this.chartData = chartData;
        this.setValue1(value1);

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

    public CharSequence getChartData() {
        return chartData;
    }

    public void setChartData(CharSequence chartData) {
        this.chartData = chartData;
    }

    public double getValue1() {
        return value1;
    }

    public void setValue1(double value1) {
        this.value1 = value1;
    }

}
