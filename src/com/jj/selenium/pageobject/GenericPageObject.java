/** @author Aazim L
 * This class will contain, the services that are provided across web pages. The services include
 * 0. Finding Web Elements. 
 * 1. Handling Page Navigation -> Clicking links,button.
 * 2. Handling Data Input -> Filling forms (textbox,checkbox,dropdown box).  
 * */

package com.jj.selenium.pageobject;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;
import junit.framework.Assert;
import org.openqa.selenium.By;
import org.openqa.selenium.ElementNotVisibleException;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.WebDriver.TargetLocator;
import org.openqa.selenium.support.ui.Select;
import com.gargoylesoftware.htmlunit.javascript.background.JavaScriptExecutor;
import com.jj.selenium.model.TestDataBean;
import com.jj.selenium.test.GenericTestCase;
import com.jj.selenium.util.CommonConstants;
import com.jj.selenium.util.TestCaseUtility;

import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.support.ui.UnexpectedTagNameException;

public class GenericPageObject {
	
	/* Represents the web driver which is used by to automate the browser to execute test cases. */
	private WebDriver driver;
	
	/** This method is responsible for navigating across web pages. 
	 * @param  testData Represents the action being executed. 
	 **/
	public void handlePageNavigation(TestDataBean testData){
		Assert.assertNotNull("Please enter the element you want to search for the action " + testData.getAction(), testData.getSearchElement());
		Assert.assertNotNull("Please enter how you want to search the element" + testData.getSearchElement() + " for the action " + testData.getAction(), testData.getSearchElementBy());
		// Find the element on which you want to perform some action.
		List<WebElement> webElement = findElement(testData.getSearchElementBy(), testData.getSearchElement());
		GenericTestCase.log = TestCaseUtility.logDebug(GenericTestCase.log,"Size: " + webElement.size());
		// If the element is found, then perform some action on that element.  
		if(webElement.size()!=0){
		navigateToSomeOtherPage(webElement.get(0));
		}
	}
	
	/** This method is used to handle data input. 
	 *  @param testData It represents the data of the action that you 
	 * 
	 **/
	public void handleDataInput(TestDataBean testData){
		GenericTestCase.log = TestCaseUtility.logDebug(GenericTestCase.log,"Started Executing handleDataInput()");
		GenericTestCase.log = TestCaseUtility.logDebug(GenericTestCase.log,"Field Type : " + testData.getFieldType());
		Assert.assertNotNull("Please enter a input field type for the action " + testData.getAction() + "", testData.getFieldType());
		if(!testData.getFieldType().equalsIgnoreCase(CommonConstants.INPUT_TYPE_BROWSER)){
			Assert.assertNotNull("Please enter the element you want to search for the action " + testData.getAction(), testData.getSearchElement());
			Assert.assertNotNull("Please enter how you want to search the element" + testData.getSearchElement() + " for the action " + testData.getAction(), testData.getSearchElementBy());
		}
		List<WebElement> webElement = null;
		try{
		if(testData.getFieldType().equalsIgnoreCase(CommonConstants.INPUT_TYPE_BROWSER)){
			// What is Field Value or Field Name is empty / missed out by the tester.
			GenericTestCase.log = TestCaseUtility.logDebug(GenericTestCase.log,"Search Element By : " + testData.getSearchElementBy());
			GenericTestCase.log = TestCaseUtility.logDebug(GenericTestCase.log,"Search Element : " + testData.getSearchElement());
			Assert.assertNotNull("Please enter a input field value for the action " + testData.getAction(), testData.getFieldValue());
			driver.get(testData.getFieldValue());
		}else if(testData.getFieldType().equalsIgnoreCase(CommonConstants.INPUT_TYPE_TEXTBOX)){
			GenericTestCase.log = TestCaseUtility.logDebug(GenericTestCase.log,"Input type is textbox");
			webElement = findElement(testData.getSearchElementBy(), testData.getSearchElement());
			GenericTestCase.log = TestCaseUtility.logDebug(GenericTestCase.log,"Web ELement :" + webElement);
			GenericTestCase.log = TestCaseUtility.logDebug(GenericTestCase.log,"Calling addDataInTextBox");
			try{
			Assert.assertNotNull("Please enter a field value for the action : " + testData.getAction(), testData.getFieldValue());
			addDataInTextBox(webElement.get(0),testData.getFieldValue());
			}catch(ElementNotVisibleException ex){
				GenericTestCase.log = TestCaseUtility.logDebug(GenericTestCase.log,"Element " + testData.getSearchElement() + " performing action + " + testData.getAction() + " has been made invisble.");
			}
		}else if(testData.getFieldType().equalsIgnoreCase(CommonConstants.INPUT_TYPE_DROPDOWNBOX)){
			GenericTestCase.log = TestCaseUtility.logDebug(GenericTestCase.log,"Inside handleDataInput() ");
			/*StringTokenizer searchElementByToken = new StringTokenizer(testData.getSearchElementBy(),",");
			StringTokenizer searchedElementToken = new StringTokenizer(testData.getSearchElement(),",");
		    webElement = findElement(searchElementByToken.nextToken(), searchedElementToken.nextToken());*/
			webElement = findElement(testData.getSearchElementBy(), testData.getSearchElement());
//			for(WebElement element : dropDownBox){
			Assert.assertNotNull("Unable to find drop down box for action : " + testData.getAction(), webElement);
			    /*if(dropDownBox!=null){
			    	WebElement element = dropDownBox.get(0);
			    	List<WebElement> options = element.findElements(By.tagName("option"));
			    	for(WebElement opt : options){
			    		String visibleFieldValue = opt.getText();
						System.out.println("Visible Field Value :" + visibleFieldValue);
						if(visibleFieldValue.equalsIgnoreCase(testData.getFieldValue())){
							opt.click();
							break;
						}
			    	}
			    	
			    }*/
				
				
				
			//}
		    // Handle scenario when Field Value is missed out by the tester. Like United States is default value, i didn't have to select it 
		    // drop down , neither did Selenium IDE include it. 
		    Assert.assertNotNull("Please enter a field value for the action : " + testData.getAction(), testData.getFieldValue());
			selectDataFromDropDown(webElement.get(0),testData.getFieldValue());
			
			
			/*if(searchedElementToken.countTokens() == 2 ){
				System.out.println("No of tokens : 2 " );
		webElement = findElement(searchElementByToken.nextToken(), searchedElementToken.nextToken());
			webElement.get(0).click();
			}*/
		}else if(testData.getFieldType().equalsIgnoreCase(CommonConstants.INPUT_TYPE_RADIO_BUTTON)){
			webElement = findElement(testData.getSearchElementBy(), testData.getSearchElement());
			webElement.get(0).click();
		}else if(testData.getFieldType().equalsIgnoreCase(CommonConstants.INPUT_TYPE_CHECKBOX)){
			webElement = findElement(testData.getSearchElementBy(), testData.getSearchElement());
			webElement.get(0).click();
			// To be done. 
		}
	}catch(UnexpectedTagNameException ex){
		Assert.fail("Field Input Type for action named " + testData.getAction() + " is incorrect.");
	}
		
	}
	
