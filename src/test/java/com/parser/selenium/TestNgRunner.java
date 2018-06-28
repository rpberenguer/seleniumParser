package com.parser.selenium;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.testng.TestNG;
import org.testng.annotations.Test;
import org.testng.xml.XmlClass;
import org.testng.xml.XmlSuite;
import org.testng.xml.XmlSuite.ParallelMode;
import org.testng.xml.XmlTest;

public class TestNgRunner  {

	@Test
	public void doThese() {

		// Suite List
		List<XmlSuite> suites = new ArrayList<XmlSuite>();

		// Suite
		XmlSuite suite = new XmlSuite();
		suite.setName("Suite");
		suite.setThreadCount(5);
		suite.setParallel(ParallelMode.TESTS);

		// Test1
		XmlTest test = new XmlTest(suite);
		test.setName("FirstTest");
		test.setThreadCount(5);
		test.setParallel(ParallelMode.METHODS);

		// Params
		Map<String, String> params = new HashMap<>();
		params.put("browser", "firefox");
		params.put("playerToAdd", "111");
		params.put("playerToRemove", "222");
		test.setParameters(params);

		// Classes
		List<XmlClass> classes = new ArrayList<XmlClass>();
		classes.add(new XmlClass("com.parser.selenium.SeleniumGridDockerParser"));
		test.setXmlClasses(classes) ;

		// Test2
		XmlTest test2 = new XmlTest(suite);
		test2.setName("SecondTest");
		test2.setParameters(params);
		test2.setXmlClasses(classes) ;

		suites.add(suite);

		TestNG tng = new TestNG();
		tng.setXmlSuites(suites);
		tng.run();
	}
}
