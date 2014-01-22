package com.mycompany.salesapp;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class App 
{

    public static void main( String[] args )
    {
    	Properties prop = new Properties();
    	InputStream input = null;
    	String efUsername = "";
    	String efPassword = "";
    	try {
     
    		input = new FileInputStream("config.properties");
     
    		// load a properties file
    		prop.load(input);
     
    		// get the property value and print it out
    		efUsername = prop.getProperty("ef_username");
    		efPassword = prop.getProperty("ef_password");
     
    	} catch (IOException ex) {
    		ex.printStackTrace();
    	} finally {
    		if (input != null) {
    			try {
    				input.close();
    			} catch (IOException e) {
    				e.printStackTrace();
    			}
    		}
    	}
    	
       SalesJFrame salesJFrame = new SalesJFrame(efUsername, efPassword);
       salesJFrame.setVisible(true);
    }
}
