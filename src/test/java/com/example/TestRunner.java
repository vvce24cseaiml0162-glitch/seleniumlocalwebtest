package com.example;

import org.testng.TestNG;
import java.io.File;
import java.util.Collections;

public class TestRunner {
    public static void main(String[] args) {
        System.out.println("=================================================");
        System.out.println("  Executing Selenium Functional Tests via TestNG");
        System.out.println("=================================================");

        TestNG testng = new TestNG();
        
        File testngXml = new File("testng.xml");
        if (testngXml.exists()) {
            System.out.println("Using suite file: " + testngXml.getAbsolutePath());
            testng.setTestSuites(Collections.singletonList(testngXml.getAbsolutePath()));
        } else {
            System.out.println("Running test class directly: LocalLoginTest");
            testng.setTestClasses(new Class[] { LocalLoginTest.class });
        }

        testng.setOutputDirectory("test-output");
        testng.run();

        System.out.println("=================================================");
        System.out.println("  Execution Completed! Failed: " + testng.hasFailure());
        System.out.println("=================================================");

        if (testng.hasFailure()) {
            System.exit(1);
        }
    }
}