	// Need to handle exceptions. What if no frame number is entered by the tester. WHat is frameName does not exist.
	public void handleFrames(TestDataBean testData){
		GenericTestCase.log = TestCaseUtility.logDebug(GenericTestCase.log,"Starting execution of handleFrames");
		driver.switchTo().defaultContent();
		if(testData.getSearchElementBy().equalsIgnoreCase(CommonConstants.SEARCH_ELEMENT_BY_NAME)){
			if(testData.getSearchElement()!=null){
				String frameName = testData.getSearchElement();
				driver.switchTo().frame(frameName);
			}
		}else if(testData.getSearchElementBy().equalsIgnoreCase(CommonConstants.SEARCH_ELEMENT_BY_ID)){
			if(testData.getSearchElement()!=null){
				int frameNumber = Integer.parseInt(testData.getSearchElement());
				driver.switchTo().frame(frameNumber);
			}
		}
		/*System.out.println("Element tagname: " + ele.getTagName());
		System.out.println("Element class: " + ele.getClass());
		System.out.println("Current URL: " + driver.getCurrentUrl());
		System.out.println("Page Source : " + driver.getPageSource());
		System.out.println("Title : " + driver.getTitle());*/
		
		
		//driver.switchTo().
		// Find frame by tag. 
	     /*List<WebElement> elem = driver.findElements(By.tagName("frame"));
	     for(WebElement webEle : elem){
	    	 System.out.println("Frame Name : " + webEle.getAttribute("name"));
	    	 System.out.println("Frame Title: " + webEle.getAttribute("title"));
	     }*/
		/*List webElements = findElement(testData.getSearchElementBy(), testData.getSearchElement());
		System.out.println("++++++++ Frame Title:  " + ((WebElement)webElements.get(0)).getAttribute("title"));
		driver.switchTo().frame((WebElement)webElements.get(0));*/
		GenericTestCase.log = TestCaseUtility.logDebug(GenericTestCase.log,"handleFrames executed");
	}
	
