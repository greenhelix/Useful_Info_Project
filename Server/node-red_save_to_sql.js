//json.parse로 json 형식의 데이터를 분석하여 sql에 저장할 수 있는 
//변수로 해석한다.
var info = JSON.parse(msg.payload);
var data = info.data; //남녀 / 입출 내용
var ID = info.id; //매장 ID
var date = info.date; //버튼을 누른 날짜
var time = info.time; //버튼을 누른 시간

//위 변수에 따라 올바른 값을 sql에 저장한다.
if(data == "min") {
    msg.topic = "INSERT INTO statistics_map (marketID, dates, times, sex, inorout) VALUES ("+ID+", '"+date+"', '"+time+"', 'm', 1)";
}
else if(data =="mout"){
    msg.topic = "INSERT INTO statistics_map (marketID, dates, times, sex, inorout) VALUES ("+ID+", '"+date+"', '"+time+"', 'm', -1)";
}
else if (data =="win"){
    msg.topic = "INSERT INTO statistics_map (marketID, dates, times, sex, inorout) VALUES ("+ID+", '"+date+"', '"+time+"', 'f', 1)";
}
else if(data == "wout"){
    msg.topic = "INSERT INTO statistics_map (marketID, dates, times, sex, inorout) VALUES ("+ID+", '"+date+"', '"+time+"', 'f', -1)";
}
else{
    return;
}

return msg;
