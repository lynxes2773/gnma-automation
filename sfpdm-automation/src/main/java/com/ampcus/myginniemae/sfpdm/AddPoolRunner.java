package com.ampcus.myginniemae.sfpdm;

import java.util.Iterator;
import java.util.List;
import java.util.concurrent.TimeUnit;
import com.google.common.base.Function;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Wait;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.ampcus.myginniemae.sfpdm.config.DashboardPageConfiguration;
import com.ampcus.myginniemae.sfpdm.config.PoolListScreenConfiguration;
import com.ampcus.myginniemae.sfpdm.config.PortalHeaderConfigurations;
import com.ampcus.myginniemae.sfpdm.config.TestConfiguration;
import com.ampcus.myginniemae.sfpdm.helper.WaitTimes;

public class AddPoolRunner {
	
	private RemoteWebDriver driver;
	private boolean poolCreated = false;
	
	private AddPoolRunner() {
	}
	
	public AddPoolRunner(RemoteWebDriver driver)
	{
		this.driver = driver;
	}
	
	public boolean run()
	{
		System.out.println("Now accessing Pools & Loans screen....");

		WebElement poolsAndLoansTab = driver.findElementById(DashboardPageConfiguration.POOLS_AND_LOANS_TAB_ID);
		poolsAndLoansTab.click();

		WebDriverWait wait = new WebDriverWait(driver, 30, 100);
		
		System.out.println("Now initiating adding a pool....");
		WebElement addPoolButton = wait.until(ExpectedConditions.elementToBeClickable(By.id(PoolListScreenConfiguration.ADD_POOL_BUTTON_ID)));
		addPoolButton.click();
		
		WebElement fileUploadInputField = driver.findElementById(PoolListScreenConfiguration.ADD_POOL_FILE_FIELD_ID);
		fileUploadInputField.sendKeys(TestConfiguration.FILE_PATH);
		
		WebElement initiateFileUploadBtn = driver.findElementById(PoolListScreenConfiguration.ADD_POOL_FILE_UPLOAD_BUTTON_ID);
		initiateFileUploadBtn.click();
		System.out.println("File upload initiated....");
		
		
		WebElement confirmationPopupCloseIcon = wait.until(ExpectedConditions.elementToBeClickable(By.className(PoolListScreenConfiguration.ADD_POOL_CONFIRMATION_POPUP_CLOSE_ICON_CLASSNAME)));
		confirmationPopupCloseIcon.click();
		
		System.out.println("Now switching tabs back & forth to kill time....");
		WebElement dashboardTab = driver.findElementById(PoolListScreenConfiguration.DASHBOARD_TAB_ID);
		dashboardTab.click();

		poolsAndLoansTab = driver.findElementById(DashboardPageConfiguration.POOLS_AND_LOANS_TAB_ID);
		poolsAndLoansTab.click();
		dashboardTab = driver.findElementById(PoolListScreenConfiguration.DASHBOARD_TAB_ID);
		dashboardTab.click();
		
		poolsAndLoansTab = driver.findElementById(DashboardPageConfiguration.POOLS_AND_LOANS_TAB_ID);
		poolsAndLoansTab.click();
		
		dashboardTab = driver.findElementById(PoolListScreenConfiguration.DASHBOARD_TAB_ID);
		dashboardTab.click();
		waitForPageLoaded(driver);
		
		driver.switchTo().frame(driver.findElement(By.id(DashboardPageConfiguration.FRAME_ID)));
		
		System.out.println("Now looking for the Pool ID link....");
		WebElement poolLink = null;
		boolean seleniumExceptionTriggered = false;
		try
		{
			poolLink = wait.until(ExpectedConditions.elementToBeClickable(By.linkText("Pool "+TestConfiguration.POOL_ID)));
			System.out.println("SUCCESS: Pool link for "+ TestConfiguration.POOL_ID + " found in first attempt.");
			String hrefLocation = findByAnchor();
			driver.get(hrefLocation);
		}
		catch(Exception nsee)
		{
			System.out.println("WARNING: Pool link for "+ TestConfiguration.POOL_ID + " NOT found.");
			System.out.println("Swiyching tabs again to refresh...");
			seleniumExceptionTriggered = true;
		}
		
		if(seleniumExceptionTriggered)
		{
			seleniumExceptionTriggered = false;
			driver.switchTo().defaultContent();
			poolsAndLoansTab = driver.findElementById(DashboardPageConfiguration.POOLS_AND_LOANS_TAB_ID);
			poolsAndLoansTab.click();
			dashboardTab = driver.findElementById(PoolListScreenConfiguration.DASHBOARD_TAB_ID);
			dashboardTab.click();
			waitForPageLoaded(driver);
			driver.switchTo().frame(driver.findElement(By.id(DashboardPageConfiguration.FRAME_ID)));
			try
			{
				
				poolLink = wait.until(ExpectedConditions.elementToBeClickable(By.linkText("Pool "+TestConfiguration.POOL_ID)));
				System.out.println("SUCCESS: Pool link for "+ TestConfiguration.POOL_ID + " found in second attempt.");
				String hrefLocation = findByAnchor();
				System.out.println("Now accessing Pools details screen....");

				driver.get(hrefLocation);
			}
			catch(Exception nsee)
			{
				seleniumExceptionTriggered = true;
			}
		}
		
		if(!seleniumExceptionTriggered)
		{	
			poolCreated = true;
		}
		
        System.out.println("Pool "+TestConfiguration.POOL_ID+" found: "+poolCreated);	
		return poolCreated;
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
	
	public String findByAnchor()
	{
      WebElement anchor = null;
      List<WebElement> anchors = driver.findElements(By.tagName("a"));
      System.out.println("Number of anchors found: "+anchors.size());
      Iterator<WebElement> i = anchors.iterator();
      
      whileLoop: while(i.hasNext()) 
      {
          anchor = i.next();
          
          if(anchor.getText().equals("Pool "+TestConfiguration.POOL_ID)) 
          {
              break whileLoop;
          }
      }
      return anchor.getAttribute("href");
	}
}
