AndroidLint_SonarProfileGenerator
=================================

This small java program makes easier the update of the sonar-android plugin (https://github.com/SonarCommunity/sonar-android) when a new version of the Android SDK is published.
It generates a new sonar compatible profile from the Android Lint Violation list


<h2>I - Generating jar</h2>

- Clone Git repository<br />
- Import project into Eclipse
- Select "Package Explorer" right-click on "AndroidLintProfileGenerator"<br />
- Select "Export..."<br />
- Choose Java --> Jar file<br />
 ! Don't forget to add AndroidLintProfileGenerator as the main class !

<h2>II - Running the program</h2>

- First you need to have installed the last version of the Android SDK
- Android Lint Tool must be accessible from the command line, so update your %PATH% variable
- Create your Android Lint Violation List with the following command : <b>lint --list > android_lint_rules_list.txt</b>
- Run the program with the following command : <b>java -jar AndroidLintProfileGenerator.jar android_lint_rules_list.txt path_for_your_generated_xml_android_lint_profile_for_Sonar</b>