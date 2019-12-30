package com.ampcus.myginniemae.sfpdm;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.ampcus.myginniemae.sfpdm.config.DashboardPageConfiguration;
import com.ampcus.myginniemae.sfpdm.config.PoolDetailsScreenConfigurations;
import com.ampcus.myginniemae.sfpdm.config.PoolListScreenConfiguration;
import com.ampcus.myginniemae.sfpdm.config.TestConfiguration;
import com.ampcus.myginniemae.sfpdm.helper.PageUtilities;

public class TestRunner {

	private RemoteWebDriver driver;

	
	public TestRunner(RemoteWebDriver driver) {
		this.driver = driver;
	}
	
	/*
	 * This test assumes the Pool Details screen is currently displayed.
	 */
	public boolean run()
	{
		boolean testCompleted = false;
		WebDriverWait wait = new WebDriverWait(driver, 30, 100);
		PageUtilities util = new PageUtilities(driver);
		
		try
		{
			driver.switchTo().frame(driver.findElement(By.id(PoolDetailsScreenConfigurations.POOL_DETAILS_FRAME_ID)));
		
			WebElement poolField = wait.until(ExpectedConditions.elementToBeClickable(By.id(TestConfiguration.POOL_FIELD_ID)));
			poolField.clear();
			poolField.sendKeys(TestConfiguration.POOL_FIELD_VALUE);
			System.out.println("Pool field value entered..");
			
			driver.switchTo().defaultContent();
			WebElement saveButton = wait.until(ExpectedConditions.elementToBeClickable(By.xpath(PoolDetailsScreenConfigurations.SAVE_BUTTON_XPATH)));
			saveButton.click();
			System.out.println("Pool changes saved..");
			
			driver.switchTo().frame(driver.findElement(By.id(PoolDetailsScreenConfigurations.POOL_DETAILS_FRAME_ID)));
			WebElement saveConfirmationLink = wait.until(ExpectedConditions.elementToBeClickable(By.id(PoolDetailsScreenConfigurations.SAVE_CONFIRMATION_POPUP_CLOSE_LINK_ID)));
			saveConfirmationLink.click();
			System.out.println("Pool saved confirmation popup closed...");
			
			driver.switchTo().defaultContent();
			WebElement validateButton = wait.until(ExpectedConditions.elementToBeClickable(By.className(PoolDetailsScreenConfigurations.VALIDATE_BUTTON_CLASSNAME)));
			validateButton.click();
			System.out.println("Pool validation initiated..");
			
			driver.switchTo().frame(driver.findElement(By.id(PoolDetailsScreenConfigurations.POOL_DETAILS_FRAME_ID)));
			WebElement validationConfirmationLink = wait.until(ExpectedConditions.elementToBeClickable(By.id(PoolDetailsScreenConfigurations.VALIDATE_CONFIRMATION_POPUP_CLOSE_LINK_ID)));
			validationConfirmationLink.click();
			System.out.println("Pool validation confirmation popup closed...");

			System.out.println("Now switching to Loans tab");
			WebElement loansTab = wait.until(ExpectedConditions.elementToBeClickable(By.linkText(PoolDetailsScreenConfigurations.LOAN_TAB_ANCHOR_LINKTEXT)));
			loansTab.click();

			driver.switchTo().defaultContent();
			System.out.println("Now switching back to Pool Details tab");
			WebElement dashboardTab = wait.until(ExpectedConditions.elementToBeClickable(By.id(PoolDetailsScreenConfigurations.DASHBOARD_TAB_ID)));
			dashboardTab.click();
			
			util.waitForPageLoaded(driver);
			driver.switchTo().frame(driver.findElement(By.id(DashboardPageConfiguration.FRAME_ID)));
			WebElement poolLink = null;
			boolean seleniumExceptionTriggered = false;
			try
			{
				poolLink = wait.until(ExpectedConditions.elementToBeClickable(By.linkText("Pool "+TestConfiguration.POOL_ID)));
				System.out.println("SUCCESS: Pool link for "+ TestConfiguration.POOL_ID + " found in first attempt.");
				poolLink = util.findElement(PageUtilities.ELEMENT_TYPE_ANCHOR, PageUtilities.QUALIFIER_TYPE_TEXT, "Pool "+TestConfiguration.POOL_ID);
				driver.get(poolLink.getAttribute("href"));
			}
			catch(Exception nsee)
			{
				System.out.println("WARNING: Pool link for "+ TestConfiguration.POOL_ID + " NOT found.");
				System.out.println("Switching tabs again to refresh...");
				seleniumExceptionTriggered = true;
			}
			
			if(seleniumExceptionTriggered)
			{
				seleniumExceptionTriggered = false;
				driver.switchTo().defaultContent();
				WebElement poolsAndLoansTab = driver.findElementById(DashboardPageConfiguration.POOLS_AND_LOANS_TAB_ID);
				poolsAndLoansTab.click();
				dashboardTab = driver.findElementById(PoolListScreenConfiguration.DASHBOARD_TAB_ID);
				dashboardTab.click();
				util.waitForPageLoaded(driver);
				driver.switchTo().frame(driver.findElement(By.id(DashboardPageConfiguration.FRAME_ID)));
				try
				{
					
					poolLink = wait.until(ExpectedConditions.elementToBeClickable(By.linkText("Pool "+TestConfiguration.POOL_ID)));
					System.out.println("SUCCESS: Pool link for "+ TestConfiguration.POOL_ID + " found in second attempt.");
					poolLink = util.findElement(PageUtilities.ELEMENT_TYPE_ANCHOR, PageUtilities.QUALIFIER_TYPE_TEXT, "Pool "+TestConfiguration.POOL_ID);
					driver.get(poolLink.getAttribute("href"));
				}
				catch(Exception nsee)
				{
					seleniumExceptionTriggered = true;
				}
			}
			
			
			testCompleted = true;
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		finally
		{
			driver.switchTo().defaultContent();
		}
		
		return testCompleted;
	}

}
