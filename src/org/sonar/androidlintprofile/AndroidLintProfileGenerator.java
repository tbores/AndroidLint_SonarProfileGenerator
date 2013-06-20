package org.sonar.androidlintprofile;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

	/**
	 * This class enables to generate the Android Lint Profile for Sonar rules. The
	 * Android Lint Profile is a xml file. It is generated using a text file that
	 * has been generated from lint with the following command: "lint --show >
	 * rules_list.txt" The Android Lint Profile file can be then used to create a
	 * new Quality Profile in Sonar.
	 *
	 * @author Florian Roncari
	 *
	 */
	public class AndroidLintProfileGenerator {

	  private static final String ANDROID_LINT_PROFILE_FILENAME = "profile-android-lint.xml";
	  private static String pathTextFile = "";
	  private static String pathProfileXml = "";
	  private List<String> listOfRules;

	  /**
	   * Create a new Android Lint Profile
	   *
	   * @param string
	   *            Argument for the path of the txt file
	   */
	  public AndroidLintProfileGenerator(String input, String output) {
	    pathTextFile = input;
	    pathProfileXml = output;
	  }

	  /**
	   * This function parse the input text file and extract the rules names in order 
	   * to create a Sonar-compatible profile.
	   * @return the number of Android Lint Violations found
	   */
	  public int parseRulesFile() {
	    BufferedReader reader = null;
	    List<String> allLines = new ArrayList<String>();
	    List<String> allLinesWithLintChecks = new ArrayList<String>();
	    listOfRules = new ArrayList<String>();
	    boolean eof = false;
	    try {
	      reader = new BufferedReader(new FileReader(pathTextFile));
	      while (!eof) {
	        String line = reader.readLine();

	        if (line != null) {
	          allLines.add(line);
	        } else {
	          eof = true;
	        }
	      }

	      for (String line : allLines) {
	        if (line.contains(":") && line.contains("\"")) {
	          allLinesWithLintChecks.add(line);
	        }
	      }

	      for (String rule : allLinesWithLintChecks) {
	        int position = rule.indexOf(":");
	        listOfRules.add(rule.substring(1, position - 1));
	      }
	    } catch (FileNotFoundException ex) {
	      System.err.println("File not found!");
	      ex.printStackTrace();
	    } catch (IOException ex) {
	      System.err.println("Error!!");
	      ex.printStackTrace();
	    } finally {
	      try {
	        reader.close();
	      } catch (IOException ex1) {
	        System.out.println("Error when closing file !!");
	      }
	    }
	    return listOfRules.size();
	  }

	  /**
	   * This function create a XML file with all the Android Lint Checks in order
	   * to be able to create a new quality profile in Sonar.
	   */
	  public void createXMLFile() {
	    int jmax = listOfRules.size();
	    int j = 0;
	    try {
	      try {

	        DocumentBuilderFactory docFactory = DocumentBuilderFactory
	            .newInstance();
	        DocumentBuilder docBuilder = docFactory.newDocumentBuilder();

	        // root elements
	        Document doc = docBuilder.newDocument();
	        Element rootElement = doc.createElement("profile");
	        doc.appendChild(rootElement);
	        // name
	        Element name = doc.createElement("name");
	        name.appendChild(doc.createTextNode("Android Lint"));
	        rootElement.appendChild(name);
	        // language
	        Element language = doc.createElement("language");
	        language.appendChild(doc.createTextNode("Java"));
	        rootElement.appendChild(language);
	        // rules
	        Element rules = doc.createElement("rules");
	        rootElement.appendChild(rules);

	        for (j = 0; j < jmax; j++) {
	          Element rule = doc.createElement("rule");
	          rules.appendChild(rule);
	          // repositoryKey
	          Element repositoryKey = doc.createElement("repositoryKey");
	          repositoryKey
	              .appendChild(doc.createTextNode("AndroidLint"));
	          rule.appendChild(repositoryKey);
	          // key
	          Element key = doc.createElement("key");
	          key.appendChild(doc.createTextNode(listOfRules.get(j)));
	          rule.appendChild(key);
	        }

	        // write the content into xml file
	        TransformerFactory transformerFactory = TransformerFactory
	            .newInstance();
	        Transformer transformer = transformerFactory.newTransformer();
	        DOMSource source = new DOMSource(doc);
	        StreamResult result = new StreamResult(new File(pathProfileXml+ANDROID_LINT_PROFILE_FILENAME
	            ));

	        transformer.transform(source, result);

	        System.out.println("File \""+pathProfileXml+ANDROID_LINT_PROFILE_FILENAME+"\" written.");
	        System.out.println("Quit.");

	      } catch (ParserConfigurationException pce) {
	        pce.printStackTrace();
	      } catch (TransformerException tfe) {
	        tfe.printStackTrace();
	      }
	    } catch (Exception e) {

	    }
	  }

	  /**
	   * Main function. Take the path of the TXT file in parameter. Read this file
	   * in order to find all Checks. Create a XML file, including all the Android
	   * Lint Checks, which can be used for create a new Sonar Quality Profile.
	   *
	   * @param args
	   *            The path of your Android_Lint_Rules.txt
	   */
	  public static void main(String[] args) {
	    if (args.length > 1) {
	      AndroidLintProfileGenerator lintGen = new AndroidLintProfileGenerator(
	          args[0], args[1]);
	      lintGen.parseRulesFile();
	      lintGen.createXMLFile();
	    } else {
	      System.out.println("Usage: java -jar "
	        + AndroidLintProfileGenerator.class.getSimpleName() + ".jar"
	        + " <android_rules_list.txt>" + "<path_for_profile_xml_file>");
	      System.out.println("With:");
	      System.out.println("\t<android_rules_list.txt> is the file you have generated with the command \"lint --list\"");
	    }
	  }
	}

