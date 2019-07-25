
 #include <SoftwareSerial.h>
 /*SoftwareSerial mySerial(8, 9);
*/void setup()
{
  Serial.begin(9600);
  pinMode(D1,OUTPUT);
  pinMode(D2,OUTPUT);
  pinMode(D3,OUTPUT);
  pinMode(D4,OUTPUT);
  digitalWrite(D1,HIGH);
  digitalWrite(D2,HIGH);
  digitalWrite(D3,HIGH);
  digitalWrite(D4,HIGH);
}
 
void loop()
{
  if (Serial.available())
  {
    int value = Serial.read();
    if      (value == '0') {digitalWrite(D1, LOW);
    }
    else if (value == '1') {digitalWrite(D1, HIGH);
    }
    else if (value == '2') {digitalWrite(D2, LOW);
    }
    else if (value == '3') {digitalWrite(D2, HIGH);
  }
    else if (value == '4') {digitalWrite(D3, LOW);
} 
    else if (value == '5') {digitalWrite(D3, HIGH);
 }   
    else if (value == '6') {digitalWrite(D4, LOW);
  }  
    else if (value == '7') {digitalWrite(D4, HIGH);
   }
    
   else if (value == '8') {
                             digitalWrite(D1, LOW); 
                             digitalWrite(D2, LOW);
                             digitalWrite(D3, LOW);
                             digitalWrite(D4, LOW);
   } 
   else if (value == '9') {
                            digitalWrite(D1, HIGH);
                            digitalWrite(D2, HIGH);
                            digitalWrite(D3, HIGH);
                            digitalWrite(D4, HIGH);
   } 
   else if (value == 'A'){
    ESP.restart();}
  }
}
