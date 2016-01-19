package com.jj.selenium.ant;

import java.io.OutputStream;
import java.util.Date;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Properties;

import org.apache.tools.ant.taskdefs.optional.junit.JUnitTest;
import org.apache.tools.ant.taskdefs.optional.junit.JUnitTestRunner;
import org.apache.tools.ant.taskdefs.optional.junit.JUnitVersionHelper;
import org.apache.tools.ant.taskdefs.optional.junit.XMLJUnitResultFormatter;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import javax.xml.parsers.DocumentBuilder;
import org.apache.tools.ant.util.DateUtils;
import javax.xml.parsers.DocumentBuilderFactory;
import org.apache.tools.ant.BuildException;
import java.io.Writer;
import junit.framework.Test;
import java.io.BufferedWriter;                    
import java.io.IOException;                       
import java.io.OutputStream;                      
import java.io.OutputStreamWriter;                
import java.io.Writer;                            
import java.net.InetAddress;                      
import java.net.UnknownHostException;             
import java.util.Date;                            
import java.util.Enumeration;                     
import java.util.Hashtable;                       
import java.util.Properties;                      
import javax.xml.parsers.DocumentBuilder;         
import javax.xml.parsers.DocumentBuilderFactory;  
import junit.framework.AssertionFailedError;      
import junit.framework.Test;                      
import org.apache.tools.ant.BuildException;       
import org.apache.tools.ant.util.DOMElementWriter;
import org.apache.tools.ant.util.DateUtils;       
import org.apache.tools.ant.util.FileUtils;       
import org.w3c.dom.Document;                      
import org.w3c.dom.Element;                       
import org.w3c.dom.Text;                          


import org.w3c.dom.Text;

import com.jj.selenium.test.GenericTestCase;
import com.jj.selenium.util.InitializeTestSuite;
import com.jj.selenium.util.TestCaseUtility;

public class CustomResultFormatter extends XMLJUnitResultFormatter  {

	private Document doc;
	private Element rootElement;
	private Hashtable testElements = new Hashtable();
	private Hashtable failedTests = new Hashtable();
	private Hashtable testStarts = new Hashtable();
	private OutputStream out;
	private String testCaseBeingExecuted ;
	
	public void startTestSuite(JUnitTest suite)                                
	{        
	  this.doc = getDocumentBuilder().newDocument();                           
	  this.rootElement = this.doc.createElement("testsuite");                  
	  String n = suite.getName();   
	  this.rootElement.setAttribute("name", (n == null) ? "unknown" : n);      
	                                                                           
	  String timestamp = DateUtils.format(new Date(), "yyyy-MM-dd'T'HH:mm:ss");
	                                                                           
	  this.rootElement.setAttribute("timestamp", timestamp);                   
	                                                                           
	  this.rootElement.setAttribute("hostname", getHostname());
	  this.rootElement.setAttribute("testCase", suite.getOutfile());
	  this.testCaseBeingExecuted = suite.getOutfile();                                                                         
	  Element propsElement = this.doc.createElement("properties");             
	  this.rootElement.appendChild(propsElement);                              
	  Properties props = suite.getProperties(); 
	  if (props != null) {                                                     
	    Enumeration e = props.propertyNames();                                 
	    while (e.hasMoreElements()) {                                          
	      String name = (String)e.nextElement();  
	      Element propElement = this.doc.createElement("property");            
	      propElement.setAttribute("name", name);                              
	      propElement.setAttribute("value", props.getProperty(name));
	      propsElement.appendChild(propElement);                               
	    }                                                                      
	  }                                                                        
	}                                                                          

	public static DocumentBuilder getDocumentBuilder(){
		try                                                                 
		{                                                                   
			return DocumentBuilderFactory.newInstance().newDocumentBuilder(); 
		} catch (Exception exc) {                                           
			throw new ExceptionInInitializerError(exc);                       
		}                                                                   
	}  
	 
	 public void formatError(String type, Test test, Throwable t) { 
		    if (test != null) {                                          
		      endTest(test);                                             
		      this.failedTests.put(test, test);                          
		    }                                                            
		                                                                 
		    Element nested = this.doc.createElement(type);               
		    Element currentTest = null;                                  
		    if (test != null)                                            
		      currentTest = (Element)this.testElements.get(test);        
		    else {                                                       
		      currentTest = this.rootElement;                            
		    }                                                            
		                                                                 
		    currentTest.appendChild(nested);                             
		                                                                 
		    String message = t.getMessage();                             
		    if ((message != null) && (message.length() > 0)) {           
		      nested.setAttribute("message", t.getMessage());            
		    }                                                            
		    nested.setAttribute("type", t.getClass().getName());         
		                                                                 
		    String strace = JUnitTestRunner.getFilteredTrace(t);         
		    Text trace = this.doc.createTextNode(strace);                
		    nested.appendChild(trace);                                   
		  }                                                              
		                                                                 
