package com.example.edu.Util;

public class PasswordEncoder {
    private static final String PASSWORD_ENCODE_KEY = "jizhanyanshou147";


    public static String encode(CharSequence rawPassword) {
        return AESCode.encrypt((String) rawPassword, PASSWORD_ENCODE_KEY);
    }
    public static String decode(CharSequence rawPassword) {
        return AESCode.decrypt((String) rawPassword, PASSWORD_ENCODE_KEY);
    }

    public static boolean matches(CharSequence rawPassword, String encodedPassword) {
        return AESCode.encrypt((String)rawPassword).equals(encodedPassword);
    }
}
