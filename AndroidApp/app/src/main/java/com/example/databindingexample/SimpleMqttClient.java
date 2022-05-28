package com.example.databindingexample;

import android.util.Log;

import com.google.gson.Gson;

import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

public class SimpleMqttClient {
    private final MemoryPersistence persistence = new MemoryPersistence();
    private MqttClient sampleClient;
    private MqttClientConfiguration configuration;

    SimpleMqttClient(MqttClientConfiguration configuration) {
        this.configuration = configuration;
    }

    public void connectToBroker() throws MqttException {
        sampleClient = new MqttClient(configuration.getBroker(), configuration.getClientId(), persistence);
        MqttConnectOptions connOpts = new MqttConnectOptions();
        connOpts.setUserName(configuration.getUsername());
        connOpts.setPassword(configuration.getPassword().toCharArray());
        connOpts.setCleanSession(true);
        Log.i("HeaterClient", "Connecting to broker: " + configuration.getBroker());
        try {
            sampleClient.connect(connOpts);
            Log.i("HeaterClient", "Connected to broker" + configuration.getBroker());
        } catch (MqttException e) {
            Log.e("HeaterClient", "Failed to connect to broker" + configuration.getBroker());
        }
    }

    public void publishMessage(HeaterStatus status, String topic) {
        Gson gson = new Gson();
        String content = gson.toJson(status);
        MqttMessage message = new MqttMessage(content.getBytes());
        int QOS = 1;
        try {
            message.setQos(QOS);
            sampleClient.publish(topic, message);
            Log.i("HeaterClient", "Send status message." + message);
        } catch (Exception e) {
            Log.e("HeaterClient", "Failed to send message. " + message, e);
        }
    }

    public void subscribeMessage(SimpleMqttCallback callback) throws MqttException {
        sampleClient.setCallback(new MqttCallback() {

            @Override
            public void connectionLost(Throwable cause) {
                System.out.println("connectionLost: " + cause);
            }

            @Override
            public void messageArrived(String topic, MqttMessage message) {
                System.out.println("New message topic  : " + topic);
                System.out.println("New message payload: " + message);
                callback.messageReceived(topic, message.toString());
            }

            @Override
            public void deliveryComplete(IMqttDeliveryToken token) {
                System.out.println("deliveryComplete: " + token);
            }
        });
        sampleClient.subscribe("benn/Android/temperature");
    }

    public void disconnectFromBroker() {
        //Disconnect broker
        try {
            sampleClient.disconnect();
        } catch (MqttException e) {
            e.printStackTrace();
        }
        System.out.println("Disconnected");
    }

}
