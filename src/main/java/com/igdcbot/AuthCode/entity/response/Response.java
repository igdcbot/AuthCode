package com.igdcbot.AuthCode.entity.response;

import lombok.*;

@Data
public class Response<T> {

    private String status;
    private String message;
    private T data;

}
