package com.ampcus.myginniemae.sfpdm.helper;

import java.util.Iterator;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.Wait;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.ampcus.myginniemae.sfpdm.config.TestConfiguration;

public class PageUtilities {

	private RemoteWebDriver driver;
	public static final String ELEMENT_TYPE_ANCHOR = "a";
	public static final String ELEMENT_TYPE_BUTTON = "button";
	
	public static final String QUALIFIER_TYPE_CLASSNAME = "class";
	public static final String QUALIFIER_TYPE_TEXT = "text";
	public static final String QUALIFIER_TYPE_TEXT_VALUE_POOLID = TestConfiguration.POOL_ID;
	
	public static final String QUALIFIER_TYPE_VALUE_SAVE = "save";
	public static final String QUALIFIER_TYPE_VALUE_VALIDATE = "validate";
	
	public PageUtilities(RemoteWebDriver driver) {
		this.driver=driver; 
	}
	
	public WebElement findElement(String elementType, String qualifierType, String qualifierValue)
	{
		WebElement element = null;
		
		List<WebElement> allButtons = driver.findElements(By.tagName(elementType));
		Iterator<WebElement> i = allButtons.iterator();

        whileLoop: while(i.hasNext()) 
        {
        	element = i.next();
          
        	if(qualifierType.equals(this.QUALIFIER_TYPE_CLASSNAME))
        	{
        		if(element.getAttribute(this.QUALIFIER_TYPE_CLASSNAME).equals(qualifierValue))
        		{
        			break whileLoop;
        		}
        	}
        	if(qualifierType.equals(this.QUALIFIER_TYPE_TEXT))
        	{
        		if(element.getText().equals(qualifierValue))
        		{
        			break whileLoop;
        		}
        	}
        }
		return element;
	}
	
	public void waitForPageLoaded(WebDriver driver) 
	{
		 ExpectedCondition<Boolean> expectation = new ExpectedCondition<Boolean>() 
		 {
		    public Boolean apply(WebDriver driver) 
		    {
		      return ((JavascriptExecutor)driver).executeScript("return document.readyState").equals("complete");
		    }
		 };

		 Wait<WebDriver> wait = new WebDriverWait(driver,30);
		 try 
		 {
		          wait.until(expectation);
		 } 
		 catch(Throwable error) 
		 {}
	}


}
