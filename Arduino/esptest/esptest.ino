#include <WiFi.h>
#include <PubSubClient.h>


// Replace the next variables with your SSID/Password combination
const char* ssid = "class9";
const char* password = "kosta9009";

// Add your MQTT Broker IP address, example:
const char* mqtt_server = "test.mosquitto.org";
//const char* mqtt_server = "YOUR_MQTT_BROKER_IP_ADDRESS";

WiFiClient espClient;
PubSubClient client(espClient);
long lastMsg = 0;
char msg[50];
int value = 0;


#define M_in 36
 #define M_out 39
 #define W_in 34
 #define W_out 35

 int Man=0;
 int Woman=0;

char mu[] = "Mu";
char md[] = "Md";

char wu[] = "Wu";
char wd[] = "Wd";

void setup() {
  Serial.begin(9600);
  
 
  setup_wifi();
  client.setServer(mqtt_server, 1883);
  client.setCallback(callback);

 pinMode(M_in,INPUT);
 pinMode(M_out, INPUT);
 pinMode(W_in, INPUT);
 pinMode(W_out, INPUT);
}

void setup_wifi() {
  delay(10);
  // We start by connecting to a WiFi network
  Serial.println();
  Serial.print("Connecting to ");
  Serial.println(ssid);

  WiFi.begin(ssid, password);

  while (WiFi.status() != WL_CONNECTED) {
    delay(500);
    Serial.print(".");
  }

  Serial.println("");
  Serial.println("WiFi connected");
  Serial.println("IP address: ");
  Serial.println(WiFi.localIP());
}

void callback(char* topic, byte* message, unsigned int length) {
  Serial.print("Message arrived on topic: ");
  Serial.print(topic);
  Serial.print(". Message: ");
  String messageTemp;
  
  for (int i = 0; i < length; i++) {
    Serial.print((char)message[i]);
    messageTemp += (char)message[i];
  }
  Serial.println();

}

void reconnect() {
  // Loop until we're reconnected
  while (!client.connected()) {
    Serial.print("Attempting MQTT connection...");
    // Attempt to connect
    if (client.connect("ESP8266Client")) {
      Serial.println("connected");
      // Subscribe
      client.subscribe("kosta_test");
    } else {
      Serial.print("failed, rc=");
      Serial.print(client.state());
      Serial.println(" try again in 5 seconds");
      // Wait 5 seconds before retrying
      delay(1000);
    }
  }
}
void loop() {
  if (!client.connected()) {
    reconnect();
  }
  client.loop();

  long now = millis();
  if (now - lastMsg > 5000) {
    lastMsg = now;
  }

if(digitalRead(M_in)==LOW){
   client.publish("kosta_test", mu);
 Serial.print(mu);

 }
 if(digitalRead(M_out)== LOW){

   client.publish("kosta_test", md);
 Serial.print(md);

 
 }
 if(digitalRead(W_in)== LOW){
   client.publish("kosta_test", wu);
 Serial.print(wu);

 }
 if(digitalRead(W_out)== LOW){
   client.publish("kosta_test", wd);
 Serial.print(wd);

 }



 delay(300);
}
