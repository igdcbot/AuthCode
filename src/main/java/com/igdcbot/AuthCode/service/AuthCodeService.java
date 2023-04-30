package com.igdcbot.AuthCode.service;

import com.igdcbot.AuthCode.constants.CommonConstants;
import com.igdcbot.AuthCode.entity.request.OauthAuthorizeRequest;
import com.igdcbot.AuthCode.entity.response.OauthAuthorizeResponse;
import com.microsoft.playwright.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.regex.Pattern;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;

@Service
public class AuthCodeService {

    @Value("${tester.username}")
    private String testerUsername;

    @Value("${tester.password}")
    private String testerPassword;

    public OauthAuthorizeResponse getOauthAuthorize(OauthAuthorizeRequest oar) {
        try (Playwright playwright = Playwright.create())  {

            String resource_url = "oauth/authorize";
            MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
            params.add("client_id", oar.getClientId());
            params.add("redirect_uri", oar.getRedirectUri());
            params.add("scope", String.join(",", oar.getScope()));
            params.add("response_type", "code");

            String request_url = UriComponentsBuilder.fromUriString(CommonConstants.API_URL + resource_url)
                    .queryParams(params).build().toString();

            BrowserType chromium = playwright.chromium();
            Browser browser = chromium.launch(new BrowserType.LaunchOptions().setHeadless(false).setChannel("chrome"));
            BrowserContext bc = browser.newContext();
            Page page = bc.newPage();
            page.navigate(request_url);
            page.setDefaultTimeout(60000);

            page.mainFrame().waitForSelector(CommonConstants.IG_LANGUAGE_DROPDOWN_CSS_SELECTOR);
            Locator locate_lang_dropdown = page.locator(CommonConstants.IG_LANGUAGE_DROPDOWN_CSS_SELECTOR);
            if (locate_lang_dropdown.count() != 0) {
                if (locate_lang_dropdown.inputValue() != "en") {
                    locate_lang_dropdown.selectOption("en");
                }
            }

            page.mainFrame().waitForSelector(CommonConstants.IG_LOGIN_USERNAME_CSS_SELECTOR);
            page.mainFrame().waitForSelector(CommonConstants.IG_LOGIN_PASSWORD_CSS_SELECTOR);
            page.mainFrame().waitForSelector(CommonConstants.IG_LOGIN_SUBMIT_CSS_SELECTOR);
            if (page.locator(CommonConstants.IG_LOGIN_USERNAME_CSS_SELECTOR).count() != 0 && page.locator(CommonConstants.IG_LOGIN_PASSWORD_CSS_SELECTOR).count() != 0) {

                page.locator(CommonConstants.IG_LOGIN_USERNAME_CSS_SELECTOR).type(testerUsername);
                page.locator(CommonConstants.IG_LOGIN_PASSWORD_CSS_SELECTOR).type(testerPassword);
                page.locator(CommonConstants.IG_LOGIN_SUBMIT_CSS_SELECTOR).click();

                page.mainFrame().waitForSelector(CommonConstants.IG_LOGIN_SAVE_INFO_CSS_SELECTOR);
                if (page.locator(CommonConstants.IG_LOGIN_SAVE_INFO_CSS_SELECTOR).count() != 0) {
                    page.locator(CommonConstants.IG_LOGIN_SAVE_INFO_CSS_SELECTOR).click();
                }
            }

            page.mainFrame().waitForSelector(CommonConstants.IG_ALLOW_APP_CONNECT_CSS_SELECTOR);
            if (page.locator(CommonConstants.IG_ALLOW_APP_CONNECT_CSS_SELECTOR).count() != 0) {
                page.locator(CommonConstants.IG_ALLOW_APP_CONNECT_CSS_SELECTOR).click();
            }

            assertThat(page).hasURL(Pattern.compile(".+\\?code=.+"));

            String code = page.url().split("\\?code=")[1];

            OauthAuthorizeResponse response = new OauthAuthorizeResponse();
            response.setCode(code);

            return response;

        } catch (Exception ex) {
            throw ex;
        }
    }

}
