package com.example.databindingexample;

public interface SimpleMqttCallback {
    void messageReceived(String topic, String message);
}
