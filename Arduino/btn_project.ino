int sw1 = 12;         
int sw1_reverse = 7;  // reverse는 카운트 -
int led1 = 10;
int led1_reverse = 9;
int sw2 = 5;
int sw2_reverse = 6; 
int led2 = 3;
int led2_reverse = 2;

int count_male = 0;
int count_female = 0;

void setup() 
{
  pinMode(led1, OUTPUT);
  pinMode(led2, OUTPUT);
  pinMode(led1_reverse, OUTPUT);
  pinMode(led2_reverse, OUTPUT);
  pinMode(sw1, INPUT_PULLUP);
  pinMode(sw2, INPUT_PULLUP);
  pinMode(sw1_reverse, INPUT_PULLUP);
  pinMode(sw2_reverse, INPUT_PULLUP);
  Serial.begin(9600);
}

void loop()
{
  if(digitalRead(sw1) == LOW) // 남자 + 스위치 눌렀을 경우
  {
    digitalWrite(led1, HIGH); // led On
    count_male++;             // 카운트 +1
    Serial.print("Male : ");
    Serial.println(count_male);
    delay(200);
    
  }
  else
  {
    digitalWrite(led1, LOW);
  }
  
  /////////////////////////////////////////
  
  if(digitalRead(sw2) == LOW) // 여자 + 스위치 눌렀을 경우
  {
    digitalWrite(led2, HIGH); // led On
    count_female++;           // 카운트 +1
    Serial.print("Female : ");
    Serial.println(count_female);
    delay(200);
     
  }
  else
  {
    digitalWrite(led2, LOW);
  }

  //////////////////////////////////////////

  if(digitalRead(sw1_reverse) == LOW)
  {
    
    digitalWrite(led1_reverse, HIGH);
    count_male--;
    
    if(count_male <= -1)    // 카운트가 음수로 넘어갈 경우 다시 0으로 초기화
    {
        count_male = 0;
    }
    
    Serial.print("Male : ");
    Serial.println(count_male);
    delay(200);
    
  }  
  
  else
  {
    digitalWrite(led1_reverse, LOW);
  }

  ///////////////////////////////////////////

  if(digitalRead(sw2_reverse) == LOW)

  {
    
    digitalWrite(led2_reverse, HIGH); 
    count_female--;
    
    if(count_female <= -1)  // 카운트가 음수로 넘어갈 경우 다시 0으로 초기화
    {
        count_female = 0;
    }
    
    Serial.print("Female : ");
    Serial.println(count_female);
    delay(200);
  }
  
  else
  {
    digitalWrite(led2_reverse, LOW);
  }
}