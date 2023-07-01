package com.biplus.saga.configuration;

import com.biplus.core.config.ActionUserHolder;
import com.biplus.core.dto.ActionUserDTO;
import com.biplus.saga.common.LocalVariable;
import io.eventuate.javaclient.commonimpl.JSonMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.token.DefaultAccessTokenConverter;
import org.springframework.stereotype.Component;

import java.util.Map;

@Slf4j
@Component
public class CustomAccessTokenConverter extends DefaultAccessTokenConverter {

    @Override
    public OAuth2Authentication extractAuthentication(Map<String, ?> claims) {
        OAuth2Authentication authentication = super.extractAuthentication(claims);
        var actionUserDTO = JSonMapper.objectMapper.convertValue(claims.get("actionUser"), ActionUserDTO.class);
        ActionUserHolder.setActionUser(actionUserDTO);
        return authentication;
    }
    @Override
    public OAuth2AccessToken extractAccessToken(String value, Map<String, ?> map) {
        OAuth2AccessToken token = super.extractAccessToken(value, map);
        LocalVariable.setToken(value);
        return token;
    }

}
