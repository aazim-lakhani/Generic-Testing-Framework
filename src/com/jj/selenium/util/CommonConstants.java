package com.jj.selenium.util;

import java.util.ResourceBundle;

public class CommonConstants {
	
public static final ResourceBundle rb = ResourceBundle.getBundle("com.jj.selenium.resources.Configuration");
public static final String HOME_PAGE_URL = "http://"+rb.getString("hostName")+":"+rb.getString("port")+"/crs";


public static final String REG_DATA_SHEET = rb.getString("regDataSheet");
public static final String CREATE_AND_CHECKOUT_DATA ="createAndCheckoutData"; 
public static final String SITE_BROWSE_DATA = "siteBrowseData";
public static final String CREATE_USER_DATA ="createUserData";
public static final String EXISTING_USER_CHKOUT_DATA ="existingUserCheckoutData";
public static final String ASDA_CHECK_POSTAL_CODE_VALIDITY_DATA ="ASDACheckPostalCode";
public static final String TEST_CASES ="TestCases";
public static final String TEMP ="Temp";

//Excel Constants

public static final int TYPE = 0;
public static final int ACTION = 1;
public static final int SEARCH_ELEMENT = 2;
public static final int SEARCH_ELEMENT_BY = 3;
public static final int FIELD_NAME = 4;
public static final int FIELD_VALUE = 5;
public static final int FIELD_INPUT_TYPE = 6;
public static final int SCREENSHOT = 7;
public static final int VERIFICATION_REQUIRED_FOR = 8;
public static final int BROWSER = 9;


public static final String ACTION_TYPE_IS_DATA_INPUT = "Data";
public static final String ACTION_TYPE_IS_PAGE_NAVIGATION = "Navigate";
public static final String ACTION_TYPE_IS_VERIFICATION = "Verify";
public static final String ACTION_TYPE_IS_VERIFY_DERIVED_DATA = "VerifyDerivedData";
public static final String ACTION_TYPE_IS_FRAME = "Frame";
public static final String ACTION_TYPE_IS_WINDOW = "Window";

public static final String SEARCH_ELEMENT_BY_ID="Id";
public static final String SEARCH_ELEMENT_BY_CLASS="Class";
public static final String SEARCH_ELEMENT_BY_LINKTEXT="LinkText";
public static final String SEARCH_ELEMENT_BY_XPATH="XPath";
public static final String SEARCH_ELEMENT_BY_CSSSELECTOR="CSSSelector";
public static final String SEARCH_ELEMENT_BY_NAME="Name";

public static final String INPUT_TYPE_BROWSER = "Browser";
public static final String INPUT_TYPE_TEXTBOX = "TextBox";
public static final String INPUT_TYPE_DROPDOWNBOX = "DropDownBox";
public static final String INPUT_TYPE_CHECKBOX = "CheckBox";
public static final String INPUT_TYPE_RADIO_BUTTON = "RadioButton";

public static final String VERIFICATION_REQUIRED_FOR_USER_PROFILE = "UserProfile";
}
