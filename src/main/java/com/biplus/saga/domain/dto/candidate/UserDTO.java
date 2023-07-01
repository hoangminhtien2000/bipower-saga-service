package com.biplus.saga.domain.dto.candidate;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class UserDTO {

    @JsonProperty("user_id")
    @JsonAlias({"userId", "user_id"})
    private Long userId;
    @JsonProperty("user_code")
    @JsonAlias({"userCode", "user_code"})
    private String userCode;
    @JsonProperty("username")
    private String username;

    @JsonProperty("phone")
    private String phone;

    @JsonProperty("email")
    private String email;

    @JsonProperty("full_name")
    @JsonAlias({"fullName", "full_name"})
    private String fullName;

    @JsonProperty("changed_password")
    @JsonAlias({"changedPassword", "changed_password"})
    private boolean changedPassword;

    @JsonProperty("changed_password_time")
    private LocalDateTime changedPasswordTime;

    @JsonProperty("last_login_time")
    private LocalDateTime lastLoginTime;

    @JsonProperty("deleted")
    private boolean deleted;

    @JsonProperty("roles")
    List<RoleDTO> roles;
}
