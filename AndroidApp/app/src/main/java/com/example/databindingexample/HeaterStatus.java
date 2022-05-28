package com.example.databindingexample;

public class HeaterStatus {
    private int temperature;
    private boolean heater_on;
    private boolean control_on;

    public HeaterStatus(int temperature, boolean heater_on, boolean control_on) {
        this.temperature = temperature;
        this.heater_on = heater_on;
        this.control_on = control_on;
    }

    public boolean isHeater_on() {
        return heater_on;
    }

    public boolean isControl_on() {
        return control_on;
    }

    public int getTemperature() {
        return temperature;
    }
}
