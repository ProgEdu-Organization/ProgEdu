package fcu.selab.progedu.data;

public class ZipFileInfo {
  long testZipChecksum = 0;
  String testZipFileUrl = "";

  public ZipFileInfo(long testZipChecksum, String testZipFileUrl) {
    this.testZipChecksum = testZipChecksum;
    this.testZipFileUrl = testZipFileUrl;
  }

  public long getChecksum() {
    return testZipChecksum;
  }

  public String getZipFileUrl() {
    return testZipFileUrl;
  }
}