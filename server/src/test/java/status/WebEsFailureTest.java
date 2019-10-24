package status;

import fcu.selab.progedu.status.WebEsFailure;

public class WebEsFailureTest {
	public static void main(String[] args) {
		WebEsFailure WE = new WebEsFailure();
		String consule = "+ npm run htmlhint\n" +
				"\n" +
				"> node_sample@1.0.0 htmlhint /var/jenkins_home/workspace/STUDENT3_1023WEB2\n" +
				"> htmlhint ./src/web/\n" +
				"\n" +
				"Scanned 2 files, no errors found (15 ms).\n" +
				"+ npm run stylelint\n" +
				"\n" +
				"> node_sample@1.0.0 stylelint /var/jenkins_home/workspace/STUDENT3_1023WEB2\n" +
				"> stylelint ./src/web/\n" +
				"\n" +
				"+ npm run eslint\n" +
				"\n" +
				"> node_sample@1.0.0 eslint /var/jenkins_home/workspace/STUDENT3_1023WEB2\n" +
				"> eslint --ext .html,.js ./src/web/\n" +
				"\n" +
				"Warning: React version was set to \"detect\" in eslint-plugin-react settings, but the \"react\" package is not installed. Assuming latest React version for linting.\n" +
				"\n" +
				"/var/jenkins_home/workspace/STUDENT3_1023WEB2/src/web/html/index.html\n" +
				"   9:1  error  Unexpected var, use let or const instead                   no-var\n" +
				"   9:5  error  'bar' is assigned a value but never used                   no-unused-vars\n" +
				"  10:1  error  More than 2 blank lines not allowed                        no-multiple-empty-lines\n" +
				"  17:1  error  Unexpected var, use let or const instead                   no-var\n" +
				"  17:5  error  'foo' is assigned a value but never used                   no-unused-vars\n" +
				"  17:8  error  Operator '=' must be spaced                                space-infix-ops\n" +
				"  18:1  error  Too many blank lines at the end of file. Max of 0 allowed  no-multiple-empty-lines\n" +
				"\n" +
				"✖ 7 problems (7 errors, 0 warnings)\n" +
				"  5 errors and 0 warnings potentially fixable with the `--fix` option.\n" +
				"\n" +
				"npm ERR! code ELIFECYCLE\n" +
				"npm ERR! errno 1\n" +
				"npm ERR! node_sample@1.0.0 eslint: `eslint --ext .html,.js ./src/web/`\n" +
				"npm ERR! Exit status 1\n" +
				"npm ERR! \n" +
				"npm ERR! Failed at the node_sample@1.0.0 eslint script.\n" +
				"npm ERR! This is probably not a problem with npm. There is likely additional logging output above.\n" +
				"\n" +
				"npm ERR! A complete log of this run can be found in:\n" +
				"npm ERR!     /root/.npm/_logs/2019-10-24T05_03_46_038Z-debug.log\n" +
				"Build step 'Run with timeout' marked build as failure\n" +
				"Archiving artifacts";

		String test = "Nothing";
		test = WE.extractFailureMsg(consule);
		System.out.println(test);
	}
}
