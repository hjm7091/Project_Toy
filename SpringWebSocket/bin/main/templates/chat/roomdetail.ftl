<!doctype html>
<html lang="en">
  <head>
    <title>WebSocket ChatRoom</title>
    <!-- Required meta tags -->
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1, maximum-scale=1, user-scalable=no">

    <!-- Bootstrap CSS -->
    <link rel="stylesheet" href="/webjars/bootstrap/4.3.1/dist/css/bootstrap.min.css">
  </head>
  <body>
    <div class="container" id="app">
        <div class="clearfix">
            <h4 class="float-left mt-1">
            	{{room.name}}
            	<span class="badge badge-info badge-pill">{{userCount}}</span>
            </h4>
            <button v-on:click="outRoom" class="btn btn-info float-right my-2">채팅방 나가기</button>
        </div>
        <div class="input-group">
            <div class="input-group-prepend">
                <label class="input-group-text">내용</label>
            </div>
            <input type="text" class="form-control" v-model="message" v-on:keypress.enter="sendMessage('TALK')">
            <div class="input-group-append">
                <button class="btn btn-primary" type="button" @click="sendMessage('TALK')">보내기</button>
            </div>
        </div>
        <ul class="list-group mb-3">
            <li class="list-group-item" v-for="message in messages">
                {{message.sender}} - {{message.message}}
            </li>
        </ul>
        <div></div>
    </div>
    <!-- JavaScript -->
    <script src="/webjars/vue/2.5.16/dist/vue.min.js"></script>
    <script src="/webjars/axios/0.17.1/dist/axios.min.js"></script>
    <script src="/webjars/sockjs-client/1.1.2/sockjs.min.js"></script>
    <script src="/webjars/stomp-websocket/2.3.3-1/stomp.min.js"></script>
    <script>

    	// websocket & stomp initialize
        var sock = new SockJS("/ws-stomp");
        var ws = Stomp.over(sock);
        var reconnect = 0;
        
        // vue.js
        var vm = new Vue({
            el: '#app',
            data: {
                roomId: '',
                room: {},
                sender: '',
                message: '',
                messages: [],
                userCount: 0
            },
            created() {
                //페이지를 떠날 때 발생하는 이벤트 등록
                window.addEventListener('beforeunload', this.leaveRoom);
                this.roomId = localStorage.getItem('wschat.roomId');
                this.sender = localStorage.getItem('wschat.sender');

                //채팅방에서 새로고침시 목록으로 돌아가도록 유도
                if(this.roomId == null) {
					alert("에러 발생!! 채팅방 목록으로 돌아갑니다.");
					location.href = "/chat/room";
                }
                
                this.findRoom();
            },
            methods: {
                findRoom: function() {
                    axios.get('/chat/room/' + this.roomId).then(response => { 
                        this.room = response.data;
                        this.userCount = this.room.userCount; 
                    });
                },
                sendMessage: function(type) {
                    ws.send("/pub/chat/message", {}, JSON.stringify({type:type, roomId:this.roomId, sender:this.sender, message:this.message}));
                    this.message = '';
                },
                recvMessage: function(recv) {
                    this.userCount = recv.userCount;
                    this.messages.unshift({"type":recv.type, "sender":recv.sender, "message":recv.message})
                },
                outRoom: function() {
					location.href = "/chat/room";
                },
                leaveRoom: function() { //페이지를 떠날 때 항상 호출됨
                	localStorage.removeItem('wschat.sender');
                    localStorage.removeItem('wschat.roomId');
                    ws.disconnect(function(frame) {}, { "sender" : this.sender })
                }
            }
        });

        //5번 webSocket 연결 시도, 그 후에는 채팅방 목록으로 돌아감 
        function connect() {
            // pub/sub event
            ws.connect({}, function(frame) {
                ws.subscribe("/sub/chat/room/"+vm.$data.roomId, function(message) {
                    var recv = JSON.parse(message.body);
                    vm.recvMessage(recv);
                }, { "sender" : vm.sender });
            }, function(error) {
                if(reconnect++ <= 5) {
                    setTimeout(function() {
                        console.log("connection reconnect");
                        sock = new SockJS("/ws-stomp");
                        ws = Stomp.over(sock);
                        connect();
                    },10*1000);
                }
                alert("서버 연결에 실패 하였습니다. 다시 접속해 주십시요.");
                location.href = "/chat/room";
            });
        }
        connect();
    </script>
  </body>
</html>