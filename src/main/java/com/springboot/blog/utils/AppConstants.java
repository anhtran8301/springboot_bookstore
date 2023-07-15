package com.springboot.blog.utils;

public class AppConstants {
    public static final String DEFAULT_PAGE_NUMBER = "0";
    public static final String DEFAULT_PAGE_SIZE = "10";
    public static final String DEFAULT_SORT_BY = "id";
    public static final String DEFAULT_SORT_DIRECTION = "asc";
    public static final String REGEX_PASSWORD = "^(?=.*\\d)(?=.*[A-Z])(?=.*[a-z])(?=.*[^\\w\\d\\s:])([^\\s]){8,16}$";



//    public static final String LANGUAGE_VI = "vi";
//    public static final String LANGUAGE_EN = "en";

    public static final String REGEX_PASSWORD_MESSAGE = "Password must be 8-16 characters with no space, " +
            "including at least 1 uppercase letter, " +
            "1 lowercase letter, " +
            "1 number (0-9), " +
            "1 non-alpha numeric number";
}
