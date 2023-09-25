package com.samsung.chatting.controllers;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ChatController {

    @GetMapping("/chat")
    public String showBoard(Model model) {
        model.addAttribute("result", "test");
        return "chat";
    }
}
