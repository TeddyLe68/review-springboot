package com.teddy.youtuberef.web.ws;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

@Controller
@MessageMapping("/chat")
public class ChatActivity {
    /**
     * Hàm xử lý tin nhắn gửi đến từ client, sau đó gửi lại cho tất cả client đã subscribe vào topic "/chat/lobby/{lobbyId}/message/data"
     * @param message
     * @return
     */
    @MessageMapping("/lobby/{lobbyId}/message")
    @SendTo("/chat/lobby/{lobbyId}/message/data")
    public String message(
            @Payload String message
    ){
        return message;
    }
}
