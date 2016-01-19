/**@author Aazim Lakhani
 * This class represents a Generic Test Case. It represents all the test cases which have been defined in the excel files.
 * */

/** TO-DO
 * 0. Generic Test Case class too big. Make it modular. Format code.
 * 1. Output Data Verification.
 * 2. Reporting System.
 * 3. Verify from database.
 * 4. Configure for variety of drivers. 
 * 5. Selenium Grid & Hudson integration.
 * 6. Mark Failure MEssages as constants.  
 * 7. Build a way in which you can point it out to the tester, the field whose verification has failed. 
 * 8. How will u handle Pop Ups.
 * 9. How will u handle SSL pages. 
 * 10. 
 * */

package com.jj.selenium.test;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import org.apache.commons.io.FileUtils;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.junit.Test;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import com.jj.selenium.model.TestDataBean;
import com.jj.selenium.pageobject.GenericPageObject;
import com.jj.selenium.util.CommonConstants;
import com.jj.selenium.util.InitializeBrowser;
import com.jj.selenium.util.TestCaseUtility;
import com.jj.selenium.verify.VerifyData;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import junit.framework.Assert;

public class GenericTestCase {
	
	/** ----------------------------------------------- CONSTANTS ----------------------------------------------------------*/
	
	/* Represents the directory where screen shots for various test cases would be stored */ 
	private static final String SCREENSHOT_PATH="C:\\Selenium\\ExternalizeSelenium\\screenshot\\";
	
	/** ----------------------------------------------- INSTANCE VARIABLES -------------*/
	
	/*Represents a test case sheet*/
	private Sheet testCase ;
	
	/* Represent the Web Driver*/
	private WebDriver driver ;
	
	/* Its a generic representation of a web page */
	private GenericPageObject genericPage;
	
	/* Its used to name the screen shot file. It represents the row/action for which the screenshot is taken   */
	private int rowCount = 1;
	
	/* It represents an Excel row/action, which contains the data required for testing. */
	private TestDataBean testData;
	
	/* While executing a test case, for every data input, we store the Field Name & Field Value, which could be required later for 
	 * verification.*/
	private Map dataInputToBeVerified;
	
	/* This instance variable represents an object of the VerifyData class, which manages the verification side of a test case.  */
	private VerifyData verifyInputData ;
	
	/* Using a StringBuilder instead of String improves the speed of execution of the test cases. Represents debug log for a test case */
	public static StringBuilder log = new StringBuilder();
	
	/* Represents error log for a test case */
	public static StringBuilder errorLog = new StringBuilder();
	
	
	/** ----------------------------------------------- METHODS ----------------------------------------------------------------------*/
	
	/** This method sets up the test case to be executed.
	 *  @param dataFile It the excel representation of the test case. 
	 **/
	public void setUp(File dataFile)  {
		log = TestCaseUtility.logDebug(GenericTestCase.log,"SetUp method is called");
		// A map which contains the fieldName & fieldValue, which will be used later for verification.
		dataInputToBeVerified = new HashMap<String,String>();
		// Loads the test case to be executed.	
		testCase= TestCaseUtility.loadTestCase(dataFile.getAbsolutePath());
		if(testCase !=null){
			driver = InitializeBrowser.initializeBrowser(testCase)	;
			// Set timeout period for the browser
			driver.manage().timeouts().implicitlyWait(25, TimeUnit.SECONDS);
		}
		genericPage = new GenericPageObject(driver);
		verifyInputData = new VerifyData();
	}
	
	@Test
	public void test() {
		// Directory path would be different for Windows & Linux. In Windows we use \\. Check whats used for Linux. File handling API should handle it. 
		File tempDirectory = new File(CommonConstants.rb.getString(CommonConstants.TEMP));
		// Get the test case excel from the temporary directory.
		File dataFile = (tempDirectory.listFiles())[0];
		// Get the name of the test case being executed. 
		String nameOfTheTestCase = TestCaseUtility.getTestCaseBeingExecuted(dataFile);
		// Initialize the test case to be executed. 
		setUp(dataFile);
		boolean isDirectoryCreated = createDirectoryForTestCase(nameOfTheTestCase);
		Assert.assertTrue("No directory created for the file " + nameOfTheTestCase, isDirectoryCreated);
		while(true){
			testData = new TestDataBean();
			Row row = testCase.getRow(rowCount++);
			if(row == null || (row!=null && row.getCell(CommonConstants.TYPE)==null)){
				TestCaseUtility.logDebug(GenericTestCase.log,"Test Case " + nameOfTheTestCase + " has been executed. ");
				break;
			}
			testData = convertExcelRowIntoBean(row);
			if(testData.getType().equalsIgnoreCase(CommonConstants.ACTION_TYPE_IS_DATA_INPUT)){
				// Adding data which could later be used for verification. 
				if(testData.getFieldName()!=null && testData.getFieldValue()!=null){
					dataInputToBeVerified.put(testData.getFieldName(),testData.getFieldValue());	
				}
				genericPage.handleDataInput(testData);
			}else if(testData.getType().equalsIgnoreCase(CommonConstants.ACTION_TYPE_IS_PAGE_NAVIGATION)){
				genericPage.handlePageNavigation(testData);
			}else if(testData.getType().equalsIgnoreCase(CommonConstants.ACTION_TYPE_IS_VERIFICATION)){
				verifyInputData.handleVerification(testData,genericPage,dataInputToBeVerified);
			}else if(testData.getType().equalsIgnoreCase(CommonConstants.ACTION_TYPE_IS_VERIFY_DERIVED_DATA)){
				// TODO What should happen when action type is verification. 
			}else if(testData.getType().equalsIgnoreCase(CommonConstants.ACTION_TYPE_IS_FRAME)){
				genericPage.handleFrames(testData);
			}else if(testData.getType().equalsIgnoreCase(CommonConstants.ACTION_TYPE_IS_WINDOW)){
				genericPage.handleWindow(testData);
			}
			if(testData.getScreenshot()!=null && testData.getScreenshot().equalsIgnoreCase("Yes")){
				TestCaseUtility.logDebug(GenericTestCase.log,"Row Count: " + rowCount + "-" + testData.getAction());
				//String directoryName = getDirectoryCreatedForTestCase(dataFile.getName());
				getScreenShot(rowCount + "-" + testData.getAction(),nameOfTheTestCase);
			}
			// delay();
		}
		tearDown();
	}
	
