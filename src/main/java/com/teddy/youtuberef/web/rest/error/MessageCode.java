package com.teddy.youtuberef.web.rest.error;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;

@Getter
@Setter
@ToString
@JsonPropertyOrder({"code", "message"})
public class MessageCode implements Serializable {
    protected String code;
    protected String message;
    public MessageCode(String code, String message) {
        this.code = code;
        this.message = message;
    }
    public MessageCode(){

    }
}
