
 #include <SoftwareSerial.h>
 #include <ESP8266WiFi.h>
#include <PubSubClient.h>
  
//Enter your wifi credentials
const char* ssid = "ESP8266";  
const char* password =  "123456789";
 
//Enter your mqtt server configurations
const char* mqttServer = "postman.cloudmqtt.com";    //Enter Your mqttServer address
const int mqttPort = 12072;       //Port number
const char* mqttUser = "zvibyzxz"; //User
const char* mqttPassword = "YHJqD8Vx7DeB"; //Password
 
WiFiClient espClient;
PubSubClient client(espClient);
  void callback(char* topic, byte* payload, unsigned int length);
void setup()
{const int D1 = 5;
 const int D2 = 4;
 const int D3 = 0;
 const int D4 = 2;
  
  pinMode(D1,OUTPUT);
  pinMode(D2,OUTPUT);
  pinMode(D3,OUTPUT);
  pinMode(D4,OUTPUT);
  digitalWrite(D1,HIGH);
  digitalWrite(D2,HIGH);
  digitalWrite(D3,HIGH);
  digitalWrite(D4,HIGH);

  Serial.begin(9600);
  WiFi.begin(ssid, password);
 
  while (WiFi.status() != WL_CONNECTED) 
  {
    delay(100);
    Serial.println("Connecting to WiFi..");
    delay(2000);
    break;
  }
  Serial.print("Connected to WiFi :");
  Serial.println(WiFi.SSID());
 
  client.setServer(mqttServer, mqttPort);
  client.setCallback(callback);
 
  while (!client.connected()) {
    Serial.println("Connecting to MQTT...");
  
 
    if (client.connect("ESP8266", mqttUser, mqttPassword )) {
 
      Serial.println("connected");  
 
    } else {
 
      Serial.print("failed with state ");
      Serial.println(client.state());  //If you get state 5: mismatch in configuration
      delay(2000);
      break;
 
    }
    
  }
 
  client.publish("Status", "Hello from ESP8266");
  client.publish("Place","****");
  client.subscribe("LED");
 
}

 


  void callback(char* topic,byte* payload, unsigned int length)
  { char msgBuffer[20];
  Serial.print("Message arrived [");
  Serial.print(topic);
  Serial.print("] ");//MQTT_BROKER
  for (int i = 0; i < length; i++) {
   // message = message + (char)payload[i];
  Serial.print((char)payload[i]);
  }
   Serial.println();
 Serial.println(payload[0]);
 switch((char)payload[0])
    {
      case '0': {
    
                            digitalWrite(D1, LOW);
                            break;
    }
    case '1': {
                            digitalWrite(D1, HIGH);
                            break;
    }
    case '2': {
                            digitalWrite(D2, LOW);
                            break;
    }
    case '3': {
                            digitalWrite(D2, HIGH);
                            break;
  }
    case '4': {
                            digitalWrite(D3, LOW);
                            break;
} 
    case '5': {
                             digitalWrite(D3, HIGH);
                             break;
 }   
    case '6': {
                             digitalWrite(D4, LOW);
                             break;
  }  
    case '7': {
                             digitalWrite(D4, HIGH);
                             break;
    }
    
   case '8': {
                             digitalWrite(D1, LOW); 
                             digitalWrite(D2, LOW);
                             digitalWrite(D3, LOW);
                             digitalWrite(D4, LOW);
                             break;
   } 
   case '9': {
                            digitalWrite(D1, HIGH);
                            digitalWrite(D2, HIGH);
                            digitalWrite(D3, HIGH);
                            digitalWrite(D4, HIGH);
                            break;
   } 
   case 'A':{
    ESP.restart();
    break;
    }
    break; 
    }
    }
  
  void loop()
{
  if (Serial.available()){
  int value = Serial.read();
switch(value){
   case '0': {
                            digitalWrite(D1, LOW);
                            break;
    }
    case '1': {
                            digitalWrite(D1, HIGH);
                            break;
    }
    case '2': {
                            digitalWrite(D2, LOW);
                            break;
    }
    case '3': {
                            digitalWrite(D2, HIGH);
                            break;
  }
    case '4': {
                            digitalWrite(D3, LOW);
                            break;
} 
    case '5': {
                             digitalWrite(D3, HIGH);
                             break;
 }   
    case '6': {
                             digitalWrite(D4, LOW);
                             break;
  }  
    case '7': {
                             digitalWrite(D4, HIGH);
                             break;
    }
    
   case '8': {
                             digitalWrite(D1, LOW); 
                             digitalWrite(D2, LOW);
                             digitalWrite(D3, LOW);
                             digitalWrite(D4, LOW);
                             break;
   } 
   case '9': {
                            digitalWrite(D1, HIGH);
                            digitalWrite(D2, HIGH);
                            digitalWrite(D3, HIGH);
                            digitalWrite(D4, HIGH);
                            break;
   } 
   case 'A':{
    ESP.restart();
    break;
    }
    }
    }
    else {
      client.loop();}
    
    
}
