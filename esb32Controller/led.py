from machine import Pin

class Led:
    def __init__(self, pin_number):
        self.led = Pin(pin_number, Pin.OUT)
    def toggle(self):
        self.led.value(1-self.led.value())
    def on(self):
        self.led.value(1)
    def off(self):
        self.led.value(0)
    def is_on(self):
        return self.led.value()