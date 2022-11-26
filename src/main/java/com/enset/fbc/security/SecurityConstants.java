package com.enset.fbc.security;

public class SecurityConstants {
    public static final long EXPIRATION_TIME = 15*60*1000; // 15 MIN
    public static final long EXPIRATION_TIME_RefrechToken = 864000000; // 10 Days
    public static final String TOKEN_PREFIX = "Bearer ";
    public static final String HEADER_STRING = "Authorization";
    public static final String SIGN_UP_URL = "/users";
    public static final String TOKEN_SECRET = "mySecret";

}
