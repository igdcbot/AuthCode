package com.igdcbot.AuthCode.controller;

import com.igdcbot.AuthCode.entity.request.OauthAuthorizeRequest;
import com.igdcbot.AuthCode.entity.response.OauthAuthorizeResponse;
import com.igdcbot.AuthCode.service.AuthCodeService;
import com.igdcbot.AuthCode.entity.response.Response;
import lombok.SneakyThrows;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api")
public class AuthCodeController {

    private final AuthCodeService acs;

    public AuthCodeController(AuthCodeService acs) {
        this.acs = acs;
    }

    @SneakyThrows
    @PostMapping("/auth-code")
    public Response<OauthAuthorizeResponse> getNewAuthCode(@RequestBody OauthAuthorizeRequest oar) {
        OauthAuthorizeResponse data = this.acs.getOauthAuthorize(oar);
        Response<OauthAuthorizeResponse> res = new Response<>();
        res.setStatus("1");
        res.setMessage("Success");
        res.setData(data);
        return res;
    }

}
