package com.biplus.saga.domain.type;

import java.util.Arrays;

public enum CandidateStatus implements IType<String> {
    INVALID(""),
    R0("R0"),
    R1("R1"),
    R2("R2"),
    R3("R3"),
    R4("R4"),
    R4_1("R4.1"),
    R4_2("R4.2"),
    R5_0("R5.0"),
    R5("R5"),
    R5_1("R5.1"),
    R5_2("R5.2"),
    R6("R6"),
    R6_1("R6.1"),
    R7("R7"),
    R7_1("R7.1"),
    R8("R8"),
    R8_1("R8.1"),
    R9("R9"),
    R9_1("R9.1"),
    ;
    private String status;

    CandidateStatus(String status) {
        this.status = status;
    }

    @Override
    public String key() {
        return name();
    }

    @Override
    public String value() {
        return status;
    }

    @Override
    public String label() {
        return name();
    }

    public static String toValue(CandidateStatus t) {
        return isValid(t) ? t.value() : INVALID.value();
    }

    public static CandidateStatus nvl(CandidateStatus t) {
        return t == null ? INVALID : t;
    }

    public static CandidateStatus fromKey(String key) {
        if (key == null) {
            return INVALID;
        }
        String k = key.trim();
        if (k.isEmpty()) {
            return INVALID;
        }
        return Arrays.stream(CandidateStatus.values()).filter(e -> k.equalsIgnoreCase(e.key())).findFirst().orElseGet(() -> INVALID);
    }

    public static CandidateStatus fromValue(String value) {
        if (value == null) {
            return INVALID;
        }
        String v = value.trim();
        if (v.isEmpty()) {
            return INVALID;
        }
        return Arrays.stream(CandidateStatus.values()).filter(e -> v.equalsIgnoreCase(e.value())).findFirst().orElseGet(() -> INVALID);
    }

    public static boolean isValid(CandidateStatus t) {
        return !isInvalid(t);
    }

    public static boolean isInvalid(CandidateStatus t) {
        return t == null || INVALID.equals(t);
    }

    public static boolean equals(CandidateStatus a, CandidateStatus b) {
        return nvl(a).equals(nvl(b));
    }
}