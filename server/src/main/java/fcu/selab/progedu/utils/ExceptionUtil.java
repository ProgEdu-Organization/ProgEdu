package fcu.selab.progedu.utils;

import java.io.PrintWriter;
import java.io.StringWriter;

public class ExceptionUtil {
  public static String getErrorInfoFromException(Exception exception) {
    try {
      StringWriter stringwiter = new StringWriter();
      PrintWriter printwriter = new PrintWriter(stringwiter);
      exception.printStackTrace(printwriter);
      return stringwiter.toString();
    } catch (Exception e) {
      return e.getMessage();
    }
  }
}