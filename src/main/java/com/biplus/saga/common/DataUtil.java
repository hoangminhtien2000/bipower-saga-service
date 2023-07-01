package com.biplus.saga.common;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.base.CharMatcher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.*;
import java.util.function.Supplier;
import java.util.regex.Matcher;
import java.util.regex.Pattern;



public class DataUtil {
    private static final Logger logger = LoggerFactory.getLogger(DataUtil.class);

    public static boolean nonEmpty(String text) {
        return !nullOrEmpty(text);
    }

    public static boolean nonEmpty(Collection collection) {
        return !nullOrEmpty(collection);
    }

    public static boolean notNullOrEmpty(String text) {
        return !nullOrEmpty(text);
    }

    public static boolean notNullOrEmpty(Collection collection) {
        return !nullOrEmpty(collection);
    }

    public static boolean nullOrEmpty(Collection objects) {
        return objects == null || objects.isEmpty();
    }

    public static boolean nullOrEmpty(Map<?, ?> map) {
        return map == null || map.isEmpty();
    }

    public static boolean notNull(Object object) {
        return !nullObject(object);
    }

    public static boolean nullObject(Object object) {
        return object == null;
    }

    public static boolean nullOrZero(Long value) {
        return (value == null || value.equals(0L));
    }

    public static boolean nullOrZero(String value) {
        return value == null || "0".equals(value);
    }

    public static boolean nullOrZero(Integer value) {
        return (value == null || value.equals(0));
    }

    public static boolean equalsObj(Object obj1, Object obj2) {
        if (obj1 == null || obj2 == null) return false;
        return obj1.equals(obj2);
    }

    public static Integer parseToInt(String value, Integer defaultVal) {
        try {
            return Integer.parseInt(value);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return defaultVal;
        }
    }

    public static Integer parseToInt(String value) {
        return parseToInt(value, null);
    }

    public static Integer parseToInt(Object value) {
        return parseToInt(parseToString(value), null);
    }

    public static boolean isNullOrZero(Long value) {
        return (value == null || value.equals(0L));
    }

    public static Long parseToLong(Object value, Long defaultVal) {
        try {
            String str = parseToString(value);
            if (nullOrEmpty(str)) {
                return null;
            }
            return Long.parseLong(str);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return defaultVal;
        }
    }

    public static Double parseToDouble(Object value) {
        return parseToDouble(value, null);
    }

    public static Double parseToDouble(Object value, Double defaultVal) {
        try {
            String str = parseToString(value);
            if (nullOrEmpty(str)) {
                return null;
            }
            return Double.parseDouble(str);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return defaultVal;
        }
    }

    public static Long parseToLong(Object value) {
        return parseToLong(value, null);
    }


    public static String parseToString(Object value, String defaultVal) {
        try {
            return String.valueOf(value);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return defaultVal;
        }
    }

    public static String parseToString(Object value) {
        return parseToString(value, "");
    }

    public static boolean matchByPattern(String value, String regex) {
        if (nullOrEmpty(regex) || nullOrEmpty(value)) {
            return false;
        }
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(value);
        return matcher.matches();
    }

    public static void throwIfFailed(boolean test, String message) throws Exception {
        if (!test) throw new Exception(message);
    }

    public static <X extends Throwable> void throwIfFailed(boolean test, Supplier<? extends X> exceptionSupplier) throws X {
        if (!test) throw exceptionSupplier.get();
    }

    public static void throwIf(boolean test, String message) throws Exception {
        if (test) throw new Exception(message);
    }

    public static void assertTrue(boolean test, String message) throws Exception {
        if (!test) throw new IllegalArgumentException(message);
    }


    public static <X extends Throwable> void throwIf(boolean test, Supplier<? extends X> exceptionSupplier) throws X {
        if (test) throw exceptionSupplier.get();
    }

    public static boolean nullOrEmpty(CharSequence cs) {
        int strLen;
        if (cs == null || (strLen = cs.length()) == 0) {
            return true;
        }
        for (int i = 0; i < strLen; i++) {
            if (!Character.isWhitespace(cs.charAt(i))) {
                return false;
            }
        }
        return true;
    }

    public static boolean isNullOrEmpty(CharSequence cs) {
        return nullOrEmpty(cs);
    }

    public static boolean isNullOrEmpty(final Collection<?> collection) {
        return collection == null || collection.isEmpty();
    }

    public static <T> T deepCloneObject(T source) {
        try {
            if (source == null) {
                return null;
            }
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            ObjectOutputStream out = new ObjectOutputStream(bos);
            out.writeObject(source);
            out.flush();
            out.close();

            ObjectInputStream in = new ObjectInputStream(
                    new ByteArrayInputStream(bos.toByteArray()));
            T dto = (T) in.readObject();
            in.close();
            return dto;
        } catch (IOException | ClassNotFoundException e) {
            logger.error(e.getMessage(), e);
            return null;
        }
    }

