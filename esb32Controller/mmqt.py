from umqtt.robust import MQTTClient
class MMQT:
    def __init__(self, host, client_id, port, username, password):
        self.host = host
        self.client_id = client_id
        self.port = port
        self.username = username
        self.password = password
        self.mqtt_client = None
    
    def connect(self):
        try:
            self.mqtt_client = MQTTClient(client_id=self.client_id, server=self.host, user=self.username, password=self.password, port=self.port, keepalive=5000, ssl=False)
            self.mqtt_client.connect()
            print('MQTT Connected')
        
        except Exception as e:
            print('Cannot connect MQTT: ' + str(e))
            raise

    def send_message(self, msg, topic):
        try:    
            self.mqtt_client.publish(topic, msg)
            print("Sent: " + msg)
        except Exception as e:
            print("Exception publish: " + str(e))
            raise
    
    def subscribe(self, topic, callback):
        self.mqtt_client.set_callback(callback)
        self.mqtt_client.subscribe(topic)

    def wait_msg(self):
        self.mqtt_client.wait_msg()

    def check_msg(self):
        self.mqtt_client.check_msg()