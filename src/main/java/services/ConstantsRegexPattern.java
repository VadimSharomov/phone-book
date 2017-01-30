package services;

/**
 * @author by Vadim Sharomov
 */
public final class ConstantsRegexPattern {
    private static final String PATTERN_LOGIN_REGEXP = "[a-z,A-Z]{3,}";
    private static final String PATTERN_MOBILE_PHONE_UKR_REGEXP = "[+][3][8][0][(][3569][0-9][)][0-9]{7}";
    private static final String PATTERN_STATIONARY_PHONE_UKR_REGEXP = "[+][3][8][0][(][3456][0-9][)][0-9]{7}";
    private static final String PATTERN_EMAIL_REGEXP = "^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$";

    public static String getPatternEmail() {
        return PATTERN_EMAIL_REGEXP;
    }

    public static String getPatternMobilePhoneUkr() {
        return PATTERN_MOBILE_PHONE_UKR_REGEXP;
    }

    public static String getPatternStationaryPhoneUkr() {
        return PATTERN_STATIONARY_PHONE_UKR_REGEXP;
    }

    public static String getPatternLogin() {
        return PATTERN_LOGIN_REGEXP;
    }
}
