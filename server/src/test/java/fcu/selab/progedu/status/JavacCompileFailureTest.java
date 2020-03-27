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

public class JavacCompileFailureTest {

  private static final String TEST_FILE = "/JavaCompileFailureText.properties";

  private static final Logger LOGGER = LoggerFactory.getLogger(JavacCompileFailureTest.class);

  private Properties props;

  public JavacCompileFailureTest() {
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
    JavacCompileFailure javacCompileFailure = new JavacCompileFailure();
    if (consoleText != null && !consoleText.equals("")) {
      System.out.println("Unable to get properties of TEST case from file;" + TEST_FILE);
    }
    if (props != null) {
      consoleText = props.getProperty("TEST_ONE").trim();
    }
    consoleText = javacCompileFailure.extractFailureMsg(consoleText);
    ArrayList arrayList = javacCompileFailure.formatExamineMsg(consoleText);
    ArrayList<FeedBack> testArray = new ArrayList<>();
    testArray.add(new FeedBack(StatusEnum.COMPILE_FAILURE, "src/HelloWorld.java", "6", "\t\tSystem.out.println(\"Hello World!\")\n\t\t                                  ^", " ';' 1expeddddddcted", "https://www.learnjavaonline.org/"));
    testArray.add(new FeedBack(StatusEnum.COMPILE_FAILURE, "src/HelloWorld.java", "6", "\t\tSystem.out.println(\"Hello World!\")\n\t\t                                  ^", " ';' 2esadqwexpejjjcted", "https://www.learnjavaonline.org/"));
    testArray.add(new FeedBack(StatusEnum.COMPILE_FAILURE, "src/HelloWorld.java", "6", "\t\tSystem.out.println(\"Hello World!\")\n\t\t                                  ^", " ';' 3expjected", "https://www.learnjavaonline.org/"));
    testArray.add(new FeedBack(StatusEnum.COMPILE_FAILURE, "src/HelloWorld.java", "6", "\t\tSystem.out.println(\"Hello World!\")\n\t\t                                  ^", " ';' 4expeckjkjkjted", "https://www.learnjavaonline.org/"));
    testArray.add(new FeedBack(StatusEnum.COMPILE_FAILURE, "src/HelloWorld2.java", "6", "\t\tSystem.out.println(\"Hello World!\")\n\t\t                                  ^", " ';' 5expeckjkjkjkjkjkted", "https://www.learnjavaonline.org/"));
    testArray.add(new FeedBack(StatusEnum.COMPILE_FAILURE, "src/HelloWorld3.java", "6", "\t\tSystem.out.println(\"Hello World!\")\n\t\t                                  ^", " ';' expected", "https://www.learnjavaonline.org/"));
    String toJson = javacCompileFailure.tojsonArray(arrayList);
    String testJson = javacCompileFailure.tojsonArray(testArray);
    Assert.assertEquals(toJson, testJson);
  }
}
