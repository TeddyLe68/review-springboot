package com.teddy.youtuberef.config.filter;

import org.jspecify.annotations.Nullable;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.HandshakeInterceptor;

import java.util.Map;

/**
 * Hàm này được sử dụng để chặn quá trình bắt tay (handshake) của WebSocket và thực hiện một số thao tác trước và sau khi bắt tay diễn ra.
 * Cụ thể, trong phương thức beforeHandshake, nó kiểm tra nếu yêu cầu là một ServletServerHttpRequest,
 * thì nó sẽ lấy địa chỉ IP của client và lưu trữ nó vào thuộc tính attributes với khóa "IP_ADDRESS".
 * Phương thức afterHandshake không thực hiện bất kỳ hành động nào sau khi bắt tay hoàn tất.
 */
@Component
public class HttpSessionHandshakeInterceptor implements HandshakeInterceptor {
    private static final String IP_ADDRESS = "IP_ADDRESS";
    @Override
    public boolean beforeHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler, Map<String, Object> attributes) throws Exception {
        if(request instanceof final ServletServerHttpRequest serverHttpRequest){
            attributes.put(IP_ADDRESS, serverHttpRequest.getRemoteAddress());
        }
        return true;
    }

    @Override
    public void afterHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler, @Nullable Exception exception) {

    }
}
