/**
 * This class is an Excel Adaptor which parses & processes data present in  the Excel file. It stores the data in Java Bean objects, 
 * which are then accessed by test cases. 
 * */

package com.jj.selenium.util;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import com.jj.selenium.test.GenericTestCase;
import java.io.File;

public class TestCaseUtility {

	/** Returns the name of the test case being executed.
	 * @param testCase File representing the test case being executed. 
	 * @return fileName Returns the name of the test case
	 **/
	public static String getTestCaseBeingExecuted(File testCase){
		String fileNameWithExcelExtension = testCase.getName();
		int index = fileNameWithExcelExtension.indexOf(".");
		// fileName : File Name without extension. Test case being executed. 
		String fileName = fileNameWithExcelExtension.substring(0, index);
		return fileName;
	}
	
	/** Returns the name of the test case being executed.
	 * @param fileNameWithExcelExtension String represention of the test case being executed. 
	 * @return fileName Returns the name of the test case
	 **/
	public static String getTestCaseBeingExecuted(String fileNameWithExcelExtension){
		int index = fileNameWithExcelExtension.indexOf(".");
		// fileName : File Name without extension. Test case being executed. 
		String fileName = fileNameWithExcelExtension.substring(0, index);
		return fileName;
	}
	
	/** Logs the debug statements to be logged.
	 * @param log Test Case Log.
	 * @param stmtToLog Statement to be logged.
	 * @return log Returns the updated Test Case Log
	 **/
	public static StringBuilder logDebug(StringBuilder log,String stmtToLog){
		log.append(stmtToLog);
		log.append("\n");
		return log;
	}
	
	/** Logs the debug statements to be logged.
	 * @param log Test Case Log.
	 * @param stmtToLog Statement to be logged.
	 * @return log Returns the updated Test Case Log
	 **/
	public static StringBuilder logError(StringBuilder log,String stmtToLog){
		return logDebug(log,stmtToLog);
	}
	
	/** This method returns the excel sheet containing the test case to be executed. 
	 *  @param fileName It represents the absolute path of the file.
	 *  @return Sheet It represents a single excel sheet. By default it is expected that the test data would be present on the 1st sheet only. 
	 **/
	public static Sheet loadTestCase(String fileName){
		Workbook workbook = parseExcelFile(fileName);
		return workbook.getSheetAt(0);
	}
	
	/**
	 *  This method is required to parse the excel test data file.
	 *  @param filePath Absolute Path of the file containing the test data
	 *  @return Workbook A parsed representation of the test data excel file
	 *  @exception FileNotFoundException This exception is thrown if the program is unable to find your test case excel. Often, if your 
	 *                                   excel test case file is not closed, while the test case is being executed then too this exception 
	 *                                   is thrown.
	 **/
	public static Workbook parseExcelFile(String filePath) {
		Workbook workBook = null;
		try {
			workBook = WorkbookFactory.create(new FileInputStream(filePath));
		} catch (InvalidFormatException e) {
			e.printStackTrace();
			GenericTestCase.log = TestCaseUtility.logDebug(GenericTestCase.log,"Inside InvalidFormatException" );
		} catch (FileNotFoundException e) {
			GenericTestCase.errorLog = TestCaseUtility.logError(GenericTestCase.errorLog, "The following exception has possible occured because your test data excel files are not closed. Kindly close them & in particular close this file  " + filePath);
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return workBook;	
	}		
	
}
