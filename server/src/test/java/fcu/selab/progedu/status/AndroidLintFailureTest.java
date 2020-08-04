package fcu.selab.progedu.status;

import fcu.selab.progedu.data.FeedBack;
import fcu.selab.progedu.status.AndroidLintFailure;
import fcu.selab.progedu.status.StatusEnum;
import fcu.selab.progedu.utils.ExceptionUtil;

import org.junit.Assert;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Properties;

public class AndroidLintFailureTest {
  private static final String TEST_FILE = "/AndroidLintFailureText.properties";

  private static final Logger LOGGER = LoggerFactory.getLogger(AndroidLintFailureTest.class);

  private Properties props;

  public AndroidLintFailureTest() {
    InputStream is = this.getClass().getResourceAsStream(TEST_FILE);
    try {
      props = new Properties();
      props.load(is);
    } catch (IOException e) {
      System.out.println(e.getMessage());
      LOGGER.debug(ExceptionUtil.getErrorInfoFromException(e));
      LOGGER.error(e.getMessage());
    }
  }

  @Test
  public void testFormatExamineMsg() {
    String consoleText = System.getenv("TEST_ONE");
    AndroidLintFailure androidLintFailure = new AndroidLintFailure();
    if (consoleText != null && !consoleText.equals("")) {
      System.out.println("Unable to get properties of TEST case from file;" + TEST_FILE);
    }
    if (props != null) {
      consoleText = props.getProperty("TEST_ONE").trim();
    }
    consoleText = androidLintFailure.extractFailureMsg(consoleText);
    ArrayList arrayList = androidLintFailure.formatExamineMsg(consoleText);
    ArrayList<FeedBack> testArray = new ArrayList<>();
    testArray.add(new FeedBack(StatusEnum.ANDROID_LINT_FAILURE,
        "/D0440792_Android-Lint/app/src/main/res/layout/activity_main.xml"
            + "\n/D0440792_Android-Lint/app/src/main/res/layout/activity_main.xml",
        "20\n10",
        "Error: Duplicate id @+id/helloWorld, already defined earlier in this layout [DuplicateIds]",
        "android:id=\"@+id/helloWorld\"",
        "https://developer.android.com/studio/write/lint"));
    testArray.add(new FeedBack(StatusEnum.ANDROID_LINT_FAILURE,
        "/D0440792_Android-Lint/app/src/main/res/layout/activity_main.xml",
        "13",
        "Error: Hardcoded string \"hello\", should use @string resource [HardcodedText]",
        "android:text=\"hello\"",
        "https://developer.android.com/studio/write/lint"));
    testArray.add(new FeedBack(StatusEnum.ANDROID_LINT_FAILURE,
        "/D0440792_Android-Lint/app/src/main/res/layout/activity_main.xml",
        "23",
        "Error: Hardcoded string \"hello\", should use @string resource [HardcodedText]",
        "android:text=\"hello\"",
        "https://developer.android.com/studio/write/lint"));
    String toJson = androidLintFailure.tojsonArray(arrayList);
    String testJson = androidLintFailure.tojsonArray(testArray);
    System.out.println(toJson);
    System.out.println(testJson);
    Assert.assertEquals(toJson, testJson);
  }

}