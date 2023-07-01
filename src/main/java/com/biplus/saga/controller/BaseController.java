package com.biplus.saga.controller;

import com.biplus.core.dto.ActionUserDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.jwt.Jwt;
import org.springframework.security.jwt.JwtHelper;
import org.springframework.web.bind.annotation.RestController;

import java.util.Locale;

import static com.biplus.saga.common.Constants.BEARER;

@Slf4j
@RestController
@RequiredArgsConstructor
public class BaseController {
    @Value("${spring.mvc.async.request-timeout:60000}")
    private int timeout;

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    public Locale getLocale(String language) {
        String lang = ObjectUtils.defaultIfNull(language, "en-VN");
        String[] strings = lang.split("-");
        return new Locale(strings[0], strings[1]);
    }

    public ActionUserDTO getActionUser(String authorization) {
        try {
            String[] array = null;
            String token;
            if(authorization.contains("," + BEARER)){
                //Truong hop chua 2 chuoi token => Lay token dau tien
                array = authorization.split(",");
                token = array[0];
            }else{
                array = authorization.split(" ");
                if (array.length > 0) {
                    token = array[1];
                } else {
                    token = array[0];
                }
            }

            Jwt jwt = JwtHelper.decode(token);
            JsonNode rootNode = OBJECT_MAPPER.readTree(jwt.getClaims());
            JsonNode node = rootNode.get("actionUser");
            String staffName = node.get("staffName") != null ? node.get("staffName").asText() : null;
            if (staffName == null) {
                staffName = node.get("name") != null ? node.get("name").asText() : null;
            }
            return ActionUserDTO.builder()
                    .userId(node.get("staffId") != null ? node.get("staffId").asLong() : null)
                    .companyCode(node.get("staffCode") != null ? node.get("staffCode").asText() : null)
                    .build();
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return ActionUserDTO.builder().build();
        }
    }
}
