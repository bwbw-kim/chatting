package com.samsung.chatting.controllers;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class ChatController {

    @GetMapping("/chat")
    public String showChat(@RequestParam int id, Model model) {
        if (id < 1 || id > 5) return "room";
        return "chat";
    }
}
