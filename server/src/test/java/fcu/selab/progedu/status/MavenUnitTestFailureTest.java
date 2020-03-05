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

public class MavenUnitTestFailureTest {

  private static final String TEST_FILE = "/MavenUnitTestFailureText.properties";

  private static final Logger LOGGER = LoggerFactory.getLogger(MavenUnitTestFailureTest.class);

  private Properties props;

  public MavenUnitTestFailureTest() {
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
    MavenUnitTestFailure mavenUnitTestFailure = new MavenUnitTestFailure();
    if (consoleText != null && !consoleText.equals("")) {
      System.out.println("Unable to get properties of TEST case from file;" + TEST_FILE);
    }
    if (props != null) {
      consoleText = props.getProperty("TEST_ONE").trim();
    }
    consoleText = mavenUnitTestFailure.extractFailureMsg(consoleText);
    ArrayList arrayList = mavenUnitTestFailure.formatExamineMsg(consoleText);
    ArrayList<FeedBack> testArray = new ArrayList<>();
    testArray.add(new FeedBack(StatusEnum.UNIT_TEST_FAILURE, "selab/cofitost/java/AppTest", "", "", "", "https://www.learnjavaonline.org/"));
    testArray.add(new FeedBack(StatusEnum.UNIT_TEST_FAILURE, "selab/cofitost/java/AppTest", "", "", "", "https://www.learnjavaonline.org/"));
    testArray.add(new FeedBack(StatusEnum.UNIT_TEST_FAILURE, "selab/cofitost/java/ThermometerTest", "", "expected:<10.0> but was:<100.0>", "", "https://www.learnjavaonline.org/"));
    testArray.add(new FeedBack(StatusEnum.UNIT_TEST_FAILURE, "selab/cofitost/java/ThermometerTest", "", "expected:<1832.0> but was:<212.0>", "", "https://www.learnjavaonline.org/"));
    String toJson = mavenUnitTestFailure.tojsonArray(arrayList);
    String testJson = mavenUnitTestFailure.tojsonArray(testArray);
    Assert.assertEquals(toJson, testJson);
  }
}
