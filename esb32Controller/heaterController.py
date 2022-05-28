from machine import Timer
from heaterStatus import HeaterStatus
import json
class HeaterController:
    def __init__(self, sender, status_topic, heat_indikator, on_indicator, thermometer, timer, period):
        self.desired_temperature = 21
        self.sender = sender
        self.status_topic = status_topic
        self.heat_indikator = heat_indikator
        self.on_indikator = on_indicator
        self.thermometer = thermometer
        self.current_temperature = thermometer.read_temperature()
        self.timer = Timer(timer)
        self.timer.init(period=period, mode=Timer.PERIODIC, callback=self.check_temperature)

    def send_status(self):
        msg = json.dumps(HeaterStatus(self.current_temperature, bool(self.heat_indikator.is_on()), bool(self.on_indikator.is_on())).__dict__)
        self.sender.send_message(msg, self.status_topic)

    def check_temperature(self, e):
        if self.on_indikator.is_on():
            self.current_temperature = self.thermometer.read_temperature()
            if self.current_temperature < self.desired_temperature:
                self.heat_indikator.on() 
            if self.current_temperature > self.desired_temperature + 1:
                self.heat_indikator.off()
        self.send_status()

    def toggle(self):
        if self.on_indikator.is_on():
            self.heat_indikator.off()
            self.on_indikator.off()
        else:
            self.on_indikator.on()
        self.send_status()
    
    def command_received(self, turn_on, new_temperature):
        if turn_on:
            self.on_indikator.on()
            self.desired_temperature = new_temperature
        else:
            self.heat_indikator.off()
            self.on_indikator.off()
            
            