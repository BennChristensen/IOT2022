import dht
from machine import Pin

class Thermometer:
    def __init__(self, pin):
        pin = Pin(pin, Pin.IN, Pin.PULL_UP)
        self.dht = dht.DHT11(pin)

    def read_temperature(self):
        self.dht.measure()
        return self.dht.temperature()
