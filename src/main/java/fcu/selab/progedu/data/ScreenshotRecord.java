package fcu.selab.progedu.data;

public class ScreenshotRecord {
  private int id;
  private int crId;
  private String pngUrl;

  public int getid() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }

  public String getPngUrl() {
    return pngUrl;
  }

  public void setPngUrl(String pngUrl) {
    this.pngUrl = pngUrl;
  }

  public int getCrId() {
    return crId;
  }

  public void setCrId(int crId) {
    this.crId = crId;
  }
}
