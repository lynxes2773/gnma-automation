package com.ampcus.myginniemae.sfpdm;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import com.ampcus.myginniemae.sfpdm.helper.WaitTimes;
import com.ampcus.myginniemae.sfpdm.config.PortalAccessConfiguration;
import com.ampcus.myginniemae.sfpdm.config.TestConfiguration;

import javax.mail.BodyPart;
import javax.mail.Flags;
import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Store;
import javax.mail.internet.MimeMultipart;
import javax.mail.search.FlagTerm;

import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.time.LocalDateTime;    

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

/**
 * @author Rohit
 * This is the class that with main() method dtd 04/30/2020
 */
public class PortalAccessRunner {

	private PortalAccessConfiguration commonConfig;
	private TestConfiguration tstConfig;
	private RemoteWebDriver driver;
	private String otp = null;
	private Properties sysProps;
	private Class<?> clazz;
		
	public static void main(String[] args)
	{
		PortalAccessRunner runner = new PortalAccessRunner();
		boolean portalAccessed = runner.run();
		boolean poolCreated = runner.addPool();
		if(poolCreated)
		{
			boolean testCompleted = runner.runTest();
			System.out.println("Test Completed: "+testCompleted);
			//boolean poolDeleted = runner.deletePool();
		}
	}
	
	public boolean deletePool()
	{
		DeletePoolRunner deleteRunner = new DeletePoolRunner(driver);
		return deleteRunner.run();
	}
	
	public boolean addPool()
	{
		AddPoolRunner addRunner = new AddPoolRunner(driver);
		return addRunner.run();
	}
	
	public boolean runTest()
	{
		TestRunner test = new TestRunner(driver);
		return test.run();
	}
	
	public PortalAccessRunner() {
		
		System.setProperty(PortalAccessConfiguration.GECKO_DRIVER_SYSTEM_PROPERTY, PortalAccessConfiguration.GECKO_DRIVER_PATH);
		
		try
		{
			clazz = Class.forName(TestConfiguration.FIREFOX_BROWSER_DRIVER_CLASSNAME);
			Constructor<?> ctor = clazz.getConstructor();
			driver = (RemoteWebDriver)ctor.newInstance();
		}
		catch(Exception e)
		{
			System.setProperty(PortalAccessConfiguration.GECKO_DRIVER_SYSTEM_PROPERTY, PortalAccessConfiguration.GECKO_DRIVER_PATH);
			driver = new FirefoxDriver();
			e.printStackTrace();
		}
		commonConfig = new PortalAccessConfiguration();
		
		driver.get(PortalAccessConfiguration.GINNIEMAE_URL);
		driver.manage().window().maximize();
		System.out.println("Window opened...");
	}
	
	public boolean run()
	{
		boolean accessSuccessful = false;
		boolean loginSuccessful = this.loginRoutine();
		boolean otpSuccessful = false;
		if(loginSuccessful)
		{
			otpSuccessful = this.otpRoutine();
			if(otpSuccessful)
			{
				System.out.println("Logged onto Ginnie Mae Dashboard");
			}
			else
			{
				System.out.println("OTP process timed out");
			}
		}
		else
		{
			System.out.println("Login unsuccessful");
		}
		if(loginSuccessful && otpSuccessful)
		{
			accessSuccessful = true;
		}
		return accessSuccessful;
	}
	
	private boolean loginRoutine()
	{
		System.out.println("Now logging in...");
		boolean loginSuccessful = false;
		
		WebElement loginLink = driver.findElementById(PortalAccessConfiguration.LOGIN_LINK_ID);
		loginLink.click();
		
		WebElement userNameField = driver.findElementById(PortalAccessConfiguration.LOGIN_USERNAME_FIELD_ID);
		WebElement passwordField = driver.findElementById(PortalAccessConfiguration.LOGIN_PASSWORD_FIELD_ID);
		WebElement submitButton = driver.findElementByClassName(PortalAccessConfiguration.LOGIN_SUBMIT_BUTTON);
		
		userNameField.sendKeys(PortalAccessConfiguration.PORTAL_USER_NAME);
		passwordField.sendKeys(PortalAccessConfiguration.PORTAL_PASSWORD);
		submitButton.click();
		
		WebElement otpByEmailOption = driver.findElementById(PortalAccessConfiguration.OTP_OPTION_EMAIL);
		if(otpByEmailOption!=null)
		{
			loginSuccessful = true;
			System.out.println("Login credentials validated....");
		}
		System.out.println("Login result: "+loginSuccessful);
		
		return loginSuccessful;
	}
	
