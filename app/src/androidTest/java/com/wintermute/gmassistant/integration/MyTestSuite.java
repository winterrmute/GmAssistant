package com.wintermute.gmassistant.integration;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(Suite.class)
@Suite.SuiteClasses( {PrepareTestEnvironment.class, UserSimulationTest.class, CleanUpTestData.class})
public class MyTestSuite
{}
