package fcu.selab.progedu.service;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

public class ScreenshotRecordServiceTest {
  private GroupScreenshotRecordService srs = new GroupScreenshotRecordService();
  private static String GROUP_NAME = "group2";
  private static String PROJECT_NAME = "project2";

//  @Test
  public void update() {

    List<String> urls = new ArrayList<>();
    urls.add("123");
    urls.add("456");
    srs.updateScreenshotPng(GROUP_NAME, PROJECT_NAME, urls);
  }

  @Test
  public void getUrl() {
    System.out.println(srs.getScreenshotPng(GROUP_NAME, PROJECT_NAME, 2).getEntity());
  }
}
