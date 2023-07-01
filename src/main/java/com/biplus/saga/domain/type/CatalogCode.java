package com.biplus.saga.domain.type;

import java.util.Arrays;

public enum CatalogCode implements IType<String> {
    INVALID(""),
    APPLY_POSITION("APPLY_POSITION"),
    TECHNOLOGY("TECHNOLOGY"),
    GENDER("GENDER"),
    CANDIDATE_STATUS("CANDIDATE_STATUS"),
    CANDIDATE_SOURCE("CANDIDATE_SOURCE"),
    CANDIDATE_CONTACT("CANDIDATE_CONTACT"),
    CANDIDATE_LEVEL("CANDIDATE_LEVEL"),
    CANDIDATE_DOMAIN("CANDIDATE_DOMAIN"),
    CANDIDATE_CERTIFICATE("CANDIDATE_CERTIFICATE"),
    CANDIDATE_LITERACY("CANDIDATE_LITERACY"),
    CANDIDATE_LITERACY_ENGLISH("CANDIDATE_LITERACY_ENGLISH"),
    CANDIDATE_SCHOOL("CANDIDATE_SCHOOL"),
    CANDIDATE_MAJORS("CANDIDATE_MAJORS"),
    CANDIDATE_CONTACT_STATUS("CANDIDATE_CONTACT_STATUS"),
    CANDIDATE_STATUS_AFTER_CONTACT("CANDIDATE_STATUS_AFTER_CONTACT"),
    CANDIDATE_RESPONSE("CANDIDATE_RESPONSE"),
    TEAM_TYPE("TEAM_TYPE"),
    TEAM_AUDIT_ACTION("TEAM_AUDIT_ACTION"),
    TEAM_AUDIT_DETAIL_COLUMN("TEAM_AUDIT_DETAIL_COLUMN"),
    ;
    private String value;

    CatalogCode(String value) {
        this.value = value;
    }

    @Override
    public String key() {
        return name();
    }

    @Override
    public String value() {
        return value;
    }

    @Override
    public String label() {
        return name();
    }

    public static String toValue(CatalogCode t) {
        return isValid(t) ? t.value() : INVALID.value();
    }

    public static CatalogCode nvl(CatalogCode t) {
        return t == null ? INVALID : t;
    }

    public static CatalogCode fromKey(String key) {
        if (key == null) {
            return INVALID;
        }
        String k = key.trim();
        if (k.isEmpty()) {
            return INVALID;
        }
        return Arrays.stream(CatalogCode.values()).filter(e -> k.equalsIgnoreCase(e.key())).findFirst().orElseGet(() -> INVALID);
    }

    public static CatalogCode fromValue(String value) {
        if (value == null) {
            return INVALID;
        }
        String v = value.trim();
        if (v.isEmpty()) {
            return INVALID;
        }
        return Arrays.stream(CatalogCode.values()).filter(e -> v.equalsIgnoreCase(e.value())).findFirst().orElseGet(() -> INVALID);
    }

    public static boolean isValid(CatalogCode t) {
        return !isInvalid(t);
    }

    public static boolean isInvalid(CatalogCode t) {
        return t == null || INVALID.equals(t);
    }

    public static boolean equals(CatalogCode a, CatalogCode b) {
        return nvl(a).equals(nvl(b));
    }
}