from machine import Pin
from machine import Timer

class Button:
    def __init__(self, pin, timer_id, callback):
        self.callback = callback
        self.timer_id = timer_id 
        button1 = Pin(pin, Pin.IN, Pin.PULL_DOWN)
        button1.irq(handler=self.debounce, trigger=Pin.IRQ_FALLING)

    def debounce(self, e):
        timer = Timer(self.timer_id)
        # Start or replace a timer for 200ms, and trigger on_pressed.
        timer.init(mode=Timer.ONE_SHOT, period=200, callback=self.callback)

