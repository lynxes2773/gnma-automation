package com.ampcus.myginniemae.sfpdm.config;

public class PortalAccessConfiguration {
	
	public final static String GECKO_DRIVER_SYSTEM_PROPERTY = "webdriver.gecko.driver";
	public final static String GECKO_DRIVER_PATH = "C:\\Users\\rohitlal\\Documents\\Tools\\geckodriver\\geckodriver.exe";
	
	public final static String IE_DRIVER_SYSTEM_PROPERTY = "webdriver.ie.driver";
	public final static String IE_DRIVER_PATH = "C:\\Users\\rohitlal\\Documents\\Tools\\iedriver\\IEDriverServer.exe";
	
	public final static String GINNIEMAE_URL = "https://myuat.ginniemae.gov";
	public final static String LOGIN_LINK_ID = "T:j_id__ctru15pc14";
	
	public final static String LOGIN_USERNAME_FIELD_ID = "username";
	public final static String LOGIN_PASSWORD_FIELD_ID = "password";
	public final static String LOGIN_SUBMIT_BUTTON = "submit";
	
	public final static String PORTAL_USER_NAME = "george.hunt@ampcusinc.com";
	public final static String PORTAL_PASSWORD = "Tester@123";
	
	public final static String WEBMAIL_URL = "https://webmail.ampcusinc.com";
	public final static String WEBMAIL_PROTOCOL_KEY = "mail.store.protocol";
	public final static String WEBMAIL_PROTOCOL_VAL = "imaps";
	public final static String WEBMAIL_HOSTNAME = "webmail.ampcusinc.com";
	public final static String WEBMAIL_LOGIN_USERNAME_FIELD_ID = "user";
	public final static String WEBMAIL_LOGIN_PASSWORD_FIELD_ID = "pass";
	public final static String WEBMAIL_LOGIN_SUBMIT_BUTTON = "login_submit";
	public final static String WEBMAIL_USER_NAME = "george.hunt@ampcusinc.com";
	public final static String WEBMAIL_PASSWORD = "Conf_14900!";
	public final static String WEBMAIL_INBOX_FOLDER = "INBOX";
	
	public final static String OTP_OPTION_EMAIL = "Email";
	public final static String OTP_OK_BUTTON_XPATH = "/html/body/div/div[3]/div[2]/div[2]/form/div[1]/p/input";
	public static final String OTP_EMAIL_SUBJECT = "Ginnie Mae OTP Code";
	public final static int OTP_NUMBER_BEGIN_POS = 29;
	public final static int OTP_NUMBER_END_POS = 37;
	
	public static final String OTP_INPUT_FIELD_ID = "passcode";
	public static final String OTP_FORM_SUBMIT_BUTTON_CLASSNAME = "formButton";
	
	public PortalAccessConfiguration() {
		System.out.println("Configuration created.");
	}
	
	

}
