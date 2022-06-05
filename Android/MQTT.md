![MQTT](https://miro.medium.com/max/832/1*Menw6EN-fwPadjkf5nK7OQ.png)
# MQTT 로 ANDROID ⛓ ARDUINO 연결
![ANDROID](https://miro.medium.com/max/794/1*6l0Ckp_WCDqdhuqnRpmnXg.png)
## 💡MQTT 정확히 이해하기 
### Message Queuing Telemetry Transport
>  일단 위의 이미지들로 대충 감을 잡아보자 
 > mqtt 의 구조는 위와 같다. <br> 
 > 간단히 말해서 MQTT는 <br>
> 게시-구독 클라이언트-서버 메시징 전송 프로토콜이다.

### mqtt는 게시 - 구독 아키텍처이다. 
게시자와 구독자는 직접 연결되어 있지 않다. <br>
그러므로 직접 데이터를 보내지도 않는다. <br>
게시자는 구독자가 누구인지 모른채 데이터를 카테고리형태로 보낸다. 이것을 **토픽(topic)** 이라고 한다.<br>
 ![mqtt](https://miro.medium.com/max/1000/0*50cEhE25wGqJ5RUT.png)
유투브와 같이 게시자와 구독자가 나눠져 있는 형태란 뜻이다. <br> 그러나 유투버가 구독자 개개인에게 영상을 보내는 것이 아닌것처럼 mqtt의 구독과 게시도 중개인을 통해서 서로의 데이터를 보내고 받는다. 
#### 그 중개인 역할이 바로 **브로커(Broker)** 이다.

### mqtt는 서버-클라이언트 구조이다. 
서버의 역할은 mqtt에서 브로커가 한다고 보면 된다. 
 ![mqtt](https://miro.medium.com/max/1400/1*c7aWFgvjCrewl6YmAwQ96Q.png)
간단하게 외우면 된다. <br> 
publisher(게시자), subscriber(구독자)는 client 클라이언트이다. <br>
broker (브로커)는 server서버이다. <br> 
## 연결하기 connect 
client (구독,게시자)는 브로커에게 메세지를 보냄으로써 연결을 해야하는데, 메세지에는 브로커에게 전달할 중요한 정보가 있을 것이다. <br> 
#### client id , clean session? , keep alive (willmessage, username&password) 
그 정보의 종류는 아래와 같다.<br>
1. client id  <br> session 세션은 클라이언트 아이디 정보를 담고 있다. 이것이 비어있으면anonymous - 익명의 상태로 세션이 저장된다.
2. clean session <br>  clean session : false 상태: <br> 클라이언트에 제공할 정보가 있는 상태라면 브로커는 이전에 있던 세션을 사용해서 축적된 정보를 꺼내서 보여준다. <br> clean session : true상태: <br> 존재하는 모든 세션을 다 버린다. 그리고 그 상태를 클라이언트에게 보내준다. 그리고 클라이언트 아이디가 비어있는 경우 반드시 이렇게 해야한다. 
3. keep alive <br> 브로커와 클라이언트단이 연결되어있는 간격 시간을 의미한다. 몇초단위로 표현한다. 이 사이에는 아무런 메세지도 없어도 된다. 그냥 연결 유지만을 의미한다. 이 사이에는 클라이언트단에서는 계속 보통의 ping 메세지를 보내야한다. 
4. username and password <br> 이 정보는 옵션이다. 보안단계를 높이고 싶으면 보내면 된다. 
5. willmessage <br> 이 정보는 옵션이다. 클라이언트와 브로커가 연결이 끊기고 다른 클라이언트가 정보를 받지 못하는 경우 브로커가 대신 메세지를 정해두고 만들어서 보내주는 것이다. 예를들면 "연결히 원할하지 않습니다." 같은 메세지 말이다. 

## 데이터보내기 topic
브로커는 토픽을 이용하여 어떤 구독자에게 데이터를 보내줄지 결정한다. <br> 클라이언트 단에서는 따로 사용하지 않는한 토픽을 만들지 않는다. 브로커 서버는 정상적인 토픽을 받고 어떠한 작업도 하지 않는다. <br> 
#### 토픽의 조건 :
- 토픽은 문자열로 되어 있다. <br> 
- 토픽은 문자하나 이상이다. <br> 
- 토픽은 대소문자를 가린다. <br> 
- 토픽은 '/' 기호로 단위(레벨)를 구분한다. <br> 
```
myhome/groundfloor/kitchen
myhome/groundfloor/dining
myhome/groundfloor/livingroom
myhome/groundfloor/bedroom1
myhome/groundfloor/bedroom2
myhome/groundfloor/bathroom
```
## 게시하기 publish
클라이언트 단에서 데이터를 토픽에 적는데, 이때 사용하는 것이 publish message 이다. <br>
게시 메세지는 아래의 정보 종류를 갖고 있다.<br>
- 토픽 이름 (topic name) 
- 메세지 내용 (payload) : 뭐가 들어가도 상관없다. 그냥 문자열로 인식한다. 
- 서비스 품질 레벨 (QoS level) : 0,1,2가 있다. <br>
- 유지여뷰 플래그 ( retain flag) : 이전의 정상적인 데이터를 저장하고 유지하고 있을지 여부를 정의한다. <br> 만약 클라이언트가 이 상태로 구독을 받게 되면 이 저장된 데이터를 받는것이다. 
예시로 아래와 같이 구성된다. 
```
Topic name: myhome/groundfloor/kitchen
Payload: 21.5
QoS level: 1
Retain flag: false
``` 
## 구독하기 subscribe 
메세지 데이터를 받기 위해 구독자는 브로커에게 subscribe 메세지를 보낸다. <br> 
구독 메세지는 간단하게 구성되어 있다. 
- 토픽 이름 (topic name) 
- 서비스 품질 레벨 (QoS) 

```
Topic name: myhome/groundfloor/dining
QoS level: 1
...
Topic name: myhome/groundfloor/bathroom
QoS level: 1
```
## 구독 안하기 unsubscribe 
클라이언트는 현재 구독하는 메세지를 끊기 위해 unsubscribe 메세지를 브로커에게 보내기도 한다. <br> 

### QoS  자세히 알아보기 
qos 는 발신자 수신자 간의 메세지 전달 계약의 형태라고 보면 된다. <br>즉, 신뢰성 향상을 위해 중요하다. <br>
- 0 : 한번 보내고 잊는다. 안정적인 통신상태에 메세지가 손상되도 상관없으면 쓴다. 
- 1 :  최소 한번은 보낸다(보증한다). 가장 많이 쓴다. 클라이언트가 중복된 메세지를 허용할 수 있으면 된다. 
- 2 : 딱 한 번. 수신자가 메세지를 한번 수신한다. 중복을 허용 안할때 쓴다. 
QoS를 높일때마다 산뢰성은 오른다. 그러나 성능은 떨어진다. 

----

## 안드로이드에서 사용하기 
> paho 라이브러리를 사용한다. <br>
> broker를 위해 mqtt라이브러리를 사용해준다. 아무거나 고른다. 
### build.gradle : app 추가 라이브러리 sync now
위의 말대로 필요한 라이브러리를 버전에 맞게 포함시켜 준다. 
```gradle 
dependencies {
  ...
    implementation 'androidx.localbroadcastmanager:localbroadcastmanager:1.0.0'
    implementation 'org.eclipse.paho:org.eclipse.paho.client.mqttv3:1.2.4'
    implementation 'org.eclipse.paho:org.eclipse.paho.android.service:1.1.1'
}
```
[build.gradle(app)](https://github.com/leonardocavagnis/MQTTClientSample_Android/blob/master/app/build.gradle)

### 화면 만들기 (파편으로) 
파편은 크게 두 파트로 나눈다. 
- connect 
- client (pub + sub) 

### 브로커 종류 선택하기 
mqtt broker 의 종류는 다양하다. 
- 하이브 엠큐 hive MQ 
- 모스키토 mosquitto 
- 클라우드 mqtt 
- adafruit IO 

----

#### mqtt 클래스 생성 
mqtt class를 통해서 객체를 만들수 있게해준다. 연결 여부를 확인 및 구독/게시 함수도 포함하고 옵션을 조절할 수 있다. <br>
[MQTTClient.kt](https://github.com/leonardocavagnis/MQTTClientSample_Android/blob/master/app/src/main/java/com/example/mqttkotlinsample/MQTTClient.kt)

#### 기본 연결에 필요한 상수값 정의 
[MQTTConstants.kt](https://github.com/leonardocavagnis/MQTTClientSample_Android/blob/master/app/src/main/java/com/example/mqttkotlinsample/MQTTConstants.kt)

#### main act - 인터넷 연결 여부 확인 
[MainActivity.kt](https://github.com/leonardocavagnis/MQTTClientSample_Android/blob/master/app/src/main/java/com/example/mqttkotlinsample/MainActivity.kt)

#### connect frag
버튼을 통해 mqtt 연결 확인 <br>
[ConnectFragment.kt](https://github.com/leonardocavagnis/MQTTClientSample_Android/blob/master/app/src/main/java/com/example/mqttkotlinsample/ConnectFragment.kt)


#### client frag 
클라이언트 단에서 mqtt 객체를 통해서 구독/게시 기능을 추가하여 사용할 수 있다. <br>
[ClientFragment.kt](https://github.com/leonardocavagnis/MQTTClientSample_Android/blob/master/app/src/main/java/com/example/mqttkotlinsample/ClientFragment.kt)


