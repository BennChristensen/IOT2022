package com.example.databindingexample;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;

import com.google.android.material.slider.Slider;
import com.google.android.material.switchmaterial.SwitchMaterial;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

import org.eclipse.paho.client.mqttv3.MqttException;

public class MainActivity extends AppCompatActivity {
    public static final String COMMAND_TOPIC = "benn/receive";
    private SimpleMqttClient simpleMqttClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        simpleMqttClient = new SimpleMqttClient(
                new MqttClientConfiguration(
                        "tcp://HOSTNAME:1883",
                        "username",
                        "password",
                        "BennsAndroidApp"));
        try {
            simpleMqttClient.connectToBroker();
        } catch (MqttException e) {
            Log.e("HeaterClient", "Failed to connect to broker", e);
        }

        subscribeOnBroker();
        Slider slider = (Slider) findViewById(R.id.slider);
        SwitchMaterial onSwitch = (SwitchMaterial) findViewById(R.id.onOffSwitch);
        slider.addOnChangeListener(new Slider.OnChangeListener() {
            @Override
            public void onValueChange(@NonNull Slider slider, float value, boolean fromUser) {
                ((TextView) findViewById(R.id.desired_temperature)).setText(Integer.toString((int)value) + "\u00b0");
            }
        });
        slider.addOnSliderTouchListener(new Slider.OnSliderTouchListener() {
            @Override
            public void onStartTrackingTouch(@NonNull Slider slider) {

            }

            @Override
            public void onStopTrackingTouch(@NonNull Slider slider) {
                    HeaterStatus heaterStatus = new HeaterStatus((int)slider.getValue(), false, true);
                    simpleMqttClient.publishMessage(heaterStatus, COMMAND_TOPIC);
            }
        });
        onSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                Gson gson = new Gson();
                HeaterStatus heaterStatus = new HeaterStatus((int)slider.getValue(), false, isChecked);
                simpleMqttClient.publishMessage(heaterStatus, COMMAND_TOPIC);
            }
        });
    }

    public void subscribeOnBroker() {
        try {
            simpleMqttClient.subscribeMessage((topic, message) -> {
                runOnUiThread(() -> {
                    Gson gson = new Gson();
                    HeaterStatus status = null;
                    try {
                        status = gson.fromJson(message, HeaterStatus.class);
                        Log.i("HeaterClient", "Received heater status. " + status.getTemperature());
                    } catch (JsonSyntaxException e) {
                        Log.e("HeaterClient", "Failed to deserialize json message", e);
                    }
                    TextView tempValue = (TextView) findViewById(R.id.current_temperature);
                    tempValue.setText(String.format("%s\u00b0", status.getTemperature()));
                    SwitchMaterial onOffSwitch = (SwitchMaterial) findViewById(R.id.onOffSwitch);
                    onOffSwitch.setChecked(status.isControl_on());
                });
            });
        } catch (MqttException e) {
            e.printStackTrace();
        }
    }


}


