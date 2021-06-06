package dev.tho.easyfile.config;


public class SecurityConstants {
    
    public static final String JWT_SECRET = System.getenv("JWT_KEY");
    public static final long EXPIRATION_TIME = 864_000_000; 
    public static final String TOKEN_PREFIX = "Bearer ";
    public static final String HEADER_STRING = "Authorization";
    public static final String SIGN_UP_URL = "/bucket";


}