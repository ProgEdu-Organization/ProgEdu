package fcu.selab.progedu.setting;

import java.io.IOException;
import java.io.InputStream;
import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import fcu.selab.progedu.status.AndroidLintFailureTest;
import fcu.selab.progedu.utils.ExceptionUtil;
import junit.framework.TestCase;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MavenAssignmentSettingTest {

  @Test
  public void createAssignmentSettingTest() {
    List<String> order = new ArrayList<String>();
    order.add("compilerFailure");
    order.add("codingStyle");
    order.add("test");
    System.out.println(MavenAssignmentSetting.createAssignmentSettingTest(order, "test").getModelEncoding());
  }

}