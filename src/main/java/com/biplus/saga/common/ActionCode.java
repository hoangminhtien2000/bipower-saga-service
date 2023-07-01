package com.biplus.saga.common;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

import java.util.Arrays;

import static com.biplus.core.message.MessageBundle.getMessage;

public enum ActionCode {
    CHAN_1C_HEN_GIO("066", "action.block1way.timer"),
    HUY_CHAN_1C_HEN_GIO("088", "action.cancelBlock1way.timer"),
    CHAN_1C_KHYC("06", "action.block1way.customerRequest"),
    CHAN_2C_KHYC("07", "action.block2way.customerRequest"),
    MO_CHAN_1C_KHYC("08", "action.open1way.customerRequest"),
    MO_CHAN_2C_KHYC("09", "action.open2way.customerRequest"),
    THAY_DOI_THONG_TIN_HOP_DONG("91", "action.udpate.agreement"),
    UNKNOWN("-111", "UNKNOWN"),
    Connection("00"),
    ChangeSIM("11"),
    ChangePrePaidToPostPaid("220"),
    ChangePostPaidToPrePaid("221"),
    THAY_DOI_THONG_TIN_KHACH_HANG("152","action.udpate.customer"),
    ChangeLimitMobile("19");
    private String code;
    private String key;

    ActionCode(String code, String key){
        this.code = code;
        this.key = key;
    }

    public String getDescription(){
        return getMessage(key);
    }

    @JsonCreator
    public static ActionCode forCode(String code) {
        return Arrays.stream(values()).filter(actionCode -> actionCode.code.equals(code)).findFirst()
                .orElse(UNKNOWN);
    }

    @JsonValue
    public String getCode() {
        return this.code;
    }

    ActionCode(String code) {
        this.code = code;
    }

    public String value() {
        return this.code;
    }
}
