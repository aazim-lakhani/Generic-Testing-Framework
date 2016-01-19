/**
 *  This class initializes the test cases to be executed.  
 */
package com.jj.selenium.util;

import java.io.File;
import java.io.IOException;
import java.util.Properties;

import junit.framework.Assert;
import org.apache.commons.io.FileUtils;
import org.apache.tools.ant.Project;
import org.apache.tools.ant.ProjectHelper;

import com.jj.selenium.test.GenericTestCase;

/**
 * @author Aazim Lakhani
 */
public class InitializeTestSuite {

	/*******************************************************INSTANCE VARIABLES*****************************************************/
	
	// Directory containing all test cases to be executed.
	private static File testDataDirectory = new File(CommonConstants.rb.getString(CommonConstants.TEST_CASES));
	
	// Directory containing the test case being executed. 
	private static File tempDirectory = new File(CommonConstants.rb.getString(CommonConstants.TEMP));
	
	/********************************************************METHODS**************************************************************/
	
	public static void main(String[] args) {
		checkWhetherTestCasesExists(testDataDirectory);
		iterateThroughAllTestCases(testDataDirectory,tempDirectory);
	}
	
	/** This method checks whether there are any testCases to be executed
	 *  @param testDataDirectory Directory containing test cases. 
	 **/
	private static void checkWhetherTestCasesExists(File testDataDirectory){
		Assert.assertNotNull("No test case exists for the specified directory. Kindly ensure that the directory exists. After that ensure that it has test cases in it. ", testDataDirectory.listFiles());
	}
	
	/** This method iterates through all test cases & calls the concerned methods to execute test cases. 
	 *  @param testDataDirectory Directory containing all test cases to be executed.
	 *  @param tempDirectory Directory containing the test case being executed. 
	 **/
	private static void iterateThroughAllTestCases(File testDataDirectory, File tempDirectory){
		// Iterate through all test cases that have to be executed. 
		for (File testCase : testDataDirectory.listFiles()) {
			try{
				// Copy the file to the temporary directory. This directory as mentioned above contains the test case that is being executed. 
				FileUtils.copyFileToDirectory(testCase, tempDirectory);
			}catch(IOException e){ 
				TestCaseUtility.logError(GenericTestCase.errorLog, "Unable to copy the test case : " + testCase + " to the destination directory " + tempDirectory.getAbsolutePath());
				Assert.fail("Unable to copy the test case : " + testCase + " to the destination directory " + tempDirectory.getAbsolutePath());
			}
			// fileNameWithExcelExtension : Complete File Name with extension. 
			String fileNameWithExcelExtension = testCase.getName();
			// fileName : Test case being executed. 
			String fileName = TestCaseUtility.getTestCaseBeingExecuted(fileNameWithExcelExtension);
			printTestCaseBeingExecuted(fileName);
			//invokeRunTestCase(p,fileName);
			invokeRunTestCase(fileName);
			deleteTestCaseAfterExecution(tempDirectory,fileNameWithExcelExtension);
		}
	}
	
	/** This method initializes the project containing the ANT target to execute. 
	 *  @return project ANT project containing the target. 
	 **/
	private static Project initializeTargetToExecute(){
		File buildFile = new File("build.xml");
		Project project = new Project();
		project.init();
		ProjectHelper helper = ProjectHelper.getProjectHelper();
		project.addReference("ant.projectHelper", helper);
		helper.parse(project, buildFile);
		Properties systemP = System.getProperties();
		systemP.put("webdriver.chrome.driver", "C:\\Selenium\\ExternalizeSelenium\\chromedriver_win_18.0.1022.0\\chromedriver.exe");
		return project;
	}
	
	/** This method deletes the test case after it has been executed.
	 *  @param tempDirectory: Directory containing the test case being executed.
	 *  @param fileNameWithExcelExtension: Complete File Name with extension.
	 **/
	private static void deleteTestCaseAfterExecution(File tempDirectory,String fileNameWithExcelExtension){
		new File((tempDirectory.getAbsolutePath() + "\\" + fileNameWithExcelExtension)).delete();	
	}
	
	/** This method prints the test case being executed. 
	 *  @param fileName Test case being executed. 
	 **/
	private static void printTestCaseBeingExecuted(String fileName) {
		System.out.println("***********************************************");
		System.out.println("Test Case Being Executed: " + fileName);
		System.out.println("***********************************************");
	}
	
	/**
	 * This method invokes the ANT target to execute the test case.
	 * @param testCase Name of the testcase being executed. 
	 **/

	private static void invokeRunTestCase(String testCase){
		   Project project = initializeTargetToExecute();
		   project.setNewProperty("xmlFile", testCase);
		   project.executeTarget("runTestCase");
	}
}
