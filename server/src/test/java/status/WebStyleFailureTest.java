package status;

import fcu.selab.progedu.status.WebStylelintFailure;

public class WebStyleFailureTest {
	public static void main(String[] args) {
		WebStylelintFailure WS = new WebStylelintFailure();
		String consule = "Scanned 3 files, no errors found (16 ms).\n" +
				"+ npm run stylelint\n" +
				"\n" +
				"> node_sample@1.0.0 stylelint /var/jenkins_home/workspace/STUDENT2_1024WEB3\n" +
				"> stylelint ./src/web/\n" +
				"\n" +
				"\n" +
				"src/web/html/index.html\n" +
				" 7:9   ✖  Expected indentation of 4 spaces                                 indentation                     \n" +
				" 7:12  ✖  Expected single space after \"{\" of a single-line block           block-opening-brace-space-after \n" +
				" 7:18  ✖  Expected single space after \":\" with a single-line declaration   declaration-colon-space-after   \n" +
				" 7:18  ✖  Unexpected whitespace before \":\"                                 declaration-colon-space-before  \n" +
				" 7:23  ✖  Expected single space before \"}\" of a single-line block          block-closing-brace-space-before\n" +
				"\n" +
				"src/web/html/index2.html\n" +
				" 7:9   ✖  Expected indentation of 4 spaces                                 indentation                             \n" +
				" 7:12  ✖  Expected single space after \"{\" of a single-line block           block-opening-brace-space-after         \n" +
				" 7:18  ✖  Expected single space after \":\" with a single-line declaration   declaration-colon-space-after           \n" +
				" 7:18  ✖  Unexpected whitespace before \":\"                                 declaration-colon-space-before          \n" +
				" 7:32  ✖  Unexpected whitespace before \";\"                                 declaration-block-semicolon-space-before\n" +
				" 7:33  ✖  Expected single space before \"}\" of a single-line block          block-closing-brace-space-before\n" +
				"\n" +
				"npm ERR! code ELIFECYCLE\n" +
				"npm ERR! errno 2\n" +
				"npm ERR! node_sample@1.0.0 stylelint: `stylelint ./src/web/`\n" +
				"npm ERR! Exit status 2";

		String test = "Nothing";
		test = WS.extractFailureMsg(consule);
		System.out.println(test);
	}
}
