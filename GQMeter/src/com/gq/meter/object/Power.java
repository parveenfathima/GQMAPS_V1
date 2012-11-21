package com.gq.meter.object;

public class Power {

    int load;
    int energy;  // if load * runHrs = energy , y do we need this ?
    int frequency;
    
    double voltage;
    double current;
    double runHrs;
    double powerFactor;

    String extras;	// anything device specific

	public Power(int load, int energy, int frequency, double voltage,
			double current, double runHrs, double powerFactor, String extras) {
		super();
		this.load = load;
		this.energy = energy;
		this.frequency = frequency;
		this.voltage = voltage;
		this.current = current;
		this.runHrs = runHrs;
		this.powerFactor = powerFactor;
		this.extras = extras;
	}

	public int getLoad() {
		return load;
	}

	public int getEnergy() {
		return energy;
	}

	public int getFrequency() {
		return frequency;
	}

	public double getVoltage() {
		return voltage;
	}

	public double getCurrent() {
		return current;
	}

	public double getRunHrs() {
		return runHrs;
	}

	public double getPowerFactor() {
		return powerFactor;
	}

	public String getExtras() {
		return extras;
	}
    

}
