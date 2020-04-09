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

public class MavenCheckstyleFailureTest {

  private static final String TEST_FILE = "/MavenCheckstyleFailureText.properties";

  private static final Logger LOGGER = LoggerFactory.getLogger(MavenCheckstyleFailureTest.class);

  private Properties props;

  public MavenCheckstyleFailureTest() {
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
    MavenCheckstyleFailure mavenCheckstyleFailure = new MavenCheckstyleFailure();
    if (consoleText != null && !consoleText.equals("")) {
      System.out.println("Unable to get properties of TEST case from file;" + TEST_FILE);
    }
    if (props != null) {
      consoleText = props.getProperty("TEST_ONE").trim();
    }
    consoleText = mavenCheckstyleFailure.extractFailureMsg(consoleText);
    System.out.println("++++++++++++++++\n" + consoleText + "\n-------------------");
    ArrayList arrayList = mavenCheckstyleFailure.formatExamineMsg(consoleText);
    ArrayList<FeedBack> testArray = new ArrayList<>();
    testArray.add(new FeedBack(StatusEnum.CHECKSTYLE_FAILURE, "maven/src/main/java/selab/myapp/App.java", "8:3", " Missing a Javadoc comment.", "", "https://google.github.io/styleguide/javaguide.html"));
    testArray.add(new FeedBack(StatusEnum.CHECKSTYLE_FAILURE, "MAVAN1119/src/main/java/selab/myapp/App.java", "8:3", " Missing a Javadoc comment.", "", "https://google.github.io/styleguide/javaguide.html"));
    testArray.add(new FeedBack(StatusEnum.CHECKSTYLE_FAILURE, "MAVAN1119/src/main/java/selab/myapp/App.java", "10:9", " Local variable name 'i' must match pattern '^[a-z][a-z0-9][a-zA-Z0-9]*$'.", "", "https://google.github.io/styleguide/javaguide.html"));
    testArray.add(new FeedBack(StatusEnum.CHECKSTYLE_FAILURE, "MAVAN1119/src/main/java/selab/myapp/App.java", "10:10", " WhitespaceAround: '=' is not preceded with whitespace.", "", "https://google.github.io/styleguide/javaguide.html"));
    testArray.add(new FeedBack(StatusEnum.CHECKSTYLE_FAILURE, "MAVAN1119/src/main/java/selab/myapp/App.java", "10:11", " WhitespaceAround: '=' is not followed by whitespace.", "", "https://google.github.io/styleguide/javaguide.html"));
    testArray.add(new FeedBack(StatusEnum.CHECKSTYLE_FAILURE, "MAVAN1119/src/main/java/selab/myapp/App.java", "11:7", " WhitespaceAround: 'if' is not followed by whitespace.", "", "https://google.github.io/styleguide/javaguide.html"));
    testArray.add(new FeedBack(StatusEnum.CHECKSTYLE_FAILURE, "MAVAN1119/src/main/java/selab/myapp/App.java", "11:9", " WhitespaceAround: '<' is not preceded with whitespace.", "", "https://google.github.io/styleguide/javaguide.html"));
    testArray.add(new FeedBack(StatusEnum.CHECKSTYLE_FAILURE, "MAVAN1119/src/main/java/selab/myapp/App.java", "11:10", " WhitespaceAround: '<' is not followed by whitespace.", "", "https://google.github.io/styleguide/javaguide.html"));
    testArray.add(new FeedBack(StatusEnum.CHECKSTYLE_FAILURE, "MAVAN1119/src/main/java/selab/myapp/App.java", "11:12", " WhitespaceAround: '{' is not preceded with whitespace.", "", "https://google.github.io/styleguide/javaguide.html"));
    testArray.add(new FeedBack(StatusEnum.CHECKSTYLE_FAILURE, "MAVAN1119/src/main/java/selab/myapp/App.java", "12", " 'if' child have incorrect indentation level 8, expected level should be 6.", "", "https://google.github.io/styleguide/javaguide.html"));
    testArray.add(new FeedBack(StatusEnum.CHECKSTYLE_FAILURE, "MAVAN1119/src/main/java/selab/myapp/App.java", "12:10", " WhitespaceAround: '=' is not preceded with whitespace.", "", "https://google.github.io/styleguide/javaguide.html"));
    testArray.add(new FeedBack(StatusEnum.CHECKSTYLE_FAILURE, "MAVAN1119/src/main/java/selab/myapp/App.java", "12:11", " WhitespaceAround: '=' is not followed by whitespace.", "", "https://google.github.io/styleguide/javaguide.html"));
    testArray.add(new FeedBack(StatusEnum.CHECKSTYLE_FAILURE, "MAVAN1119/src/main/java/selab/myapp/App.java", "12:12", " WhitespaceAround: '+' is not preceded with whitespace.", "", "https://google.github.io/styleguide/javaguide.html"));
    testArray.add(new FeedBack(StatusEnum.CHECKSTYLE_FAILURE, "MAVAN12132131119/src/main/java/selab/myapp/App.java", "12:13", " WhitespaceAround: '+' is not followed by whitespace.", "", "https://google.github.io/styleguide/javaguide.html"));
    String toJson = mavenCheckstyleFailure.tojsonArray(arrayList);
    String testJson = mavenCheckstyleFailure.tojsonArray(testArray);
    Assert.assertEquals(toJson, testJson);
  }
}
