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

public class MavenCompileFailureTest {

  private static final String TEST_FILE = "/MavenCompileFailureText.properties";

  private static final Logger LOGGER = LoggerFactory.getLogger(MavenCompileFailureTest.class);

  private Properties props;

  public MavenCompileFailureTest() {
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
    MavenCompileFailure mavenCompileFailure = new MavenCompileFailure();
    if (consoleText != null && !consoleText.equals("")) {
      System.out.println("Unable to get properties of TEST case from file;" + TEST_FILE);
    }
    if (props != null) {
      consoleText = props.getProperty("TEST_ONE").trim();
    }
    consoleText = mavenCompileFailure.extractFailureMsg(consoleText);
    ArrayList arrayList = mavenCompileFailure.formatExamineMsg(consoleText);
    ArrayList<FeedBack> testArray = new ArrayList<>();
    testArray.add(new FeedBack(StatusEnum.COMPILE_FAILURE, "MAVAN1119/src/main/java/selab/myapp/App.java", "9:9", "", "not a statement", "https://www.learnjavaonline.org/"));
    testArray.add(new FeedBack(StatusEnum.COMPILE_FAILURE, "MAVAN1119/src/main/java/selab/myapp/App.java", "9:10", "", "not a statement", "https://www.learnjavaonline.org/"));
    testArray.add(new FeedBack(StatusEnum.COMPILE_FAILURE, "MAVEND/src/main/java/selab/myapp/App.java", "9:34", "", "';' expected", "https://www.learnjavaonline.org/"));
    testArray.add(new FeedBack(StatusEnum.COMPILE_FAILURE, "MAVAN1119/src/main/java/selab/myapp/App.java", "9:11", "", "not a statement", "https://www.learnjavaonline.org/"));
    testArray.add(new FeedBack(StatusEnum.COMPILE_FAILURE, "MAVEND/src/main/java/selab/myapp/App.java", "9:35", "", "';' expected", "https://www.learnjavaonline.org/"));
    testArray.add(new FeedBack(StatusEnum.COMPILE_FAILURE, "MAVAN1119/src/main/java/selab/myapp/App.java", "9:12", "", "not a statement", "https://www.learnjavaonline.org/"));
    testArray.add(new FeedBack(StatusEnum.COMPILE_FAILURE, "MAVEND/src/main/java/selab/myapp/App.java", "9:36", "", "';' expected", "https://www.learnjavaonline.org/"));
    testArray.add(new FeedBack(StatusEnum.COMPILE_FAILURE, "MAVAN1119/src/main/java/selab/myapp/App.java", "9:13", "", "not a statement", "https://www.learnjavaonline.org/"));
    testArray.add(new FeedBack(StatusEnum.COMPILE_FAILURE, "MAVEND/src/main/java/selab/myapp/App.java", "9:37", "", "';' expected", "https://www.learnjavaonline.org/"));
    testArray.add(new FeedBack(StatusEnum.COMPILE_FAILURE, "MAVAN1119/src/main/java/selab/myapp/App.java", "9:14", "", "not a statement", "https://www.learnjavaonline.org/"));
    String toJson = mavenCompileFailure.tojsonArray(arrayList);
    String testJson = mavenCompileFailure.tojsonArray(testArray);
    Assert.assertEquals(toJson, testJson);
  }

}
