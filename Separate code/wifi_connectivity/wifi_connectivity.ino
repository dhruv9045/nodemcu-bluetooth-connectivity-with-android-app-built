/*
 *  ESP8266 NodeMCU MQTT Example
 *  https://circuits4you.com
 *  -Manoj R. Thakur
 */
#include <ESP8266WiFi.h>
#include <PubSubClient.h>
  
//Enter your wifi credentials
const char* ssid = "Redmi 3S";  
const char* password =  "123456789";
 
//Enter your mqtt server configurations
const char* mqttServer = "postman.cloudmqtt.com";    //Enter Your mqttServer address
const int mqttPort = 12072;       //Port number
const char* mqttUser = "zvibyzxz"; //User
const char* mqttPassword = "YHJqD8Vx7DeB"; //Password
 
WiFiClient espClient;
PubSubClient client(espClient);

 
 
 void callback(char* topic, byte* payload, unsigned int length);
void setup() {
  
   pinMode(D1, OUTPUT);
   pinMode(D2, OUTPUT);
   pinMode(D3, OUTPUT);
   pinMode(D4, OUTPUT);
   digitalWrite(D1,HIGH);
   digitalWrite(D2,HIGH);
   digitalWrite(D3,HIGH);
   digitalWrite(D4,HIGH);
  
  Serial.begin(115200);
 
  WiFi.begin(ssid, password);
 
  while (WiFi.status() != WL_CONNECTED) 
  {
    delay(100);
    Serial.println("Connecting to WiFi..");
  }
  Serial.print("Connected to WiFi :");
  Serial.println(WiFi.SSID());
 
  client.setServer(mqttServer, mqttPort);
 //client.setCallback(MQTTcallback);
 client.setCallback(callback);
  while (!client.connected()) {
    Serial.println("Connecting to MQTT...");
 
    if (client.connect("ESP8266", mqttUser, mqttPassword )) {
 
      Serial.println("connected");  
 
    } else {
 
      Serial.print("failed with state ");
      Serial.println(client.state());  //If you get state 5: mismatch in configuration
      delay(2000);
 
    }
  }
 
  client.publish("Status", "Hello from ESP8266");
  client.publish("Place","****");
  client.subscribe("LED");
 
}
 
 
 
  /*Serial.print("Message arrived in topic: ");
  Serial.println(topic);
 
  Serial.print("Message:");
  String message;
 */
 void callback(char* topic, byte* payload, unsigned int length)
 {
  char msgBuffer[20];
  Serial.print("Message arrived [");
  Serial.print(topic);
  Serial.print("] ");//MQTT_BROKER
  for (int i = 0; i < length; i++) {
    Serial.print((char)payload[i]);
  }
  Serial.println();
 Serial.println(payload[0]);
  if((byte)payload[0]== '0' ){                       //"0" of ASCII is 48
    digitalWrite(D1, LOW);
  } else if ((byte)payload[0]== '1'){
     digitalWrite(D1, HIGH);
  }
  else if( (byte)payload[0]== '2'){
    digitalWrite(D2, LOW);
  } else if ((byte) payload[0]=='3'){
     digitalWrite(D2, HIGH);
  }
   else if( (byte)payload[0]=='4'){
    digitalWrite(D3, LOW);
  } else if ( (byte)payload[0]=='5'){
     digitalWrite(D3, HIGH);
  }
   else if( (byte)payload[0]=='6'){
    digitalWrite(D4, LOW);
  } else if ( (byte)payload[0]=='7'){
     digitalWrite(D4, HIGH);
  } else if ((byte)payload[0] == '8') {
                             digitalWrite(D1, LOW); 
                             digitalWrite(D2, LOW);
                             digitalWrite(D3, LOW);
                             digitalWrite(D4, LOW);
   } 
   else if ((byte)payload[0] == '9') {
                            digitalWrite(D1, HIGH);
                            digitalWrite(D2, HIGH);
                            digitalWrite(D3, HIGH);
                            digitalWrite(D4, HIGH);
   } 
   else if ((char)payload[0] == 'A'){
    ESP.restart();}
  
  
  /* Serial.print(message);
  if(message == "#on") {digitalWrite(LED,LOW);}   //LED on  
  if(message == "#off") {digitalWrite(LED,HIGH);} //LED off
  Serial.println();
  Serial.println("-----------------------");  
 */

}
 
void loop() {
  client.loop();
}
