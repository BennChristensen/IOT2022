package com.example.databindingexample;

import android.icu.lang.UProperty;

public class MqttClientConfiguration {
    private String broker;
    private String username;
    private String password;
    private String clientId;

    public String getBroker() {
        return broker;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getClientId() {
        return clientId;
    }

    public MqttClientConfiguration(String broker, String username, String password, String clientId) {
        this.broker = broker;
        this.username = username;
        this.password = password;
        this.clientId = clientId;
    }
}
