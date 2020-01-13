package promo_creation.utils;

import org.jboss.aerogear.security.otp.Totp;

public class OtpUtil {
    private String otpKeyStr;

    public OtpUtil(String otpKeyStr) {
    }

    public static String getOtpCode(String otpKeyStr) {
        Totp tOtp = new Totp(otpKeyStr);
        return tOtp.now();
    }
}