	public void handleWindow(TestDataBean testData){
		GenericTestCase.log = TestCaseUtility.logDebug(GenericTestCase.log,"Inside handleWindow");
		GenericTestCase.log = TestCaseUtility.logDebug(GenericTestCase.log,"Action : " + testData.getAction() + " Window : " + driver.getWindowHandle());
		driver = driver.switchTo().window(driver.getWindowHandle());
		GenericTestCase.log = TestCaseUtility.logDebug(GenericTestCase.log,"handleWindow executed.");
	}
	
	
	public GenericPageObject(WebDriver driver){
		this.driver = driver;
	}
	
	public List findElement(String searchElementBy , String searchElement){
		GenericTestCase.log = TestCaseUtility.logDebug(GenericTestCase.log,"Inside findElement");
		List<WebElement> webElement = new ArrayList<WebElement>(); 
		try{
		if(searchElementBy.equalsIgnoreCase(CommonConstants.SEARCH_ELEMENT_BY_ID)){
			GenericTestCase.log = TestCaseUtility.logDebug(GenericTestCase.log,"Searching by ID" + searchElement);
			WebElement ele = driver.findElement(By.id(searchElement));
			GenericTestCase.log = TestCaseUtility.logDebug(GenericTestCase.log,"Ele : " + ele);
			webElement.add(ele) ;
		}else if(searchElementBy.equalsIgnoreCase(CommonConstants.SEARCH_ELEMENT_BY_LINKTEXT)){
			webElement.add(driver.findElement(By.linkText(searchElement)));
		}else if(searchElementBy.equalsIgnoreCase(CommonConstants.SEARCH_ELEMENT_BY_CLASS)){
			//webElement.add(driver.findElement(By.className(searchElement)));
			//webElement = driver.findElement(By.className(searchElement));
			webElement = driver.findElements(By.className(searchElement));
		}else if(searchElementBy.equalsIgnoreCase(CommonConstants.SEARCH_ELEMENT_BY_XPATH)){
			webElement.add(driver.findElement(By.xpath(searchElement)));
		}else if(searchElementBy.equalsIgnoreCase(CommonConstants.SEARCH_ELEMENT_BY_CSSSELECTOR)){
			webElement.add(driver.findElement(By.cssSelector(searchElement)));
		}else if(searchElementBy.equalsIgnoreCase(CommonConstants.SEARCH_ELEMENT_BY_NAME)){
			webElement.add(driver.findElement(By.name(searchElement)));
		}
	}catch(NoSuchElementException ex){
		Assert.fail("Unable to find the element " + searchElement + " by using " + searchElementBy );
	}
	GenericTestCase.log = TestCaseUtility.logDebug(GenericTestCase.log,"findElement executed.");
		return webElement;
	}
	
   /*public List findElementByClass(String searchElement){
	   List<WebElement> webElements = driver.findElements(By.className(searchElement));
	   return webElements;
   }*/
	
	private void addDataInTextBox(WebElement webElement, String dataToInput) throws ElementNotVisibleException{
		GenericTestCase.log = TestCaseUtility.logDebug(GenericTestCase.log,"Started executing addDataInTextBox ");
		webElement.click();
		webElement.clear();
		webElement.sendKeys(dataToInput);
		GenericTestCase.log = TestCaseUtility.logDebug(GenericTestCase.log,"Finished executing addDataInTextBox ");
	}
	
	// NOTE: Some drop down's allows for multiple selection. Need to handle that. 
	private void selectDataFromDropDown(WebElement webElement, String dataToSelect){
		GenericTestCase.log = TestCaseUtility.logDebug(GenericTestCase.log,"Inside selectDataFromDropDown");
		webElement.click();
		Select select = new Select(webElement);
		select.selectByVisibleText(dataToSelect);
		List<WebElement> options = select.getOptions();
		GenericTestCase.log = TestCaseUtility.logDebug(GenericTestCase.log,"No of options : " + options.size());
		for(WebElement option : options){
			String optionsVisibleText = option.getText();
			GenericTestCase.log = TestCaseUtility.logDebug(GenericTestCase.log,"Options Visible Text: " + optionsVisibleText);
			if(optionsVisibleText.equalsIgnoreCase(dataToSelect)){
				GenericTestCase.log = TestCaseUtility.logDebug(GenericTestCase.log,"Option Found & Clicked.");
				option.click();
				break;
			}
		}
		GenericTestCase.log = TestCaseUtility.logDebug(GenericTestCase.log,"Finished executing selectDataFromDropDown");
	}	

	private void navigateToSomeOtherPage(WebElement webElement){
		webElement.click();
	}
}
