package com.biplus.saga.domain.dto;

import com.biplus.core.dto.AbstractBaseDTO;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class UserDTO extends AbstractBaseDTO {

    @JsonProperty("user_id")
    private Long userId;

    @JsonProperty("username")
    private String username;

    @JsonProperty("phone")
    private String phone;

    @JsonProperty("email")
    private String email;

    @JsonProperty("full_name")
    private String fullName;

    @JsonProperty("changed_password")
    private boolean changedPassword;

    @JsonProperty("changed_password_time")
    private LocalDateTime changedPasswordTime;

    @JsonProperty("last_login_time")
    private LocalDateTime lastLoginTime;

    @JsonProperty("deleted")
    private boolean deleted;
}
