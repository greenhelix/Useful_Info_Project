#include <WiFi.h>
#include <PubSubClient.h>
#include <Bounce2.h>
#include <ArduinoJson.h>
#include <NTPClient.h>
#include <WiFiUdp.h>

#define mIled 2
#define mOled 4
#define wIled 32
#define wOled 25

#define manin 15
#define manout 0
#define womanin 33
#define womanout 26

const char* ssid = "class9";
const char* password = "kosta9009";
const char* mqttserver = "test.mosquitto.org";

//NTP서버에서 시간 받아오는 것
WiFiUDP ntpUDP;
NTPClient timeClient(ntpUDP);

//날짜와 시간을 저장할 스트링 선언
String formattedDate;
String dayStamp;
String timeStamp;
String formattedTime;

//Json 데이터 용량 설정
const int cap = JSON_OBJECT_SIZE(8);
char output[256];
StaticJsonDocument<cap> data;


//start espclient instance
WiFiClient espclient;
//use espclient as pubsubclient
PubSubClient client(espclient);

//Instantiate a bounce object
Bounce button_min = Bounce(); 
Bounce button_mout = Bounce();
Bounce button_win = Bounce();
Bounce button_wout = Bounce();

void initWiFi(){
  WiFi.mode(WIFI_STA);
  WiFi.begin(ssid, password);
  Serial.print("Connecting to Wifi...");
  while (WiFi.status() != WL_CONNECTED){
    Serial.print('.');
    delay(500);
  }
  Serial.println(WiFi.localIP());
}

void reconnect() {
  while(!client.connected()){
    Serial.print("Attempting MQTT connection...");
    if (client.connect("esp32")){
      Serial.println("connected to broker");
    }
    else{
      Serial.print("failed, rc =");
      Serial.print(client.state());
      Serial.println("reconnecting...");
      delay(1000);
    }
  }
}

//제이슨 데이터 형식, 채널 설정
void publish_json() {
  timeClient.update();
  data["date"] = timeClient.getFormattedDate();
  data["time"] = timeClient.getFormattedTime();
  serializeJson(data, output);
  if(client.publish("kosta_test", output)){
    Serial.println("successfully published");
  }
  else{
    Serial.println("could no publish");
  }
}

void setup() {
  Serial.begin(9600); //set data transmission rate to 9600 bits/s
  initWiFi();//와이파이 접속
  client.setServer(mqttserver, 1883); //MQTT 서버연결
  
  //NTP 서버 연결
  timeClient.begin();
  timeClient.setTimeOffset(32400);// 시차에 맞게 숫자를 조정해야함

  button_min.attach(manin, INPUT_PULLUP); //pullup input pin is HIGH when switched is open and LOW when switch is closed
  button_min.interval(25);//debounce interval of 25 ms
  button_mout.attach(manout, INPUT_PULLUP);
  button_mout.interval(25);
  button_win.attach(womanin, INPUT_PULLUP);
  button_win.interval(25);
  button_wout.attach(womanout, INPUT_PULLUP);
  button_wout.interval(25);

  pinMode(mIled, OUTPUT);
  pinMode(mOled, OUTPUT);
  pinMode(wIled, OUTPUT);
  pinMode(wOled, OUTPUT); 

  data["id"] = 1;
}

void loop() {
  button_min.update(); //update bounce instance
  button_mout.update();
  button_win.update();
  button_wout.update();

  //계속 커넥트 시도
  if (!client.connected()){
    reconnect();
  }
  client.loop();

  //버튼을 누르면 데이터를 보내는 것
  if (button_min.fell()){
    Serial.println("pressed");
    data["data"] = "min";
    publish_json();
  }

  if (button_mout.fell()){
    Serial.println("pressed");
    data["data"] = "mout";
    publish_json();
  }

  if (button_win.fell()){
    Serial.println("pressed");
    data["data"] = "win";
    publish_json();
  }

  if (button_wout.fell()){
    Serial.println("pressed");
    data["data"] = "wout";
    publish_json();
  }

//버튼을 누르면 불이 들어왔다가 꺼지게 하는 것
if(digitalRead(manin)==LOW){
  digitalWrite(mIled,HIGH);
}
else digitalWrite(mIled,LOW);

if(digitalRead(manout)==LOW){
  digitalWrite(mOled,HIGH);
}
else digitalWrite(mOled,LOW);

if(digitalRead(womanin)==LOW){
  digitalWrite(wIled,HIGH);
}
else digitalWrite(wIled,LOW);

 if(digitalRead(womanout)==LOW){
  digitalWrite(wOled,HIGH);
}
else digitalWrite(wOled,LOW);

}
