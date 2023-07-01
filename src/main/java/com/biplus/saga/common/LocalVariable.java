package com.biplus.saga.common;

import com.biplus.core.dto.ActionUserDTO;
import lombok.Getter;

import java.time.LocalDateTime;

public class LocalVariable {
    @Getter
    private static final ThreadLocal<ActionUserDTO> actionUser = new ThreadLocal<>();
    @Getter
    private static final ThreadLocal<LocalDateTime> sysDate = new ThreadLocal<>();
    @Getter
    private static final ThreadLocal<String> token = new ThreadLocal<>();

    public static void setActionUser(ActionUserDTO actionUser) {
        LocalVariable.actionUser.set(actionUser);
    }

    public static void setSysDate(LocalDateTime sysDate) {
        LocalVariable.sysDate.set(sysDate);
    }

    public static void setToken(String token) {
        LocalVariable.token.set(token);
    }
}
