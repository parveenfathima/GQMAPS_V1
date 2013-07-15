/**
 * 
 */
package com.gq.meter.object;

/**
 * @author Rathish
 * 
 */
public class PieChart {
    private String name;
    private String memLoad;

    public PieChart() {

    }

    public PieChart(String name, String memLoad) {
        this.name = name;
        this.memLoad = memLoad;
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
     * @return the memLoad
     */
    public String getMemLoad() {
        return memLoad;
    }

    /**
     * @param memLoad the memLoad to set
     */
    public void setMemLoad(String memLoad) {
        this.memLoad = memLoad;
    }

    public String toString() {
        String pieChart = "name: " + this.getName() + "\t" + "memload" + this.memLoad;
        System.out.println(pieChart);
        return pieChart;
    }
}
