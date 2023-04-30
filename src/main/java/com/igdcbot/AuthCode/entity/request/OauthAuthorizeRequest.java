package com.igdcbot.AuthCode.entity.request;

import lombok.Data;

@Data
public class OauthAuthorizeRequest {

    private String clientId;
    private String redirectUri;
    private String[] scope;

}
