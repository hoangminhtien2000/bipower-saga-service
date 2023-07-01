package com.biplus.saga.domain.dto.candidate;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class RoleDTO {

    @JsonProperty("role_id")
    private Integer roleId;

    @JsonProperty("code")
    private String code;

    @JsonProperty("name")
    private String name;

    @JsonProperty("note")
    private String note;

    @JsonProperty("deleted")
    private boolean deleted;
}