	private boolean otpRoutine()
	{
		boolean otpRetrievalSuccessful = false;
		this.setExistingEmailstoRead();
		
		System.out.println("Now accessing email to wait for OTP...");
		
		WebDriverWait wait = new WebDriverWait(driver, 30, 100);
		
//		WebElement otpByEmailOption = wait.until(ExpectedConditions.elementToBeClickable(By.id(PortalAccessConfiguration.OTP_OPTION_EMAIL)));
//		otpByEmailOption.click();
//		WebElement okButton = wait.until(ExpectedConditions.elementToBeClickable(By.xpath(PortalAccessConfiguration.OTP_OK_BUTTON_XPATH)));
//		okButton.click();
   	    Date otpTime = new Date(System.currentTimeMillis() - (2 * 3600 * 1000));  
   	    
		sysProps = System.getProperties();
		sysProps.setProperty(PortalAccessConfiguration.WEBMAIL_PROTOCOL_KEY, PortalAccessConfiguration.WEBMAIL_PROTOCOL_VAL);
		
		try
		{
			Session session = Session.getInstance(sysProps, null);
	        Store store = session.getStore();
	        store.connect(PortalAccessConfiguration.WEBMAIL_HOSTNAME, PortalAccessConfiguration.WEBMAIL_USER_NAME, PortalAccessConfiguration.WEBMAIL_PASSWORD);
	        Folder emailInbox = store.getFolder(PortalAccessConfiguration.WEBMAIL_INBOX_FOLDER);
	        emailInbox.open(Folder.READ_WRITE);
	        Flags seen = new Flags(Flags.Flag.SEEN);
	        FlagTerm unseenFlagTerm = new FlagTerm(seen, false);
        
	        int unreadMessagesCount = 0;
	        Message latestMessage = null;
	        Message messages[] = null;
	        while(unreadMessagesCount<1)
	        {
		        messages = emailInbox.search(unseenFlagTerm);	        
		        unreadMessagesCount = messages.length;
	        }
	        
	        Date sentDate = null;
	        Message emailMessage = null;
	        String subject = null;
	        String content = null;
	        for(int j=0;j<messages.length;j++)
	        {
    	        emailMessage = messages[j];
	        	sentDate = emailMessage.getSentDate();
	        	subject = emailMessage.getSubject();
	        	if(sentDate.after(otpTime) && subject.equals(PortalAccessConfiguration.OTP_EMAIL_SUBJECT))
	        	{
	        		content = emailMessage.getContent().toString();
	    	        System.out.println(content);
	    	        otp = content.substring(PortalAccessConfiguration.OTP_NUMBER_BEGIN_POS, PortalAccessConfiguration.OTP_NUMBER_END_POS);
	    	        System.out.println(otp);
	        	}
	        }
	        
		    emailInbox.close(true);
	        store.close();

	        WebElement otpInputField = driver.findElementById(PortalAccessConfiguration.OTP_INPUT_FIELD_ID);
			otpInputField.sendKeys(otp);
			
			WebElement otpSubmitBtn = driver.findElementByClassName(PortalAccessConfiguration.OTP_FORM_SUBMIT_BUTTON_CLASSNAME);
			otpSubmitBtn.click();
			otpRetrievalSuccessful = true;
			System.out.println("OTP retreival successful....");
		}
	    catch(Exception e)
		{
			e.printStackTrace();
		}
		
		
		return otpRetrievalSuccessful;
	}
	

	private void setExistingEmailstoRead()
	{
		try
		{
			Session session = Session.getInstance(sysProps, null);
	        Store store = session.getStore();
	        store.connect(PortalAccessConfiguration.WEBMAIL_HOSTNAME, PortalAccessConfiguration.WEBMAIL_USER_NAME, PortalAccessConfiguration.WEBMAIL_PASSWORD);
	        Folder emailInbox = store.getFolder(PortalAccessConfiguration.WEBMAIL_INBOX_FOLDER);
	        emailInbox.open(Folder.READ_WRITE);
        
	        Message messages[] = null;
	        messages = emailInbox.getMessages();
	        
	        Message emailMessage = null;
	        String content = null;
	        for(int j=0;j<messages.length;j++)
	        {
    	        emailMessage = messages[j];
        		content = emailMessage.getContent().toString();
	        }
		    emailInbox.close(true);
	        store.close();
	        
		}
		catch(Exception e)
		{}
	}
	
	private void openWebMailTab()
	{
		String portalTabTitle = driver.getTitle();
		Actions action = new Actions(driver);
		action.keyDown(Keys.CONTROL).sendKeys("t");
		
		JavascriptExecutor jse = (JavascriptExecutor)driver;
		jse.executeScript("window.open()");
		ArrayList<String> tabs = new ArrayList<String> (driver.getWindowHandles());
		
		driver.switchTo().window(tabs.get(1));
		driver.get(PortalAccessConfiguration.WEBMAIL_URL);
		String webmailTabTitle = driver.getTitle();

		WebElement userNameField = driver.findElementById(PortalAccessConfiguration.WEBMAIL_LOGIN_USERNAME_FIELD_ID);
		WebElement passwordField = driver.findElementById(PortalAccessConfiguration.WEBMAIL_LOGIN_PASSWORD_FIELD_ID);
		WebElement submitButton = driver.findElementById(PortalAccessConfiguration.WEBMAIL_LOGIN_SUBMIT_BUTTON);
		
		userNameField.sendKeys(PortalAccessConfiguration.WEBMAIL_USER_NAME);
		passwordField.sendKeys(PortalAccessConfiguration.WEBMAIL_PASSWORD);
		submitButton.click();
	}

}
