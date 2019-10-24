package status;

import fcu.selab.progedu.status.WebHtmlFailure;

public class WebHtmlFailureTest {
    public static void main(String[] args) {
        WebHtmlFailure WH = new WebHtmlFailure();
        String consule = "  4 passing (784ms)\n" +
            "\n" +
            "+ npm run htmlhint\n" +
            "\n" +
            "> node_sample@1.0.0 htmlhint /var/jenkins_home/workspace/STUDENT2_WEB2\n" +
            "> htmlhint ./src/web/\n" +
            "\n" +
            "\n" +
            "   /var/jenkins_home/workspace/STUDENT2_WEB2/src/web/html/index.html\n" +
            "\u001B[37m      L9 |\u001B[90m    <form name='loginForm' action=\"sec.html\" method=\"post\">\u001B[39m\n" +
            "\u001B[37m                   ^ \u001B[31mThe value of attribute [ name ] must be in double quotes. (attr-value-double-quotes)\u001B[39m\n" +
            "\u001B[1112333m      L10 |\u001B[90m        <input name='username' type=\"text\" />\u001B[39m\n" +
            "\u001B[37m                         ^ \u001B[31mThe value of attribute [ name ] must be in double quotes. (attr-value-double-quotes)\u001B[39m\n" +
            "\u001B[37m      L12 |\u001B[90m        <Input name=\"continue\" type=\"submit\" value=\"Login\" />\u001B[39m\n" +
            "\u001B[37m                   ^ \u001B[31mThe html element name of [ Input ] must be in lowercase. (tagname-lowercase)\u001B[39m\n" +
            "\n" +
            "Scanned 2 files, found 3 errors in 1 files (16 ms)\n" +
            "npm ERR! code ELIFECYCLE\n" +
            "npm ERR! errno 1\n" +
            "npm ERR! node_sample@1.0.0 htmlhint: `htmlhint ./src/web/`\n" +
            "npm ERR! Exit status 1\n" +
            "npm ERR! \n" +
            "npm ERR! Failed at the node_sample@1.0.0 htmlhint script.\n" +
            "npm ERR! This is probably not a problem with npm. There is likely additional logging output above.\n" +
            "\n" +
            "npm ERR! A complete log of this run can be found in:\n" +
            "npm ERR!     /root/.npm/_logs/2019-10-22T07_43_03_412Z-debug.log\n" +
            "Build step 'Run with timeout' marked build as failure\n" +
            "Archiving artifacts";

        String test = "Nothing";
//        consule = consule.replaceAll("\u001B", "u001B");
//        int deletesubstringstart = consule.indexOf("u001B");
//        int ddd = consule.indexOf("m", deletesubstringstart);
        test = WH.extractFailureMsg(consule);
//        System.out.println(deletesubstringstart + "   m:" + ddd);
        System.out.println(test);
    }
}
