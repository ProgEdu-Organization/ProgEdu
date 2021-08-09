package fcu.selab.progedu.status;

import fcu.selab.progedu.data.FeedBack;
import fcu.selab.progedu.status.AndroidCheckstyleFailure;
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

public class AndroidCheckstyleFailureTest {
  private static final String TEST_FILE = "/AndroidCheckstyleFailureText.properties";

  private static final Logger LOGGER = LoggerFactory.getLogger(AndroidCheckstyleFailureTest.class);

  private Properties props;

  public AndroidCheckstyleFailureTest() {
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
  public void extractFailureMsgTest() {
    String consoleText = System.getenv("TEST_ONE");
    AndroidCheckstyleFailure androidCheckstyleFailure = new AndroidCheckstyleFailure();
    String result = androidCheckstyleFailure.extractFailureMsg(consoleText);
    System.out.println(result);
  }

  @Test
  public void testFormatExamineMsg() {
    String consoleText = System.getenv("TEST_ONE");
    AndroidCheckstyleFailure androidCheckstyleFailure = new AndroidCheckstyleFailure();
    if (consoleText != null && !consoleText.equals("")) {
      System.out.println("Unable to get properties of TEST case from file;" + TEST_FILE);
    }
    if (props != null) {
      consoleText = props.getProperty("TEST_ONE").trim();
    }
    consoleText = androidCheckstyleFailure.extractFailureMsg(consoleText);
    ArrayList arrayList = androidCheckstyleFailure.formatExamineMsg(consoleText);
    String toJson = androidCheckstyleFailure.tojsonArray(arrayList);
    System.out.println(toJson);
  }

}