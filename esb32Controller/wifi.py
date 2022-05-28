class Wifi:
    def __init__(self, ssid, password):
        self.ssid = ssid
        self.pw = password

    def connect(self):
        import network
        wlan = network.WLAN(network.STA_IF)
        wlan.active(True)
        if not wlan.isconnected():
            print('connecting to network...')
            wlan.connect(self.ssid, self.pw)
            while not wlan.isconnected():
                pass
        print('network config:', wlan.ifconfig())

