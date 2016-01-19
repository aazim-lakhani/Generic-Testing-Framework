SET JAVA_HOME=C:\Java\jdk1.6.0_20
SET PATH=%PATH%;%JAVA_HOME%\bin;C:\Ant\apache-ant-1.7.1\bin
SET CLASSPATH=%CLASSPATH%;C:\Ant\apache-ant-1.7.1\lib
SET ANT_OPTS=-Xms512m -Xmx1024m
SET webdriver.chrome.driver=C:\Selenium\ExternalizeSelenium\chromedriver_win_18.0.1022.0\chromedriver.exe
cd C:\Selenium\ExternalizeSelenium
ant generateReport