		  public void formatOutput(String type, String output) {   
		    Element nested = this.doc.createElement(type);               
		    this.rootElement.appendChild(nested);                        
		    nested.appendChild(this.doc.createCDATASection(output));     
		  }   
		  public void setSystemOutput(String out) 
		  {                                      
		    GenericTestCase.log = TestCaseUtility.logDebug(GenericTestCase.log,"77777777 Inside setSystemOutput");
		    out += GenericTestCase.log.toString() ;
		    formatOutput("system-out", out);
		  }                                      
		                                         
		  public void setSystemError(String out) 
		  {                                      
			  GenericTestCase.log = TestCaseUtility.logDebug(GenericTestCase.log,"9999 Inside setSystemError");
			  System.out.println("GenericTestCase.errorLog.toString() : " + GenericTestCase.errorLog.toString());
			  out += GenericTestCase.errorLog.toString() ;
			  System.out.println("11111 setSystemError");
			  formatOutput("system-err", out);     
		  }                                     
		  
		  public void endTestSuite(JUnitTest suite) throws BuildException{    
			 // super.endTestSuite(suite);
			  this.rootElement.setAttribute("tests", "" + suite.runCount());             
			  this.rootElement.setAttribute("failures", "" + suite.failureCount());      
			  this.rootElement.setAttribute("errors", "" + suite.errorCount());          
			  this.rootElement.setAttribute("time", "" + (suite.getRunTime() / 1000.0D));
			  GenericTestCase.log = TestCaseUtility.logDebug(GenericTestCase.log,"Output Stream : " + out);                                                                           
			  if (this.out != null) {                                                    
				  Writer wri = null;                                                       
				  try {                                                                    
					  wri = new BufferedWriter(new OutputStreamWriter(this.out, "UTF8"));    
					  wri.write("<?xml version=\"1.0\" encoding=\"UTF-8\" ?>\n");            
					  new DOMElementWriter().write(this.rootElement, wri, 0, "  ");          
					  wri.flush();                                                           
				  } catch (IOException exc) {                                              
					  throw new BuildException("Unable to write log file", exc);             
				  } finally {                                                              
					  if ((this.out != System.out) && (this.out != System.err))              
						  FileUtils.close(wri);                                                
				  }                                                                        
			  }                                                                          
		  }     
		  
		  public void setOutput(OutputStream out){
			  this.out = out;
		  }
            
		  public String getHostname(){
			  try{
				  return InetAddress.getLocalHost().getHostName(); 
			  }catch (UnknownHostException e) {
			  }
			  return "localhost";
		  }
		  
		  public void startTest(Test t)                                                                     
		  {                                                                                                  
		    //super.startTest(t);
			 this.testStarts.put(t, new Long(System.currentTimeMillis()));      
		    
		  }                                                                                                  
		                                                                                                     
		  public void endTest(Test test){      
			  //super.endTest(test);
		    if (!(this.testStarts.containsKey(test))) {                                                      
		      startTest(test);                                                                               
		    }                                                                                                
		                                                                                                     
		    Element currentTest = null;                                                                      
		    if (!(this.failedTests.containsKey(test))) {                                                     
		      currentTest = this.doc.createElement("testcase");                                              
		      String n = JUnitVersionHelper.getTestCaseName(test);                                           
		      currentTest.setAttribute("name", (n == null) ? "unknown" : n);                                 
		                                                                                                     
		      currentTest.setAttribute("classname", JUnitVersionHelper.getTestCaseClassName(test));          
		      currentTest.setAttribute("test", this.testCaseBeingExecuted);                                                                                               
		      this.rootElement.appendChild(currentTest);                                                     
		      this.testElements.put(test, currentTest);                                                      
		    } else {                                                                                         
		      currentTest = (Element)this.testElements.get(test);                                            
		    }                                                                                                
		                                                                                                     
		    Long l = (Long)this.testStarts.get(test);                                                        
		    currentTest.setAttribute("time", "" + ((System.currentTimeMillis() - l.longValue()) / 1000.0D)); 
		  }                                                                                                  


		  public void addFailure(Test test, Throwable t)            
		  {                                                        
		   // super.addFailure(test, t);
			  formatError("failure", test, t);                       
		  }                                                        
		                                                                                                                 
		                                                           
		  public void addError(Test test, Throwable t)             
		  {                                                        
		    //super.addError(test, t);
			  formatError("error", test, t);                         
		  }                                                        

}
