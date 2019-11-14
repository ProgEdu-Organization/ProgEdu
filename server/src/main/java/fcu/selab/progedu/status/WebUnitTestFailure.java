package fcu.selab.progedu.status;

import java.util.ArrayList;
import java.util.List;

import com.google.gson.Gson;

import fcu.selab.progedu.data.FeedBack;

public class WebUnitTestFailure implements Status {

  @Override
  public String extractFailureMsg(String consoleText) {
    String unitTestFailureStart = "npm run test";
    String unitTestFailureEnd = "npm ERR! code ELIFECYCLE";

    String unitTestInfo = consoleText.substring(
        consoleText.indexOf(unitTestFailureStart) + unitTestFailureStart.length(),
        consoleText.indexOf(unitTestFailureEnd));

    return unitTestInfo.trim();
  }

  @Override
  public String formatFailureMsg(String consoleText) {
    int consoleStart = consoleText.indexOf("測試");
    int consoleEnd = consoleText.indexOf("failing");
    String unitTestInfo = consoleText.substring(consoleStart, consoleEnd);
    int nextRow = unitTestInfo.indexOf("\n");
    int endIndex = consoleEnd - consoleStart;
    unitTestInfo = unitTestInfo.substring(nextRow + 1, endIndex);
    endIndex = endIndex - nextRow - 1;
    List<FeedBack> feedbacklist = new ArrayList<>();
    while (unitTestInfo.indexOf(")") != -1) {
      int nextparentheses = unitTestInfo.indexOf(")");
      int nextrow = unitTestInfo.indexOf("\n", nextparentheses);
      if (nextrow - nextparentheses == 1) { //
        unitTestInfo = unitTestInfo.substring(nextrow + 1, endIndex);
        endIndex = endIndex - nextrow - 1;
      } else {
        int netspace = unitTestInfo.indexOf("\n", nextparentheses + 1);
        feedbacklist.add(new FeedBack(
            "Unit",
            "",
            unitTestInfo.substring(nextparentheses + 2, netspace),
            "",
            ""));
        unitTestInfo = unitTestInfo.substring(netspace + 1, endIndex);
        endIndex = endIndex - nextrow - 1;
      }
    }
    Gson gson = new Gson();
    return gson.toJson(feedbacklist).toString();
  }

  public String console() {
    String console = "+ npm run test\n"
        + "> node_sample@1.0.0 test /var/jenkins_home/workspace/test11_1024WEB3\n"
        + "> mocha ./src/test/*.js --timeout 100000\n"
        + "\n"
        + "\n"
        + "\n"
        + "  測試index.html\n"
        + "    ✓ 開啟作業網頁 (788ms)\n"
        + "    1) 測試input功能\n"
        + "    ✓ 測試按鈕功能 (77ms)\n"
        + "    2) 測試頁面title是否為HelloWorld\n"
        + "\n"
        + "\n"
        + "  3 passing (1s)\n"
        + "  1 failing\n"
        + "\n"
        + "  1) 測試index.html\n"
        + "       測試頁面title是否為HelloWorld:\n"
        + "\n"
        + "      AssertionError: expected 'AAAAAAAAAAAAAAAAAAA' to equal 'HelloWorld'\n"
        + "      + expected - actual\n"
        + "\n"
        + "      -AAAAAAAAAAAAAAAAAAA\n"
        + "      +HelloWorld\n"
        + "      \n"
        + "      at Context.it (src/test/indexTest.js:31:22)\n"
        + "      at process._tickCallback (internal/process/next_tick.js:68:7)\n"
        + "\n"
        + "\n"
        + "\n"
        + "npm ERR! code ELIFECYCLE";
    return console;
  }

}
