package com.jj.selenium.verify;

import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

import junit.framework.Assert;

import org.openqa.selenium.WebElement;

import com.jj.selenium.model.TestDataBean;
import com.jj.selenium.pageobject.GenericPageObject;
import com.jj.selenium.test.GenericTestCase;
import com.jj.selenium.util.CommonConstants;
import com.jj.selenium.util.TestCaseUtility;

public class VerifyData {
	
	/*enum UserProfile{
		EMAIL,FIRST_NAME,LAST_NAME,POSTAL_CODE,GENDER,DATE_OF_BIRTH
	};*/
	/*Alternate: Specify the parameters to be verified in excel.*/
	// private static final String[] USER_PROFILE_FIELDS = new String[]{"EMAIL","FIRST_NAME","LAST_NAME","POSTAL_CODE","GENDER","DATE_OF_BIRTH"};
	
	public void handleVerification(TestDataBean testData,GenericPageObject genericPage,Map dataInputToBeVerified){
	//public void handleVerification(String testData,GenericPageObject genericPage,Map dataInputToBeVerified){
		/**
		 * 1. Find the web elements.
		 * 2. Check what needs to be verified (UserProfile,Shipping Address, Payment Card etc.)
		 * 3. Using enums, get appropriate value from the map.
		 * 4. Verify whether that value is present. 
		 * */
		GenericTestCase.log = TestCaseUtility.logDebug(GenericTestCase.log,"Handle Verification is called");
		boolean isDataVerifiedSuccessfully ;
		List<WebElement> webElement = null;
		Assert.assertNotNull("Please enter the element you want to search for the action " + testData.getAction(), testData.getSearchElement());
		Assert.assertNotNull("Please enter how you want to search the element" + testData.getSearchElement() + " for the action " + testData.getAction(), testData.getSearchElementBy());
		webElement = genericPage.findElement(testData.getSearchElementBy(), testData.getSearchElement());
		String[] fieldsToBeVerified = getFieldsToCompare(testData);
		//if(testData.getVerificationRequiredFor().equalsIgnoreCase(CommonConstants.VERIFICATION_REQUIRED_FOR_USER_PROFILE)){
			//verifyData(webElement,dataInputToBeVerified,USER_PROFILE_FIELDS);
		isDataVerifiedSuccessfully = verifyData(webElement,dataInputToBeVerified,fieldsToBeVerified);
		if(!isDataVerifiedSuccessfully){
			Assert.fail("Verification for action : " + testData.getAction() + " has failed. This happens when data specified in test case & that visible on the website do not match. Verification has failed for some of the following fields : " + testData.getVerificationRequiredFor());
		}
		//}
		GenericTestCase.log = TestCaseUtility.logDebug(GenericTestCase.log,"End of handleVerification");
		
	}
	
	private String[] getFieldsToCompare(TestDataBean testData){
		//String[] fieldsToBeVerified = new String[]{}; 
		String verificationRequiredFor = testData.getVerificationRequiredFor();
		/*System.out.println("verificationRequiredFor :" + verificationRequiredFor);
		Assert.assertNotNull("The action type specified for the action : " + testData.getAction() + " was VERIFY, however, which fields are to be verified is not mentioned properly in the test case.", verificationRequiredFor);*/
		StringTokenizer token = new StringTokenizer(verificationRequiredFor,",");
		GenericTestCase.log = TestCaseUtility.logDebug(GenericTestCase.log,"token.countTokens()" + token.countTokens());
		String[] fieldsToBeVerified = new String[token.countTokens()];
		for(int i=0;token.hasMoreTokens();i++){
			fieldsToBeVerified[i]=token.nextToken();
		}
		return fieldsToBeVerified;
	}
	
	private boolean verifyData(List<WebElement> webElementDataToBeVerified, Map inputDataFromExternalSource,String[] fieldNames){
		/**
		 * 1. Iterate through all web Elements. If atleast one webElement contains all the data we are looking out for, then break 
		 * 2.  
		 **/
		GenericTestCase.log = TestCaseUtility.logDebug(GenericTestCase.log,"Handle verifyData is called");
		boolean isDataMatching = false;
		String profileFieldName;
		String profileFieldValue;
		//UserProfile[] userProfile = UserProfile.values();
		for(WebElement pageElement  : webElementDataToBeVerified){
			String pageDataToBeVerified = pageElement.getText();
			GenericTestCase.log = TestCaseUtility.logDebug(GenericTestCase.log,"pageElement.getText() : " + pageElement.getText());
			for(int i = 0; i < fieldNames.length; i++){
				profileFieldName = fieldNames[i];
				/*if(profileFieldName == null){
					System.out.println("There's no field to verify. Kindly enter fields to be verified");
				}*/
				profileFieldValue =  (String)inputDataFromExternalSource.get(profileFieldName);
				isDataMatching = pageDataToBeVerified.contains(profileFieldValue);
				if(!isDataMatching){
					break;
				}
			}
			if(isDataMatching){
				GenericTestCase.log = TestCaseUtility.logDebug(GenericTestCase.log,"Verification succeeded");
				break;
			}
		}
		return isDataMatching;
	}
	
	/** This method is used to verify derived data, such as price which is not entered by the customer. The price of an order is decided 
	 *  by the system. Hence, it is considered as a derived data.  
	 *  @param testData Expected value is stored in this bean.
	 *  @param genericPage Actual value is displayed on this page. 
	 **/
	
	private boolean verifyDerivedData(TestDataBean testData, GenericPageObject genericPage){
		boolean isDerivedDataMatching = false ;
		List<WebElement> webElement = null;
		Assert.assertNotNull("Please enter a input field value for the action " + testData.getAction(), testData.getFieldValue());
		String expectedDerivedData = testData.getFieldValue();
		GenericTestCase.log = TestCaseUtility.logDebug(GenericTestCase.log,"Expected Derived Data: " + expectedDerivedData);
		Assert.assertNotNull("Please enter the element you want to search for the action " + testData.getAction(), testData.getSearchElement());
		Assert.assertNotNull("Please enter how you want to search the element" + testData.getSearchElement() + " for the action " + testData.getAction(), testData.getSearchElementBy());
		webElement = genericPage.findElement(testData.getSearchElementBy(), testData.getSearchElement());
		for(WebElement element : webElement){
			String text = element.getText();
			GenericTestCase.log = TestCaseUtility.logDebug(GenericTestCase.log,"Actual Data: " + text);
			isDerivedDataMatching = text.contains(expectedDerivedData);
			if(isDerivedDataMatching){
				GenericTestCase.log = TestCaseUtility.logDebug(GenericTestCase.log,"Derived Data matches");
				break;
			}
		}
		return isDerivedDataMatching;
	}
}
