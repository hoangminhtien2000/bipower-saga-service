package com.biplus.saga.service;

import com.biplus.saga.domain.dto.RoleMap;

import java.io.IOException;
import java.util.List;

public interface RedisService {
    <T> T getValue(String key, Class<T> classOutput);
    RoleMap getRoleMapByStaffCode(String staffCode);
    void put(String key, Object value);

    public List<String> getRoles() throws IOException;
}
