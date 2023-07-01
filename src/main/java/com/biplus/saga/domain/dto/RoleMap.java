package com.biplus.saga.domain.dto;

import com.google.common.collect.Lists;

import java.io.Serializable;
import java.util.List;

public class RoleMap implements Serializable {

    private List<String> values = Lists.newArrayList();

    public boolean hasRole(String key) {
        return values.contains(key);
    }

    public void add(String role) {
        values.add(role);
    }

    public List<String> getValues() {
        return values;
    }

    public void setValues(List<String> values) {
        this.values = values;
    }
}