	/**
	 * This method creates a directory for each test case to store screen shots.  
	 * @param nameOfTheTestCase Name of the Directory to be created
	 * @return true If directory is created. 
	 **/
	private boolean createDirectoryForTestCase(String nameOfTheTestCase){
		return new File(SCREENSHOT_PATH + nameOfTheTestCase).mkdir();
	}
	
	/* This method is executed after the execution of each test case. It shuts down the browser. */
	public void tearDown()  {
		TestCaseUtility.logDebug(GenericTestCase.log,"tearDown() is called");
		driver.quit();
	}
	 
	/**
	 * This method set the path where the screenshot would be stored. 
	 * @param fileName Name of the Screenshot file.
	 * @param directoryName The directory name representing the test case being executed where the screenshot would be stored.   
	 **/
	public void getScreenShot(String fileName, String directoryName){
		String screenShotName=SCREENSHOT_PATH + directoryName + "\\" + fileName + ".jpeg";
		TestCaseUtility.logDebug(GenericTestCase.log,"Screenshot Path: " + screenShotName);
		takeScreenshot(screenShotName);
	}
	
	/**
	 * This method takes the screenshot & copies it to its destination
	 * @param destinationPath Screenshot file name 
	 **/
	public void takeScreenshot(String destinationPath){
		TestCaseUtility.logDebug(GenericTestCase.log,"Inside takeScreenshot");
		TestCaseUtility.logDebug(GenericTestCase.log,"Driver: " + driver);
		File scrFile = ((TakesScreenshot)driver).getScreenshotAs(OutputType.FILE);
		try{
			TestCaseUtility.logDebug(GenericTestCase.log,"Copying screenshot");
			FileUtils.copyFile(scrFile, new File(destinationPath));
			TestCaseUtility.logDebug(GenericTestCase.log,"Screenshot copied");
		}catch(IOException exception){
			TestCaseUtility.logDebug(GenericTestCase.log," Unable to copy the screenshot. Destination Path for screenshot is : " + destinationPath);
		}
	}
	
	/**
	 * This method obtains the data from the test case action being executed & converts it into a java bean. 
	 * @param row Test case action being executed. 
	 * @return TestDataBean Java bean representation of the test case action being executed.
	 **/
	public TestDataBean convertExcelRowIntoBean(Row row){
		if(row!=null){
			if(row.getCell(CommonConstants.TYPE)!=null){
				testData.setType(row.getCell(CommonConstants.TYPE).toString());
			}
			if(row.getCell(CommonConstants.ACTION)!=null){
				testData.setAction(row.getCell(CommonConstants.ACTION).toString());
			}
			if(row.getCell(CommonConstants.SEARCH_ELEMENT)!=null){
				testData.setSearchElement(row.getCell(CommonConstants.SEARCH_ELEMENT).toString());
			}
			if(row.getCell(CommonConstants.SEARCH_ELEMENT_BY)!=null){
				testData.setSearchElementBy(row.getCell(CommonConstants.SEARCH_ELEMENT_BY).toString());
			}
			if(row.getCell(CommonConstants.FIELD_NAME)!=null){
				testData.setFieldName(row.getCell(CommonConstants.FIELD_NAME).toString());
			}
			if(row.getCell(CommonConstants.FIELD_VALUE)!=null){
				testData.setFieldValue(row.getCell(CommonConstants.FIELD_VALUE).toString());
			}
			if(row.getCell(CommonConstants.FIELD_INPUT_TYPE)!=null){
				testData.setFieldType(row.getCell(CommonConstants.FIELD_INPUT_TYPE).toString());
			}
			if(row.getCell(CommonConstants.SCREENSHOT)!=null){
				testData.setScreenshot(row.getCell(CommonConstants.SCREENSHOT).toString());
			}
			if(row.getCell(CommonConstants.VERIFICATION_REQUIRED_FOR)!=null){
				testData.setVerificationRequiredFor(row.getCell(CommonConstants.VERIFICATION_REQUIRED_FOR).toString());
			}

		}
		return testData;
	}
	
	/**
	 * This method is user to ensure that the test case being executed is visible to the front-end tester. 
	 **/
	public void delay(){
		try{
			TestCaseUtility.logDebug(GenericTestCase.log,"Inside Delay");
			Thread.sleep(3000);
		}catch(InterruptedException ex){
			TestCaseUtility.logDebug(GenericTestCase.log,"Exception: " + ex.getMessage());
		}
	}

}
