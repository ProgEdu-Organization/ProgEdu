package status;

import fcu.selab.progedu.status.WebEslistFailure;

public class WebEsFailureTest {
	public static void main(String[] args) {
		WebEslistFailure WE = new WebEslistFailure();
		String consule = "+ npm run eslint\n" +
				"\n" +
				"> node_sample@1.0.0 eslint /var/jenkins_home/workspace/STUDENT3_1024WEB3\n" +
				"> eslint --ext .html,.js ./src/web/\n" +
				"\n" +
				"Warning: React version was set to \"detect\" in eslint-plugin-react settings, but the \"react\" package is not installed. Assuming latest React version for linting.\n" +
				"\n" +
				"/var/jenkins_home/workspace/STUDENT3_1024WEB3/src/web/html/index.html\n" +
				"   8:1  error  Unexpected var, use let or const instead  no-var\n" +
				"   8:5  error  'bar' is assigned a value but never used  no-unused-vars\n" +
				"  10:1  error  Unexpected var, use let or const instead  no-var\n" +
				"  10:5  error  'fo' is assigned a value but never used   no-unused-vars\n" +
				"  10:7  error  Operator '=' must be spaced               space-infix-ops\n" +
				"  12:1  error  Unexpected var, use let or const instead  no-var\n" +
				"  12:5  error  'foo' is assigned a value but never used  no-unused-vars\n" +
				"  12:8  error  Operator '=' must be spaced               space-infix-ops\n" +
				"\n" +
				"/var/jenkins_home/workspace/STUDENT3_1024WEB3/src/web/html/index2.html\n" +
				"   9:1  error  Unexpected var, use let or const instead                   no-var\n" +
				"   9:5  error  'bar' is assigned a value but never used                   no-unused-vars\n" +
				"  10:1  error  More than 2 blank lines not allowed                        no-multiple-empty-lines\n" +
				"  17:1  error  Unexpected var, use let or const instead                   no-var\n" +
				"  17:5  error  'foo' is assigned a value but never used                   no-unused-vars\n" +
				"  17:8  error  Operator '=' must be spaced                                space-infix-ops\n" +
				"  18:1  error  Too many blank lines at the end of file. Max of 0 allowed  no-multiple-empty-lines\n" +
				"\n" +
				"✖ 15 problems (15 errors, 0 warnings)\n" +
				"  10 errors and 0 warnings potentially fixable with the `--fix` option.\n" +
				"\n" +
				"npm ERR! code ELIFECYCLE\n" +
				"npm ERR! errno 1";

		String test = "Nothing";
		test = WE.extractFailureMsg(consule);
		System.out.println(test);
	}
}