    public static String objectToJson(Object object) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        if (object == null) {
            return null;
        }
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        return mapper.writeValueAsString(object);
    }

    public static <T> T jsonToObject(String json, Class<T> classOutput) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        if (json == null || json.isEmpty()) {
            return null;
        }
        return mapper.readValue(json, classOutput);
    }

    private static boolean safeEqualString(String str1, String str2, boolean ignoreCase, boolean trimspace) {
        if (str1 == null || str2 == null) {
            return false;
        }

        if (trimspace) {
            str1 = str1.trim();
            str2 = str2.trim();
        }

        if (ignoreCase) {
            return str1.equalsIgnoreCase(str2);
        } else {
            return str1.equals(str2);
        }
    }

    public static boolean safeEqualIgnoreCaseString(String str1, String str2) {
        return safeEqualString(str1, str2, true, true);
    }

    public static boolean safeEqualString(String str1, String str2) {
        return safeEqualString(str1, str2, false, true);
    }

    public static boolean safeEqualIgnoreCaseWithoutTrimSpaceString(String str1, String str2) {
        return safeEqualString(str1, str2, true, false);
    }

    public static boolean safeEqualWithoutTrimSpaceString(String str1, String str2) {
        return safeEqualString(str1, str2, false, false);
    }


    public static List<String> split(String separate, String object) {
        return Optional.ofNullable(object)
                .map(x -> x.split(separate))
                .map(Arrays::asList)
                .orElseGet(ArrayList::new);
    }

    public static String firstNonEmpty(String... strings) {
        for (String string : strings) {
            if (!isNullOrEmpty(string)) {
                return string;
            }
        }
        return "";
    }

    public static <T> T defaultIfNull(final T object, final T defaultValue) {
        return object != null ? object : defaultValue;
    }

    /**
     * Tra ve doi tuong default neu object la null, neu khong thi tra object
     *
     * @param object
     * @param defaultValueSupplier
     * @param <T>
     * @return
     */
    public static <T> T defaultIfNull(final T object, final Supplier<T> defaultValueSupplier) {
        return object != null ? object : defaultValueSupplier.get();
    }

    public static boolean safeEqual(Object obj1, Object obj2) {
        return ((obj1 != null) && (obj2 != null) && obj2.toString().equals(obj1.toString()));
    }

//    public static String convertNumberUsingCurrentLocale(Number number) {
//        if (number == null) {
//            return "0";
//        }
//        DecimalFormat decimalFormat = new DecimalFormat(LanguageBundleUtil.getText("common.currency.format"));
//        DecimalFormatSymbols symbols = decimalFormat.getDecimalFormatSymbols();
//        symbols.setGroupingSeparator(DataUtil.safeToCharacter(LanguageBundleUtil.getText("common.currency.thousandSeparator")));
//        symbols.setDecimalSeparator(DataUtil.safeToCharacter(LanguageBundleUtil.getText("common.currency.decimalSeparator")));
//        decimalFormat.setDecimalFormatSymbols(symbols);
//        return decimalFormat.format(number);
//    }

    public static Character safeToCharacter(Object value) {
        return safeToCharacter(value, '0');
    }

    public static Character safeToCharacter(Object value, Character defaulValue) {
        if (value == null) return defaulValue;
        return String.valueOf(value).charAt(0);
    }

    public static String removeStartingZeroes(String number) {
        if (DataUtil.nullOrEmpty(number)) {
            return "";
        }
        return CharMatcher.anyOf("0").trimLeadingFrom(number);
    }

    public static String plusZeroByLength(int length) {
        String strZero = "";
        for (int i = 0; i < length; i++) {
            strZero += "0";
        }
        return strZero;
    }


