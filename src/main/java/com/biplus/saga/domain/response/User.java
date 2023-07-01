package com.biplus.saga.domain.response;

import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class User {
    private Long userId;

    private String username;

    private String userCode;

    private String phone;

    private String email;

    private String fullName;
}
