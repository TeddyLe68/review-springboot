package com.teddy.youtuberef.common;

import com.teddy.youtuberef.web.rest.error.MessageCode;
import lombok.NoArgsConstructor;

@NoArgsConstructor
public final class AppConstant {
    public static final MessageCode SERVICE_ERROR = new MessageCode("500","Service Error");
    public static final MessageCode BAD_REQUEST = new MessageCode("400","Bad Request");
}
