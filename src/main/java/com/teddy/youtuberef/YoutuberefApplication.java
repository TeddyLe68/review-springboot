package com.teddy.youtuberef;

import com.teddy.youtuberef.config.properties.SecurityProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties({SecurityProperties.class})
public class YoutuberefApplication {

    public static void main(String[] args) {
        SpringApplication.run(YoutuberefApplication.class, args);
    }

}
