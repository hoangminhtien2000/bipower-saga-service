package com.biplus.saga.service.impl;


import com.biplus.core.config.ActionUserHolder;
import com.biplus.core.dto.ActionUserDTO;
import com.biplus.saga.domain.dto.RoleDTO;
import com.biplus.saga.domain.dto.RoleMap;
import com.biplus.saga.service.RedisService;
import com.biplus.saga.common.DataUtil;
import com.fasterxml.jackson.core.type.TypeReference;
import io.eventuate.javaclient.commonimpl.JSonMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Service
@Slf4j
public class RedisServiceImpl implements RedisService {
    @Autowired
    private RedisTemplate redisTemplate;
    private ValueOperations<String, String> valueOps;
    private static final long EXPIRE = 30;

    @PostConstruct
    private void init() {
        valueOps = redisTemplate.opsForValue();
    }

    @Override
    public <T> T getValue(String key, Class<T> classOutput) {
        if (DataUtil.nullOrEmpty(key)) {
            return null;
        }
        String value = valueOps.get(key);
        if (DataUtil.nullOrEmpty(value)) {
            return null;
        }
        return JSonMapper.fromJson(value, classOutput);
    }

    @Override
    public RoleMap getRoleMapByStaffCode(String staffCode) {
        String value = valueOps.get("roleMap-" + staffCode.toUpperCase());
        if (DataUtil.nullOrEmpty(value))
            return null;

        RoleMap roleMap = new RoleMap();
        roleMap.setValues(JSonMapper.fromJson(value, List.class));
        return roleMap;
    }

    @Override
    public void put(String key, Object value) {
        redisTemplate.opsForValue().set(key, JSonMapper.toJson(value));
        redisTemplate.expire(key, EXPIRE, TimeUnit.DAYS);
    }

    public List<String> getRoles() throws IOException {
        ActionUserDTO user = ActionUserHolder.getActionUser();
        String username = StringUtils.isEmpty(user.getUsername()) ? "" : user.getUsername();
        String key = String.format("userRole-%s", username.toUpperCase());
        String stringRoleValues = (String) redisTemplate.opsForValue().get(key);
        List<RoleDTO> userRoles = DataUtil.jsonToObject(stringRoleValues, new TypeReference<>() {});
        return userRoles.stream().map(RoleDTO::getCode).collect(Collectors.toList());
    }

}
