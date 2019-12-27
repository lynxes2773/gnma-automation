package com.ampcus.myginniemae.sfpdm.helper;

import org.openqa.selenium.remote.RemoteWebDriver;

public class WaitTimes {

	public final static long WAIT_ONE_SECOND = 1000000;
	public final static long WAIT_TWO_SECONDS = 2000000;
	public final static long WAIT_FIVE_SECONDS = 5000000;
	public final static long WAIT_TEN_SECONDS = 10000000;
	public final static long WAIT_THIRTY_SECONDS = 30000000;
	
	private WaitTimes() {
		// TODO Auto-generated constructor stub
	}
	
	public static synchronized void waitFor(RemoteWebDriver driver, long waitTime)
	{
		try
		{
			driver.wait(waitTime);
		}
		catch(Exception e)
		{}
	}

}
