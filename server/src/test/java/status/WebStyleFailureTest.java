package status;

import fcu.selab.progedu.status.WebStyleFailure;

public class WebStyleFailureTest {
	public static void main(String[] args) {
		WebStyleFailure WS = new WebStyleFailure();
		String consule = "+ npm run htmlhint\n" +
				"\n" +
				"> node_sample@1.0.0 htmlhint /var/jenkins_home/workspace/test11_1023WEB2\n" +
				"> htmlhint ./src/web/\n" +
				"\n" +
				"\n" +
				"Scanned 2 files, no errors found (15 ms).\n" +
				"+ npm run stylelint\n" +
				"\n" +
				"> node_sample@1.0.0 stylelint /var/jenkins_home/workspace/test11_1023WEB2\n" +
				"> stylelint ./src/web/\n" +
				"\n" +
				"src/web/html/index.html\n" +
				" 7:9   ✖  Expected indentation of 4 spaces                                 indentation                     \n" +
				" 7:12  ✖  Expected single space after \"{\" of a single-line block           block-opening-brace-space-after \n" +
				" 7:18  ✖  Expected single space after \":\" with a single-line declaration   declaration-colon-space-after   \n" +
				" 7:21  ✖  Expected single space before \"}\" of a single-line block          block-closing-brace-space-before\n" +
				"\n" +
				"npm ERR! code ELIFECYCLE\n" +
				"npm ERR! errno 2\n" +
				"npm ERR! node_sample@1.0.0 stylelint: `stylelint ./src/web/`\n" +
				"npm ERR! Exit status 2\n" +
				"npm ERR! \n" +
				"npm ERR! Failed at the node_sample@1.0.0 stylelint script.\n" +
				"npm ERR! This is probably not a problem with npm. There is likely additional logging output above.\n" +
				"\n" +
				"npm ERR! A complete log of this run can be found in:\n" +
				"npm ERR!     /root/.npm/_logs/2019-10-24T03_48_49_587Z-debug.log\n" +
				"Build step 'Run with timeout' marked build as failure\n" +
				"Archiving artifacts";

		String test = "Nothing";
		test = WS.extractFailureMsg(consule);
		System.out.println(test);
	}
}
