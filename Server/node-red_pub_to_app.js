//앱에서 publish한 값이 매장 ID라고 가정하고 이 값을 input이라는 
//변수에 저장한다. 
//이 매장 ID에 맞는 값을 sql에서 탐색해 남녀 비율을 반환한다.

var input = msg.payload;

msg.topic = "SELECT men, women, ratio FROM manage WHERE marketID = "+input+"";

return msg;