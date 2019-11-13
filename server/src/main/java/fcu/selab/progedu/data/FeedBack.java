package fcu.selab.progedu.data;

public class FeedBack {

  private String style;

  private String line;

  private String massage;

  private String suggest;

  public  FeedBack(String style, String line, String massage, String suggest) {
    this.style = style;
    this.line = line;
    this.massage = massage;
    this.suggest = suggest;
  }

  public String getStyle() {
    return this.style;
  }

  public void setStyle(String style) {
    this.style = style;
  }

  public String getLine() {
    return this.line;
  }

  public void setLine(String line) {
    this.line = line;
  }

  public String getMassage() {
    return this.massage;
  }

  public void setMassage(String massage) {
    this.massage = massage;
  }

  public String getSuggest() {
    return this.suggest;
  }

  public void setSuggest(String suggest) {
    this.suggest = suggest;
  }

}
