package org.sonar.androidlintprofile;

import java.io.File;

import junit.framework.Assert;

import org.junit.Test;

/**
 * Test class for the AndroidLintProfileGenerator
 * Analyze the file in resource and create a quality profile and verify that
 * all the results are exacts
 * Code Coverage = 75,4%
 * @author Florian Roncari
 *
 */
public class AndroidLintProfileGeneratorTest {

	// File generated with lint --list > /resource/android_lint_rules_list.txt
	private static final String ANDROID_LINT_LIST_PATH = "resource\\android_lint_rules_list.txt";
	// Path for our Output File
	private static final String ANDROID_LINT_PROFILE_PATH = "test\\org\\sonar\\androidlintprofile\\";
	// Name of the Output File
	private static final String ANDROID_LINT_PROFILE_NAME = "profile-android-lint.xml";
	
	@Test
	public void mainTest()
	{
		AndroidLintProfileGenerator lintGen = new AndroidLintProfileGenerator(
				ANDROID_LINT_LIST_PATH,ANDROID_LINT_PROFILE_PATH);
		int numberOfLintViolations = lintGen.parseRulesFile();
	    lintGen.createXMLFile();
	    // In this version of Android lint we have 154 Violations
	    // The number of Violation might change when new version of SDK are released
	    Assert.assertEquals(154, numberOfLintViolations);
	}
	
	@Test
	public void isNewProfileGenerated()
	{
		File f = new File(ANDROID_LINT_PROFILE_PATH + ANDROID_LINT_PROFILE_NAME);
		Assert.assertEquals(true, f.exists());
	}
}
