from heaterStatus import HeaterStatus
from wifi import Wifi
from mmqt import MMQT
from led import Led
from button import Button
from heaterController import HeaterController
from thermometer import Thermometer
import json

WIFI_SSID = "YOUR_SSID"
WIFI_PW = "WIFI_PASSWORD"

#Connection settings for Mosquitto
MQTT_HOST = "MQTT_HOST"
MQTT_CLIENT_ID = "CLIENT_ID"
MQTT_TOPIC = "PUBLISH_TOPIC"
MQTT_PORT = 1883
MQTT_USERNAME = "USERNAME"
MQTT_PASSWORD = "PASSWORD"
MQTT_RECIEVE_TOPIC = "SUBCRIBE_TOPIC"

#Pin settings
GREEN_LED_PIN = 14
RED_LED_PIN = 19
DHT_PIN = 18
BTN_PIN = 13

#Timer settings
CTRL_TIMER = 0
CTRL_PERIOD = 5000
BTN_TIMER = 1

wifi = Wifi(WIFI_SSID, WIFI_PW)
mmqt = MMQT(MQTT_HOST, MQTT_CLIENT_ID, MQTT_PORT, MQTT_USERNAME, MQTT_PASSWORD)

green_led = Led(GREEN_LED_PIN)
red_led = Led(RED_LED_PIN)
controller = HeaterController(mmqt, MQTT_TOPIC, red_led, green_led, Thermometer(DHT_PIN), CTRL_TIMER, CTRL_PERIOD)

def receive_msg(e, msg):
    status_dict = json.loads(msg)
    status = HeaterStatus(**status_dict)
    controller.command_received(status.control_on, status.temperature)
    
try:
    print("Connecting WIFI")
    wifi.connect()
    print("OK")
except Exception as e:
    print(str(e))

try:
    print("Connecting to MQTT") 
    mmqt.connect()
    print("OK")
except Exception as e:
    print(str(e))

def button1_pressed(change):
    controller.toggle()

button1 = Button(BTN_PIN, BTN_TIMER, button1_pressed)

mmqt.subscribe(MQTT_RECIEVE_TOPIC, receive_msg)
while True:
    print("Subscribing waiting")
    mmqt.wait_msg()