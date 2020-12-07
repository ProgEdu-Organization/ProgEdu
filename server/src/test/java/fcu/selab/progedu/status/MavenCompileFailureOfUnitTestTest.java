package fcu.selab.progedu.status;

import fcu.selab.progedu.data.FeedBack;
import fcu.selab.progedu.utils.ExceptionUtil;

import org.junit.Assert;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Properties;

public class MavenCompileFailureOfUnitTestTest {
  
  private static final String TEST_FILE = "/MavenCompileFailureOfUnitTestText.properties";

  private static final Logger LOGGER = LoggerFactory.getLogger(MavenCompileFailureOfUnitTestTest.class);

  private Properties props;

  public MavenCompileFailureOfUnitTestTest() {
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
    MavenCompileFailureOfUnitTest MavenCompileFailureOfUnitTest = new MavenCompileFailureOfUnitTest();
    if (consoleText != null && !consoleText.equals("")) {
      System.out.println("Unable to get properties of TEST case from file;" + TEST_FILE);
    }
    if (props != null) {
      consoleText = props.getProperty("TEST_ONE").trim();
    }
    consoleText = MavenCompileFailureOfUnitTest.extractFailureMsg(consoleText);
    ArrayList arrayList = MavenCompileFailureOfUnitTest.formatExamineMsg(consoleText);
    ArrayList<FeedBack> testArray = new ArrayList<>();
    testArray.add(new FeedBack(StatusEnum.COMPILE_FAILURE_OF_UNIT_TEST, "hw5/src/test/java/fcu/iecs/q2/Q2Test.java", "21:11", "", "cannot find symbol", "https://www.learnjavaonline.org/"));
    testArray.add(new FeedBack(StatusEnum.COMPILE_FAILURE_OF_UNIT_TEST, "hw5/src/test/java/fcu/iecs/q2/Q2Test.java", "43:64", "", "cannot find symbol", "https://www.learnjavaonline.org/"));
    testArray.add(new FeedBack(StatusEnum.COMPILE_FAILURE_OF_UNIT_TEST, "hw5/src/test/java/fcu/iecs/q2/Q2Test.java", "46:63", "", "cannot find symbol", "https://www.learnjavaonline.org/"));

    String toJson = MavenCompileFailureOfUnitTest.tojsonArray(arrayList);
    String testJson = MavenCompileFailureOfUnitTest.tojsonArray(testArray);
    Assert.assertEquals(toJson, testJson);
  }
}