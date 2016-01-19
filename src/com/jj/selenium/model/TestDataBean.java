/** @author Aazim Lakhani
 *  This java bean represents the row (in the test case excel ) which is being executed.   
 **/

package com.jj.selenium.model;

public class TestDataBean {

	public TestDataBean(){

	}

	/* It represents the type of action that is being performed. It can have the following values. 
	 * Data: If the action is to insert data in a text box,check box,dropdown box, radio button among others. 
	 * Navigate: If the action is to navigate across pages through links, buttons etc. 
	 * Verify: If the action is to verify the data visible on the page, with the data present in the excel.  
	 * */
	
	private String type ; 
	
	/* Used by the test case creator, to express the action which is being performed, to have a better understanding of the action being taken */
	private String action ; 
	
	
	private String searchElement ;
	private String searchElementBy;
	private String fieldName;
	private String fieldValue;
	private String fieldType;
	private String screenshot;
	private String verificationRequiredFor;

	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getAction() {
		return action;
	}
	public void setAction(String action) {
		this.action = action;
	}
	public String getSearchElement() {
		return searchElement;
	}
	public void setSearchElement(String searchElement) {
		this.searchElement = searchElement;
	}
	public String getSearchElementBy() {
		return searchElementBy;
	}
	public void setSearchElementBy(String searchElementBy) {
		this.searchElementBy = searchElementBy;
	}
	public String getFieldName() {
		return fieldName;
	}
	public void setFieldName(String fieldName) {
		this.fieldName = fieldName;
	}
	public String getFieldValue() {
		return fieldValue;
	}
	public void setFieldValue(String fieldValue) {
		this.fieldValue = fieldValue;
	}
	public String getFieldType() {
		return fieldType;
	}
	public void setFieldType(String fieldType) {
		this.fieldType = fieldType;
	}
	public String getScreenshot() {
		return screenshot;
	}
	public void setScreenshot(String screenshot) {
		this.screenshot = screenshot;
	}
	public String getVerificationRequiredFor() {
		return verificationRequiredFor;
	}
	public void setVerificationRequiredFor(String verificationRequiredFor) {
		this.verificationRequiredFor = verificationRequiredFor;
	}

}
