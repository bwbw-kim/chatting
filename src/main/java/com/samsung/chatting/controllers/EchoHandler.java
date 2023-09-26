package com.samsung.chatting.controllers;

import org.springframework.web.socket.WebSocketSession;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import java.util.List;
import java.util.HashMap;
import java.util.Map;
import java.io.IOException;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

import org.springframework.web.socket.handler.TextWebSocketHandler;

@RequestMapping("/echo")
@Component
public class EchoHandler extends TextWebSocketHandler {

	private List<WebSocketSession> sessions = new ArrayList<WebSocketSession>();
	private Map<String, List<WebSocketSession>> roomSessionMap = new HashMap<String, List<WebSocketSession>>();
	private Map<WebSocketSession, String> sessionRoomMap = new HashMap<WebSocketSession, String>();
	
	@Override
	public void afterConnectionEstablished(WebSocketSession session) throws Exception {
		sessions.add(session);
	}
	
	@Override
	protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
		if (message.getPayload().startsWith("id:")) {
			handleRoomId(session, message);
		} else if (message.getPayload().startsWith("msg:")) {
			handleMsg(session, message);
		}
	}

	private void handleRoomId(WebSocketSession session, TextMessage message) throws IOException {
		String roomId = message.getPayload().substring(3);
		sessionRoomMap.put(session, roomId);
		if(!roomSessionMap.containsKey(roomId)) roomSessionMap.put(roomId, new ArrayList<WebSocketSession>());
		roomSessionMap.get(roomId).add(session);
		for (WebSocketSession single : roomSessionMap.get(roomId)) {
			TextMessage newMessage = new TextMessage("<div class=\"center\">A new person has joined</div>");
			single.sendMessage(newMessage);
		}
	}

	private void handleMsg (WebSocketSession session, TextMessage message) throws IOException {
		LocalTime now = LocalTime.now();
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH시 mm분 ss초");
        String formatedNow = now.format(formatter);
		String leftMsg = "<div class=\"msg left-msg\">" + //
				"                <div" + //
				"                 class=\"msg-img\"" + //
				"                ></div>" + //
				"                <div class=\"msg-bubble\">" + //
				"                  <div class=\"msg-info\">" + //
				"                    <div class=\"msg-info-name\">%s</div>" + //
				"                    <div class=\"msg-info-time\">%s</div>" + //
				"                  </div>" + //
				"                  <div class=\"msg-text\">" + //
				"                    %s" + //
				"                  </div>" + //
				"                </div>" + //
				"              </div>";
		String rightMsg = "<div class=\"msg right-msg\">" + //
				"                <div" + //
				"                 class=\"msg-img\"" + //
				"                ></div>" + //
				"                <div class=\"msg-bubble\">" + //
				"                  <div class=\"msg-info\">" + //
				"                    <div class=\"msg-info-name\">%s</div>" + //
				"                    <div class=\"msg-info-time\">%s</div>" + //
				"                  </div>" + //
				"                  <div class=\"msg-text\">" + //
				"                    %s" + //
				"                  </div>" + //
				"                </div>" + //
				"              </div>";
		String roomId = sessionRoomMap.get(session);
		for(WebSocketSession single : roomSessionMap.get(roomId)) {
			if(session.getId().equals(single.getId())){
				TextMessage newMessage = new TextMessage(String.format(rightMsg, "Me", formatedNow, message.getPayload().substring(4)));
				single.sendMessage(newMessage);
			}
			else {
				TextMessage newMessage = new TextMessage(String.format(leftMsg, single.getId(), formatedNow, message.getPayload().substring(4)));
				single.sendMessage(newMessage);
			}
		}
	}
	
	@Override
	public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
		sessions.remove(session);
		String roomId = sessionRoomMap.remove(session);
		roomSessionMap.get(roomId).remove(session);
		for (WebSocketSession single : roomSessionMap.get(roomId)) {
			TextMessage newMessage = new TextMessage("<div class=\"center\">Someone left</div>");
			single.sendMessage(newMessage);
		}
	}
}
