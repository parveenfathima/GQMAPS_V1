/**
 * 
 */
package com.gq.meter.object;

/**
 * @author GQ
 * 
 */
public class ChartContainer {

    private CharSequence chartData;
    private String chartType;
    private String positionId;

    public ChartContainer(CharSequence chartData, String chartType, String positionId) {
        super();
        this.chartData = chartData;
        this.chartType = chartType;
        this.positionId = positionId;
    }

    public CharSequence getChartData() {
        return chartData;
    }

    public String getChartType() {
        return chartType;
    }

    public String getPositionId() {
        return positionId;
    }

}
