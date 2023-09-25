
$(document).ready(function () {
    $("#msg-button").click(function () {
        sendMessage()
        $('#msg-input').val('');
    });
});

var sock = new SockJS("/echo");
sock.onmessage = onMessage;
sock.onclose = onClose;

function sendMessage() {
    sock.send($("#msg-input").val());
}
function onMessage(msg) {
    var data = msg.data;
    $(".msger-chat").append(data + "<br/>");
    $('.msger-chat').scrollTop($('.msger-chat').scrollTop() + 500);
}
function onClose(evt) {
    $(".msger-chat").append("<div>Connection Lost.</div>");
}