//    /**
//     * NamLX : Convert tien thanh chuoi string
//     *
//     * @param money
//     * @return
//     */
//    public static String speakMoney(Long money) {
//        String moneyString = expressMoney(money);
//        String khong = getText("toword.zero");
//        String mot = getText("toword.one");
//        String hai = getText("toword.two");
//        String ba = getText("toword.three");
//        String bon = getText("toword.four");
//        String nam = getText("toword.five");
//        String sau = getText("toword.six");
//        String bay = getText("toword.seven");
//        String tam = getText("toword.eight");
//        String chin = getText("toword.nine");
//
//        String[] moneyStringArr = moneyString.split(" ");
//        if (moneyStringArr[0].equals("0")) {
//            moneyString = moneyStringArr[2];
//            for (int i = 3; i < moneyStringArr.length; i++) {
//                moneyString += " " + moneyStringArr[i];
//            }
//        }
//        moneyString = moneyString.replaceAll("0", khong);
//        moneyString = moneyString.replaceAll("1", mot);
//        moneyString = moneyString.replaceAll("2", hai);
//        moneyString = moneyString.replaceAll("3", ba);
//        moneyString = moneyString.replaceAll("4", bon);
//        moneyString = moneyString.replaceAll("5", nam);
//        moneyString = moneyString.replaceAll("6", sau);
//        moneyString = moneyString.replaceAll("7", bay);
//        moneyString = moneyString.replaceAll("8", tam);
//        moneyString = moneyString.replaceAll("9", chin);
//        moneyString += " " + getText("money.unit.vnd") + " ";
//
//        moneyString = toUpperCharacters(moneyString, 0, 1);
//        moneyString = moneyString.replaceAll("  ", " ");
//
//        moneyString = moneyString.replaceAll(getText("toword.fifteen.wrong"), getText("toword.fifteen"));
//
//        return moneyString;
//    }

    public static String toUpperCharacters(String inputString, int startIndex, int numberOfCharacter) {
        if (startIndex < 0) {
            startIndex = 0;
        }
        int endIndex = startIndex + numberOfCharacter;
        if (endIndex > inputString.length()) {
            endIndex = inputString.length() - 1;
        }
        String returnString = "";
        String toUpperString = inputString.substring(startIndex, endIndex);
        toUpperString = toUpperString.toUpperCase();
        returnString = inputString.substring(0, startIndex) + toUpperString + inputString.substring(endIndex);
        return returnString;
    }

//    private static String expressMoney(Long money) {
//        String moneyString = "";
//        Long billion = 1000000000L;
//        Long million = 1000000L;
//        Long thousand = 1000L;
//
//        Long billionNo = money / billion;
//        money = money - billionNo * billion;
//        Long millionNo = money / million;
//        money = money - millionNo * million;
//        Long thousandNo = money / thousand;
//        money = money - thousandNo * thousand;
//
//        if (billionNo != null && billionNo != 0L) {
//            moneyString += expressMoney(billionNo) + " " + getText("toword.billion") + " ";
//
//        }
//        if (millionNo != null && millionNo != 0L) {
//            moneyString += expressDetailMoney(millionNo) + " " + getText("toword.million") + " ";
//        }
//        if (thousandNo != null && thousandNo != 0L) {
//            moneyString += expressDetailMoney(thousandNo) + " " + getText("toword.thousand") + " ";
//        }
//        if (money != 0L) {
//            moneyString += expressDetailMoney(money);
//        }
//        return moneyString;
//    }

//    private static String expressDetailMoney(Long money) {
//        String moneyString = "";
//        Long hundred = 100L;
//        Long ten = 10L;
//        Long hundredNo = money / hundred;
//        money = money - hundredNo * hundred;
//        Long tenNo = money / ten;
//        money = money - tenNo * ten;
//        if (hundredNo != null) {
//            moneyString += hundredNo.toString() + " " + getText("toword.hundred") + " ";
//        }
//        if (tenNo != null && tenNo != 0L) {
//            if (tenNo == 1L) {
//                moneyString += getText("toword.ten") + " ";
//            } else {
//                moneyString += tenNo.toString() + " " + getText("toword.tens") + " ";
//            }
//        } else if (tenNo == 0L && hundredNo != 0 && money != 0L) {
//            moneyString += " " + getText("toword.donvi") + " ";
//        }
//        if (money != 0L) {
//            moneyString += money.toString();
//        }
//        return moneyString;
//    }

    public static long parseStringToLong(String strValue) {
        long result = 0;
        try {
            NumberFormat numberFormat = NumberFormat.getInstance();
            result = numberFormat.parse(strValue.trim()).longValue();
        } catch (ParseException e) {
            logger.error(e.getMessage(), e);
        }
        return result;
    }

    public static String readFromInputStream(InputStream inputStream)
            throws IOException {
        StringBuilder resultStringBuilder = new StringBuilder();
        try (BufferedReader br
                     = new BufferedReader(new InputStreamReader(inputStream))) {
            String line;
            while ((line = br.readLine()) != null) {
                resultStringBuilder.append(line).append("\n");
            }
        }
        return resultStringBuilder.toString();
    }

    public static boolean isNull(Long value) {
        return (value == null);
    }
    public static boolean isNull(Object value) {
        return (value == null);
    }
    public static <T> T jsonToObject(String input, TypeReference<T> typeReference) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        return mapper.readValue(input, typeReference);
    }
}
