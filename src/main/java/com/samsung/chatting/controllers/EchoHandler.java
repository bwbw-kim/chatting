package com.samsung.chatting.controllers;

import org.springframework.web.socket.WebSocketSession;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import java.util.List;
import java.util.HashMap;
import java.util.Map;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

import org.springframework.web.socket.handler.TextWebSocketHandler;

@RequestMapping("/echo")
@Component
public class EchoHandler extends TextWebSocketHandler {

	private List<WebSocketSession> sessions = new ArrayList<WebSocketSession>();
	private Map<String, WebSocketSession> userSessionsMap = new HashMap<String, WebSocketSession>();
	
	@Override
	public void afterConnectionEstablished(WebSocketSession session) throws Exception {
		sessions.add(session);
	}
	
	@Override
	protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
		LocalTime now = LocalTime.now();
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH시 mm분 ss초");
        String formatedNow = now.format(formatter);
		String leftMsg = "<div class=\"msg left-msg\">\r\n" + //
				"                <div\r\n" + //
				"                 class=\"msg-img\"\r\n" + //
				"                ></div>\r\n" + //
				"          \r\n" + //
				"                <div class=\"msg-bubble\">\r\n" + //
				"                  <div class=\"msg-info\">\r\n" + //
				"                    <div class=\"msg-info-name\">%s</div>\r\n" + //
				"                    <div class=\"msg-info-time\">%s</div>\r\n" + //
				"                  </div>\r\n" + //
				"          \r\n" + //
				"                  <div class=\"msg-text\">\r\n" + //
				"                    %s\r\n" + //
				"                  </div>\r\n" + //
				"                </div>\r\n" + //
				"              </div>";
		String rightMsg = "<div class=\"msg right-msg\">\r\n" + //
				"                <div\r\n" + //
				"                 class=\"msg-img\"\r\n" + //
				"                ></div>\r\n" + //
				"          \r\n" + //
				"                <div class=\"msg-bubble\">\r\n" + //
				"                  <div class=\"msg-info\">\r\n" + //
				"                    <div class=\"msg-info-name\">%s</div>\r\n" + //
				"                    <div class=\"msg-info-time\">%s</div>\r\n" + //
				"                  </div>\r\n" + //
				"          \r\n" + //
				"                  <div class=\"msg-text\">\r\n" + //
				"                    %s\r\n" + //
				"                  </div>\r\n" + //
				"                </div>\r\n" + //
				"              </div>";
		for(WebSocketSession single : sessions) {
			if(session.getId().equals(single.getId())){
				TextMessage newMessage = new TextMessage(String.format(rightMsg, "Me", formatedNow, message.getPayload()));
				single.sendMessage(newMessage);
			}
			else {
				TextMessage newMessage = new TextMessage(String.format(leftMsg, single.getId(), formatedNow, message.getPayload()));
				single.sendMessage(newMessage);
			}
		}
	}
	
	@Override
	public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
		sessions.remove(session);
	}

}
