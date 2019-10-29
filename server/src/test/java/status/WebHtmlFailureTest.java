package status;

import fcu.selab.progedu.status.WebHtmlhintFailure;

public class WebHtmlFailureTest {
    public static void main(String[] args) {
        WebHtmlhintFailure WH = new WebHtmlhintFailure();
        String consule = "+ npm run htmlhint\n" +
            "\n" +
            "> node_sample@1.0.0 htmlhint /var/jenkins_home/workspace/STUDENT1_1024WEB3\n" +
            "> htmlhint ./src/web/\n" +
            "\n" +
            "\n" +
            "   /var/jenkins_home/workspace/STUDENT1_1024WEB3/src/web/html/index.html\n" +
            "\u001B[37m      L10 |\u001B[90m        <Input name=\"username\" type=\"text\" />\u001B[39m\n" +
            "\u001B[37m                   ^ \u001B[31mThe html element name of [ Input ] must be in lowercase. (tagname-lowercase)\u001B[39m\n" +
            "\u001B[37m      L11 |\u001B[90m        <input name='password' type=\"password\" />\u001B[39m\n" +
            "\u001B[37m                         ^ \u001B[31mThe value of attribute [ name ] must be in double quotes. (attr-value-double-quotes)\u001B[39m\n" +
            "\n" +
            "   /var/jenkins_home/workspace/STUDENT1_1024WEB3/src/web/html/index2.html\n" +
            "\u001B[37m      L2 |\u001B[90m<html>\u001B[39m\n" +
            "\u001B[37m          ^ \u001B[31mDoctype must be declared first. (doctype-first)\u001B[39m\n" +
            "\u001B[37m      L12 |\u001B[90m        <input name=\"continue\" Type=\"submit\" value=\"Login\" />\u001B[39m\n" +
            "\u001B[37m                                         ^ \u001B[31mThe attribute name of [ Type ] must be in lowercase. (attr-lowercase)\u001B[39m\n" +
            "\n" +
            "Scanned 3 files, found 4 errors in 2 files (17 ms)\n" +
            "npm ERR! code ELIFECYCLE\n" +
            "npm ERR! errno 1\n" +
            "npm ERR! node_sample@1.0.0 htmlhint: `htmlhint ./src/web/`\n" +
            "npm ERR! Exit status 1\n" +
            "npm ERR! \n" +
            "npm ERR! Failed at the node_sample@1.0.0 htmlhint script.\n" +
            "npm ERR! This is probably not a problem with npm. There is likely additional logging output above.\n" +
            "\n" +
            "npm ERR! A complete log of this run can be found in:\n" +
            "npm ERR!     /root/.npm/_logs/2019-10-24T08_55_32_364Z-debug.log\n" +
            "Build step 'Run with timeout' marked build as failure\n" +
            "Archiving artifacts";

        String test = "Nothing";
        test = WH.extractFailureMsg(consule);
        System.out.println(test);
    }
}
