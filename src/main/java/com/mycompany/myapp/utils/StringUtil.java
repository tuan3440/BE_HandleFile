package com.mycompany.myapp.utils;

import java.util.UUID;
import java.util.function.IntPredicate;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.apache.commons.lang3.StringUtils;

public class StringUtil extends StringUtils {

    public static final String SPECIAL_CHAR_REGEX = "[^a-z0-9 ]";

    public static String generateString() {
        return UUID.randomUUID().toString();
    }

    public static boolean hasLowerCase(String value) {
        return !value.equals(value.toUpperCase());
    }

    public static boolean hasUpperCase(String value) {
        return !value.equals(value.toLowerCase());
    }

    public static boolean notHasLowerCase(String value) {
        return !hasLowerCase(value);
    }

    public static boolean notHasUpperCase(String value) {
        return !hasUpperCase(value);
    }

    public static boolean hasNumber(String value) {
        return has(value, Character::isDigit);
    }

    public static boolean onlyHasNumber(String value) {
        return onlyHas(value, Character::isDigit);
    }

    public static boolean notHasNumber(String value) {
        return notHas(value, Character::isDigit);
    }

    public static boolean hasSymbol(String value) {
        Pattern pattern = Pattern.compile(SPECIAL_CHAR_REGEX, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(value);
        return matcher.find();
    }

    public static boolean notHasSymbol(String value) {
        return !hasSymbol(value);
    }

    public static boolean has(String value, IntPredicate predicate) {
        return value.chars().anyMatch(predicate);
    }

    public static boolean onlyHas(String value, IntPredicate predicate) {
        return value.chars().allMatch(predicate);
    }

    public static boolean notHas(String value, IntPredicate predicate) {
        return value.chars().noneMatch(predicate);
    }
}
