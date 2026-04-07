package com.teddy.youtuberef.web.rest.error;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.Getter;
import org.apache.logging.log4j.util.Strings;

@Getter
public class BusinessException extends RuntimeException{
    private final String errorCode;
    private final String message;

    /**
     * Constructor for BusinessException with error code and message.
     * @param msg
     */
    public BusinessException(String msg){
        super(msg);
        this.errorCode = Strings.EMPTY;
        this.message = msg;
    }

    /**
     * Constructor for BusinessException with error code, message, and cause.
     * @param errorCode
     * @param msg
     * @param ex
     */
    public BusinessException(String errorCode, String msg, Throwable ex){
        super(msg, ex);
        this.errorCode = errorCode;
        this.message = msg;
    }

    /**
     * Constructor for BusinessException with error code and message.
     * @param errorCode
     * @param msg
     */
    public BusinessException(String errorCode, String msg){
        super(msg);
        this.errorCode = errorCode;
        this.message = msg;
    }
}
