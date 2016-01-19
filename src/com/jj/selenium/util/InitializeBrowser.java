/**
 *@author Aazim Lakhani
 *This class initialize the appropriate driver to be used, based on the browser specified in the test case.   
 **/

package com.jj.selenium.util;

import junit.framework.Assert;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Sheet;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.ie.InternetExplorerDriver;

import com.jj.selenium.test.GenericTestCase;

public class InitializeBrowser {

	public static final String FIREFOX = "Firefox" ;
	public static final String IE = "IE" ;
	public static final String CHROME = "Chrome" ;
	
	public static WebDriver initializeBrowser(Sheet testCase){
		GenericTestCase.log = TestCaseUtility.logDebug(GenericTestCase.log,"Starting execution of initializeBrowser()");
		String browser = null ; 
		WebDriver driver = null;
		GenericTestCase.log = TestCaseUtility.logDebug(GenericTestCase.log,"Test Case Being executed :" + testCase);
		GenericTestCase.log = TestCaseUtility.logDebug(GenericTestCase.log,"testCase.getRow(1) : " + testCase.getRow(1));
		Assert.assertNotNull("Your first row cannot be empty", testCase.getRow(1));
		Cell cell = testCase.getRow(1).getCell(CommonConstants.BROWSER);
		GenericTestCase.log = TestCaseUtility.logDebug(GenericTestCase.log,"Cell : " + cell);
		Assert.assertNotNull("Please specify the browser on which you want to execute this test case.", cell);
		//if(cell !=null){
			browser = cell.toString();
			GenericTestCase.log = TestCaseUtility.logDebug(GenericTestCase.log,"Browser: " + browser);
		//}
			
		if(browser != null && browser.equalsIgnoreCase(FIREFOX)){
			driver = initializeFireFox();
	    }else if(browser != null && browser.equalsIgnoreCase(IE)){
	    	driver = initializeInternetExplorer();
	    }else if(browser != null && browser.equalsIgnoreCase(CHROME)){
	    	driver = initializeGoogleChrome();
	    }else{
			/* If we cannot initialize the driver, then the browser mentioned in the test case is not valid.*/
			Assert.fail("Please enter a valid Browser value in your test case. Your browser could be either Firefox / IE / Chrome /");
		}
		return driver;
	}
	
	private static WebDriver initializeFireFox(){
		GenericTestCase.log = TestCaseUtility.logDebug(GenericTestCase.log,"Started execution of initializeFireFox");
		return new FirefoxDriver();
	}
	
	private static WebDriver initializeInternetExplorer(){
		// When use IE to create a new user, we will have to delete session cookies, coz after the user is created, then for every subsequent attempt it shows that the user is logged in 
		GenericTestCase.log = TestCaseUtility.logDebug(GenericTestCase.log,"Started execution of initializeInternetExplorer");
		return new InternetExplorerDriver();
	}
	
	private static WebDriver initializeGoogleChrome(){
		GenericTestCase.log = TestCaseUtility.logDebug(GenericTestCase.log,"Started execution of initializeGoogleChrome");
		return new ChromeDriver();
	}
	
}
