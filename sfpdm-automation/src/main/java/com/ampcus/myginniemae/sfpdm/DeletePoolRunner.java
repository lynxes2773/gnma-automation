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

public class DeletePoolRunner {

	private RemoteWebDriver driver;
	
	private DeletePoolRunner() {
	}
	
	public DeletePoolRunner(RemoteWebDriver driver) {
		this.driver = driver;
	}

	/**
	 * This method assumes the Pool Details screen is currently displayed.
	 * Therefore, the Delete button should be visible.
	 * 
	 * Once the pool is successfully deleted, the method returns a boolean(true) to the calling method.
	 */
	public boolean run()
	{
		boolean poolDeleted = false;
		driver.switchTo().defaultContent();
		WebDriverWait wait = new WebDriverWait(driver, 30);

		System.out.println("Now starting cleanup process. Deleting pool "+TestConfiguration.POOL_ID);
		WebElement deleteButton = wait.until(ExpectedConditions.elementToBeClickable(By.className(PoolDetailsScreenConfigurations.DELETE_BUTTON_CLASSNAME))); 
		deleteButton.click();
		
		driver.switchTo().frame(driver.findElement(By.id(PoolDetailsScreenConfigurations.DELETE_POPUP_FRAME_ID)));
		
		WebElement confirmDeleteButton = wait.until(ExpectedConditions.elementToBeClickable(By.id(PoolDetailsScreenConfigurations.DELETE_CONFIRMATION_POPUP_DELETE_BUTTON_ID))); 
		confirmDeleteButton.click();
		
		WebElement deleteSuccessOKButton = wait.until(ExpectedConditions.elementToBeClickable(By.id(PoolDetailsScreenConfigurations.DELETE_SUCCESS_POPUP_CLOSE_BUTTON_ID)));
		deleteSuccessOKButton.click();
		
		poolDeleted = true;
		System.out.println("Pool "+TestConfiguration.POOL_ID+" deleted: "+poolDeleted);
		
		return poolDeleted;
	}

}
