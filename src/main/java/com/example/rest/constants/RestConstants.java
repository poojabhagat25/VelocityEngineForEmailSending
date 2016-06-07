package com.example.rest.constants;

public class RestConstants {

	public static final String AUTHTOKEN = "authToken";
	public static final String SIGN_UP_USER_URL = "/user";
	public static final String LOGIN_URL = "/logIn";
	public static final String FORGOT_PASSWORD = "/forgot_password";
	public static final String AUTHTOKEN_KEYWORD = "authToken= ";
	public static final String FAIL = "fail";
	public static final String USER = "user";

	// Paths
	public static final String REGISTRATION_CONFIRMATION_EMAIL_PATH = "com/example/rest/emailTemplates/RegistrationConfirmationMail.html";
	public static final String FORGOT_PASSWORD_EMAIL_PATH = "com/example/rest/emailTemplates/ForgotPassword.html";

	// Success Messages
	public static final String USER_REGISTERED_SUCCESSFULLY_MESSAGE = "message.user.successfully.registered";
	public static final String USER_LOGGED_IN_SUCCESSFULLY = "message.user.logedin.successfully";
    public static final String PASSWORD_SENT_SUCCESSFULLY = "message.password.sent.successfully";
    public static final String USER_LIST_SENT_SUCCESSFULLY = "message.user.list.sent.successfully";
    
	// Exception Messages
	public static final String NOT_FOUND = "not.found";
	public static final String USER_ALREADY_EXIST_EXCEPTION = "exception.message.user.already.exists";
	public static final String REGISTER_FAILED_EXCEPTION = "exception.message.register.fail";
	public static final String INVALID_USER_TOKEN = "Invalid User Token.";
	public static final String USER_NOT_REGISTERED = "exception.message.phone.not.registered";
	public static final String PASSWORD_MISMATCH = "exception.password.mismatch";
	public static final String LOG_IN_FAILED = "exception.login.failed";

}