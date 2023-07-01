package com.biplus.saga.common;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.i18n.LocaleContextHolder;

import java.text.MessageFormat;
import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

@Slf4j
public abstract class MessageUtils {
    private final static String BASE_NAME = "messages";

    public static String getMessage(String code, Locale locale) {
        return getMessage(code, null, locale);
    }

    public static String getMessage(String code, Object[] args, Locale locale) {
        String message;
        try {
            ResourceBundle resourceBundle = ResourceBundle.getBundle(BASE_NAME, locale);
            message = resourceBundle.getString(code);
        } catch (MissingResourceException ex) {
            log.debug(ex.getMessage(), ex);
            message = code;
        }
        return MessageFormat.format(message, args);
    }

    public static String getMessage(String code) {
        return getMessage(code, null, LocaleContextHolder.getLocale());
    }

    public static String getMessage(String code, Object... args) {
        return getMessage(code, args, LocaleContextHolder.getLocale());
    }
}
