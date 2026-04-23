package com.teddy.youtuberef.config;

import com.teddy.youtuberef.config.filter.HttpSessionHandshakeInterceptor;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;
import org.springframework.web.socket.server.support.DefaultHandshakeHandler;

import java.security.Principal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

@Configuration
@RequiredArgsConstructor
@EnableWebSocketMessageBroker
public class WebSocketConfiguration implements WebSocketMessageBrokerConfigurer {
    private final HttpSessionHandshakeInterceptor httpSessionHandshakeInterceptor;


    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
        WebSocketMessageBrokerConfigurer.super.configureMessageBroker(registry);
        registry.enableSimpleBroker("/chat");
    }

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        WebSocketMessageBrokerConfigurer.super.registerStompEndpoints(registry);
        registry.addEndpoint("/youtuberef/ws").setAllowedOrigins("*");
        registry.addEndpoint("youtuberef/ws")
                .setHandshakeHandler(defaultHandshakeHandler())
                .setAllowedOriginPatterns("*")
                .withSockJS()
                .setInterceptors(httpSessionHandshakeInterceptor);
    }

    /**
     * Hàm này được sử dụng để xác định người dùng (Principal) trong quá trình handshake của WebSocket.
     * Nếu người dùng đã đăng nhập, nó sẽ trả về Principal của người dùng đó.
     * Nếu không, nó sẽ tạo một AnonymousAuthenticationToken với vai trò "ANONYMOUS" và trả về token đó như là Principal.
     * @return
     */
    private DefaultHandshakeHandler defaultHandshakeHandler() {
        return new DefaultHandshakeHandler() {
           protected Principal determineUser(HttpServletRequest request, WebSocketHandler wsHandler, Map<String, Object> attributes){
                Principal principal = request.getUserPrincipal();
                if(principal == null){
                   Collection<SimpleGrantedAuthority> authorities = new ArrayList<>();
                   authorities.add(new SimpleGrantedAuthority("ANONYMOUS"));
                   principal = new AnonymousAuthenticationToken("WebsocketConfiguration", "anonymous", authorities);
               }
               return principal;
           }
        };
    }
}
