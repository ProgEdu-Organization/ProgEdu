package fcu.selab.progedu.service;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class StatusServiceTest {

  StatusService statusService = StatusService.getInstance();

  @Test
  public void isInitialization() {
    boolean ans = statusService.isInitialization(1);
    assertEquals(true, ans);

    boolean ans2 = statusService.isInitialization(3);
    assertEquals(false, ans2);
  }

  @Test
  public void isBuildSuccess() {
    boolean ans = statusService.isBuildSuccess("dasd");
    assertEquals(false, ans);
  }

  @Test
  public void isMavenCheckstyleFailure() {
  }

  @Test
  public void isMavenUnitTestFailure() {
  }

  @Test
  public void isWebUnitTestFailure() {
  }

  @Test
  public void isWebHtmlhintFailure() {
  }

  @Test
  public void isWebStylelintFailure() {
  }

  @Test
  public void isWebEslintFailure() {
  }

  @Test
  public void isAndroidCompileFailure() {
  }

  @Test
  public void isAndroidUnitTestFailure() {
  }

  @Test
  public void isAndroidUiTestFailure() {
  }

  @Test
  public void isAndroidCheckstyleFailure() {
  }

  @Test
  public void isAndroidLintFailure() {
  }

  @Test
  public void isMavenCompileFailureOfUnitTest() {
  }
}