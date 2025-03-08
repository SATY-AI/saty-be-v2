package com.ims.IMS.lib.utils;


import org.apache.commons.lang3.StringUtils;
import org.springframework.util.CollectionUtils;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

public final class AppStringUtils {
    private static final String DEFAULT_MASKING_CHARACTER;
    private static final int DEFAULT_SHOW_CHARACTER;

    private AppStringUtils() {
    }

    public static List<String> masking(List<String> values, int characterShow, String... maskingCharacter) {
        return values.stream().map((v) -> {
            return maskingCharacter(v, characterShow, maskingCharacter);
        }).toList();
    }

    public static List<String> maskingObjects(Collection<?> values) {
        return maskingObjects(values, DEFAULT_SHOW_CHARACTER);
    }

    public static List<String> maskingObjects(Collection<?> values, int characterShow, String... maskingCharacter) {
        return values.stream().map((v) -> {
            return maskingCharacter(String.valueOf(v), characterShow, maskingCharacter);
        }).toList();
    }

    public static String maskingCharacter(String value) {
        return maskingCharacter(value, DEFAULT_SHOW_CHARACTER);
    }

    public static String maskingCharacter(Object value) {
        return maskingCharacter(String.valueOf(value), DEFAULT_SHOW_CHARACTER);
    }

    public static String maskingCharacter(String value, int characterShow, String... maskingCharacter) {
        String result = "";
        if (StringUtils.isBlank(value)) {
            return result;
        } else {
            String makingChar = CollectionUtils.isEmpty(Arrays.asList(maskingCharacter)) ? DEFAULT_MASKING_CHARACTER : maskingCharacter[0];
            result = value;
            if (value.length() > characterShow) {
                int beginIndexShowCharacter = value.length() - characterShow;
                result = StringUtils.leftPad(value.substring(beginIndexShowCharacter), value.length(), makingChar);
            }

            return result;
        }
    }

    static {
        String defaultMaskingCharacter = System.getenv("DEFAULT_MASKING_CHARACTER");
        String defaultShowCharacter = System.getenv("DEFAULT_SHOW_CHARACTER");
        DEFAULT_MASKING_CHARACTER = StringUtils.isBlank(defaultMaskingCharacter) ? "*" : defaultMaskingCharacter;
        DEFAULT_SHOW_CHARACTER = StringUtils.isBlank(defaultShowCharacter) ? 5 : Integer.parseInt(defaultShowCharacter);
    }
}
