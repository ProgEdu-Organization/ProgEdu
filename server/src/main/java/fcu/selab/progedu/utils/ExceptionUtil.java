package fcu.selab.progedu.utils;

import java.io.PrintWriter;
import java.io.StringWriter;

public class ExceptionUtil {
  /**
   * transform printStackTrace to String
   */
  public static String getErrorInfoFromException(Exception exception) {
    try {
      StringWriter stringWriter = new StringWriter();
      PrintWriter printwriter = new PrintWriter(stringWriter);
      exception.printStackTrace(printwriter);
      return stringWriter.toString();
    } catch (Exception e) {
      return e.getMessage();
    }
  }
}