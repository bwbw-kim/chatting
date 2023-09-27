let urlParams = new URLSearchParams(window.location.search);
let id = urlParams.get('id');

$(document).ready(function () {
    $("#msg-button").click(function () {
        sendMessage()
        $('#msg-input').val('');
    });
});

$(document).keyup(function(event) {
    if (event.which === 13) {
        sendMessage()
        $('#msg-input').val('');
    }
});

var sock = new SockJS("/echo");
sock.onmessage = onMessage;
sock.onclose = onClose;
sendRoomId(id)

function sendRoomId(id){
    waitForSocketConnection(sock, function(){
        sock.send("id:" + id);
    });
}

function onMessage(msg) {
    let data = msg.data;
    if (data.startsWith("msgarea:")) {
        $(".msger-chat").append(data.substring(8) + "<br/>");
        $('.msger-chat').scrollTop($('.msger-chat').scrollTop() + 500);
    } else if (data.startsWith("partipipantsarea:")) {
        $("#participants").html(data.substring(17))
    }
    
}
function onClose(evt) {
    $(".msger-chat").append("<div>Connection Lost.</div>");
}

function sendMessage(msg){
    if($("#msg-input").val().trim().length === 0) return;
    sock.send("msg:" + $("#msg-input").val());
}

function waitForSocketConnection(socket, callback){
    setTimeout(
        function () {
            if (socket.readyState === 1) {
                if (callback != null){
                    callback();
                }
            } else {
                console.log("wait for connection...")
                waitForSocketConnection(socket, callback);
            }

        }, 5);
}

