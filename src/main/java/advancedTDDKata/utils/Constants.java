package advancedTDDKata.utils;

public class Constants {

    public static final String MSG_ERROR_NO_PARAM_KEY = "Parameters should not be empty";
    public static final String MSG_ERROR_EMPTY_USEREMAIL = "this user is already registered";
    public static final String MSG_ERROR_EMPTY_USEREmail ="email should not be empty";
    public static final String MSG_ERROR_EMPTY_USER_FIRSTNAME ="firstname should not be empty";
    public static final String MSG_ERROR_EMPTY_USER_LASTNAME ="email should not be empty";
    public static final String MSG_INVALID_EMAIL = "email is not valid" ;
    public static final String MSG_VERIFICATION_SUCCESS = "successfull verivication";
    public static final String MSG_VERIFICATION_FAILURE = "verification issue";
    public static final String EMAIL_REGEX =
            "(?:[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*|\"(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21\\x23-\\x5b\\x5d-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])*\")@(orange.com)";
    private Constants() {
    }


}
