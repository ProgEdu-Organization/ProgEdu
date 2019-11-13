package fcu.selab.progedu.status;

public class WebUnitTestFailure implements Status {

  @Override
  public String extractFailureMsg(String consoleText) {
    String unitTestInfo = "WebUnitTestFailure";
    String unitTestFailureStart = "npm run test";
    String unitTestFailureEnd = "npm ERR! code ELIFECYCLE";

    unitTestInfo = consoleText.substring(
        consoleText.indexOf(unitTestFailureStart) + unitTestFailureStart.length(),
        consoleText.indexOf(unitTestFailureEnd));

    return unitTestInfo.trim();
  }

  @Override
  public String formatFailureMsg(String consoleText) {
    return consoleText;
  }

  public String console(){
    String console = "+ npm run test\n"
        + "> node_sample@1.0.0 test /var/jenkins_home/workspace/test11_1024WEB3\n"
        + "> mocha ./src/test/*.js --timeout 100000\n"
        + "\n"
        + "\n"
        + "\n"
        + "  測試index.html\n"
        + "    ✓ 開啟作業網頁 (788ms)\n"
        + "    ✓ 測試input功能 (161ms)\n"
        + "    ✓ 測試按鈕功能 (77ms)\n"
        + "    1) 測試頁面title是否為HelloWorld\n"
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
